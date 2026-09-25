# 1. Верификация строки
```
char __cdecl Java_sg_vantagepoint_uncrackable3_CodeCheck_bar(JNIEnv *a1, int a2, void *pData)
{
  jbyte *pData_BA; // esi
  unsigned int v5; // eax
  char **i; // ecx
  bool v7; // zf
  char result; // al
  __int128 v9; // [esp+0h] [ebp-3Ch] BYREF
  int v10; // [esp+10h] [ebp-2Ch]
  int v11; // [esp+14h] [ebp-28h]
  char v12; // [esp+18h] [ebp-24h]
  unsigned int v13; // [esp+28h] [ebp-14h]

  v13 = __readgsdword(0x14u);
  v9 = 0LL;
  v11 = 0;
  v10 = 0;
  v12 = 0;
  if ( dword_6038 == 2 )
  {
    sub_FA0((int)&v9);
    pData_BA = (*a1)->GetByteArrayElements(a1, pData, 0);
    if ( (*a1)->GetArrayLength(a1, pData) == 24 )
    {
      v5 = 0;
      for ( i = &xorKey; pData_BA[v5] == (*((_BYTE *)&v9 + v5) ^ *(_BYTE *)i); i = (char **)((char *)i + 1) )
      {
        v7 = ++v5 == 24;
        if ( v5 >= 0x18 )
        {
          result = 1;
          if ( v7 )
            return result;
          return 0;
        }
      }
    }
  }
  return 0;
}
int __cdecl Java_sg_vantagepoint_uncrackable3_MainActivity_init(JNIEnv *a1, int a2, void *a3)
{
  const char *v4; // esi
  int result; // eax

  sub_3250();
  v4 = (*a1)->GetByteArrayElements(a1, a3, 0);
  strncpy((char *)&xorKey, v4, 24u);
  result = ((int (__cdecl *)(JNIEnv *, void *, const char *, int))(*a1)->ReleaseByteArrayElements)(a1, a3, v4, 2);
  ++dword_6038;
  return result;
}
```
> strlen() == 24     
> xorkey передается из java как 
```
    private static final String xorkey = "pizzapizzapizzapizzapizz"; 

    ...

    private native void init(byte[] bArr);
    
    ...

    init(xorkey.getBytes());`
```

# 2. Обфускация?
Функция sub_FA0 содержит развернутый цикл на 100ит., которые по сути ничего не делают.  
> Вызывающая функция верификации строки не ожидает ответа, а рассчитывает на данные, которые по итогу будут внутри `v9`         

Поэтому смотрим, что именно пишется в аргумент внутри FA0:
```
Xref	Line	Column	Pseudocode line
r	2307	23	    result = (_DWORD *)retn_;
r	2308	16	    *(_DWORD *)(retn_ + 4) = 0;
w	2309	15	    *(_DWORD *)retn_ = 0;
r	2310	16	    *(_DWORD *)(retn_ + 12) = 0;
r	2311	16	    *(_DWORD *)(retn_ + 8) = 0;
r	2312	15	    *(_BYTE *)(retn_ + 24) = 0;
w	2313	15	    *(_OWORD *)retn_ = xmmword_3480;
r	2314	16	    *(_DWORD *)(retn_ + 16) = 5901832;
r	2315	16	    *(_DWORD *)(retn_ + 20) = 336791575;
```
1. Зачищаем 24 байта (как раз наша длинна строки)
2. В самое начало блока 24-байт пишем 16 байт из 128битного `xmmword_3480` = `00003480  1D 08 11 13 0F 17 49 15 0D 00 03 19 5A 1D 13 15`
3. Точечно пишем по оффсетам: 0x10-0x13 = `0x005A0E08`, 0x14-0x17 = `0x14130817`
4. Байт 24 / 0x18 уже записан как 0x00
> 1D 08 11 13 0F 17 49 15 0D 00 03 19 5A 1D 13 15 | 08 0E 5A 00 | 17 08 13 14 | 00

> Ответ = 0x1d0811130f1749150d0003195a1d1315080e5a0017081314 ^ `pizzapizzapizzapizzapizz` = `making owasp great again`


[Ответ по задаче](../sol/result.txt)    