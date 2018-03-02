package com.liumapp.async.http.core.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class PrefixThreadFactory implements ThreadFactory {

    private final String mPrefix;
    private final AtomicInteger mNum = new AtomicInteger(0);

    public PrefixThreadFactory(String prefix) {
        mPrefix = prefix;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, mPrefix + "#" + mNum.incrementAndGet());
        return t;
    }
}