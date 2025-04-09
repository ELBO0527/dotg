FROM openjdk:17-alpine
ARG username
ARG password
ENV USERNAME=root
ENV PASSWORD=root
VOLUME /tmp
COPY build/libs/api17-0.0.1-SNAPSHOT.jar dotg-backend.jar
ENTRYPOINT ["java","-Dspring.profiles.activate=prod", "-jar","dotg-backend.jar"]