package com.allinfinance.dev.ccs.dal.mapper;


import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
public interface TblMenuAuthMapper {

    List<TblMenuAuth> selectBatchIds(@Param("authIds") ArrayList<String> authIds);

    List<TblMenuAuth> selectMenuAuths();

    int deleteMenuAuths(String authId);

    int insertSelective(TblMenuAuth record);
}
