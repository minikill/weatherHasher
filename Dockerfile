FROM openjdk:8-jre-alpine

COPY target/weatherHasher-1.0-SNAPSHOT-jar-with-dependencies.jar /app/weatherHasher.jar
ENTRYPOINT ["java"]
CMD ["-Dcom.sun.management.jmxremote.port=5555", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-jar", "/app/weatherHasher.jar"]