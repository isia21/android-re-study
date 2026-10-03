#!/usr/bin/env bash
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
VENV_PATH="$HOME/tools/frida/venv"

# Подтягиваем venv, если frida не в PATH
#       if ! command -v frida &> /dev/null && [ -f "$VENV_PATH/bin/activate" ]; then
#           # shellcheck disable=SC1091
#           source "$VENV_PATH/bin/activate"
#       fi


if [ -d "$VENV_PATH/bin" ]; then
    export PATH="$VENV_PATH/bin:$PATH"
fi

LEVEL=$1
if [ -z "$LEVEL" ]; then
    echo -e "${BLUE}=== Запуск пайплайна OWASP UnCrackable ===${NC}"
    echo "1) UnCrackable Level 1"
    echo "2) UnCrackable Level 2"
    echo "3) UnCrackable Level 3"
    echo "4) UnCrackable Level 4 (r2pay)"
    read -rp "Выберите уровень [1-4]: " LEVEL
fi

case $LEVEL in
    1)
        PKG="owasp.mstg.uncrackable1"
        APK_NAME="UnCrackable-Level1.apk"
        SOL_FILE="$SCRIPT_DIR/sols/ucl_1.js"
        ;;
    2)
        PKG="owasp.mstg.uncrackable2"
        APK_NAME="UnCrackable-Level2.apk"
        SOL_FILE="$SCRIPT_DIR/sols/ucl_2.js"
        ;;
    3)
        PKG="owasp.mstg.uncrackable3"
        APK_NAME="UnCrackable-Level3.apk"
        SOL_FILE="$SCRIPT_DIR/sols/ucl_3.js"
        ;;
    4)
        PKG="re.pwnme"
        APK_NAME="r2pay-v1.0.apk"
        SOL_FILE="$SCRIPT_DIR/sols/ucl_4.js"
        ;;
    *)
        echo -e "${RED}[-] Неизвестный уровень: $LEVEL${NC}"
        exit 1
        ;;
esac

APK_PATH="$SCRIPT_DIR/UCLs/APKs/$APK_NAME"

echo -e "${BLUE}[*] Целевой пакет : ${GREEN}$PKG${NC}"
echo -e "${BLUE}[*] APK файл     : ${YELLOW}$APK_PATH${NC}"
echo -e "${BLUE}[*] Скрипт Frida : ${YELLOW}$SOL_FILE${NC}"

# 1. Проверяем наличие JS-решения
if [ ! -f "$SOL_FILE" ]; then
    echo -e "${RED}[-] Ошибка: Файл решения $SOL_FILE не найден!${NC}"
    exit 1
fi

# 2. Проверяем, установлено ли приложение на устройстве
echo -e "${BLUE}[*] Проверка установки пакета $PKG...${NC}"
IS_INSTALLED=$(adb shell "pm list packages | grep -w '$PKG' || true" | tr -d '\r')

if [ -z "$IS_INSTALLED" ]; then
    echo -e "${YELLOW}[!] Пакет не установлен. Устанавливаем из $APK_PATH...${NC}"
    if [ ! -f "$APK_PATH" ]; then
        echo -e "${RED}[-] APK файл не найден по пути: $APK_PATH${NC}"
        exit 1
    fi
    adb install -r "$APK_PATH"
    echo -e "${GREEN}[+] APK успешно установлен!${NC}"
else
    echo -e "${GREEN}[+] Пакет $PKG уже установлен.${NC}"
fi

# 3. Проверяем, запущен ли frida-server
if ! frida-ps -U > /dev/null 2>&1; then
    echo -e "${YELLOW}[!] frida-server не отвечает. Запускаем инициализацию...${NC}"
    "$SCRIPT_DIR/frida_connect_init.sh"
fi

# 4. Запуск в режиме SPAWN с подгрузкой скрипта
echo -e "${GREEN}[*] Запуск таргета через Frida SPAWN...${NC}"
echo -e "${YELLOW}[!] Для выхода из сессии нажмите Ctrl+C или Ctrl+D${NC}"
frida -U -f "$PKG" -l "$SOL_FILE"