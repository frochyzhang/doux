package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.common.dictionary.result.Result;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    Result reLogin(@RequestBody SecondCheckPassVo checkPassVo);

    Result getCurrentUser(HttpServletRequest request);

    Result getQrCodeUrl(String userName, String userId, HttpServletRequest request, HttpServletResponse response);
}
