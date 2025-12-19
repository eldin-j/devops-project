#!/usr/bin/env bash
set -euo pipefail

LOG_DIR="/opt/devops-project/logs"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/part0-setup-$(date +%Y%m%d-%H%M%S).log"

exec > >(tee -a "$LOG_FILE") 2>&1

echo "=== Part0 setup started: $(date -Is) ==="

echo "[1/4] OS update/upgrade"
sudo apt-get update -y
sudo apt-get upgrade -y

echo "[2/4] Install Git + Java 17"
sudo apt-get install -y git openjdk-17-jdk

echo "[3/4] Install Gradle (snap, classic)"
# snapd обычно уже есть на Ubuntu Server, но на всякий случай:
sudo apt-get install -y snapd
sudo snap install gradle --classic || true

echo "[4/4] Versions"
echo "--- java -version ---"
java -version || true
echo "--- git --version ---"
git --version || true
echo "--- gradle -v ---"
gradle -v || true

echo "=== Part0 setup finished: $(date -Is) ==="
echo "Log saved to: $LOG_FILE"
