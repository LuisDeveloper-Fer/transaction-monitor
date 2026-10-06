package dev.portfolio.domain;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(indexes={@Index(name="event_time",columnList="occurredAt"),@Index(name="event_service_status",columnList="service,status")})
public class TransactionEvent {
 @Id public UUID id;public String service;public String status;public long durationMs;public Instant occurredAt;
 protected TransactionEvent(){}
 public TransactionEvent(UUID id,String service,String status,long durationMs,Instant occurredAt){this.id=id;this.service=service;this.status=status;this.durationMs=durationMs;this.occurredAt=occurredAt;}
}
