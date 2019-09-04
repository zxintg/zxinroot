package com.zxin.root.util;

import android.os.Handler;
import android.os.Looper;

import com.zxin.root.util.logger.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The file is the thread pool manager
 */
public class ThreadManager {

    public static final LogUtils.Tag TAG = new LogUtils.Tag("ThreadManager");

    private static final int CORE_THREAD_POOL_SIZE = 3;
    private static ScheduledExecutorService mExecutorService;


    static {
        init();
    }

    /**
     * Init thread pool.
     */
    public synchronized final static void init() {
        if (null == mExecutorService) {
            LogUtils.d(TAG, "Init Thread Pool Core Pool Size: " + CORE_THREAD_POOL_SIZE);
            mExecutorService = new ThreadPool(CORE_THREAD_POOL_SIZE);
        }
    }

    public static class ThreadPool extends ScheduledThreadPoolExecutor {

        public ThreadPool(int corePoolSize) {
            super(corePoolSize);
        }

        @Override
        public void shutdown() {
            //super.shutdown();
            LogUtils.e(TAG, "Not allow to shutdown");
        }
    }

    public static final ExecutorService getThreadPool() {
        return mExecutorService;
    }

    /**
     * Clear thread pool
     */
    public static final void clear() {

    }

    /**
     * Performs a task in sub thread.
     *
     * @param runnable task runnable
     * @return Task name, use it to stop task.
     */
    public static final Future post(Runnable runnable) {
        return mExecutorService.submit(runnable);
    }

    /**
     * Delay runs a task in a thread.
     *
     * @param runnable task runnable
     * @delay delay to run the task
     * @return Task name, use it to stop task.
     */
    public static final Future postDelayed(Runnable runnable, long delay) {
        return mExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }


    private static Handler sMainHandler;

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    public static final boolean runOnUiThread(Runnable action) {
        if (!isUIThread()) {
            return getMainHandler().post(action);
        } else {
            action.run();
        }
        return true;
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     * @param delay delayMillis The delay (in milliseconds) until the Runnable
     *        will be executed.
     */
    public static final boolean runOnUiThread(Runnable action, long delay) {
        return getMainHandler().postDelayed(action, delay);
    }

    public static void runOnSubThread(Runnable action) {
        runOnSubThread(action, 0);
    }

    public static final void runOnSubThread(Runnable action, long delay) {
        postDelayed(action, delay);
    }

    public static final boolean isUIThread() {
        Looper looper = Looper.getMainLooper();
        return Thread.currentThread() == looper.getThread();
    }

    public final static Handler getMainHandler() {
        if (sMainHandler == null) {
            synchronized (ThreadManager.class) {
                if (sMainHandler == null) {
                    sMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sMainHandler;
    }

    public final static Thread newThread(Runnable runnable, String name) {
        return new Thread(runnable, "BN_Thread_" + name);
    }

    public final static Future startPeriodTask(Runnable runnable, long period) {
        return startPeriodTask(runnable, 0, period);
    }

    public final static Future startPeriodTask(Runnable runnable, long delay, long period) {
        return mExecutorService.scheduleWithFixedDelay(runnable, delay, period, TimeUnit.MILLISECONDS);
    }
}
