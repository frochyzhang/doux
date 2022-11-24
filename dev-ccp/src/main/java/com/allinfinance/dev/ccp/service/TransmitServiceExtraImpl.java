package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.api.transmit.TransmitService;
import com.allinfinance.dev.common.api.transmit.dto.TransmitMode;
import com.allinfinance.dev.common.api.transmit.dto.TransmitRequestDTO;
import com.allinfinance.dev.common.api.transmit.dto.TransmitResponseDTO;
import com.allinfinance.dev.common.util.transmit.FtpUtils;
import com.allinfinance.dev.common.util.transmit.SftpUtils;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author qipeng
 * @date 2022/9/9 16:06
 * @desc
 */
@Service
public class TransmitServiceExtraImpl implements TransmitService {
    private static final Logger logger = LoggerFactory.getLogger(TransmitServiceExtraImpl.class);

    @Value("${dev.ccp.file.local-path}")
    private String localPath;

    /**
     * 从本地上传文件到远程
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    @Override
    public TransmitResponseDTO upload(TransmitRequestDTO requestDTO) {
        logger.info(requestDTO.toString());
        TransmitResponseDTO transmitResponseDTO = new TransmitResponseDTO();
        String realLocalPath = null;
        if (requestDTO.getAppend()) {
            realLocalPath = localPath + File.separator + requestDTO.getLocalPath();
        } else {
            if (StringUtils.isBlank(requestDTO.getLocalPath())) {
                logger.error("localPath不能为空");
                transmitResponseDTO.setSuccess(false);
                transmitResponseDTO.setReason("localPath为空");
                return transmitResponseDTO;
            }
            realLocalPath = requestDTO.getLocalPath();
        }
        logger.info("本地文件路径={}", realLocalPath);
        TransmitRequestDTO.RemoteMessage target = requestDTO.getTarget();
        TransmitMode mode = target.getTransmitMode();
        if (mode.matches("FTP")) {
            logger.info("开始进行FTP模式上传文件");
            try {
                boolean upload = FtpUtils.upload(target.getIp(), target.getPort(), target.getUsername(),
                        target.getPassword(), target.getPath(), requestDTO.getFileName(), realLocalPath);
                transmitResponseDTO.setSuccess(upload);
            } catch (IOException e) {
                logger.error("FTP上传文件失败");
                transmitResponseDTO.setSuccess(false);
            }
        } else if (mode.matches("SFTP")) {
            logger.info("开始进行SFTP模式上传文件");
            boolean passwordFree = StringUtils.isBlank(target.getPassword());
            boolean upload = SftpUtils.upload(target.getIp(), target.getPort(), target.getUsername(), target.getPassword(),
                    target.getPath(), realLocalPath, requestDTO.getFileName(), requestDTO.getTimeout(), passwordFree);
            transmitResponseDTO.setSuccess(upload);
        } else {
            logger.error("传输模式不支持");
        }
        return transmitResponseDTO;
    }

    /**
     * 从远程下载文件到本地
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    @Override
    public TransmitResponseDTO download(TransmitRequestDTO requestDTO) {
        logger.info(requestDTO.toString());
        TransmitResponseDTO transmitResponseDTO = new TransmitResponseDTO();
        TransmitRequestDTO.RemoteMessage source = requestDTO.getSource();
        TransmitMode mode = source.getTransmitMode();

        String realLocalPath = null;
        if (requestDTO.getAppend()) {
            realLocalPath = localPath + File.separator + requestDTO.getLocalPath();
        } else {
            if (StringUtils.isBlank(requestDTO.getLocalPath())) {
                logger.error("localPath不能为空");
                transmitResponseDTO.setSuccess(false);
                transmitResponseDTO.setReason("localPath为空");
                return transmitResponseDTO;
            }
            realLocalPath = requestDTO.getLocalPath();
        }
        logger.info("本地文件路径={}", realLocalPath);
        if (mode.matches("FTP")) {
            logger.info("开始进行FTP模式下载文件");
            try {
                boolean download = FtpUtils.download(source.getIp(), source.getPort(), source.getUsername(),
                        source.getPassword(), source.getPath(), requestDTO.getFileName(), realLocalPath);
                transmitResponseDTO.setSuccess(download);
            } catch (IOException e) {
                logger.error("FTP文件下载失败");
                transmitResponseDTO.setSuccess(false);
            }
        } else if (mode.matches("SFTP")) {
            logger.info("开始进行SFTP模式下载文件");
            boolean passwordFree = StringUtils.isBlank(source.getPassword());
            boolean download = SftpUtils.download(source.getIp(), source.getPort(), source.getUsername(),
                    source.getPassword(), source.getPath(), realLocalPath, requestDTO.getFileName(), requestDTO.getTimeout(), passwordFree);
            transmitResponseDTO.setSuccess(download);
        } else {
            logger.error("传输模式不支持");
            transmitResponseDTO.setSuccess(false);
            transmitResponseDTO.setReason("传输模式不支持");
        }
        return transmitResponseDTO;
    }

    /**
     * 从远程传输文件到远程
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    @Override
    public TransmitResponseDTO transmit(TransmitRequestDTO requestDTO) {
        TransmitResponseDTO transmitResponseDTO = new TransmitResponseDTO();
        TransmitResponseDTO downloadResult = download(requestDTO);
        String realLocalPath = null;
        if (requestDTO.getAppend()) {
            realLocalPath = localPath + File.separator + requestDTO.getLocalPath();
        } else {
            if (StringUtils.isBlank(requestDTO.getLocalPath())) {
                logger.error("自定义localPath不能为空");
                transmitResponseDTO.setSuccess(false);
                transmitResponseDTO.setReason("自定义localPath为空");
                return transmitResponseDTO;
            }
            realLocalPath = requestDTO.getLocalPath();
        }
        File folder = new File(realLocalPath);
        File[] files = folder.listFiles();
        if (downloadResult.getSuccess() && files != null) {
            logger.info("文件下载成功，开始进行上传");
            transmitResponseDTO = upload(requestDTO);
            for (File file : files) {
                if (file.getName().equals(requestDTO.getFileName())) {
                    file.delete();
                }
            }
        } else {
            logger.error("文件传输失败");
            transmitResponseDTO.setSuccess(false);
        }
        return transmitResponseDTO;
    }
}
