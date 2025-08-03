# FinTrack Monitoring trên Ubuntu 24.04.1

Hướng dẫn triển khai hệ thống monitoring FinTrack trên Ubuntu 24.04.1 

## 🚀 Phương pháp 1: Docker Compose (Khuyến nghị cho Development)

### Ưu điểm:
- Dễ setup và quản lý
- Isolated environment
- Consistent across different environments

### Cách triển khai:

```bash
# Cấp quyền thực thi
chmod +x deploy-ubuntu.sh stop-deployment.sh

# Triển khai
./deploy-ubuntu.sh

# Dừng deployment
./stop-deployment.sh
```

### Truy cập:
- **Backend API**: http://localhost:5000
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3001 (admin/admin123)
- **cAdvisor**: http://localhost:8080

---

## 🔧 Cấu hình nâng cao

### 1. Cấu hình Alerting

Tạo file `alerting-rules.yml`:

```yaml
groups:
  - name: fintrack-alerts
    rules:
      - alert: HighCPUUsage
        expr: rate(process_cpu_usage{application="be-fintrack"}[5m]) > 0.8
        for: 2m
        labels:
          severity: warning
        annotations:
          summary: "High CPU usage detected"
          description: "CPU usage is above 80% for 2 minutes"

      - alert: HighMemoryUsage
        expr: jvm_memory_used_bytes{application="be-fintrack"} / jvm_memory_max_bytes{application="be-fintrack"} > 0.9
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "High memory usage detected"
          description: "Memory usage is above 90%"
```

### 2. Cấu hình Grafana Dashboard

Import dashboard từ file `grafana-dashboard.json` hoặc tạo custom dashboard với các panels:

- **HTTP Request Rate**: `rate(http_server_requests_seconds_count{application="be-fintrack"}[5m])`
- **Response Time**: `histogram_quantile(0.95, rate(http_server_requests_seconds_bucket{application="be-fintrack"}[5m]))`
- **JVM Memory**: `jvm_memory_used_bytes{application="be-fintrack"}`
- **CPU Usage**: `rate(process_cpu_usage{application="be-fintrack"}[5m]) * 100`

### 3. Cấu hình Logging

Thêm logging configuration vào `application.properties`:

```properties
# Logging configuration
logging.level.com.example.be_fintrack=INFO
logging.level.org.springframework.web=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.file.name=logs/fintrack.log
logging.file.max-size=10MB
logging.file.max-history=30
```

---

## 🔍 Troubleshooting

### 1. Kiểm tra connectivity

```bash
# Test backend
curl http://localhost:5000/actuator/health

# Test Prometheus
curl http://localhost:9090/api/v1/targets

# Test Grafana
curl http://localhost:3001/api/health
```

### 2. Kiểm tra metrics

```bash
# Spring Boot metrics
curl http://localhost:5000/actuator/prometheus

# Node Exporter metrics
curl http://localhost:9100/metrics
```

### 3. Kiểm tra logs

```bash
# Docker logs
docker logs fintrack-backend
docker logs prometheus
docker logs grafana

# System logs
sudo journalctl -u prometheus -f
sudo journalctl -u grafana-server -f
```

### 4. Common issues

**Issue**: Prometheus không scrape được metrics
**Solution**: Kiểm tra network connectivity và cấu hình targets

**Issue**: Grafana không kết nối được Prometheus
**Solution**: Thêm Prometheus data source trong Grafana

**Issue**: High memory usage
**Solution**: Tăng heap size cho JVM: `-Xmx2g -Xms1g`

---

## 📊 Monitoring Best Practices

### 1. Metrics cần monitor:
- **Application**: Request rate, response time, error rate
- **Infrastructure**: CPU, memory, disk, network
- **Business**: User activity, transaction volume

### 2. Alerting rules:
- Set up alerts for critical metrics
- Use different severity levels
- Include actionable descriptions

### 3. Dashboard design:
- Group related metrics
- Use appropriate visualizations
- Include time ranges

### 4. Performance optimization:
- Use appropriate scrape intervals
- Configure retention policies
- Monitor Prometheus performance

---

## 🔄 Maintenance

### 1. Backup data:
```bash
# Prometheus data
sudo cp -r /var/lib/prometheus /backup/

# Grafana data
sudo cp -r /var/lib/grafana /backup/
```

### 2. Update components:
```bash
# Update Prometheus
wget https://github.com/prometheus/prometheus/releases/download/v2.45.0/prometheus-2.45.0.linux-amd64.tar.gz

# Update Grafana
sudo apt update && sudo apt upgrade grafana
```

### 3. Cleanup:
```bash
# Clean old data
sudo rm -rf /var/lib/prometheus/wal/*
sudo rm -rf /var/lib/grafana/grafana.db
``` 