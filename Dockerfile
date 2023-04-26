FROM openjdk:17
COPY target/online-shop.jar online-shop.jar
ENTRYPOINT ["java","-jar","/online-shop.jar"]