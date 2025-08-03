#!/bin/bash

echo "🚀 Triển khai FinTrack trên Ubuntu 24.04.1..."

# Kiểm tra Docker
if ! command -v docker &> /dev/null; then
    echo "📦 Cài đặt Docker..."
    sudo apt update
    sudo apt install -y apt-transport-https ca-certificates curl gnupg lsb-release
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt update
    sudo apt install -y docker-ce docker-ce-cli containerd.io
    sudo usermod -aG docker $USER
    echo "✅ Docker đã được cài đặt. Vui lòng logout và login lại."
    exit 1
fi

# Kiểm tra Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "📦 Cài đặt Docker Compose..."
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

# Tạo network
echo "📡 Tạo network fintrack-net..."
docker network create fintrack-net 2>/dev/null || true

# Build và khởi động backend
echo "🔨 Build Spring Boot application..."
cd ../be-fintrack-master
./mvnw clean package -DskipTests
cd ..

# Tạo Dockerfile cho backend nếu chưa có
if [ ! -f "be-fintrack-master/Dockerfile" ]; then
    cat > be-fintrack-master/Dockerfile << 'EOF'
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
fi

# Build backend image
echo "🐳 Build Docker image cho backend..."
cd be-fintrack-master
docker build -t fintrack-backend .
cd ..

# Khởi động monitoring
echo "📊 Khởi động hệ thống monitoring..."
cd monitoring
docker-compose -f docker-compose.monitoring.yml up -d

# Khởi động backend
echo "🚀 Khởi động backend..."
docker run -d --name fintrack-backend --network fintrack-net -p 5000:5000 fintrack-backend

echo "✅ Triển khai hoàn tất!"
echo ""
echo "📋 Thông tin truy cập:"
echo "   - Backend API: http://localhost:5000"
echo "   - Prometheus: http://localhost:9090"
echo "   - Grafana: http://localhost:3001 (admin/admin123)"
echo "   - cAdvisor: http://localhost:8080"
echo ""
echo "🔧 Để dừng: ./stop-deployment.sh" 