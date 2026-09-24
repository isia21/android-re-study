#include "stdafx.h"
#include "common/Utils.h"

int main()
{
	std::vector<uint8_t> byteArray = Utils::HexString2ByteArray("8d127684cbc37c17616d806cf50473cc");
	for (auto byte : byteArray) 
		printf("%02X ", byte);
	
	return S_OK;
}