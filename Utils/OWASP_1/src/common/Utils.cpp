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

	std::vector<BYTE> AesEcbPkcs7Decrypt(const std::vector<BYTE>& key, const std::vector<BYTE>& ciphertext) {
		BCRYPT_ALG_HANDLE hAlg = nullptr;
		BCRYPT_KEY_HANDLE hKey = nullptr;
		NTSTATUS status;
		std::vector<BYTE> vPlainText;

		// 1. Открываем алгоритм AES
		if (!BCRYPT_SUCCESS(status = BCryptOpenAlgorithmProvider(&hAlg, BCRYPT_AES_ALGORITHM, nullptr, 0)))
			throw std::runtime_error("BCryptOpenAlgorithmProvider failed");

		// 2. Устанавливаем режим ECB
		if (!BCRYPT_SUCCESS(status = BCryptSetProperty(hAlg, BCRYPT_CHAINING_MODE,
			(PUCHAR)BCRYPT_CHAIN_MODE_ECB, sizeof(BCRYPT_CHAIN_MODE_ECB), 0)))
			throw std::runtime_error("BCryptSetProperty (ECB) failed");

		// 3. Создаём симметричный ключ
		if (!BCRYPT_SUCCESS(status = BCryptGenerateSymmetricKey(hAlg, &hKey, nullptr, 0,
			(PUCHAR)key.data(), (ULONG)key.size(), 0)))
			throw std::runtime_error("BCryptGenerateSymmetricKey failed");

		// 4. Получаем размер расшифрованных данных
		ULONG cbResult = 0;
		if (!BCRYPT_SUCCESS(status = BCryptDecrypt(hKey, (PUCHAR)ciphertext.data(), (ULONG)ciphertext.size(),
			nullptr, nullptr, 0, nullptr, 0, &cbResult, BCRYPT_BLOCK_PADDING)))
			throw std::runtime_error("BCryptDecrypt (size query) failed");

		vPlainText.resize(cbResult);

		// 5. Выполняем расшифровку с PKCS#7
		if (!BCRYPT_SUCCESS(status = BCryptDecrypt(hKey, (PUCHAR)ciphertext.data(), (ULONG)ciphertext.size(),
			nullptr, nullptr, 0, vPlainText.data(), (ULONG)vPlainText.size(), &cbResult, BCRYPT_BLOCK_PADDING)))
			throw std::runtime_error("BCryptDecrypt failed");

		vPlainText.resize(cbResult);

		// 6. Очистка ресурсов
		if (hKey) 
			BCryptDestroyKey(hKey);
		if (hAlg) 
			BCryptCloseAlgorithmProvider(hAlg, 0);

		return vPlainText;
	}

	namespace Base64
	{
		static const std::string base64_chars =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			"abcdefghijklmnopqrstuvwxyz"
			"0123456789+/";

		std::string Encode(const std::vector<uint8_t>& data) {
			std::string encoded;
			int val = 0, valb = -6;
			for (uint8_t c : data) {
				val = (val << 8) + c;
				valb += 8;
				while (valb >= 0) {
					encoded.push_back(base64_chars[(val >> valb) & 0x3F]);
					valb -= 6;
				}
			}
			if (valb > -6) encoded.push_back(base64_chars[((val << 8) >> (valb + 8)) & 0x3F]);
			while (encoded.size() % 4) encoded.push_back('=');
			return encoded;
		}
		std::vector<uint8_t> Decode(const std::string& base64String) {
			std::vector<uint8_t> decoded;
			std::vector<int> T(256, -1);
			for (int i = 0; i < 64; i++) 
				T[base64_chars[i]] = i;
			int val = 0, valb = -8;
			for (uint8_t c : base64String) {
				if (T[c] == -1) 
					break;
				val = (val << 6) + T[c];
				valb += 6;
				if (valb >= 0) {
					decoded.push_back(static_cast<uint8_t>((val >> valb) & 0xFF));
					valb -= 8;
				}
			}
			return decoded;
		}
	}
}