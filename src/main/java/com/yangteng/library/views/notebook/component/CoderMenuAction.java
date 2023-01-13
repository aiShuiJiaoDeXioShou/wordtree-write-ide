package com.yangteng.library.views.notebook.component;

import com.yangteng.library.function.OrdinaryFunction;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

public interface CoderMenuAction {

    /**
     * 按钮的双击事件，默认为打开文件
     *
     * @param fileTree
     */
    void doubleClick(TreeItem<Label> fileTree, OrdinaryFunction fn);

    /**
     * 新建一个操作menu集合
     *
     * @param fileTree 事件发生地点的menu参数
     * @param fn       OrdinaryFunction 实现对事件的发生
     */
    void newFileOrFolderAction(TreeItem<Label> fileTree, OrdinaryFunction fn);

    /**
     * 每当双击该文件树的时候添加一个tab导航
     */
    void tabAction(TreeItem<Label> fileTree);

    /**
     * 重命名
     *
     * @param fileTree
     * @param fn       OrdinaryFunction 实现对事件的发生
     */
    void renameAction(TreeItem<Label> fileTree, OrdinaryFunction fn);

    /**
     * 删除方法
     *
     * @param fileTree 事件发生的节点
     * @param fn       OrdinaryFunction 实现对事件的发生
     */
    void delAction(TreeItem<Label> fileTree, OrdinaryFunction fn);

}
