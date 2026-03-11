# Local Grafana + Loki Setup

This folder contains a local observability stack for development:

- Grafana on `http://localhost:3000`
- Loki on `http://localhost:3100`
- Pre-provisioned Loki datasource
- Payment service dashboard JSON

## Prerequisites

- Docker running (Colima is fine)

## Start stack

### Option A: Docker Compose

```bash
cd infra/grafana
docker compose up -d
```

### Option B: Docker run fallback

Use this if `docker compose` is unavailable on your machine.

```bash
docker network create grafana-net || true
docker rm -f grafana loki || true

docker run -d --name loki \
  --network grafana-net \
  -p 3100:3100 \
  -v "$PWD/infra/grafana/loki-config.yml:/etc/loki/local-config.yaml" \
  grafana/loki:3.0.0 \
  -config.file=/etc/loki/local-config.yaml

docker run -d --name grafana \
  --network grafana-net \
  -p 3000:3000 \
  -e GF_SECURITY_ADMIN_USER=admin \
  -e GF_SECURITY_ADMIN_PASSWORD=admin123 \
  -v grafana-data:/var/lib/grafana \
  -v "$PWD/infra/grafana/grafana/provisioning:/etc/grafana/provisioning" \
  grafana/grafana:11.1.0
```

## Login

- URL: `http://localhost:3000`
- Username: `admin`
- Password: `admin123`

## Dashboard

- Dashboard UID: `payments-analytics`
- File: `infra/grafana/dashboards/payment-service-dashboard.json`

## MCP configuration

Copy `infra/grafana/.env.example` to your local env setup and generate your own Grafana service account token.

Example MCP server config:

```json
{
  "mcpServers": {
    "grafana": {
      "command": "uvx",
      "args": ["mcp-grafana"],
      "env": {
        "GRAFANA_URL": "http://localhost:3000",
        "GRAFANA_SERVICE_ACCOUNT_TOKEN": "<your service account token>"
      }
    }
  }
}
```

## Stop stack

```bash
cd infra/grafana
docker compose down
```
