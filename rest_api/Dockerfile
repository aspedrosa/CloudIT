FROM maven:3.6.1-jdk-8

RUN git clone https://github.com/aspedrosa/CloudIT /app

WORKDIR /app

# Maybe I can get the jar from the tests stages
#  not having to compile again the project
RUN mvn package -Dmaven.test.skip=true

EXPOSE 8080

CMD java -jar target/*.jar
