#!/bin/bash
PROJECT_ID=zinc-cooler-465416-r3
echo "PROJECT_ID"+${PROJECT_ID}

echo "开始构建微服务Docker镜像..."
# 构建所有Maven项目
echo "1. 构建Maven项目..."
mvn clean package -DskipTests

# 构建Docker镜像
echo "2. 构建Docker镜像..."

# 构建eureka-server
echo "构建 eureka-server..."
docker build -t us-central1-docker.pkg.dev/${PROJECT_ID}/hello-repo/eureka-server:latest ./eureka-server

# 构建gateway-service
echo "构建 gateway-service..."
docker build -t us-central1-docker.pkg.dev/${PROJECT_ID}/hello-repo/gateway-service:latest ./gateway-service

# 构建user-service
echo "构建 user-service..."
docker build -t us-central1-docker.pkg.dev/${PROJECT_ID}/hello-repo/user-service:latest ./user-service

# 构建product-service
echo "构建 product-service..."
docker build -t us-central1-docker.pkg.dev/${PROJECT_ID}/hello-repo/product-service:latest ./product-service

echo "所有Docker镜像构建完成！"

# 显示构建的镜像
echo "构建的镜像列表："
docker images 