package com.liumapp.async.http.core.response;

/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
import java.util.concurrent.Future;

public interface ResponseFuture<V> extends Future<V> {

    boolean isTimeout();

    boolean done(V result);

    void touch();

    boolean abort(Throwable t);

    void addListener(Runnable listener);
}
