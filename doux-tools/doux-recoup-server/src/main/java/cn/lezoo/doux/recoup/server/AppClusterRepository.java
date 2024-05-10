package cn.lezoo.doux.recoup.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AppClusterRepository extends JpaRepository<AppInfo, Long> {
    List<AppInfo> findByStatusTrueAndAppName(String appName);

    Stream<AppInfo> findByStatusTrue();
}