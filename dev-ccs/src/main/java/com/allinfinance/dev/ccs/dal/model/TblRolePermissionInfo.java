package com.allinfinance.dev.ccs.dal.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("TBL_ROLE_PERMISSION_INFO")
@Accessors(chain = true)
public class TblRolePermissionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String roleId;

    private String permissionCode;


}
