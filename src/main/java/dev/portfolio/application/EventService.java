package dev.portfolio.application;
import dev.portfolio.domain.TransactionEvent;
import dev.portfolio.infrastructure.EventRepository;
import io.micrometer.core.instrument.*;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
@Service public class EventService {
 private final EntityManager em;private final TransactionTemplate tx;private final MeterRegistry meters;private final EventRepository repo;
 public EventService(EntityManager em,TransactionTemplate tx,MeterRegistry meters,EventRepository repo){this.em=em;this.tx=tx;this.meters=meters;this.repo=repo;}
 public TransactionEvent record(TransactionEvent event){
  try{tx.executeWithoutResult(status->{em.persist(event);em.flush();});}
  catch(RuntimeException failure){if(repo.existsById(event.id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Event ID already recorded");throw failure;}
  // Count only committed writes. Metric counters remain process-local by design.
  Timer.builder("transactions.processing").tags("service",event.service,"outcome",event.status).publishPercentileHistogram().serviceLevelObjectives(Duration.ofMillis(100),Duration.ofMillis(500),Duration.ofSeconds(1),Duration.ofSeconds(2),Duration.ofSeconds(5)).register(meters).record(Duration.ofMillis(event.durationMs));
  return event;
 }
}
