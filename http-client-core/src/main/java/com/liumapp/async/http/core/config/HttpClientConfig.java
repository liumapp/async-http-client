package com.liumapp.async.http.core.config;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMessageDecoder;

/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class HttpClientConfig {

    protected List<String> acceptedContentTypes;
    protected String bossNamePrefix = "Http Boss";
    protected int connectionTimeOutInMs = 4500;
    protected int maxChunkSize = 32 * 1024;
    protected int maxLength = 1024 * 512;
    protected int receiveBuffer = 1024 * 32;
    protected int sendBuffer = 1024 * 32;
    protected int requestTimeoutInMs = 20000;
    protected int timerInterval = 1500;
    protected String userAgent = "Async-java/1.0.0";

    protected String workerNamePrefix = "Http Worker";
    protected int workerThread = 1;

    public HttpClientConfig() {
    }

    public void setAcceptedContentTypes(List<String> acceptedContentTypes) {
        this.acceptedContentTypes = acceptedContentTypes;
    }

    public void setBossNamePrefix(String bossNamePrefix) {
        this.bossNamePrefix = bossNamePrefix;
    }

    public void setConnectionTimeOutInMs(int connectionTimeOutInMs) {
        this.connectionTimeOutInMs = connectionTimeOutInMs;
    }

    /**
     * @see HttpMessageDecoder
     */
    public void setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setReceiveBuffer(int receiveBuffer) {
        this.receiveBuffer = receiveBuffer;
    }

    public void setRequestTimeoutInMs(int requestTimeoutInMs) {
        this.requestTimeoutInMs = requestTimeoutInMs;
    }

    public void setSendBuffer(int sendBuffer) {
        this.sendBuffer = sendBuffer;
    }

    public void setTimerInterval(int timerInterval) {
        this.timerInterval = timerInterval;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setWorkerNamePrefix(String workerNamePrefix) {
        this.workerNamePrefix = workerNamePrefix;
    }

    public void setWorkerThread(int workerThread) {
        this.workerThread = workerThread;
    }

}
