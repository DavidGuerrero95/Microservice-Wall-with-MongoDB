FROM openjdk:15
VOLUME /tmp
ADD ./target/springboot-muro-0.0.1-SNAPSHOT.jar muro.jar
ENTRYPOINT ["java","-jar","/muro.jar"]