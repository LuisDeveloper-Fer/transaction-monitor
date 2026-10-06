package dev.portfolio.api;

import dev.portfolio.application.EventService;
import dev.portfolio.domain.TransactionEvent;
import dev.portfolio.infrastructure.EventRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class MonitorController {
  private final EventService service;
  private final EventRepository repo;

  public MonitorController(EventService service, EventRepository repo) {
    this.service = service;
    this.repo = repo;
  }

  public record Input(
      @NotNull UUID id,
      @NotNull @Pattern(regexp = "PAYMENTS|WEBHOOKS|RECONCILIATION") String service,
      @NotNull @Pattern(regexp = "SUCCESS|ERROR") String status,
      @Min(0) @Max(60000) long durationMs,
      @NotNull @PastOrPresent Instant occurredAt) {}

  @PostMapping("/transactions")
  @ResponseStatus(HttpStatus.CREATED)
  public TransactionEvent record(@Valid @RequestBody Input input) {
    return service.record(
        new TransactionEvent(
            input.id(), input.service(), input.status(), input.durationMs(), input.occurredAt()));
  }

  @GetMapping("/transactions")
  public Map<String, Object> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(required = false) String service,
      @RequestParam(required = false) String status) {
    if (page < 0 || size < 1 || size > 100)
      throw new IllegalArgumentException("page >= 0; size between 1 and 100");
    Specification<TransactionEvent> spec =
        (root, query, cb) -> {
          var terms = new ArrayList<jakarta.persistence.criteria.Predicate>();
          if (service != null) terms.add(cb.equal(root.get("service"), service));
          if (status != null) terms.add(cb.equal(root.get("status"), status));
          return cb.and(terms.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    var result = repo.findAll(spec, PageRequest.of(page, size, Sort.by("occurredAt").descending()));
    return Map.of(
        "items",
        result.getContent(),
        "total",
        result.getTotalElements(),
        "page",
        page,
        "size",
        size);
  }

  @GetMapping("/transactions/{id}")
  public TransactionEvent get(@PathVariable UUID id) {
    return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/summary")
  public Map<String, Object> summary() {
    return Map.of(
        "total",
        repo.count(),
        "success",
        repo.countByStatus("SUCCESS"),
        "error",
        repo.countByStatus("ERROR"),
        "averageDurationMs",
        repo.averageDuration());
  }
}
