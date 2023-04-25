package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.transmit.api.dto.TransmitMode;
import com.allinfinance.dev.common.transmit.api.dto.TransmitRequestDTO;
import com.allinfinance.dev.common.transmit.api.dto.TransmitResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransmitServiceExtraImplTest {
    @Autowired
    private TransmitServiceExtraImpl transmitServiceExtra;

    @Test
    public void test_ftp_download_success() {
        //sftp: 10.250.20.207 22 wkftp wkftp
        //ftp: 10.250.20.207 21 wkftp wkftp
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage source = new TransmitRequestDTO.RemoteMessage();
        source.setIp("10.250.20.207");
        source.setPort(21);
        source.setUsername("wkftp");
        source.setPassword("wkftp");
        source.setTransmitMode(TransmitMode.FTP);
        source.setPath("/");
        transmitRequestDTO.setSource(source);
        transmitRequestDTO.setLocalPath("C:\\temp\\testt");
        transmitRequestDTO.setFileName("test");
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.download(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

    @Test
    public void test_sftp_download_success() {
        //sftp: 10.250.20.207 22 wkftp wkftp
        //ftp: 10.250.20.207 21 wkftp wkftp
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage source = new TransmitRequestDTO.RemoteMessage();
        source.setIp("10.250.20.207");
        source.setPort(22);
        source.setUsername("wkftp");
        source.setPassword("wkftp");
        source.setPath("/app/wkftp/Temp");
        source.setTransmitMode(TransmitMode.SFTP);
        transmitRequestDTO.setSource(source);
        transmitRequestDTO.setLocalPath("C:\\temp\\tmp");
        transmitRequestDTO.setAppend(false);
        transmitRequestDTO.setFileName("test3");
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.download(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

    @Test
    public void test_upload_success() {
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage target = new TransmitRequestDTO.RemoteMessage();
        target.setIp("10.250.20.207");
        target.setPort(22);
        target.setUsername("wkftp");
        target.setPassword("wkftp");
        target.setPath("/app/wkftp");
        target.setTransmitMode(TransmitMode.SFTP);
        transmitRequestDTO.setTarget(target);
        transmitRequestDTO.setLocalPath("test");
        transmitRequestDTO.setFileName("test001.txt");
        transmitRequestDTO.setAppend(true);
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.upload(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

    @Test
    public void test_sftp_transmit_success() {
        //sftp: 10.250.20.207 22 wkftp wkftp
        //ftp: 10.250.20.207 21 wkftp wkftp
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage target = new TransmitRequestDTO.RemoteMessage();
        TransmitRequestDTO.RemoteMessage source = new TransmitRequestDTO.RemoteMessage();
        source.setIp("10.250.20.207");
        source.setPort(22);
        source.setUsername("wkftp");
        source.setPassword("wkftp");
        source.setPath("/app/wkftp/Temp");
        source.setTransmitMode(TransmitMode.SFTP);

        target.setIp("10.250.20.207");
        target.setPort(22);
        target.setUsername("wkftp");
        target.setPassword("wkftp");
        target.setPath("/app/wkftp");
        target.setTransmitMode(TransmitMode.SFTP);

        transmitRequestDTO.setTarget(target);
        transmitRequestDTO.setSource(source);
        transmitRequestDTO.setAppend(true);
        transmitRequestDTO.setLocalPath("tmp");
        transmitRequestDTO.setFileName("test001");
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.transmit(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

    @Test
    public void test_ftp_transmit_success() {
        //sftp: 10.250.20.207 22 wkftp wkftp
        //ftp: 10.250.20.207 21 wkftp wkftp
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage target = new TransmitRequestDTO.RemoteMessage();
        TransmitRequestDTO.RemoteMessage source = new TransmitRequestDTO.RemoteMessage();
        source.setIp("10.250.20.207");
        source.setPort(21);
        source.setUsername("wkftp");
        source.setPassword("wkftp");
        source.setPath("/");
        source.setTransmitMode(TransmitMode.FTP);

        target.setIp("10.250.20.207");
        target.setPort(21);
        target.setUsername("wkftp");
        target.setPassword("wkftp");
        target.setPath("/Temp/");
        target.setTransmitMode(TransmitMode.FTP);

        transmitRequestDTO.setTarget(target);
        transmitRequestDTO.setSource(source);
        transmitRequestDTO.setFileName("test");
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.transmit(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

    @Test
    void test_sftpdown_ftpup_transmit_success() {
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage target = new TransmitRequestDTO.RemoteMessage();
        TransmitRequestDTO.RemoteMessage source = new TransmitRequestDTO.RemoteMessage();
        source.setIp("10.250.20.207");
        source.setPort(22);
        source.setUsername("wkftp");
        source.setPassword("wkftp");
        source.setPath("/app/wkftp/Temp");
        source.setTransmitMode(TransmitMode.SFTP);

        target.setIp("10.250.20.207");
        target.setPort(21);
        target.setUsername("wkftp");
        target.setPassword("wkftp");
        target.setPath("/Temp/");
        target.setTransmitMode(TransmitMode.FTP);

        transmitRequestDTO.setTarget(target);
        transmitRequestDTO.setSource(source);
        transmitRequestDTO.setFileName("test3");
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.transmit(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

    @Test
    void test_ftpdown_sftpup_transmit_success() {
        TransmitRequestDTO transmitRequestDTO = new TransmitRequestDTO();
        TransmitRequestDTO.RemoteMessage target = new TransmitRequestDTO.RemoteMessage();
        TransmitRequestDTO.RemoteMessage source = new TransmitRequestDTO.RemoteMessage();
        source.setIp("10.250.20.207");
        source.setPort(21);
        source.setUsername("wkftp");
        source.setPassword("wkftp");
        source.setPath("/Temp");
        source.setTransmitMode(TransmitMode.FTP);

        target.setIp("10.250.20.207");
        target.setPort(22);
        target.setUsername("wkftp");
        target.setPassword("wkftp");
        target.setPath("/app/wkftp");
        target.setTransmitMode(TransmitMode.SFTP);

        transmitRequestDTO.setTarget(target);
        transmitRequestDTO.setSource(source);
        transmitRequestDTO.setAppend(true);
        transmitRequestDTO.setLocalPath("double\\sdfds");
        transmitRequestDTO.setFileName("test3");
        transmitRequestDTO.setTimeout(5000);
        TransmitResponseDTO responseDTO = transmitServiceExtra.transmit(transmitRequestDTO);
        Assertions.assertEquals(true, responseDTO.getSuccess());
        System.out.println(responseDTO);
    }

}