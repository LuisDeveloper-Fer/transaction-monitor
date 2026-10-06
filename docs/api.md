# API — Transaction Monitor

Base: http://localhost:8080. Content-Type: application/json.

| Método | Ruta | Contrato |
| --- | --- | --- |
| POST | `/api/transactions` | 201 nuevo; 409 duplicado |
| GET | `/api/transactions?page=0&size=20&service=PAYMENTS&status=ERROR` | Consulta; size máximo 100 |
| GET | `/api/transactions/{id}` | Detalle |
| GET | `/api/summary` | Total, success, error, averageDurationMs |
| GET | `/actuator/prometheus` | Histogramas y runtime |

## Request
```json
{
  "id": "10000000-0000-4000-8000-000000000001",
  "service": "PAYMENTS",
  "status": "SUCCESS",
  "durationMs": 250,
  "occurredAt": "2026-01-01T00:00:00Z"
}
```

## Response (campos relevantes)
```json
{
  "id": "10000000-0000-4000-8000-000000000001",
  "service": "PAYMENTS",
  "status": "SUCCESS",
  "durationMs": 250,
  "occurredAt": "2026-01-01T00:00:00Z"
}
```

## Errores
400 indica validación o formato inválido; 404 indica recurso inexistente. Los estados específicos se detallan en la tabla. ProblemDetail se usa para errores de negocio y validación donde aplica; autenticación puede devolver cuerpo vacío y WWW-Authenticate. Los clientes deben usar códigos, no parsear mensajes internos.

UUID único; service=PAYMENTS|WEBHOOKS|RECONCILIATION; status=SUCCESS|ERROR; durationMs=0..60000; occurredAt ISO-8601 no futuro. Filtros opcionales service/status. Paginación 1..100 resultados.

