FROM openjdk:21-jdk-slim
COPY build/libs/*.jar prestudy-framework-spring-0.0.1.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "prestudy-framework-spring-0.0.1.jar", "--spring.profiles.active=dev"]