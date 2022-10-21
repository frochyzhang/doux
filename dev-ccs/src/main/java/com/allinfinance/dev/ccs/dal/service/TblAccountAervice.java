package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.common.dictionary.result.Result;

public interface TblAccountAervice {

    Result updateNewPass(UpdatePasswordParam passwordParam);
}
