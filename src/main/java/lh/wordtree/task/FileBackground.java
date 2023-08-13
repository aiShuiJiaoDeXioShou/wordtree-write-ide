package lh.wordtree.task;

import lh.wordtree.comm.BeanFactory;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 注册后台检测系统服务,等待其他task初始化之后初始化该程序
 */
@Task(name = "文件更改监听", value = -999)
public class FileBackground implements WTTask {
    public static final Logger logger = Logger.getLogger(FileBackground.class);
    @Override
    public void init() {
        // 监控目录
        String rootDir = BeanFactory.nowRootFile.get().getPath();
        logger.info("正在监听文件夹..."+rootDir);
        // 轮询间隔 5 秒
        Integer time = 10;
        long interval = TimeUnit.SECONDS.toMillis(time);
        // 创建一个文件观察器用于处理文件的格式,
        // FileFilterUtils.suffixFileFilter(".txt")
        FileAlterationObserver _observer = new FileAlterationObserver(
                rootDir,
                FileFilterUtils.and(
                        FileFilterUtils.fileFileFilter()),  //过滤文件格式
                null);
        FileAlterationObserver observer = new FileAlterationObserver(rootDir);
        observer.addListener(new FileListener()); //设置文件变化监听器
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class FileListener  extends FileAlterationListenerAdaptor {
        public static final Logger logger = Logger.getLogger(FileListener.class);

        @Override
        public void onStart(FileAlterationObserver observer) {
//             System.out.println("启动监听器：");
        }
        @Override
        public void onDirectoryCreate(File directory) {
            logger.info("有新文件夹生成："+directory.getName());
        }
        @Override
        public void onDirectoryChange(File directory) {
            logger.info("有文件夹内容发生变化："+directory.getName());
        }
        @Override
        public void onDirectoryDelete(File directory) {
            logger.info("有文件夹被删除："+directory.getName());
        }
        @Override
        public void onFileCreate(File file){
            logger.info("有新文件生成："+file.getName());
        }

        @Override
        public void onFileChange(File file){
            logger.info("有文件被修改："+file.getName());
        }

        @Override
        public void onFileDelete(File file){
            logger.info("有文件被删除："+file.getName());
        }

        @Override
        public void onStop(FileAlterationObserver observer){
//             System.out.println("监听停止");
        }
    }
}
