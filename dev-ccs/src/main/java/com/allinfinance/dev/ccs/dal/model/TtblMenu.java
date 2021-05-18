package com.allinfinance.dev.ccs.dal.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TTBL_MENU")
public class TtblMenu implements Serializable {

    private static final long serialVersionUID=1L;

   // "菜单编码需唯一"
    private String menuId;

    private String menuName;

    private String parentMid;

    //"节点类型，1文件夹，2页面，3按钮"
    private String nodeType;

    private String icon;

    private String sort;

    private String pageUrl;

    // "0：一级目录，1：一级菜单，2：二级菜单"
    private String level;

    // "树id的路径 整个层次上的路径id，逗号分隔，想要找父节点特别快"
    private String path;

   //"1：不可用；0：可用"
    private String isAvailable;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private String reservedField1;

    private String reservedField2;

    private String reservedField3;


}
