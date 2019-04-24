package com.zxin.root.util.logger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.system.Os;

import com.zxin.root.util.AppManager;
import com.zxin.root.util.BaseStringUtils;
import com.zxin.root.util.GlobalUtil;
import com.zxin.root.util.SystemInfoUtil;
import com.zxin.root.util.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * CSV formatted file logging for Android.
 * Writes to CSV the following data:
 * epoch timestamp, ISO8601 timestamp (human-readable), log level, tag, log message.
 */
public class CsvFormatStrategy implements FormatStrategy {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = " ";

    @NonNull
    private final Date date;
    @NonNull
    private final SimpleDateFormat dateFormat;
    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;

    private CsvFormatStrategy(@NonNull Builder builder) {
        Utils.checkNotNull(builder);

        date = builder.date;
        dateFormat = builder.dateFormat;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        Utils.checkNotNull(message);

        String tag = formatTag(onceOnlyTag);
        date.setTime(System.currentTimeMillis());

        StringBuilder builder = new StringBuilder();

        // machine-readable date/time
//        builder.append(Long.toString(date.getTime()));

        // human-readable date/time
        //builder.append(SEPARATOR);
        builder.append(dateFormat.format(date));

        // process
        builder.append(SEPARATOR);
        builder.append(Process.myPid()).append("-").append(Os.getppid()).append("/").append(SystemInfoUtil.getInstance(AppManager.getAppManager().getApplicationContext()).getProcessName());

        // level and tag
        builder.append(SEPARATOR);
        builder.append(BaseStringUtils.logLevel(priority)).append("/").append(tag).append(": ");

        // message
        if (message.contains(NEW_LINE)) {
            // a new line would break the CSV format, so we replace it here
            message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
        }
        builder.append(SEPARATOR);
        builder.append(message);

        // new line
        builder.append(NEW_LINE);

        logStrategy.log(priority, tag, builder.toString());
    }

    @Nullable
    private String formatTag(@Nullable String tag) {
        if (!BaseStringUtils.isEmpty(tag) && !BaseStringUtils.equals(this.tag, tag)) {
            return this.tag + tag;
        }
        return this.tag;
    }


    public static final class Builder {
        private static final int MAX_BYTES = 5 * 1024 * 1024; // 5M averages to a 4000 lines per file
        Date date;
        SimpleDateFormat dateFormat;
        LogStrategy logStrategy;
        String tag = "";

        private Builder() {

        }

        @NonNull
        public Builder date(@Nullable Date val) {
            date = val;
            return this;
        }

        @NonNull
        public Builder dateFormat(@Nullable SimpleDateFormat val) {
            dateFormat = val;
            return this;
        }

        @NonNull
        public Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull
        public CsvFormatStrategy build() {
            if (date == null) {
                date = new Date();
            }
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
            }
            if (logStrategy == null) {
                String folder = GlobalUtil.getAppLogDirPath();

                HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
                ht.start();
                Handler handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), folder, MAX_BYTES);
                logStrategy = new DiskLogStrategy(handler);
            }
            return new CsvFormatStrategy(this);
        }
    }

}
