#include "stdafx.h"
#include "Utils.h"

namespace Utils
{

	std::vector<uint8_t> HexString2ByteArray(const std::string& str) {
		size_t length = str.length();
		std::vector<uint8_t> result(length / 2);
		for (size_t i = 0; i < length; i += 2) {
			auto digit = [](char c) -> int {
				if (c >= '0' && c <= '9') return c - '0';
				if (c >= 'a' && c <= 'f') return c - 'a' + 10;
				if (c >= 'A' && c <= 'F') return c - 'A' + 10;
				return -1;
				};
			result[i / 2] = static_cast<uint8_t>(
				(digit(str[i]) << 4) + digit(str[i + 1])
				);
		}
		return result;
	}
}