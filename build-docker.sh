#!/bin/bash

echo "开始构建微服务Docker镜像..."

# 构建所有Maven项目
echo "1. 构建Maven项目..."
mvn clean package -DskipTests

# 构建Docker镜像
echo "2. 构建Docker镜像..."

# 构建eureka-server
echo "构建 eureka-server..."
docker build -t eureka-server:latest ./eureka-server

# 构建gateway-service
echo "构建 gateway-service..."
docker build -t gateway-service:latest ./gateway-service

# 构建user-service
echo "构建 user-service..."
docker build -t user-service:latest ./user-service

# 构建product-service
echo "构建 product-service..."
docker build -t product-service:latest ./product-service

echo "所有Docker镜像构建完成！"

# 显示构建的镜像
echo "构建的镜像列表："
docker images | grep -E "(eureka-server|gateway-service|user-service|product-service)"

echo ""
echo "使用以下命令启动服务："
echo "docker-compose up -d"
echo ""
echo "或者单独启动某个服务："
echo "docker run -p 8761:8761 eureka-server:latest"
echo "docker run -p 8080:8080 gateway-service:latest"
echo "docker run -p 8081:8081 user-service:latest"
echo "docker run -p 8082:8082 product-service:latest" 