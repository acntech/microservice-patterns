FROM openjdk:8-jre-alpine

EXPOSE 9020

VOLUME /tmp

COPY ./target/*.war /app.war

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.war"]
