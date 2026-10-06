# ADR 001 — OBSERVABILITY

Estado: aceptada · 2026-10-05

## Contexto
Un contador global no explica qué servicio se degrada. Este proyecto registra eventos consultables y publica histogramas por servicio y resultado con cardinalidad controlada.

## Decisión
La base de datos conserva eventos; Prometheus conserva series temporales. Las métricas se publican después del commit para no inflar contadores ante rollback o duplicados. Existe una ventana entre commit y métrica: no es un libro contable. El conjunto fijo de servicios limita cardinalidad.

## Alternativas
Separar más microservicios o incorporar un broker agregaría despliegue y operación fuera del objetivo. Concentrar todo en el controlador dificultaría probar fallos y razonar sobre el contrato. Se elige una aplicación pequeña con API, casos de uso y adaptadores diferenciados.

## Consecuencias
Métricas del proceso actual, reiniciadas al arrancar; histórico SQL persistente. TPS es tasa de ingesta, no de ocurrencia remota. Sin retención automática ni motor OLAP. Las agregaciones SQL son sencillas y necesitarían evolucionar con el volumen.

## Validación
Duplicado rechazado sin incrementar el histograma; evento original permanece consultable.
