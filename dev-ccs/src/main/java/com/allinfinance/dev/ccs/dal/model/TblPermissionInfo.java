package com.allinfinance.dev.ccs.dal.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuqi
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PERMISSION_INFO")
public class TblPermissionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url;

    private String permissionCode;

    private String description;


}
