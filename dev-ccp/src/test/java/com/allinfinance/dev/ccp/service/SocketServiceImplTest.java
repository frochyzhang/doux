package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.ccp.CcpApplication;
import com.allinfinance.dev.common.socket.client.ISocketService;
import com.allinfinance.dev.common.socket.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.dto.SocketResponseDTO;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@SpringBootTest(classes = CcpApplication.class)
public class SocketServiceImplTest extends AbstractBenchmark{

    private static ISocketService socketService;

    @Autowired
    public void setSocketService(ISocketService socketService) {
        SocketServiceImplTest.socketService = socketService;
    }

    @Benchmark
   public void clientRequest() {
        SocketResponseDTO socketResponseDTO = socketService.clientRequest(new SocketRequestDTO("127.0.0.1", "4396", "8583", "4", "UTF-8"), "hello world");
//        Assertions.assertEquals(true,socketResponseDTO.getSuccess());
    }
}