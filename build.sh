echo "Build started"

echo "Building user service"
mvn -f user-service install

echo "Building message service"
mvn -f message-service install

echo "Building rest api"
mvn -f restapi install

echo "Build finished"
