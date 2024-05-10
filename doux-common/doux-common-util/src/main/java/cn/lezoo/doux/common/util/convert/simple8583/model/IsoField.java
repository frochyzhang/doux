package cn.lezoo.doux.common.util.convert.simple8583.model;


import cn.lezoo.doux.common.util.convert.simple8583.key.SimpleConstants;
import cn.lezoo.doux.common.util.convert.simple8583.util.EncodeUtil;
import cn.lezoo.doux.common.util.convert.simple8583.util.SimpleUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 报文域详情.
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IsoField implements Serializable {

    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlAttribute(name = "type", required = true)
    private String type;

    // 该属性为字节长度
    @XmlAttribute(name = "length")
    private int length = 0;

    // 字符值的value
    private String value;

    // 字节数组值的value
    private byte[] byteValue;

    // 域类型
    private IsoType isoType;

    // 该域是否被使用
    private boolean checked = false;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // 获取本域的IsoType
    public IsoType getIsoType() {
        if (this.isoType == null) {
            this.isoType = IsoType.valueOf(this.type);
        }
        return isoType;
    }

    public String getValue() {
        return value;
    }

    public boolean isChecked() {
        return checked;
    }

    public byte[] getByteValue() {
        return byteValue;
    }

    public void setByteValue(byte[] bts) throws UnsupportedEncodingException {
        this.byteValue = bts;
        this.checked = true;
        switch (this.getIsoType()) {
            case BINARY:
                this.value = EncodeUtil.hex(bts);
                break;
            case NUMERIC:
            case LLVAR_NUMERIC:
            case CHAR:
            case LLVAR:
            case LLLVAR:
                this.value = new String(bts, SimpleConstants.ENCODING);
                break;
            default:
                // 无效设置
                this.checked = false;
                break;
        }
    }

    public void setValue(String value) throws UnsupportedEncodingException {
        // 应用数据域被选中
        this.checked = true;
        this.value = value;
        // 格式化
        format(this.value, this.length);
        if (this.value != null) {
            switch (this.getIsoType()) {
                case BINARY:
                    this.byteValue = EncodeUtil.bcd(this.value);
                    break;
                case NUMERIC:
                case LLVAR_NUMERIC:
                case CHAR:
                case LLVAR:
                case LLLVAR:
                    this.byteValue = this.value.getBytes(SimpleConstants.ENCODING);
                    break;
                default:
                    this.checked = false;
                    break;
            }
        }
    }

    public void format(String value, int length) throws UnsupportedEncodingException {
        if (this.isoType == null) {
            this.isoType = IsoType.valueOf(this.type);
        }
        switch (this.isoType) {
            case CHAR:
                if (value.length() > length) {
                    this.value = this.value.substring(0, length);
                } else if (value.length() < length) {
                    // 将缺少的部分补全空格
                    this.value = EncodeUtil.addBlankRight(this.value, this.length - value.getBytes(SimpleConstants.ENCODING).length, " ");
                }
                break;
            // LLVAR和LLLVAR类型的数据不格式化,
            // 通联NUMBERIC采用ASCII码表示，不用BCD码。所有都加上长度判断，判断最大长度是否
            case LLVAR:
            case LLLVAR:
            case LLVAR_NUMERIC:
                break;
            case NUMERIC:
                if (this.value.length() > length) {
                    throw new IllegalArgumentException("数据域 " + this.id + "长度超出，值为:" + this.value + "，约定长度" + length);
                } else {
                    this.value = EncodeUtil.addBlankLeft(this.value, length - this.value.length(), "0");
                }
                break;
            case BINARY:
                if (this.value.length() != 2 * length) {
                    throw new IllegalArgumentException("数据域 " + this.id + "长度错误，值为:" + this.value + "，约定长度" + length);
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的参数类型：" + this.isoType);
        }
    }

    @Override
    public String toString() {
        return "id=" + this.id + ",type=" + this.type + ",value=" + this.value;
    }

    // 该域是否为1~64/1~128的数据域
    public boolean isAppData() {
        return SimpleUtil.isNumeric(this.id);
    }

}
