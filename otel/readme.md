# Otel and Jaegger setup

`docker compose logs -f otel-collector`

`docker compose -f 'otel/compose.yaml' up -d --build`

`docker compose ps`

export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc