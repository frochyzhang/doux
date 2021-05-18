package com.allinfinance.dev.ccs.dal.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_BANK")
public class TblBank implements Serializable {

    private static final long serialVersionUID=1L;

    private String bankId;

    private String bankName;

   // "1：不可用；0：可用"
    private String isAvailable;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private String reservedField1;

    private String reservedField2;

    private String reservedField3;


}
