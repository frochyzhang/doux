package com.allinfinance.dev.core.util.transmit;

/**
 * TransmitService
 *
 * @author hongmr
 * @date 2017/3/30
 */
public interface TransmitService {

    boolean fileDownload(TransmitSysDto sourceSys, String sourcePath, String fileName);

    boolean fileUpload(TransmitSysDto destSys, String destPath, String fileName);

    boolean fileDownloadAndUpload(TransmitSysDto sourceSys, TransmitSysDto destSys, String sourcePath, String destPath, String fileName);
}
