#!/bin/bash

# 设置变量
PROJECT_ID=zinc-cooler-465416-r3
REGION="us-central1"
REPO_NAME="hello-repo"

echo "开始构建和推送Docker镜像到GKE..."

# 1. 构建Maven项目
echo "1. 构建Maven项目..."
mvn clean package -DskipTests

# 2. 配置Docker认证
echo "2. 配置Docker认证..."
gcloud auth configure-docker ${REGION}-docker.pkg.dev

# 3. 构建和标记Docker镜像
echo "3. 构建Docker镜像..."

# Eureka Server
echo "构建 eureka-server..."
docker build -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/eureka-server:v1 ./eureka-server

# Gateway Service
echo "构建 gateway-service..."
docker build -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/gateway-service:v1 ./gateway-service

# User Service
echo "构建 user-service..."
docker build -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/user-service:v1 ./user-service

# Product Service
echo "构建 product-service..."
docker build -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/product-service:v1 ./product-service

# 4. 推送镜像到Artifact Registry
echo "4. 推送镜像到Artifact Registry..."

docker push ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/eureka-server:v1
docker push ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/gateway-service:v1
docker push ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/user-service:v1
docker push ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/product-service:v1

echo "所有镜像推送完成！"

# 5. 显示镜像列表
echo "推送的镜像列表："
gcloud artifacts docker images list ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}

echo ""
echo "接下来可以使用以下命令部署到GKE："
echo "kubectl apply -f k8s/" 