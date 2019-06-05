FROM maven:3.6.1-jdk-8

WORKDIR /app

# this jar comes from command `mvn package` executed
#  on the deploy stage of the Jenkins pipeline
COPY target/*.jar /app

EXPOSE 8080

CMD java -jar *.jar
