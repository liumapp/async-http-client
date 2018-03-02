package com.liumapp.async.http.core.utils;

import static org.jboss.netty.handler.codec.http.HttpHeaders.getContentLength;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

import com.liumapp.async.http.core.config.HttpClientConfig;
import com.liumapp.async.http.core.response.HttpResponseFuture;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;

import static com.liumapp.async.http.core.constant.HttpClientConstant.*;

/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class Decoder extends HttpResponseDecoder {
    final HttpClientConfig conf;

    protected Object decode(ChannelHandlerContext ctx, Channel channel,
                            ChannelBuffer buffer, State state) throws Exception {
        Object o = super.decode(ctx, channel, buffer, state);

        HttpResponseFuture future = (HttpResponseFuture) ctx.getAttachment();
        if (o instanceof HttpMessage && future != null) {
            future.touch();
            HttpMessage msg = (HttpMessage) o;
            String type = msg.getHeader(CONTENT_TYPE);
            if (getContentLength(msg) > conf.getMaxLength()) {
                future.done(TOO_LARGE);
            } else if (type != null && conf.acceptedContentTypes != null) {
                boolean accept = false;
                for (String t : conf.acceptedContentTypes) {
                    if (type.contains(t)) {
                        accept = true;
                        break;
                    }
                }
                if (!accept) {
                    future.done(UNKNOWN_CONTENT);
                }
            }
        }
        return o;
    }

    public Decoder(HttpClientConfig conf) {
        super(4096, 8192, conf.maxChunkSize);
        this.conf = conf;
    }
}