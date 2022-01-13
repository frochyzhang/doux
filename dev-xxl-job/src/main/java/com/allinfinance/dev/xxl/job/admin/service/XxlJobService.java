package com.allinfinance.dev.xxl.job.admin.service;


import com.allinfinance.dev.xxl.job.admin.dto.ChartInfoResponseDTO;
import com.allinfinance.dev.xxl.job.admin.dto.IndexInfoResponseDTO;

import java.util.Date;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {
    /**
     * dashboard info
     *
     * @return
     */
    public IndexInfoResponseDTO dashboardInfo();

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public ChartInfoResponseDTO chartInfo(Date startDate, Date endDate);

}
