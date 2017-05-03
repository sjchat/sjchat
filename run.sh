trap 'kill %1; kill %2;' SIGINT

echo "Starting services"

echo "Starting user service"
mvn -f user-service exec:java &

echo "Starting message service"
mvn -f message-service exec:java &

echo "Starting rest api"
mvn -f restapi exec:java
