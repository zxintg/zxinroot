package com.zxin.root.listener;

import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基础的监听器集合(弱引用), 特点:<br>
 * 1.只会以弱引用持有监听器,避免内存泄露<br>
 * 2.通知时发现引用不存在就会自动清理<br>
 *
 * @param <E> 具体的监听器类型,必须实现{@link Listener.GenericListener}
 */
public class WeakGenericListeners<E extends BaseEventInfo> {
    /* Add this by Alex.Liu start */
    private ReadWriteLock mLock = new ReentrantReadWriteLock();
    /* Add this by Alex.Liu end */
    /**
     * 各监听器引用集合
     */
    private WeakHashMap<Listener.GenericListener<E>, Object> references = new WeakHashMap<Listener.GenericListener<E>, Object>();

    /**
     * 添加监听器
     *
     * @param listener
     */
    public void add(Listener.GenericListener<E> listener) {
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

    public void remove(Listener.GenericListener<E> listener) {
        /* Modify this by Alex.Liu start */
        //references.remove(listener);
        Lock lock = mLock.writeLock();
        try {
            lock.lock();
            references.remove(listener);
        } finally {
            lock.unlock();
        }
        /* Modify this by Alex.Liu start */
    }

    /**
     * 通知事件到各监听器
     *
     * @param e
     */
    public void conveyEvent(E e) {
        /* Modify this by Alex.Liu start */
        /*for (GenericListener<E> listener : this.references.keySet()) {
            listener.onEvent(e);
        }*/
        Lock lock = mLock.writeLock();
        try {
            lock.lock();
            for (Listener.GenericListener<E> listener : this.references.keySet()) {
                listener.onEvent(e);
            }
        } finally {
            lock.unlock();
        }
        /* Modify this by Alex.Liu start */
    }


}
