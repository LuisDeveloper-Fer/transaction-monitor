# Demostración en cinco minutos

## Problema — 30 segundos
Un contador global no explica qué servicio se degrada. Este proyecto registra eventos consultables y publica histogramas por servicio y resultado con cardinalidad controlada.

## Experimento — 2 minutos
Registra PAYMENTS, WEBHOOKS y RECONCILIATION con distintas duraciones y estados. Filtra por servicio y observa los histogramas en Grafana.

## Decisión — 1 minuto
La base de datos conserva eventos; Prometheus conserva series temporales. Las métricas se publican después del commit para no inflar contadores ante rollback o duplicados. Existe una ventana entre commit y métrica: no es un libro contable. El conjunto fijo de servicios limita cardinalidad.

## Discusión
- ¿Qué operación es atómica?
- ¿Qué ocurre entre confirmar una escritura y enviar una respuesta?
- ¿Qué impide agotar recursos?
- ¿Qué cambia al ejecutar dos réplicas?
- ¿Qué mide el dashboard y qué no permite concluir?

## Límites que conviene explicar
Métricas del proceso actual, reiniciadas al arrancar; histórico SQL persistente. TPS es tasa de ingesta, no de ocurrencia remota. Sin retención automática ni motor OLAP. Las agregaciones SQL son sencillas y necesitarían evolucionar con el volumen.
