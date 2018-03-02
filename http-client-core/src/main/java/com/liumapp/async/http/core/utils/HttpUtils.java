package com.liumapp.async.http.core.utils;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;
import static org.jboss.netty.handler.codec.compression.ZlibWrapper.GZIP;
import static org.jboss.netty.handler.codec.compression.ZlibWrapper.ZLIB_OR_NONE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_ENCODING;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.TRANSFER_ENCODING;
import static org.jboss.netty.util.CharsetUtil.UTF_8;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.embedder.CodecEmbedderException;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by liumapp on 3/2/18.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class HttpUtils {

    static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static byte[] toBytes(int i) {
        return new byte[] { (byte) (i >> 8), (byte) (i & 0x00ff) };
    }

    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0);
    }

    public static int toInt(byte[] bytes, int start) {
        return (toInt(bytes[start]) << 8) + toInt(bytes[start + 1]);
    }

    public static int toInt(int b) {
        if (b < 0)
            b += 256;
        return b;
    }

    public static boolean isIP(String host) {
        for (int i = 0; i < host.length(); ++i) {
            if (!(Character.isDigit(host.charAt(i)) || host.charAt(i) == '.')) {
                return false;
            }
        }
        return true;
    }

    private static final String CS = "charset=";

    public static String getPath(URI uri) {
        String path = uri.getPath();
        String query = uri.getRawQuery();
        if ("".equals(path))
            path = "/";
        if (query == null)
            return path;
        else
            return path + "?" + query;
    }

    public static List<String> getNameServer() {
        return sun.net.dns.ResolverConfiguration.open().nameservers();
    }

    public static int getPort(URI uri) {
        int port = uri.getPort();
        if (port == -1) {
            if ("https".equals(uri.getScheme()))
                port = 443;
            else
                port = 80;
        }
        return port;
    }

    public static Charset parseCharset(String type) {
        if (type != null) {
            try {
                type = type.toLowerCase();
                int i = type.indexOf(CS);
                if (i != -1) {
                    String charset = type.substring(i + CS.length()).trim();
                    return Charset.forName(charset);
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    static Charset detectCharset(HttpResponse resp) {
        Charset result = parseCharset(resp.getHeader(CONTENT_TYPE));
        if (result == null) {
            // decode a little the find charset=???
            byte[] arr = resp.getContent().array();
            String s = new String(arr, 0, Math.min(350, arr.length), UTF_8);
            int idx = s.indexOf(CS);
            if (idx != -1) {
                int start = idx + CS.length();
                int end = s.indexOf('"', start);
                if (end != -1) {
                    try {
                        result = Charset.forName(s.substring(start, end));
                    } catch (Exception ignore) {
                    }
                }
            }
        }
        return result;
    }

    public static String bodyStr(HttpResponse m) {
        // TODO it's a bug, should not happen, "http://logos.md/2008/"
        m.removeHeader(TRANSFER_ENCODING);

        try {
            String contentEncoding = m.getHeader(CONTENT_ENCODING);
            DecoderEmbedder<ChannelBuffer> decoder = null;
            if ("gzip".equalsIgnoreCase(contentEncoding)
                    || "x-gzip".equalsIgnoreCase(contentEncoding)) {
                decoder = new DecoderEmbedder<ChannelBuffer>(new ZlibDecoder(
                        GZIP));
            } else if ("deflate".equalsIgnoreCase(contentEncoding)
                    || "x-deflate".equalsIgnoreCase(contentEncoding)) {
                decoder = new DecoderEmbedder<ChannelBuffer>(new ZlibDecoder(
                        ZLIB_OR_NONE));
            }

            ChannelBuffer body = m.getContent();

            if (decoder != null) {
                decoder.offer(body);
                ChannelBuffer b = wrappedBuffer(decoder
                        .pollAll(new ChannelBuffer[decoder.size()]));
                if (decoder.finish()) {
                    ChannelBuffer r = wrappedBuffer(decoder
                            .pollAll(new ChannelBuffer[decoder.size()]));
                    body = wrappedBuffer(b, r);
                } else {
                    body = b;
                }
                m.setContent(body);// for detect charset
            }
            Charset ch = detectCharset(m);
            if (ch == null)
                ch = UTF_8;
            return new String(body.array(), 0, body.readableBytes(), ch);
        } catch (CodecEmbedderException e) {
            logger.trace(e.getMessage(), e); // incorrect CRC32 checksum
            return "";
        }
    }

}
