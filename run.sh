## mvn clean package
docker kill my_application_container
docker rm my_application_container
docker build -t myapp .
docker run -itd -p 8080:8080 --name my_application_container myapp