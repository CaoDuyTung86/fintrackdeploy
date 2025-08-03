#!/bin/bash

echo "🚀 Khởi động hệ thống monitoring FinTrack..."

# Kiểm tra xem network đã tồn tại chưa
if ! docker network ls | grep -q "fintrack-net"; then
    echo "📡 Tạo network fintrack-net..."
    docker network create fintrack-net
fi

# Khởi động các service monitoring
echo "📊 Khởi động Prometheus..."
docker-compose -f docker-compose.monitoring.yml up -d prometheus

echo "📈 Khởi động Grafana..."
docker-compose -f docker-compose.monitoring.yml up -d grafana

echo "🖥️  Khởi động Node Exporter..."
docker-compose -f docker-compose.monitoring.yml up -d node-exporter

echo "🐳 Khởi động cAdvisor..."
docker-compose -f docker-compose.monitoring.yml up -d cadvisor

echo "✅ Hệ thống monitoring đã được khởi động!"
echo ""
echo "📋 Thông tin truy cập:"
echo "   - Prometheus: http://localhost:9090"
echo "   - Grafana: http://localhost:3001 (admin/admin123)"
echo "   - cAdvisor: http://localhost:8080"
echo ""
echo "🔧 Để dừng monitoring: ./stop-monitoring.sh" 