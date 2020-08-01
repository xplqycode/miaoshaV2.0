package com.pxu.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/6/19 9:49
 */
public class NamedThreadFactoryPxu implements ThreadFactory {

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final String mPrefix;

    private final boolean mDaemon;

    private final ThreadGroup mGroup;

    public NamedThreadFactoryPxu() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    public NamedThreadFactoryPxu(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactoryPxu(String prefix, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemon);
        return ret;
    }
}
