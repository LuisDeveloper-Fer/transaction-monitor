#!/usr/bin/env bash
set -euo pipefail
BASE="${BASE:-http://localhost:8080}"
curl -i -X POST "$BASE/api/transactions" -H 'Content-Type: application/json' --data '{"id":"10000000-0000-4000-8000-000000000001","service":"PAYMENTS","status":"SUCCESS","durationMs":250,"occurredAt":"2026-01-01T00:00:00Z"}'
curl -i "$BASE/api/transactions"
curl -i "$BASE/api/summary"
