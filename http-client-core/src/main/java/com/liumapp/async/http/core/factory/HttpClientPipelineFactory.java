package com.liumapp.async.http.core.factory;

import com.liumapp.async.http.core.config.HttpClientConfig;
import com.liumapp.async.http.core.response.ResponseHandler;
import com.liumapp.async.http.core.utils.Decoder;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;

/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class HttpClientPipelineFactory implements ChannelPipelineFactory {

    final int maxLength;
    final HttpClientConfig conf;

    public HttpClientPipelineFactory(int maxLength, HttpClientConfig conf) {
        this.maxLength = maxLength;
        this.conf = conf;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
        pipeline.addLast("decoder", new Decoder(conf));
        pipeline.addLast("encoder", new HttpRequestEncoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(maxLength));
        pipeline.addLast("handler", new ResponseHandler());
        return pipeline;
    }
}