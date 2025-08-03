#!/bin/bash

echo "🛑 Dừng FinTrack deployment..."

# Dừng backend
echo "🛑 Dừng backend..."
docker stop fintrack-backend 2>/dev/null || true
docker rm fintrack-backend 2>/dev/null || true

# Dừng monitoring
echo "🛑 Dừng monitoring..."
cd monitoring
docker-compose -f docker-compose.monitoring.yml down

echo "✅ Đã dừng tất cả services!"
echo ""
echo "💡 Để xóa dữ liệu và volumes:"
echo "   docker-compose -f docker-compose.monitoring.yml down -v"
echo "   docker rmi fintrack-backend" 