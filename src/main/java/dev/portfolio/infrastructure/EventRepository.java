package dev.portfolio.infrastructure;

import dev.portfolio.domain.TransactionEvent;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;

public interface EventRepository
    extends JpaRepository<TransactionEvent, UUID>, JpaSpecificationExecutor<TransactionEvent> {
  long countByStatus(String status);

  @Query("select coalesce(avg(e.durationMs),0) from TransactionEvent e")
  double averageDuration();
}
