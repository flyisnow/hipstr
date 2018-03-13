FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/hipstr.jar /hipstr/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/hipstr/app.jar"]
