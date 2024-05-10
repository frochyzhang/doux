package cn.lezoo.doux.white.list.diversion.socket.filter;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.white.list.diversion.socket.client.ForwardClientService;
import cn.lezoo.doux.white.list.diversion.socket.config.WhiteListConfig;
import cn.lezoo.doux.white.list.diversion.socket.service.WhiteListService;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanghf
 * @date 2024/3/18 20:24
 */
public class MinaWhiteListFilter extends IoFilterAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinaWhiteListFilter.class);

    private final WhiteListService whiteListService;
    private final WhiteListConfig whiteListConfig;
    private final ForwardClientService forwardClientService;

    public MinaWhiteListFilter() {
        this.whiteListService = SpringUtil.getBean(WhiteListService.class);
        this.whiteListConfig = SpringUtil.getBean(WhiteListConfig.class);
        this.forwardClientService = SpringUtil.getBean(ForwardClientService.class);
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        LOGGER.info("收到请求内容：{}", message);
        String reqMsg = (String) message;
        if (!whiteListConfig.getOnFlag()) {
            LOGGER.info("白名单功能未开启");
            super.messageReceived(nextFilter, session, message);
            return;
        }
        LOGGER.info("白名单功能已开启");
        boolean isWhiteList = whiteListService.isWhiteList(reqMsg);
        if (isWhiteList) {
            LOGGER.info("在白名单中，转发到新服务");
            String forwardResult = forwardClientService.forward(reqMsg);
            LOGGER.info("转发完成，响应内容：{}", forwardResult);
            session.write(forwardResult);
        } else {
            LOGGER.info("不在白名单中，不进行转发");
            super.messageReceived(nextFilter, session, message);
        }
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        super.messageSent(nextFilter, session, writeRequest);
    }
}
