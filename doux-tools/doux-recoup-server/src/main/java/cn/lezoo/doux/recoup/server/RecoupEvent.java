package cn.lezoo.doux.recoup.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 21:52
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class RecoupEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String className;

    private String eventData;

    private String appName;

    private Boolean status = true;

    private Integer currentRecoupTimes = 0;

    @Column(updatable = false)
    private Integer maxRecoupTimes = 3;

    private LocalDateTime nextPollingTime = LocalDateTime.now().plusMinutes(1);

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
