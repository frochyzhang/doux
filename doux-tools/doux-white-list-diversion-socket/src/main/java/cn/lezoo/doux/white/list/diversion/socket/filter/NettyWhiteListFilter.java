package cn.lezoo.doux.white.list.diversion.socket.filter;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.white.list.diversion.socket.client.ForwardClientService;
import cn.lezoo.doux.white.list.diversion.socket.config.WhiteListConfig;
import cn.lezoo.doux.white.list.diversion.socket.service.WhiteListService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanghf
 * @date 2024/3/18 20:53
 */
public class NettyWhiteListFilter extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyWhiteListFilter.class);

    private final String name;
    private final WhiteListService whiteListService;
    private final WhiteListConfig whiteListConfig;
    private final ForwardClientService forwardClientService;

    public NettyWhiteListFilter(String name) {
        this.name = name;
        this.whiteListService = SpringUtil.getBean(WhiteListService.class);
        this.whiteListConfig = SpringUtil.getBean(WhiteListConfig.class);
        this.forwardClientService = SpringUtil.getBean(ForwardClientService.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("收到请求内容：{}", msg);
        String reqMsg = (String) msg;
        if (!whiteListConfig.getOnFlag()) {
            LOGGER.info("白名单功能未开启");
            super.channelRead(ctx, msg);
            return;
        }
        LOGGER.info("白名单功能已开启");
        boolean isWhiteList = whiteListService.isWhiteList(reqMsg);
        if (isWhiteList) {
            LOGGER.info("在白名单中，转发到新服务");
            String forwardResult = forwardClientService.forward(reqMsg);
            LOGGER.info("转发完成，响应内容：{}", forwardResult);
            ctx.writeAndFlush(forwardResult);
        } else {
            LOGGER.info("不在白名单中，不进行转发");
            super.channelRead(ctx, msg);
        }
    }
}
