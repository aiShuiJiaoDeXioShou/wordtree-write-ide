package lh.wordtree.model.user;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import lh.wordtree.archive.entity.Author;
import lh.wordtree.component.SystemMessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EditeUserController {
    private EditUserView userView;
    private EditeUserModel userModel = new EditeUserModel();

    public EditeUserController(EditUserView userView) {
        this.userView = userView;
        userView.confim.setOnMouseClicked(this::confim);
        userView.wtIcon.setOnMouseClicked(this::selectUserImage);
        userView.cel.setOnMouseClicked(this::exit);
    }

    // 表单提交
    public void confim(MouseEvent event) {
        Author author = null;
        try {
            String name = userView.nameInput.getTextField().getText();
            String signature = userView.qiangInput.getTextField().getText();
            String password = userView.basePassword.getTextField().getText();
            if (name.isBlank() || signature.isBlank() || password.isBlank()){
                SystemMessage.sendError("所填写值不能为空！");
                return;
            }
            author = new Author(name,
                    signature,
                    userView.file.get().getPath(),
                    password,
                    new ArrayList<>(), new ArrayList<>());
        } catch (Exception e2) {
            SystemMessage.sendError("必须要输入全部信息！");
        }
        if (author != null) userModel.flush(author);
        userView.close();
    }

    // 头像选择
    public void selectUserImage(MouseEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("请选择您头像的图片");
        userView.file.set(fileChooser.showOpenDialog(userView));
        try {
            userView.wtIcon.setImage(new Image(new FileInputStream(userView.file.get().getPath()), 64, 64, true, true));
        } catch (FileNotFoundException ex) {
            SystemMessage.sendError("这个文件不能作为您的头像");
        }
    }

    // 弹窗退出
    public void exit(MouseEvent event) {
        userView.close();
    }
}
