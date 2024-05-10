package cn.lezoo.doux.dispatch.scaffold.api;

/**
 * @author qipeng
 * @date 2022/1/20 11:38
 */

public interface IJobHandler {
    /**
     * 设置任务名
     *
     * @return 任务名
     */
    String dispatcherName();

    /**
     * 任务执行方法
     */
    void execute() throws Exception;
}
