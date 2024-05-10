package cn.lezoo.doux.recoup.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoupEventRepository extends JpaRepository<RecoupEvent, Long> {
}