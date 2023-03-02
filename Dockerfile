FROM gradle:jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jammy

EXPOSE 9000

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/smartHomeBackend-0.0.1.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-jar","/app/spring-boot-application.jar"]
