package cn.lezoo.doux.white.list.diversion.http.backup;

/**
 * @author huanghf
 * @date 2024/3/26 16:25
 */
public class BackupInfo {
    private String tradeId;
    private TradeDirection tradeDirection;
    private String timeStamp;
    private String source;
    private String destination;
    private MessageInfo message;

    public String getTradeId() {
        return tradeId;
    }

    public BackupInfo setTradeId(String tradeId) {
        this.tradeId = tradeId;
        return this;
    }

    public TradeDirection getTradeDirection() {
        return tradeDirection;
    }

    public BackupInfo setTradeDirection(TradeDirection tradeDirection) {
        this.tradeDirection = tradeDirection;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public BackupInfo setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getSource() {
        return source;
    }

    public BackupInfo setSource(String source) {
        this.source = source;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public BackupInfo setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public MessageInfo getMessage() {
        return message;
    }

    public BackupInfo setMessage(MessageInfo message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "BackupInfo{" +
                "tradeId='" + tradeId + '\'' +
                ", tradeDirection=" + tradeDirection +
                ", timeStamp='" + timeStamp + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", message=" + message +
                '}';
    }
}
