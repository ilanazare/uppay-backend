FROM openjdk:21-jdk-slim
WORKDIR /goldpay-aws
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]