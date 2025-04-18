FROM openjdk:21
LABEL service="MonitorAPI"
ENV _JAVA_OPTIONS="-Xms128m -Xmx1024m -Dfile.encoding=utf-8"

ARG JAR_FILE="target/*.jar"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
