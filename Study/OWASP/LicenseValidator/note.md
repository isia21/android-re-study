```cpp
int __fastcall validate_key(_BYTE *_pInputData)
{
  int bResult_at0; // r4
  int bResult_at1; // r4
  int bResult_at2; // r4
  int bResult_at3; // r4
  int bResult_at4; // r4
  _BYTE bResult[8]; // [sp+8h] [bp-1Ch]
  int i; // [sp+10h] [bp-14h]
  _BYTE *pInputData; // [sp+14h] [bp-10h]
                                                // byte pInputData[10+];
                                                // size(pInputData)>=10;
                                                // pInputData[i*2+1] == xorKey;
                                                // pInputData[i*2]   == xoredVal;
  pInputData = _pInputData;
  for ( i = 0; i <= 4; ++i )
  {
    bResult[i] = *pInputData ^ pInputData[1];
    pInputData += 2;                            // 
                                                // [i] => result[i] == pInputData[i*2] ^ pInputData[i*2+1];
                                                // 
                                                // [0] => result[0] == pInputData[0] ^ pInputData[1];
                                                // [1] => result[1] == pInputData[2] ^ pInputData[3];
                                                // [2] => result[2] == pInputData[4] ^ pInputData[5];
                                                // [3] => result[3] == pInputData[6] ^ pInputData[7];
                                                // [4] => result[4] == pInputData[8] ^ pInputData[9];
                                                // 
  }
  bResult_at0 = bResult[0];



  if ( bResult_at0 == sub_16F0()                // 0x4C
    && (bResult_at1 = bResult[1], bResult_at1 == sub_170C())// 0x4F
    && (bResult_at2 = bResult[2], bResult_at2 == sub_16F0())// 0x4C
    && (bResult_at3 = bResult[3], bResult_at3 == sub_1728())// 0x5A
    && (bResult_at4 = bResult[4], bResult_at4 == sub_1744()) )// 0x21
  {
    return puts("Product activation passed. Congratulations!");//  == byte bReslut[5] = 4C4F4C5A21 = `LOLZ!`
  }
  else
  {
    return puts("Incorrect serial.");
  }
}
```