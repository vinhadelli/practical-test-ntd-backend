FROM eclipse-temurin:22.0.2_9-jdk-noble AS build
WORKDIR /app
COPY .mvn/ ./.mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests -DdbUrl=jdbc:mysql://ec2-18-118-146-136.us-east-2.compute.amazonaws.com:3306/NTD_CALCULATOR

FROM eclipse-temurin:22.0.2_9-jdk-noble
WORKDIR /app
COPY --from=build /app/target/practical-test-ntd-backend-0.0.1-SNAPSHOT.jar /app/practical-test-ntd-backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app/practical-test-ntd-backend-0.0.1-SNAPSHOT.jar"]