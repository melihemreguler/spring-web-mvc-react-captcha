#!/bin/bash

# EC2 Disk Space Cleanup Script
# This script cleans up common space-consuming items on EC2

echo "=== EC2 Disk Space Cleanup ==="
echo "Current disk usage:"
df -h /

echo -e "\n=== Step 1: Stopping running containers ==="
docker compose down --remove-orphans || true

echo -e "\n=== Step 2: Docker cleanup ==="
# Remove all stopped containers, unused networks, and dangling images
docker system prune -af --volumes

# Remove all unused images (be aggressive about cleanup)
docker image prune -af

# Remove all unused volumes
docker volume prune -f

echo -e "\n=== Step 3: APT package cleanup ==="
# Clean APT cache
sudo apt-get autoremove -y
sudo apt-get autoclean
sudo apt-get clean

echo -e "\n=== Step 4: Log cleanup ==="
# Clean systemd journal logs (keep only last 3 days)
sudo journalctl --vacuum-time=3d

# Clean older log files
sudo find /var/log -type f -name "*.log" -mtime +7 -delete 2>/dev/null || true
sudo find /var/log -type f -name "*.gz" -mtime +7 -delete 2>/dev/null || true

echo -e "\n=== Step 5: Temporary files cleanup ==="
# Clean /tmp directory
sudo find /tmp -type f -atime +7 -delete 2>/dev/null || true

# Clean user cache
rm -rf ~/.cache/* 2>/dev/null || true

echo -e "\n=== Step 6: Other cleanup ==="
# Clean snap cache if snap is installed
if command -v snap &> /dev/null; then
    sudo snap set system refresh.retain=2
    LANG=en_US.UTF-8 sudo snap list --all | awk '/disabled/{print $1, $3}' | while read snapname revision; do
        sudo snap remove "$snapname" --revision="$revision" 2>/dev/null || true
    done
fi

echo -e "\n=== Cleanup Complete ==="
echo "Disk usage after cleanup:"
df -h /

echo -e "\nLargest directories in /home:"
du -h --max-depth=1 /home/ 2>/dev/null | sort -hr | head -10

echo -e "\nLargest files in /var:"
sudo find /var -type f -size +100M -exec ls -lh {} \; 2>/dev/null | head -10

echo -e "\n=== Cleanup script finished ==="
