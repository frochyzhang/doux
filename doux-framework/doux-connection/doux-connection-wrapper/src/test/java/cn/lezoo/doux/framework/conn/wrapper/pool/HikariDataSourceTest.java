package cn.lezoo.doux.framework.conn.wrapper.pool;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.wrapper.pool.util.JavassistProxyFactory;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HikariDataSourceTest {

    @BeforeEach
    void setup() throws Exception {
        String tmp = System.getProperty("java.io.tmpdir");
        JavassistProxyFactory.main(tmp + (tmp.endsWith("/") ? "" : "/"));
    }

    @Test
    void getConnection() throws InterruptedException {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(2);
        config.setKeepaliveTime(30000);
        HikariDataSource dataSource = new HikariDataSource(config);

        //        IntStream.rangeClosed(1, 100000).parallel()
        //            .forEach(i -> {
        //                try {
        Connection connection = dataSource.getConnection();
//        connection.send("00");
//        connection.close();
        //                } catch (Exception e) {
        //                    e.printStackTrace();
        //                }
        //            });

        TimeUnit.SECONDS.sleep(60 * 3);
    }
}