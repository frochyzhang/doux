package cn.lezoo.doux.framework.conn.wrapper.pool.exception;

/**
 * @author huanghf
 * @date 2024/5/16 12:59
 */
public class PoolException extends RuntimeException {
    public PoolException(String message) {
        super(message);
    }

    public PoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
