package cn.lezoo.doux.white.list.diversion.socket.backup;

/**
 * @author huanghf
 * @date 2024/3/26 15:27
 */
public class BackupInfo {
    private String aicTradeId;
    private TradeDirection tradeDirection;
    private String message;

    public String getAicTradeId() {
        return aicTradeId;
    }

    public BackupInfo setAicTradeId(String aicTradeId) {
        this.aicTradeId = aicTradeId;
        return this;
    }

    public TradeDirection getTradeDirection() {
        return tradeDirection;
    }

    public BackupInfo setTradeDirection(TradeDirection tradeDirection) {
        this.tradeDirection = tradeDirection;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BackupInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "BackupInfo{" +
                "aicTradeId='" + aicTradeId + '\'' +
                ", tradeDirection=" + tradeDirection +
                ", message='" + message + '\'' +
                '}';
    }
}
