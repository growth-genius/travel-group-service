# bin/bash
docker stop travel-group-service || true
docker rm travel-group-service || true
docker rmi leesg107/travel-group-service || true
./gradlew clean bootBuildImage --imageName=leesg107/travel-group-service
