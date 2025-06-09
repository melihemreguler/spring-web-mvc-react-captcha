#!/bin/bash

# EC2 Deployment Verification Script
# This script checks the deployment status and common issues

echo "=== Captcha App Deployment Status ==="
echo "Timestamp: $(date)"

echo -e "\n=== System Information ==="
echo "Hostname: $(hostname)"
echo "OS: $(lsb_release -d | cut -f2)"
echo "Kernel: $(uname -r)"
echo "Uptime: $(uptime)"

echo -e "\n=== Disk Usage ==="
df -h /
echo -e "\nInodes usage:"
df -i /

echo -e "\n=== Memory Usage ==="
free -h

echo -e "\n=== Docker Status ==="
if command -v docker &> /dev/null; then
    echo "Docker version: $(docker --version)"
    echo "Docker Compose version: $(docker compose version)"
    
    echo -e "\n--- Docker Images ---"
    docker images | grep -E "(captcha|nginx-proxy|letsencrypt)"
    
    echo -e "\n--- Running Containers ---"
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    
    echo -e "\n--- Container Logs (last 10 lines each) ---"
    for container in $(docker ps --format "{{.Names}}" | grep -E "(captcha|nginx-proxy|letsencrypt)"); do
        echo "=== $container logs ==="
        docker logs --tail 10 "$container" 2>&1 || echo "No logs available"
        echo
    done
else
    echo "Docker is not installed or not in PATH"
fi

echo -e "\n=== Network Status ==="
echo "--- Port 80 ---"
sudo ss -tlnp | grep :80 || echo "Port 80 not listening"

echo "--- Port 443 ---"
sudo ss -tlnp | grep :443 || echo "Port 443 not listening"

echo "--- Port 8080 ---"
sudo ss -tlnp | grep :8080 || echo "Port 8080 not listening"

echo -e "\n=== Project Directory ==="
if [ -d ~/spring-web-mvc-react-captcha ]; then
    echo "Project directory exists: ~/spring-web-mvc-react-captcha"
    cd ~/spring-web-mvc-react-captcha
    echo "Git status:"
    git status --porcelain 2>/dev/null || echo "Not a git repository"
    echo "Git branch:"
    git branch 2>/dev/null || echo "No git branches"
    echo "Files in directory:"
    ls -la
    
    if [ -f docker-compose.yml ]; then
        echo -e "\n--- Docker Compose Status ---"
        docker compose ps 2>/dev/null || echo "Docker compose not running or error"
    else
        echo "docker-compose.yml not found"
    fi
    
    if [ -f .env ]; then
        echo -e "\n--- Environment Variables (masked) ---"
        cat .env | sed 's/=.*/=***masked***/'
    else
        echo ".env file not found"
    fi
else
    echo "Project directory does not exist: ~/spring-web-mvc-react-captcha"
fi

echo -e "\n=== SSL Certificate Status ==="
if command -v openssl &> /dev/null; then
    echo "Checking SSL certificate for captcha.melihemre.dev..."
    echo | timeout 5 openssl s_client -servername captcha.melihemre.dev -connect captcha.melihemre.dev:443 2>/dev/null | openssl x509 -noout -dates 2>/dev/null || echo "Cannot retrieve SSL certificate"
else
    echo "OpenSSL not available"
fi

echo -e "\n=== Web Service Test ==="
echo "Testing HTTP connection to localhost..."
curl -I http://localhost/ 2>/dev/null || echo "HTTP connection failed"

echo -e "\nTesting HTTPS connection to localhost..."
curl -I -k https://localhost/ 2>/dev/null || echo "HTTPS connection failed"

echo -e "\n=== Recent System Logs ==="
echo "--- Docker service logs ---"
sudo journalctl -u docker --since "1 hour ago" --no-pager --lines 5 2>/dev/null || echo "No Docker service logs"

echo -e "\n--- System errors ---"
sudo journalctl --priority=err --since "1 hour ago" --no-pager --lines 5 2>/dev/null || echo "No recent system errors"

echo -e "\n=== Verification Complete ==="
