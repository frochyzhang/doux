package cn.lezoo.doux.recoup.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 21:57
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class AppInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appName;

    private String host;

    private Integer port;

    private Boolean status = true;

    private int weight = 1;

    private int currentWeight = 0;


    public void online() {
        this.status = true;
    }

    public void offline() {
        this.status = false;
    }
}
