package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.core.util.transmit.FtpUtils;
import com.allinfinance.dev.core.util.transmit.SftpUtils;
import com.allinfinance.dev.core.util.transmit.TransmitModeEnum;
import com.allinfinance.dev.core.util.transmit.TransmitService;
import com.allinfinance.dev.core.util.transmit.TransmitSysDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * TransmitServiceImpl
 *
 * @author hongmr
 * @date 2017/3/30
 */
@Service("transmitService")
public class TransmitServiceImpl implements TransmitService {

    private Logger logger = LoggerFactory.getLogger(TransmitServiceImpl.class);

    @Value("${dev.ccp.file.localSysPath:}")
    private String localSysPath;

    @Override
    public boolean fileDownload(TransmitSysDto sourceSys, String sourcePath, String fileName) {
        boolean downloadRes = false;
        try {
            if (sourceSys == null || StringUtils.isEmpty(fileName)) {
                logger.error("传入参数异常！");
                return false;
            } else if (TransmitModeEnum.valueByCode(sourceSys.getTransmitMode()) == null) {
                logger.error("不支持的传输方式:{}", sourceSys.toString());
                return false;
            }

            String orgId = StringUtils.isEmpty(sourceSys.getOrgId()) ? "local" : sourceSys.getOrgId();

            String localFileFullName = localSysPath + File.separator + orgId + File.separator + fileName;
            logger.info("本地临时文件路径:{}", localFileFullName);
            logger.info("开始文件下载.");
            if (StringUtils.isEmpty(sourcePath)) {
                sourcePath = sourceSys.getDefaultPath();
            }
            if (TransmitModeEnum.MODE_FTP.getCode().equals(sourceSys.getTransmitMode())) {
                downloadRes = FtpUtils.downloadFileResume(sourceSys.getSysIp(), sourceSys.getSysPort(), sourceSys.getSysUser(), sourceSys.getSysPassword(), sourcePath, fileName, localFileFullName);
            } else if (TransmitModeEnum.MODE_SFTP.getCode().equals(sourceSys.getTransmitMode())) {
                downloadRes = SftpUtils.download(sourceSys.getSysIp(), sourceSys.getSysPort(), sourceSys.getSysUser(),
                        sourceSys.getSysPassword(), sourcePath, fileName, localFileFullName);
            }
            if (!downloadRes) {
                logger.error("下载文件失败,sourcePath:{},sourceSys:{},fileName:{}", sourcePath, sourceSys.toString(), fileName);
                return false;
            }
        } catch (Exception e) {
            logger.error("下载文件出现异常!", e);
//            e.printStackTrace();
            return false;
        }
        logger.info("文件下载成功:{},From:{},To:{}", fileName, sourceSys.toString(), localSysPath);
        return true;
    }

    @Override
    public boolean fileUpload(TransmitSysDto destSys, String destPath, String fileName) {
        boolean uploadRes = false;
        try {
            if (destSys == null || StringUtils.isEmpty(fileName)) {
                logger.error("传入参数异常！");
                return false;
            } else if (TransmitModeEnum.valueByCode(destSys.getTransmitMode()) == null) {
                logger.error("不支持的传输方式:{}", destSys.toString());
                return false;
            }

            String orgId = StringUtils.isEmpty(destSys.getOrgId()) ? "local" : destSys.getOrgId();
            String localFileFullName = localSysPath + File.separator + orgId + File.separator + fileName;
            logger.info("本地临时文件路径:{}", localFileFullName);
            if (StringUtils.isEmpty(destPath)) {
                destPath = destSys.getDefaultPath();
            }
            logger.info("开始文件上传.");
            if (TransmitModeEnum.MODE_FTP.getCode().equals(destSys.getTransmitMode())) {
                uploadRes = FtpUtils.uploadFileResume(destSys.getSysIp(), destSys.getSysPort(), destSys.getSysUser(),
                        destSys.getSysPassword(), destPath, fileName, localFileFullName);
            } else if (TransmitModeEnum.MODE_SFTP.getCode().equals(destSys.getTransmitMode())) {
                uploadRes = SftpUtils.upload(destSys.getSysIp(), destSys.getSysPort(), destSys.getSysUser(),
                        destSys.getSysPassword(), destPath, fileName, localFileFullName);
            }
            if (!uploadRes) {
                logger.error("上传文件失败,dest:{},fileName:{}", destSys.toString(), fileName);
                return false;
            }
        } catch (Exception e) {
            logger.error("上传文件出现异常!", e);
//            e.printStackTrace();
            return false;
        }
        logger.info("文件上传成功:{},From:{},To:{}", fileName, localSysPath, destSys.toString());
        return true;
    }

    @Override
    public boolean fileDownloadAndUpload(TransmitSysDto sourceSys, TransmitSysDto destSys, String sourcePath, String destPath, String fileName) {
        return this.fileDownload(sourceSys, sourcePath, fileName)
                && this.fileUpload(destSys, destPath, fileName);
    }
}
