package lh.wordtree.plugin.bookshelf;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONWriter;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lh.wordtree.comm.config.Config;
import lh.wordtree.component.SystemMessage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetCrawler extends Stage {
    private String netBook = "http://www.gashuw.com/modules/article/search.php?searchkey=%s";
    private final BorderPane pane = new BorderPane();
    public double width = 800;
    public double height = 700;
    private ExecutorService service = Executors.newFixedThreadPool(10);
    private final Scene scene = new Scene(pane, width, height);
    private String baseButtonStyle = "-fx-background-color: #2980b9;-fx-text-fill: #ffff;-fx-min-height: 35;-fx-min-width: 80";
    private String bookshelfUrl = Config.APP_CONFIG_DIR + "/bookshelf";
    private final MFXListView<NovelBox> data = new MFXListView<>();
    private final MFXTextField input = new MFXTextField();
    private final MFXButton search = new MFXButton("搜索");
    private final MFXTextField downUrl = new MFXTextField("C:\\Users\\28322\\Documents\\书籍");
    private final HBox hbox = new HBox();
    private final HBox box = new HBox();
    private final VBox vbox = new VBox();
    private final MFXButton open = new MFXButton("打开目录");

    // 布局
    {
        // list view布局
        data.prefWidthProperty().bind(this.widthProperty());
        data.prefHeightProperty().bind(this.heightProperty().add(-50));

        // 搜索框
        input.setFloatingText("搜索指定名称小说：");
        input.setMinWidth(200);

        search.setStyle(baseButtonStyle);
        search.setOnMouseClicked(this::search);

        // 下载路径
        downUrl.setFloatingText("下载路径");
        downUrl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!FileUtil.exist(newValue)) SystemMessage.sendError("该路径不存在！！！");
        });
        downUrl.setMinWidth(250);

        box.getChildren().addAll(input, search);
        box.setPadding(new Insets(8));
        box.setSpacing(15);

        open.setStyle(baseButtonStyle);
        hbox.getChildren().addAll(downUrl,open);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(8));
        open.setOnMouseClicked(this::openDir);
        vbox.getChildren().addAll(box, hbox);

        vbox.setSpacing(10);
        pane.setTop(vbox);
        pane.setCenter(data);
    }

    public NetCrawler() {
        this.init();
    }

    public void init() {
        this.setTitle("网络下载");
        this.getIcons().add(new Image(Config.src("static/icon/icon.png")));
        this.resizableProperty().setValue(Boolean.FALSE);
        Config.setBaseStyle(scene);
        this.setScene(scene);
    }

    /**
     * 搜索一种名称的书籍，并更新到listView上面
     * @param event
     */
    private void search(MouseEvent event) {
        if (input.getText().isEmpty()) {
            SystemMessage.sendWarning("不能搜索为空！");
            return;
        }
        if (data.getItems().size() > 0) data.getItems().clear();
        service.execute(()-> {
            String word = input.getText();
            Connection connect = Jsoup.connect(netBook.formatted(word));
            Document document;
            try {
                document = connect.get();
                Element body = document.body();
                body.select("#content > table > tbody > tr[id=nr]")
                        .forEach(element -> {
                            // 获取href以及书籍标题
                            Element book_title = element.selectFirst("td:nth-child(1)");
                            String href = book_title.selectFirst("a").attr("href");
                            String title = book_title.text();
                            Novel novel = new Novel();
                            novel.url = href;
                            novel.title = title;

                            // 获取作者
                            Element author = element.selectFirst("td:nth-child(3)");
                            novel.author = author.text();

                            // 获取字数
                            Element even = element.selectFirst("td:nth-child(4)");
                            novel.number = even.text();

                            // 获取最后更新时间
                            Element update = element.selectFirst("td:nth-child(5)");
                            if (update != null) novel.update = Long.parseLong(update.text());

                            // 状态
                            Element state = element.selectFirst("td:nth-child(6)");
                            novel.state = state.text();
                            Platform.runLater(()-> data.getItems().add(new NovelBox(novel)));
                        });
            } catch (IOException e) {
                SystemMessage.sendError("抓取错误，无法抓取该网站信息！");
                e.printStackTrace();
            }
        });
    }

    /**
     * 打开目录文件夹
     */
    private void openDir(MouseEvent event) {
        String[] cmd = new String[]{
                "cmd","/c","start"," ", downUrl.getText()
        };
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public class NovelBox extends HBox {
        public Novel novel;
        private double labelWidth = 70;
        private MFXButton down = new MFXButton("下载");
        private MFXButton updateDown = new MFXButton("更新");
        public NovelBox(Novel novel) {
            this.novel = novel;
            Label title = new Label(novel.title);
            Label author = new Label(novel.author);
            Label number = new Label(novel.number);
            Label state = new Label(novel.state);
            Label update = new Label(String.valueOf(novel.update));

            title.setMinWidth(labelWidth * 2);
            author.setMinWidth(labelWidth);
            number.setMinWidth(labelWidth);
            state.setMinWidth(labelWidth);
            update.setMinWidth(labelWidth);

            down.setStyle("-fx-background-color: #1abc9c;-fx-text-fill: #ffff;");
            updateDown.setStyle("-fx-background-color: #3498db;-fx-text-fill: #ffff;");
            this.getChildren().addAll(title, author, number, state, update,down, updateDown);
            this.setSpacing(15);

            down.setOnMouseClicked(this::downFun);
        }


        private void downFun(MouseEvent mouseEvent) {
            service.execute(()-> {
                Platform.runLater(()-> down.setText("正在下载...."));
                try {
                    Element body = Jsoup.connect(novel.url).get().body();
                    Element element = body.selectFirst("#hotcontent > div.l > div > table > tbody > tr > td:nth-child(2) > div:nth-child(5) > a:nth-child(2)");
                    String href = element.attr("href");
                    String path = downUrl.getText() + "\\%s.txt".formatted(novel.title);
                    HttpUtil.downloadFile(href, path);
                    Platform.runLater(()->{
                        down.setText("下载");
                        SystemMessage.sendSuccess("下载成功，请放心食用！");
                    });
                    // 下载之后在指定C盘目录创建有关配置文件
                    BookshelfItem bookshelfItem = new BookshelfItem(new File(path));
                    File touch = FileUtil.touch(bookshelfUrl + "\\" + novel.title + ".config.json");
                    FileUtil.writeBytes(
                            JSON.toJSONBytes(bookshelfItem, JSONWriter.Feature.PrettyFormat.ordinal()), touch
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static class Novel {
        public String title = "无";
        public String author = "无";
        public String url = "无";
        public String number = "无";
        public String state = "无";
        public long update = 0L;

        public String toString() {
            return "Novel{" +
                    "title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", url='" + url + '\'' +
                    ", number='" + number + '\'' +
                    ", state='" + state + '\'' +
                    ", update=" + update +
                    '}';
        }
    }

}
