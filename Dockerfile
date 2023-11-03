FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace/app

COPY .mvn .mvn
COPY pom.xml mvnw ./
COPY src src

RUN ./mvnw clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../kavatar-*.jar)

FROM eclipse-temurin:20-jre-alpine AS runner
VOLUME /tmp

RUN addgroup -S app && adduser -S spring-app -G app
USER spring-app

ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.glinboy.kavatar.KavatarApplication"]
