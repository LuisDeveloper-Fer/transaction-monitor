package dev.portfolio;
import dev.portfolio.application.EventService;
import dev.portfolio.domain.TransactionEvent;
import dev.portfolio.infrastructure.EventRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest class MonitorTest {
 @Autowired EventService service;@Autowired EventRepository repo;@Autowired MeterRegistry meters;
 @Test void duplicateDoesNotInflateMetrics(){var id=UUID.randomUUID();service.record(new TransactionEvent(id,"PAYMENTS","SUCCESS",250,Instant.now()));
  var timer=meters.find("transactions.processing").tags("service","PAYMENTS","outcome","SUCCESS").timer();long before=timer.count();
  assertThatThrownBy(()->service.record(new TransactionEvent(id,"PAYMENTS","SUCCESS",250,Instant.now()))).hasMessageContaining("409");assertThat(timer.count()).isEqualTo(before);assertThat(repo.findById(id)).isPresent();
 }
}
