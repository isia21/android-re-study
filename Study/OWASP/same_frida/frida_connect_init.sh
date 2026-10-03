#!/usr/bin/env bash
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

FRIDA_SERVER_PATH="/data/local/tmp/frida-server"
VENV_PATH="$HOME/tools/frida/venv"

echo -e "${BLUE}[*] === Frida Connect & Init ===${NC}"

# 1. Используем Frida из venv
if [ -d "$VENV_PATH/bin" ]; then
    export PATH="$VENV_PATH/bin:$PATH"
fi

# 2. Проверяем ADB
DEVICE_ID=$(adb devices | grep -w "device" | awk '{print $1}' | head -n 1)
if [ -z "$DEVICE_ID" ]; then
    echo -e "${RED}[-] Ошибка: Устройство не найдено в ADB!${NC}"
    exit 1
fi
echo -e "${GREEN}[+] Устройство: $DEVICE_ID${NC}"

# 3. Проверяем, запущен ли frida-server
PID=$(adb shell "su -c 'pidof frida-server'" 2>/dev/null | tr -d '\r' || true)

if [ -n "$PID" ]; then
    echo -e "${GREEN}[+] frida-server уже работает (PID: $PID). Не трогаем его.${NC}"
else
    echo -e "${YELLOW}[!] frida-server не запущен. Запускаем...${NC}"
    adb shell "su -c '$FRIDA_SERVER_PATH > /dev/null 2>&1 &'"
    sleep 2
    PID=$(adb shell "su -c 'pidof frida-server'" 2>/dev/null | tr -d '\r' || true)
    echo -e "${GREEN}[+] frida-server запущен (PID: $PID)${NC}"
fi

# 4. Быстрая проверка связи
echo -e "${BLUE}[*] Проверка связи...${NC}"
frida-ps -U | head -n 5
echo -e "${GREEN}[+] УСПЕШНО: Frida готова к работе!${NC}"