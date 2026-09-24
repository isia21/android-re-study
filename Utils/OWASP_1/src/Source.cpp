#include "stdafx.h"
#include "common/Utils.h"


namespace OWASP
{
	void UCL1()
	{
		printf("`%s` Sol\n", __FUNCTION__);

		std::vector<uint8_t> vHSBA = Utils::HexString2ByteArray("8d127684cbc37c17616d806cf50473cc");
		printf("HS2BA: ");
		for (auto byte : vHSBA)
			printf("%02X ", byte);
		printf("\n");

		std::vector<uint8_t> vB64De = Utils::Base64::Decode("5UJiFctbmgbDoLXmpL12mkno8HT4Lv8dlat8FxR2GOc=");
		printf("B64De: ");
		for (auto byte : vB64De)
			printf("%02X ", byte);
		printf("\n");

		std::vector<uint8_t> vResult = Utils::AesEcbPkcs7Decrypt(vHSBA, vB64De);
		printf("\n--Result--\n");
		for (auto byte : vResult)
			printf("%02X -> `%c`\n", byte, byte);

		printf("\n");
	}
}


int main()
{
	OWASP::UCL1();

	return EXIT_SUCCESS;
}