# Using Eclipse Temurin as the base image for OpenJDK, as it is a well-supported, secure, and production-ready JDK distribution
FROM eclipse-temurin:19-jdk-jammy as build
WORKDIR /workspace/app


# copy maven stuff first to cache dependancies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# build that app
RUN ./mvnw install -DskipTests

# setup runtime img
FROM eclipse-temurin:19-jre-jammy
WORKDIR /app

# add non-root user for securty
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --group appuser

# grab the jar from build stage
COPY --from=build /workspace/app/target/*.jar app.jar

# fix ownership adn switch to non-root user
RUN chown -R appuser:appuser /app
USER appuser

# setup helth check
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# env vars for java
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# open up the applicationport
EXPOSE 8080

# run the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 