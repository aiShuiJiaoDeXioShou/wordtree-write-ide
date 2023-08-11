package lh.wordtree.plugin.bookshelf;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONWriter;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lh.wordtree.comm.config.Config;
import lh.wordtree.component.SystemMessage;
import lh.wordtree.component.TreeStage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetCrawler extends TreeStage {
    private String netBook = "http://www.gashuw.com/modules/article/search.php?searchkey=%s";
    private final BorderPane pane = new BorderPane();
    public double width = 650;
    public double height = 700;
    private ExecutorService service = Executors.newFixedThreadPool(10);
    private final Scene scene = new Scene(pane, width, height);
    private String baseButtonStyle = "-fx-background-color: #2980b9;-fx-text-fill: #ffff;-fx-min-height: 35;-fx-min-width: 80";
    private String bookshelfUrl = Config.APP_CONFIG_DIR + "/bookshelf";
    private final MFXListView<NovelBox> data = new MFXListView<>();{
        // 制作表格标题
        NovelBox novelBox = new NovelBox(new Novel());
        novelBox.setStyle("-fx-background-color: #3498db;-fx-text-fill: #ffff");
        novelBox.hover.setValue(true);
        data.getItems().add(novelBox);
    }
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
        input.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) this.search(null);
        });
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
        if (data.getItems().size() > 0) {
            data.getItems()
                    .removeIf(box ->
                            !box.novel.title.equals("标题") && !box.novel.number.equals("字数(千字)"));
        }
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
        public SimpleBooleanProperty hover = new SimpleBooleanProperty(false);
        public NovelBox(Novel novel) {
            this.novel = novel;
            Label title = new Label(novel.title);
            Label author = new Label(novel.author);
            Label number = new Label(novel.number);
            Label state = new Label(novel.state);
            Label update = new Label();

            if (novel.update != 0L) {
                // 时间转化
                Date date = new Date(novel.update);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                update.setText(format.format(date));
            } else {
                update.setText("最后更新时间");
            }

            title.setPrefWidth(labelWidth * 2);
            List.of(author, number, state, update)
                            .forEach(label -> label.setPrefWidth(labelWidth));

            List.of(title, author, number, state, update)
                    .forEach(label -> {
                        label.setTextOverrun(OverrunStyle.ELLIPSIS);
                        label.setPadding(new Insets(4, 0,0, 0));
                    });

            down.setStyle("-fx-background-color: #1abc9c;-fx-text-fill: #ffff;");
            updateDown.setStyle("-fx-background-color: #3498db;-fx-text-fill: #ffff;");
            this.getChildren().addAll(title, author, number, state, update,down, updateDown);
            this.setSpacing(15);

            down.setOnMouseClicked(this::downFun);

            hover.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    List.of(title, author, number, state, update)
                            .forEach(label -> {
                                label.setTextFill(Color.WHITE);
                                down.setStyle("-fx-background-color: #3498db;-fx-text-fill: #ffff;");
                                updateDown.setStyle("-fx-background-color: #3498db;-fx-text-fill: #ffff;");
                            });
                }
            });
        }


        private void downFun(MouseEvent mouseEvent) {
            service.execute(()-> {
                if (novel.url.equals("无下载链接")) return;
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
                } catch (Exception e) {
                    Platform.runLater(()-> {
                        SystemMessage.sendError("下载失败！");
                        down.setText("下载失败");
                    });
                    e.printStackTrace();
                }
            });
        }
    }

    public static class Novel {
        public String title = "标题";
        public String author = "作者";
        public String url = "无下载链接";
        public String number = "字数(千字)";
        public String state = "状态";
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
