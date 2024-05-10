package cn.lezoo.doux.common.util.convert.simple8583.type;

import java.io.Serializable;

/**
 * <p>报文域类型接口定义.</p>
 * <p>继承Serializable接口，实例需要被序列化</p>
 */
public interface IsoType extends Serializable {

    /**
     * 是否为变长域
     *
     * @return
     */
    boolean isLVar();

    /**
     * 字节数组转换为字符串储存
     *
     * @param bts
     * @return
     */
    String setByteValue(byte[] bts);

    /**
     * 字符串转换为字节数组
     *
     * @param bts
     * @return
     */
    byte[] setValue(String bts);


    /**
     * 变长长度
     *
     * @return
     */
    int varLength(byte[] bytes);
}

