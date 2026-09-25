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
	namespace UCL3
	{
        /*
        struct OpaqueNode {
            int32_t     val;
            OpaqueNode* next;
        };

        uint32_t    dword_6004;                 
        OpaqueNode* _1_sub_doit__opaque_list1_1;
        OpaqueNode* _1_sub_doit__opaque_list2_1;

        static inline int32_t lcg_rand() {
            dword_6004 = dword_6004 * 1103515245u + 12345u;
            return static_cast<int32_t>(dword_6004 & 0x7FFFFFFF);
        }
        */


        // Реальная полезная структура, передаваемая через a1
        struct TargetContext {
            uint8_t  raw_guid_or_key[16]; // xmmword_3480 (16 байт)
            uint32_t magic_field1;        // 5901832   (0x005A0E08)
            uint32_t magic_field2;        // 336791575 (0x14130C17)
            uint8_t  terminator_flag;     // 0
        };

        extern const uint8_t xmmword_3480[16] = 
            //xmmword_3480    xmmword 15131D5A1903000D1549170F1311081Dh
        { 0x1D, 0x08, 0x11, 0x13, 0x0F, 0x17, 0x49, 0x15, 0x0D, 0x00, 0x03, 0x19, 0x5A, 0x1D, 0x13, 0x15 };

        extern const uint8_t xmmword_3480[16];

        TargetContext* InitContext(TargetContext* ctx)
        {
            /* ========================================================================= 
            const int NUM_NODES = 100;
            for (int i = 0; i < NUM_NODES; ++i) {
                int32_t val = lcg_rand();
                OpaqueNode* node = static_cast<OpaqueNode*>(malloc(sizeof(OpaqueNode)));
                if (!node) {
                    continue;
                }

                node->val = val;
                if (!_1_sub_doit__opaque_list1_1) {
                    node->next = node;
                    _1_sub_doit__opaque_list1_1 = node;
                } else {
                    node->next = _1_sub_doit__opaque_list1_1->next;
                    _1_sub_doit__opaque_list1_1->next = node;
                }
            }
            _1_sub_doit__opaque_list2_1 = _1_sub_doit__opaque_list1_1;
            ========================================================================= */

            // ПОЛЕЗНАЯ НАГРУЗКА (Payload)
            if (!ctx) {
                return nullptr;
            }

            // Инициализация структуры контекста
            std::memcpy(ctx->raw_guid_or_key, xmmword_3480, sizeof(ctx->raw_guid_or_key));
            ctx->magic_field1 = 5901832;
            ctx->magic_field2 = 336791575;
            ctx->terminator_flag = 0;

            return ctx;
        }

		void UCL3()
		{
			printf("`%s` Sol\n", __FUNCTION__);

			std::string strXorkey = "pizzapizzapizzapizzapizz";
            char xorKey[24];

			memcpy(xorKey, strXorkey.c_str(), sizeof(xorKey));

            TargetContext* pCtx = new TargetContext();
            InitContext(pCtx);

            int v5 = 0;

            for (char* i = xorKey; ; i++) {
				printf("[%02x] -> `%c` \n", v5, (*((BYTE*)pCtx + v5) ^ *(BYTE*)i));

                v5++;
                if (v5 == 24)
                    break;

            }

            delete pCtx;
			printf("\n");
		}
	}
}


int main()
{
	//OWASP::UCL1();
	OWASP::UCL3::UCL3();

	return EXIT_SUCCESS;
}