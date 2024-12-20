package cn.lezoo.doux.socket.server.tool.hsp;

public class RequestIdContext {
    private static final ThreadLocal<Long> requestId = new ThreadLocal<>();

    public static Long getRequestId() {
        return requestId.get();
    }

    public static void setRequestId(Long requestId) {
        RequestIdContext.requestId.set(requestId);
    }

    public static void removeRequestId() {
        requestId.remove();
    }
}
