SHA=$(git rev-parse --short HEAD)

docker build \
  -t jafarisharib/pi-harness:$SHA \
  -t jafarisharib/pi-harness:latest \
  .

docker push jafarisharib/pi-harness:$SHA
docker push jafarisharib/pi-harness:latest