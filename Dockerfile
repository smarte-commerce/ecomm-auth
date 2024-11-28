FROM amazoncorretto:21-alpine-jdk

ADD target/gateway-0.0.1-SNAPSHOT.jar gateway-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT [ "java","-jar","gateway-0.0.1-SNAPSHOT.jar" ]
