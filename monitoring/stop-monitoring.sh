#!/bin/bash

echo "🛑 Dừng hệ thống monitoring FinTrack..."

# Dừng các service monitoring
docker-compose -f docker-compose.monitoring.yml down

echo "✅ Hệ thống monitoring đã được dừng!"
echo ""
echo "💡 Để xóa dữ liệu và volumes:"
echo "   docker-compose -f docker-compose.monitoring.yml down -v" 