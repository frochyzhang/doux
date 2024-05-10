package cn.lezoo.doux.white.list.diversion.http.config;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author huanghf
 * @date 2024/2/28 16:05
 */
public class RequestBodyRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] requestBodyByteArray;

    public RequestBodyRequestWrapper(ContentCachingRequestWrapper request) {
        super(request);
        requestBodyByteArray = request.getContentAsByteArray();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBodyByteArray);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
}
