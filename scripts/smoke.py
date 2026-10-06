"""Exercise the local Compose stack with fictional data; Python standard library only."""
import json
import time
import uuid
import urllib.request
import urllib.error

BASE = 'http://localhost:8080'

def request(route, body=None, headers=None):
    payload = None if body is None else json.dumps(body).encode()
    req = urllib.request.Request(BASE + route, data=payload, headers={'Content-Type':'application/json', **(headers or {})})
    try:
        with urllib.request.urlopen(req, timeout=5) as response:
            raw=response.read()
            return response.status, json.loads(raw) if raw else {}
    except urllib.error.HTTPError as error:
        raw=error.read()
        return error.code, json.loads(raw) if raw else {}

for _ in range(90):
    try:
        if request('/actuator/health')[0] == 200: break
    except OSError: pass
    time.sleep(2)
else: raise RuntimeError('API did not become ready')

for _ in range(30):
    try:
        with urllib.request.urlopen('http://localhost:4200',timeout=5) as response:
            assert b'<app-root>' in response.read(), 'Angular shell missing'
            break
    except OSError: time.sleep(1)
else: raise RuntimeError('Frontend did not become ready')

body = {'id':str(uuid.uuid4()),'service':'PAYMENTS','status':'SUCCESS','durationMs':250,'occurredAt':'2026-01-01T00:00:00Z'}
assert request('/api/transactions',body)[0] == 201
assert request('/api/transactions',body)[0] == 409
assert request('/api/summary')[1]['total'] >= 1

print('transaction-monitor: HTTP smoke passed')
