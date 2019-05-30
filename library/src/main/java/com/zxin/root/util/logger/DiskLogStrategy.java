package com.zxin.root.util.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.zxin.root.util.GlobalUtil;
import com.zxin.root.util.Utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract class that takes care of background threading the file log operation on Android.
 * implementing classes are free to directly perform I/O operations there.
 * <p>
 * Writes all logs to the disk with CSV format.
 */
public class DiskLogStrategy implements LogStrategy {
    private static final LogUtils.Tag TAG = new LogUtils.Tag("DiskLogStrategy");

    @NonNull
    private final Handler handler;

    public DiskLogStrategy(@NonNull Handler handler) {
        this.handler = Utils.checkNotNull(handler);
    }

    @Override
    public void log(int level, @Nullable String tag, @NonNull String message) {
        if (!GlobalUtil.getIsRecordLog()) {
            return;
        }
        Utils.checkNotNull(message);

        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(level, message));
    }

    static class WriteHandler extends Handler {

        @NonNull
        private final String folder;
        private final int maxFileSize;

        private File logFile;
        private FileWriter fileWriter;

        WriteHandler(@NonNull Looper looper, @NonNull String folder, int maxFileSize) {
            super(Utils.checkNotNull(looper));
            this.folder = Utils.checkNotNull(folder);
            this.maxFileSize = maxFileSize;
        }

        @SuppressWarnings("checkstyle:emptyblock")
        @Override
        public void handleMessage(@NonNull Message msg) {
            String content = (String) msg.obj;
            try {
                if (logFile == null || logFile.length() >= maxFileSize) {
                    closeFileWriter(fileWriter);
                    logFile = getLogFile(folder, "nionav_");
                    fileWriter = new FileWriter(logFile, true);
                }

                writeLog(fileWriter, content);
                //flush操作可以保证内容写入到文件中
                fileWriter.flush();
            } catch (IOException e) {
                closeFileWriter(fileWriter);
                logFile = null;
                fileWriter = null;
            }
        }

        private void closeFileWriter(FileWriter fileWriter) {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e1) { /* fail silently */ }
            }
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
            Utils.checkNotNull(fileWriter);
            Utils.checkNotNull(content);

            fileWriter.append(content);
        }

        /******
         * 获取最近一次日志文件，判断日志内容是否已达到最大上限了，
         * 1.如果达到重新新建一个
         * 2.如果没有达到，获取该日志文件继续写入系统日志信息
         * 3.不存在文件新建文件
         *
         * @param folderName 文件夹路径
         * @param fileName 部分文件名称
         * @return 日志文件
         */
        private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
            Utils.checkNotNull(folderName);
            Utils.checkNotNull(fileName);

            File folder = new File(folderName);
            if (!folder.exists()) {
                //TODO: What if folder is not created, what happens then?
                folder.mkdirs();
            }

            File appLogDir = new File(GlobalUtil.getAppLogDirPath());
            String[] fileList = appLogDir.list();

            File newFile = null;
            int newFileCount = fileList.length;
            //通过倒叙获取最新的文件，判断是否可以继续写入信息
            if (fileList != null) {
                for (; newFileCount > 0; newFileCount--) {
                    newFile = new File(folder, fileList[newFileCount - 1]);
                    if (!newFile.exists()) {
                        newFile = null;//文件不存在，需要置空
                        continue;
                    } else {
                        if (newFile.length() >= maxFileSize) {//文件内容已达上限，新建文件
                            newFile = createNewFile(fileName);
                        }
                        break;
                    }
                }
            }

            if (fileList == null
                    || (newFileCount <= fileList.length && newFile == null)
                    || (newFile != null && !newFile.exists())) {//无效文件需要新建
                newFile = createNewFile(fileName);
            }

            return newFile;
        }

        private File createNewFile(String fileName) {
            Date dt = new Date();
            long lSysTime1 = dt.getTime() / 1000;   //得到秒数，Date类型的getTime()返回毫秒数

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd__HH_mm_ss");
            dt = new Date(lSysTime1 * 1000);
            String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00

            File newFile = new File(folder, String.format("%s%s.txt", fileName, sDateTime));

            return newFile;
        }
    }
}
