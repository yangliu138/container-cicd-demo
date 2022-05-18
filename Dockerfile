# Stage build
FROM adoptopenjdk/openjdk11:jdk-11.0.15_10-alpine as build
LABEL author="Leo Liu" \
      summary="Simple CICD Demo with Springboot" \
      description="Simple CICD Demo with Springboot" 

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

# Stage run
FROM adoptopenjdk/openjdk11:alpine-jre
LABEL Leo Liu
WORKDIR /workspace/app
COPY --from=build /workspace/app/target/container-cicd-demo-1.0.0.jar /workspace/app

EXPOSE 8081

# Setup users
RUN chown -R 1001:0 /workspace/app && \
    chmod -R g=u /workspace/app

USER 1001
ENTRYPOINT [ "java", "-jar", "./container-cicd-demo-1.0.0.jar" ]
