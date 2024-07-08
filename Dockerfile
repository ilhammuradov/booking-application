FROM openjdk:22
COPY target/booking-plane-tickets-1.0-SNAPSHOT.jar booking-app.jar
ENTRYPOINT ["java", "-jar","booking-app.jar"]