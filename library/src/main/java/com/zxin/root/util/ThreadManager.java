package com.zxin.root.util;

import android.os.Handler;
import android.os.Looper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The file is the thread pool manager
 */
public class ThreadManager {

    private static final int CORE_THREAD_POOL_SIZE = 3;
    private static ScheduledExecutorService mExecutorService;
    private static Map<Integer, Future> mTaskMap = new HashMap<>();

    /**
     * Init thread pool.
     */
    public static void init() {
        if (null == mExecutorService) {
            mExecutorService = Executors.newScheduledThreadPool(CORE_THREAD_POOL_SIZE);
        }
        clear();
    }

    /**
     * Clear thread pool
     */
    public static void clear() {
        Collection<Future> futures = mTaskMap.values();
        if (futures.size() > 0) {
            for (Future future : futures) {
                stop(future.hashCode());
            }
        }
    }

    /**
     * Performs a task in a thread.
     *
     * @param runnable task runnable
     * @return Task name, use it to stop task.
     */
    public static Integer post(Runnable runnable) {
        if (null == mExecutorService) {
            init();
        }
        Future future = mExecutorService.submit(runnable);
        Integer taskName = future.hashCode();
        mTaskMap.put(future.hashCode(), future);
        return taskName;
    }

    /**
     * Delay runs a task in a thread.
     *
     * @param runnable task runnable
     * @delay delay to run the task
     * @return Task name, use it to stop task.
     */
    public static Integer postDelayed(Runnable runnable, long delay) {
        if (null == mExecutorService) {
            init();
        }
        Future future = mExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        Integer taskName = future.hashCode();
        mTaskMap.put(future.hashCode(), future);
        return taskName;
    }

    /**
     * Finish the specified task.
     *
     * @param taskName
     */
    public static void stop(Integer taskName) {
        Future future = mTaskMap.get(taskName);
        if (null != future) {
            mTaskMap.remove(taskName);
            if (!future.isDone() && !future.isCancelled() && (null != mExecutorService)) {
                future.cancel(true);
            }
        }
    }


    private static Handler sMainHandler;

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    public static final void runOnUiThread(Runnable action) {

        if (!isUIThread()) {
            getMainHandler().post(action);
        } else {
            action.run();
        }
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
    public static final void runOnUiThread(Runnable action, long delay) {
        getMainHandler().postDelayed(action, delay);
    }

    public static final void runOnSubThread(Runnable action) {
        if (!isUIThread()) {
            action.run();
        } else {
            post(action);
        }

    }

    public static final void runOnSubThread(Runnable action, long delay) {
        if (!isUIThread()) {
            action.run();
        } else {
            postDelayed(action, delay);
        }
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

    public static Future getThread(int futureCode){
        Future future = mTaskMap.get(futureCode);
        return future;
    }
}
