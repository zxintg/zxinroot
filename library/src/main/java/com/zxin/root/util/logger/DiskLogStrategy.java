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

        WriteHandler(@NonNull Looper looper, @NonNull String folder, int maxFileSize) {
            super(Utils.checkNotNull(looper));
            this.folder = Utils.checkNotNull(folder);
            this.maxFileSize = maxFileSize;
        }

        @SuppressWarnings("checkstyle:emptyblock")
        @Override
        public void handleMessage(@NonNull Message msg) {
            String content = (String) msg.obj;

            FileWriter fileWriter = null;
            File logFile = getLogFile(folder, "nionav_");

            try {
                fileWriter = new FileWriter(logFile, true);

                writeLog(fileWriter, content);

                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e1) { /* fail silently */ }
                }
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
            int newFileCount = 0;

            if (fileList != null) {
                for (; newFileCount < fileList.length; newFileCount++) {
                    newFile = new File(folder, fileList[newFileCount]);
                    if (!newFile.exists()) {
                        continue;
                    } else if (newFile.length() >= maxFileSize) {
                        newFile = createNewFile(fileName);
                    } else {
                        break;
                    }
                }
            }

            if (fileList == null || (newFileCount >= fileList.length && newFile == null)) {
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
