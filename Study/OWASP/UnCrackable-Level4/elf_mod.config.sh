#!/usr/bin/env bash
# Конфиг для re-elf-mod.sh — цель UCL4/r2pay

TARGET="$HOME/dev/sandbox/android-re-study/Study/OWASP/UnCrackable-Level4"
SRC="$TARGET/elf_mod/rootbeer/rootbeerlib/src/main/cpp"
CPP_FILES="toolChecker.cpp"

SO_NAME="libtool-checker.so"
APK_NAME="r2pay-v1.0.apk"
PKG="re.pwnme"

ABI="arm64-v8a armeabi-v7a x86_64"

# Опционально:
# BUILD_DIR="$TARGET/elf_mod/build"
# WORK_DIR="$TARGET/work"
# OUT_NAME="r2pay-hooked"
# NDK_VER="26.1.10909125"
# EXTRA_CFLAGS=""
# EXTRA_LDFLAGS=""
