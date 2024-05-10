package cn.lezoo.doux.framework.conn.wrapper.constant.enums;

/**
 * @author qipeng
 * @date 2022/6/14 15:20
 */
public enum ConnectionStatus {
    /**
     * 连接活跃
     */
    ACTIVE,
    /**
     * 超时的活跃连接
     */
    TIMEOUT,
    /**
     * 从活跃连接到空闲连接的中间态
     */
    PENDING,
    /**
     * 无效的连接
     */
    INACTIVE
}
