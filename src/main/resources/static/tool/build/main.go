package main

import (
	"archive/zip"
	"flag"
	"fmt"
	"html/template"
	"io"
	"io/fs"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/exec"
	"path"
	"path/filepath"
	"runtime"
	"strings"
	"sync"
)

//.\main.exe -path D:/wordtree/小说/77 -tmpl ./book.tmpl -start

var upath *string
var tmpl *string
var startWeb *bool
var createBook *bool
var outpath *string
var exePath = os.Args[0]
var args = os.Args[1:]
var aPath = getCurrentAbPath()
var cssFile = aPath + "/bulma.css"
var vueFile = aPath + "/vue.js"
var outputFile = aPath + "/output.html"

type Accumulator struct {
	SrcText    string
	Outline    string
	Material   string
	RootPath   string
	ConfigPath string
	BookFiles  *BookFiles
	Book       *Book
}

type ChapterFile struct {
	Package       string
	ChapterPath   string
	IsPackage     bool
	ChildChapters []string
}

type Chapter struct {
	Name      string
	Content   string
	IsPackage bool
	Chapters  []Chapter
}

type Outline struct{}

type Book struct {
	Name         string
	Author       string
	Introduction string
	Chapters     []Chapter
	Outline      Outline
}

type BookFiles struct {
	Chapters []ChapterFile
	Outline  []string
	Material []string
}

func NewAccumulator(path string) *Accumulator {
	var srcText = "原文"
	var outline = "大纲"
	var material = "素材"
	var configPath = ".wordtree"
	// 初始化文件路径
	srcText = fmt.Sprintf("%s/%s", path, srcText)
	outline = fmt.Sprintf("%s/%s", path, outline)
	material = fmt.Sprintf("%s/%s", path, material)
	configPath = fmt.Sprintf("%s/%s", path, configPath)
	accumulator := &Accumulator{
		RootPath:   path,
		Outline:    outline,
		Material:   material,
		ConfigPath: configPath,
		SrcText:    srcText,
		BookFiles:  &BookFiles{},
		Book:       &Book{},
	}
	// 帮助解析不同的文件路径
	accumulator.classify()
	// 对数据源进行处理,最终生成可靠的原数据
	accumulator.bookParse()
	return accumulator
}

// Classify 分类和收集这个文件夹下面所有的文件
func (accumulator *Accumulator) classify() {
	var wg sync.WaitGroup
	wg.Add(3)

	go func() {
		defer wg.Done()
		// 将不同路径，解析的不同值都放进去，从而生成一种树状结构
		err := filepath.Walk(accumulator.Outline, func(path string, info fs.FileInfo, err error) error {
			if !info.IsDir() {
				accumulator.BookFiles.Outline = append(accumulator.BookFiles.Outline, path)
			}
			return err
		})
		if err != nil {
			panic(err.Error())
			return
		}
	}()

	go func() {
		defer wg.Done()
		err := filepath.Walk(accumulator.Material, func(path string, info fs.FileInfo, err error) error {
			if !info.IsDir() {
				accumulator.BookFiles.Material = append(accumulator.BookFiles.Material, path)
			}
			return err
		})
		if err != nil {
			panic(err.Error())
			return
		}
	}()

	go func() {
		defer wg.Done()
		files, err := os.ReadDir(accumulator.SrcText)
		if err != nil {
			fmt.Println(err)
			return
		}

		for _, file := range files {
			var chapterFile ChapterFile
			if file.IsDir() {
				chapterFile.ChapterPath = filepath.Join(accumulator.SrcText, file.Name())
				chapterFile.Package = file.Name()
				chapterFile.IsPackage = true
				accumulator.BookFiles.Chapters = append(accumulator.BookFiles.Chapters, chapterFile)
				continue
			}
			chapterFile.ChapterPath = filepath.Join(accumulator.SrcText, file.Name())
			chapterFile.IsPackage = false
			chapterFile.Package = "Root"
			accumulator.BookFiles.Chapters = append(accumulator.BookFiles.Chapters, chapterFile)
		}

		for index, _ := range accumulator.BookFiles.Chapters {
			// 使用指针方便修改元素的值
			chapter := &accumulator.BookFiles.Chapters[index]
			if chapter.IsPackage {
				err := filepath.Walk(chapter.ChapterPath, func(path string, info fs.FileInfo, err error) error {
					if !info.IsDir() {
						chapter.ChildChapters = append(chapter.ChildChapters, path)
					}
					return err
				})
				if err != nil {
					panic(err.Error())
					return
				}
			}
		}
	}()

	wg.Wait()
}

// bookParse 该函数在classify的后面
func (accumulator *Accumulator) bookParse() {
	accumulator.Book.Name = filepath.Base(accumulator.RootPath)
	chapters := accumulator.BookFiles.Chapters
	for _, chapter := range chapters {
		var c Chapter
		if chapter.IsPackage {
			c.Name = filepath.Base(chapter.ChapterPath)
			c.IsPackage = chapter.IsPackage
			for _, ch := range chapter.ChildChapters {
				var cc Chapter
				cc.Name = filepath.Base(ch)
				cc.IsPackage = false
				data, err := ioutil.ReadFile(ch)
				if err != nil {
					fmt.Println("Error reading file:", err)
					return
				}
				cc.Content = string(data)
				c.Chapters = append(c.Chapters, cc)
			}
			accumulator.Book.Chapters = append(accumulator.Book.Chapters, c)
			continue
		}
		c.Name = filepath.Base(chapter.ChapterPath)
		c.IsPackage = chapter.IsPackage
		data, err := ioutil.ReadFile(chapter.ChapterPath)
		if err != nil {
			fmt.Println("Error reading file:", err)
			return
		}
		c.Content = string(data)
		accumulator.Book.Chapters = append(accumulator.Book.Chapters, c)
	}
}

func (accumulator *Accumulator) Service(tmpl string) {
	http.HandleFunc("/index", func(w http.ResponseWriter, r *http.Request) {
		// 解析 HTML 模板文件
		tmpl, err := template.ParseFiles(tmpl)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		// 渲染 HTML 模板
		err = tmpl.Execute(w, accumulator.Book)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
	})
	http.Handle("/", http.FileServer(http.Dir(".")))
	err := exec.Command("cmd", "/c", "start", "http://localhost:8080/index").Run()
	if err != nil {
		panic(err)
	}
	err = http.ListenAndServe(":8080", nil)
	if err != nil {
		panic("启动web服务器失败：" + err.Error())
		return
	}
	log.Println("Web服务已退出！")
}

func (accumulator *Accumulator) CreateHtml(t string) {
	// 解析模板文件
	tmpl, err := template.ParseFiles(t)
	if err != nil {
		log.Fatal("Failed to parse template:", err)
	}

	// 打开输出文件
	output, err := os.Create(outputFile)
	if err != nil {
		log.Fatal("Failed to create output file:", err)
	}
	defer func(output *os.File) {
		err := output.Close()
		if err != nil {
			log.Fatal("关闭文件失败！")
		}
	}(output)

	// 渲染模板并输出到文件
	err = tmpl.Execute(output, accumulator.Book)
	if err != nil {
		log.Fatal("Failed to render template:", err)
	}
	// 将该文件和已有的样式以及js文件移动到创建的指定目录里面并且覆盖
	var files = []string{outputFile, cssFile, vueFile}
	err = MoveAndCompressFiles(files, *outpath, true, true)
	if err != nil {
		log.Fatal("压缩失败！\n" + err.Error())
		return
	}
}

func MoveAndCompressFiles(sourceFilePaths []string, destinationDirectoryPath string, overwrite bool, compress bool) error {
	if _, err := os.Stat(destinationDirectoryPath); os.IsNotExist(err) {
		os.MkdirAll(destinationDirectoryPath, os.ModePerm)
	}

	for _, sourceFilePath := range sourceFilePaths {
		sourceFile, err := os.Open(sourceFilePath)
		if err != nil {
			return err
		}
		defer sourceFile.Close()
		destinationFilePath := filepath.Join(destinationDirectoryPath, filepath.Base(sourceFilePath))
		var destinationFile *os.File
		if overwrite {
			destinationFile, err = os.OpenFile(destinationFilePath, os.O_WRONLY|os.O_CREATE|os.O_TRUNC, os.ModePerm)
		} else {
			destinationFile, err = os.OpenFile(destinationFilePath, os.O_WRONLY|os.O_CREATE|os.O_EXCL, os.ModePerm)
		}
		if err != nil {
			return err
		}
		defer destinationFile.Close()

		_, err = io.Copy(destinationFile, sourceFile)
		if err != nil {
			return err
		}
	}

	if compress {
		zipFilePath := filepath.Join(destinationDirectoryPath, filepath.Base(sourceFilePaths[0])+".zip")
		zipFile, err := os.Create(zipFilePath)
		if err != nil {
			return err
		}
		defer zipFile.Close()

		zipWriter := zip.NewWriter(zipFile)
		defer zipWriter.Close()

		err = filepath.Walk(destinationDirectoryPath, func(filePath string, fileInfo os.FileInfo, err error) error {
			if err != nil {
				return err
			}
			if fileInfo.IsDir() || filePath == zipFilePath {
				return nil
			}
			zipFile, err := zipWriter.Create(filePath)
			if err != nil {
				return err
			}
			file, err := os.Open(filePath)
			if err != nil {
				return err
			}
			defer file.Close()
			_, err = io.Copy(zipFile, file)
			if err != nil {
				return err
			}
			return nil
		})
		if err != nil {
			return err
		}
	}
	return nil
}

// ParseArgs 接受可观测的参数
func ParseArgs() {
	upath = flag.String("path", "", "要解析的文件夹位置。")
	tmpl = flag.String("tmpl", "", "要解析输出的模板")
	startWeb = flag.Bool("start", false, "是否开启服务器？")
	createBook = flag.Bool("create", false, "是否创造书籍？")
	outpath = flag.String("outpath", "./out", "是否创造书籍？")
	flag.Parse()
	if *upath == "" {
		log.Fatal("请指定文件夹路径！")
		return
	}
	if *tmpl == "" {
		log.Fatal("请指定模板路径！")
		return
	}
	accumulator := NewAccumulator(*upath)
	if *createBook {
		accumulator.CreateHtml(*tmpl)
	}
	if *startWeb {
		accumulator.Service(*tmpl)
	}

}

func CommandParsing() {
	registrationCommand("create", "project", func(value string) {})
}

func registrationCommand(prefix string, between string, execute func(value string)) {
	pre := args[0]
	bet := args[1]
	v := args[2]
	if pre == prefix && bet == between {
		execute(v)
	}
}

// 获取当前的工作目录
func getCurrentAbPathByCaller() string {
	var abPath string
	_, filename, _, ok := runtime.Caller(0)
	if ok {
		abPath = path.Dir(filename)
	}
	return abPath
}

// 最终方案-全兼容
func getCurrentAbPath() string {
	dir := getCurrentAbPathByExecutable()
	if strings.Contains(dir, getTmpDir()) {
		return getCurrentAbPathByCaller()
	}
	return dir
}

// 获取系统临时目录，兼容go run
func getTmpDir() string {
	dir := os.Getenv("TEMP")
	if dir == "" {
		dir = os.Getenv("TMP")
	}
	res, _ := filepath.EvalSymlinks(dir)
	return res
}

// 获取当前执行文件绝对路径
func getCurrentAbPathByExecutable() string {
	exePath, err := os.Executable()
	if err != nil {
		log.Fatal(err)
	}
	res, _ := filepath.EvalSymlinks(filepath.Dir(exePath))
	return res
}

func main() {
	ParseArgs()
}
