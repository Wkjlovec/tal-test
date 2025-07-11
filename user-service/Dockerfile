# 使用OpenJDK 8作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建的jar文件
COPY target/user-service-1.0-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8081

# 设置JVM参数
ENV JAVA_OPTS="-Xmx1024m -Xms512m"

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 