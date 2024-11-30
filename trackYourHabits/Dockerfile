#FROM maven as builder
#WORKDIR opt/app
#COPY mvnw pom.xml ./
#COPY ./src ./src
#RUN mvn clean install
#
#FROM openjdk:18
#WORKDIR opt/app
#COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]

FROM openjdk:18-jdk-oracle

# Set the working directory in the container
WORKDIR /app

# Specify the JAR file to be copied
ARG JAR_FILE=target/*.jar

# Copy the JAR file from your host machine into the working directory
COPY ${JAR_FILE} app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]