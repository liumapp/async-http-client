package com.liumapp.async.http.core.factory;

import com.liumapp.async.http.core.config.HttpClientConfig;
import org.jboss.netty.channel.ChannelPipelineFactory;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import javax.net.ssl.SSLEngine;

/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class HttpsClientPipelineFactory implements ChannelPipelineFactory {

    final int maxLength;
    final HttpClientConfig conf;

    public HttpsClientPipelineFactory(int maxLength, HttpClientConfig conf) {
        this.maxLength = maxLength;
        this.conf = conf;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
        SSLEngine engine = SslContextFactory.getClientContext()
                .createSSLEngine();
        engine.setUseClientMode(true);
        pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new Decoder(conf));
        pipeline.addLast("encoder", new HttpRequestEncoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(maxLength));
        pipeline.addLast("handler", new ResponseHandler());
        return pipeline;
    }

}
