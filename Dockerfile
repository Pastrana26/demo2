FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app

ENV PORT=8080
EXPOSE 8080

COPY --from=build /app/target/*.war /app/app.war

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.war"]
