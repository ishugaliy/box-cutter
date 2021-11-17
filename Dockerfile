FROM openjdk:11-jdk-slim
EXPOSE 8080:8080
RUN mkdir /app
COPY . /app
WORKDIR /app
RUN ./gradlew --no-daemon installDist
WORKDIR /app/build/install/box-cutter
CMD ["./bin/box-cutter"]
