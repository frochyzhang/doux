package cn.lezoo.doux.batch.scaffold.dal.model;

public class TblBatchJobExecutionContext {
    private Long jobExecutionId;

    private String shortContext;

    private byte[] serializedContext;

    public TblBatchJobExecutionContext(Long jobExecutionId, String shortContext) {
        this.jobExecutionId = jobExecutionId;
        this.shortContext = shortContext;
    }

    public TblBatchJobExecutionContext(Long jobExecutionId, String shortContext, byte[] serializedContext) {
        this.jobExecutionId = jobExecutionId;
        this.shortContext = shortContext;
        this.serializedContext = serializedContext;
    }

    public TblBatchJobExecutionContext() {
        super();
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getShortContext() {
        return shortContext;
    }

    public void setShortContext(String shortContext) {
        this.shortContext = shortContext == null ? null : shortContext.trim();
    }

    public byte[] getSerializedContext() {
        return serializedContext;
    }

    public void setSerializedContext(byte[] serializedContext) {
        this.serializedContext = serializedContext;
    }
}
