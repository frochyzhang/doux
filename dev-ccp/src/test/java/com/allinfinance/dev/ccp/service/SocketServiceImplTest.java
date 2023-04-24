package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.ccp.AbstractBenchmark;
import com.allinfinance.dev.ccp.CcpApplication;
import com.allinfinance.dev.common.socket.api.client.SocketClientService;
import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.api.client.dto.SocketResponseDTO;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@SpringBootTest(classes = CcpApplication.class)
public class SocketServiceImplTest extends AbstractBenchmark {

    private static SocketClientService socketService;

    @Autowired
    public void setSocketService(SocketClientService socketService) {
        SocketServiceImplTest.socketService = socketService;
    }

    @Benchmark
    public void clientRequest() {
        SocketResponseDTO socketResponseDTO = socketService.request(new SocketRequestDTO("127.0.0.1", "4396", "8583", "4", "UTF-8"), "hello world");
//        Assertions.assertEquals(true,socketResponseDTO.getSuccess());
    }
}