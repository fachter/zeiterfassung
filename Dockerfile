FROM openjdk:14-slim
COPY ./zeiterfassung-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app
EXPOSE 8089
CMD ["java", "-jar", "zeiterfassung-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
