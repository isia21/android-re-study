#pragma once

namespace Utils
{
	std::vector<uint8_t> HexString2ByteArray(const std::string& str);
	std::vector<BYTE> AesEcbPkcs7Decrypt(const std::vector<BYTE>& key, const std::vector<BYTE>& ciphertext);

	namespace Base64
	{
		std::string Encode(const std::vector<uint8_t>& data);
		std::vector<uint8_t> Decode(const std::string& base64String);
	}
}