package com.zxin.root.listener;

import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基础的监听器集合（弱引用）, 特点：<br>
 * 1、只会以弱引用持有监听器，避免内存泄露<br>
 * 2、通知时发现引用不存在就会自动清理<br>
 *
 * @author baimi
 */
public class WeakSuccinctListeners {

    /* Add this by Alex.Liu start */
    private ReadWriteLock mLock = new ReentrantReadWriteLock();
    /* Add this by Alex.Liu end */
    /**
     * 各监听器引用集合
     */
    private WeakHashMap<Listener.SuccinctListener, Object> references = new WeakHashMap<Listener.SuccinctListener, Object>();

    /**
     * 添加监听器
     *
     * @param listener
     */
    public void add(Listener.SuccinctListener listener) {
        /* Modify this by Alex.Liu start */
        //references.put(listener, null);
        Lock lock = mLock.writeLock();
        try {
            lock.lock();
            references.put(listener, null);
        } finally {
            lock.unlock();
        }
        /* Modify this by Alex.Liu end */
    }

    /**
     * 通知事件到各个监听器
     */
    public void conveyEvent() {
        /* Modify this by Alex.Liu start */
        /*for (Listener.SuccinctListener listener : references.keySet()) {
            listener.onEvent();
        }*/
        Lock lock = mLock.writeLock();
        try {
            lock.lock();
            for (Listener.SuccinctListener listener : references.keySet()) {
                listener.onEvent();
            }
        } finally {
            lock.unlock();
        }
        /* Modify this by Alex.Liu end */
    }

}
