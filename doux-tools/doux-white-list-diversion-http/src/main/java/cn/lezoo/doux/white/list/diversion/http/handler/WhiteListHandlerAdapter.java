package cn.lezoo.doux.white.list.diversion.http.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanghf
 * @date 2024/3/20 20:19
 */
@Component
public class WhiteListHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof WhiteListHandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return 0;
    }
}
