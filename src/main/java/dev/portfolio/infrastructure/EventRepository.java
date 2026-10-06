package dev.portfolio.infrastructure;
import dev.portfolio.domain.TransactionEvent;
import org.springframework.data.jpa.repository.*;
import java.util.UUID;
public interface EventRepository extends JpaRepository<TransactionEvent,UUID>,JpaSpecificationExecutor<TransactionEvent>{
 long countByStatus(String status);
 @Query("select coalesce(avg(e.durationMs),0) from TransactionEvent e") double averageDuration();
}
