package cn.lezoo.doux.rpc.scaffold.advice.dto;

import cn.lezoo.doux.common.dictionary.advice.ResponseCode;

/**
 * @author huanghf
 * @date 2023/3/21 09:30
 */
public class SofaResponseDTO<T> {
    /**
     * 响应数据
     */
    private T data;
    /**
     * 请求是否成功
     */
    private Boolean success;
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应信息
     */
    private String message;

    private SofaResponseDTO() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SofaResponseDTO{" +
                "data=" + data +
                ", success=" + success +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public static <T> SofaResponseDTO<T> success(T data) {
        SofaResponseDTO<T> sofaResponseDTO = new SofaResponseDTO<>();
        sofaResponseDTO.setData(data);
        sofaResponseDTO.setSuccess(Boolean.TRUE);
        sofaResponseDTO.setCode(BaseResponseCodeEnum.SUCCESS.getCode());
        sofaResponseDTO.setMessage(BaseResponseCodeEnum.SUCCESS.getMessage());
        return sofaResponseDTO;
    }

    public static <T> SofaResponseDTO<T> success(T data, ResponseCode responseCode) {
        SofaResponseDTO<T> sofaResponseDTO = new SofaResponseDTO<>();
        sofaResponseDTO.setData(data);
        sofaResponseDTO.setSuccess(Boolean.TRUE);
        sofaResponseDTO.setCode(responseCode.getCode());
        sofaResponseDTO.setMessage(responseCode.getMessage());
        return sofaResponseDTO;
    }

    public static <T> SofaResponseDTO<T> fail() {
        SofaResponseDTO<T> sofaResponseDTO = new SofaResponseDTO<>();
        sofaResponseDTO.setSuccess(Boolean.FALSE);
        sofaResponseDTO.setCode(BaseResponseCodeEnum.FAIL.getCode());
        sofaResponseDTO.setMessage(BaseResponseCodeEnum.FAIL.getMessage());
        return sofaResponseDTO;
    }

    public static <T> SofaResponseDTO<T> fail(ResponseCode responseCode) {
        SofaResponseDTO<T> sofaResponseDTO = new SofaResponseDTO<>();
        sofaResponseDTO.setSuccess(Boolean.FALSE);
        sofaResponseDTO.setCode(responseCode.getCode());
        sofaResponseDTO.setMessage(responseCode.getMessage());
        return sofaResponseDTO;
    }
}
