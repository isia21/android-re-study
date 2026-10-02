1. strlen(PIN) == 4 && strlen(cost) <= 8

2. Если мы нигде не тригернули защиту приложения, то на момент обращения к генератору хэша мы передаем ключ как (byte)(0xF0)

3. Мы обращаемся к генератору хэша как `byte[] gXftm3iswpkVgBNDUp(byte[] bArr, byte b)` 

|Тип |Значение|     
|-|-|
|PIN| `%08d` |     
|Сумма| `%08d` |   
|Key| `(byte) 0xF0` |   
|Data| `%08d%08d`, PIN, Сумма| 

> RetnStruct   

|Type | Offset | Description|     
|-|-|-|
|BYTE| 0 | eStatus: Флаг корректности ключа |     
|BYTE[] | 1-N | pKetData: Сам ключ |

> RetnStruct.eStatus == 81 == eStatus.Error == сгенерированный ключ не валиден  
> RetnStruct.eStatus != 81 == eStatus.Success == сгенерированный ключ валиден
```
byte[] out = mainActivity2.gXftm3iswpkVgBNDUp(inBuff, mainActivity2.u8_Key); // В `out[]` возвращается контейнер.   

RetnStruct gXftm3iswpkVgBNDUp(Data.GetBytes(), Key);
```

4. Наша задача в библиотеке `libnative-lib.so` определить внутри метода `gXftm3iswpkVgBNDUp`, что определяет значение нулевого байта, что туда ставит 81 (ошибка)




---

ТЕОРИЯ

0. Нативная библиотека вообще не содержит реализации метода `*gXftm3iswpkVgBNDUp*`. Поэтому поиск шел от противного - флаг невалидности ключа -> Find String `, 51h` (установка флага ошибки == 81)
1. Найдено лишь одно включение внутри sub_1780F0. Функция раздута/обфусцирована до состояния, что IDA/HexRays ее не тянет `too big function`, поэтому пробуем ориентироваться по asm 
2. Флаг валидности ключа изначаль
```
.text:00000000001780F0 var_460         = byte ptr -460h
.text:00000000001780F0 var_260         = byte ptr -260h
.text:00000000001780F0 var_240         = byte ptr -240h
.text:00000000001780F0 var_140         = byte ptr -140h
.text:00000000001780F0 var_30          = qword ptr -30h
.text:00000000001780F0
.text:00000000001780F0                 push    rbp
.text:00000000001780F1                 mov     rbp, rsp
.text:00000000001780F4                 push    r15
.text:00000000001780F6                 push    r14
.text:00000000001780F8                 push    r13
.text:00000000001780FA                 push    r12
.text:00000000001780FC                 push    rbx
.text:00000000001780FD                 sub     rsp, 4078h
.text:0000000000178104                 mov     al, cl
.text:0000000000178106                 xor     ecx, ecx
.text:0000000000178108                 mov     r8d, 10h
.text:000000000017810E                 mov     r9d, r8d
.text:0000000000178111                 lea     r10, [rbp+s]
.text:0000000000178118                 mov     r11, fs:28h
.text:0000000000178121                 mov     [rbp+var_30], r11
.text:0000000000178125                 mov     [rbp+var_1418], rdi
.text:000000000017812C                 mov     [rbp+var_1420], rsi
.text:0000000000178133                 mov     [rbp+var_1428], rdx
.text:000000000017813A                 mov     [rbp+var_1429], al
.text:0000000000178140                 mov     [rbp+var_4C1], 51h ; 'Q' ; 51h == 81 == error_code
.text:0000000000178140                                         ; итого, в самом начале анализа, мы устанавливаем флаг eStatus.Error
.text:0000000000178147                 mov     [rbp+var_143C], 10h ; 10h == 16 == looks like md5 ByteArray[].len
.text:0000000000178151                 mov     rdi, r10        ; s
.text:0000000000178154                 mov     esi, ecx        ; c
.text:0000000000178156                 mov     rdx, r9         ; n
.text:0000000000178159                 call    _memset
```

после чего, согласно rw ops от оффсета 4С1
```
Direction	Type	Address	Text
	    w	sub_1780F0+50	mov     [rbp+var_4C1], 51h ; 'Q'; 51h == 81 == error_code
Down	w	sub_1780F0+29C4	mov     [rbp+var_4C1], 23h ; '#'; Меняем флаг истинности на 35?
Down	r	sub_1780F0+E7C92	lea     r8, [rbp+var_4C1]; Читаем перед пушем в буффер вывода?
Down	w	sub_1780F0:loc_263557	mov     [rbp+var_4C1], 23h ; '#'; Меняем флаг истинности на 35?
```


ОФФТОП. 
Функция верифицированна.
Я задался вопросом, "а как вообще скрыть имя метода, если Java требует соотв/согласования для вызовов?"

Выжимка из интернета
```cpp

// Таблица соответствия Java-методов и C++ функций
static JNINativeMethod methods[] = {
    // {"имя_метода_в_Java", "сигнатура", (void*)указатель_на_C++_функцию}
    {"getSecretKey", "()Ljava/lang/String;", (void*)my_super_hidden_function}
};


// Вызывается автоматически при System.loadLibrary
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // Ищем класс, в котором объявлен native-метод
    jclass clazz = env->FindClass("com/example/app/NativeLib");
    if (clazz == nullptr) return JNI_ERR;

    // Регистрируем методы
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) < 0) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}
```

А в нашей libnative-lib.so :: JNI_OnLoad
```cpp
                        pJClazz = (*(__int64 (__fastcall **)(_QWORD, const char *))(*(_QWORD *)*pEnv + 48LL))(// env->FindClass ?
                                    *pEnv,
                                    "re/pwnme/MainActivity");

                                    ...

                                                    v18 = (*(__int64 (__fastcall **)(_QWORD, __int64, _UNKNOWN **, __int64))(*(_QWORD *)*pEnv + 1720LL))(// env->RegisterNatives(pJClazz, JNINativeMethod methods, sizeof(methods) / sizeof(methods[0])
                        *pEnv,
                        *pJClazz_1,
                        &methods,
                        1);
```
И layout отлично ложится в шаблон
```
;MethodName
.data:0000000000319020 methods         dq offset byte_319140   ; DATA XREF: JNI_OnLoad:loc_177E1A↑o
;MethodSign
.data:0000000000319020                                         ; JNI_OnLoad:loc_17808D↑o
.data:0000000000319028                 dq offset byte_319153
;MethodAddr
.data:0000000000319030                 dq offset sub_1780F0
```

однако, там лежит мусор (пока мусор)
```
.data:0000000000319140 byte_319140     db 58h, 67h, 59h, 4Bh, 52h, 0Ch, 56h, 4Ch, 48h, 4Fh, 54h
.data:0000000000319140                                         ; DATA XREF: _datadiv_decode16813968421045149467+91↑o
.data:0000000000319140                                         ; .data:methods↑o
.data:000000000031914B                 db 69h, 58h, 7Dh, 71h, 7Bh, 6Ah, 4Fh, 3Fh
```
```
0000000000319140  58 67 59 4B 52 0C 56 4C  48 4F 54 69 58 7D 71 7B  XgYKR.VLHOTiX}q{
0000000000319150  6A 4F 3F -- -- -- -- --  -- -- -- -- -- -- -- --  jO?----- -------
```

а "пока" - т.к. я нашел метод, который судя по всему декодирует имена скрытых методов в библиотеке (не факт, что только имена методов)

```cpp
__int64 datadiv_decode16813968421045149467()
{
...
  //И вот наше "имя метода"
  do
  {
    v0 = v99;
    byte_319140[v99] = byte_319140[v99] & 0xC0 | ~byte_319140[v99] & 0x3F;
    ++v99;
  }
  while ( v0 != 18 );
  
  //А сразу следом - его сигнатура
  for ( j = 0; j != 8; ++j )
    byte_319153[j] = (byte_319153[j] & 0x3E | ~byte_319153[j] & 0xC1) ^ 0x3A;
...
}
```

Я попрошу ИИ конвертировать данную функцию в IDA Python, чтоб Python скрипт немного размапил мне реальные названия X сабов

[Скрипт декода](ida_py_fix_strings.py)    
[Лог декода](ida_fixed_strings.txt)    
гол. 


Имя функций - совпало
```
2026-09-26 19:03:01.240 [+] 0x00319140 - 0x00319153 (19 байт) | byte_319140
2026-09-26 19:03:01.240     Было (Hex):  5867594b520c564c484f5469587d717b6a4f3f
2026-09-26 19:03:01.240     Стало (Hex): 675866746d33697377706b5667424e44557000
2026-09-26 19:03:01.240     Текст:       "gXftm3iswpkVgBNDUp\0"
```
Сигнатура функции:
```
2026-09-26 19:03:01.240 [+] 0x00319153 - 0x0031915B ( 8 байт) | byte_319153
2026-09-26 19:03:01.240     Было (Hex):  d3a0b9b9d2a0b9fb
2026-09-26 19:03:01.240     Стало (Hex): 285b4242295b4200
2026-09-26 19:03:01.240     Текст:       "([BB)[B\0"
```
`([BB)[B` — сигнатура метода `gXftm3iswpkVgBNDUp`  `(byte[], byte -> byte[])`



---
Валидация, что андроид не передал нам 0xFF (мы не тригернули защиты APK)
```
.text:0000000000178425
.text:0000000000178425 AndroidSecurityCheck:                   ; CODE XREF: gXftm3iswpkVgBNDUp+2D1↑j
.text:0000000000178425                 mov     eax, 0F05E28BFh ; "хорошее" состояние
.text:000000000017842A                 mov     ecx, 0A6779D96h ; "плохое" состояние
.text:000000000017842F                 mov     edx, [rbp+var_1410_Android_u8Key] ; U8-значение из ключа Android
.text:0000000000178435                 cmp     edx, 0FFh
.text:000000000017843B                 cmovge  eax, ecx        ; если edx >= 0xFF (фактически == 0xFF), взять плохое
.text:000000000017843E                 mov     [rbp+var_145C_Android_IsAndroidSafe], eax ; state = выбранное состояние
.text:0000000000178444                 jmp     loc_26362F
```

Значит AndroidSecurityCheck должно вызываться либо перед, либо после валидного PIN, ибо 
Стало быть `gXftm3iswpkVgBNDUp+2D1↑j` == часть валидного пути


а тут у нас явная проверка 
```
.text:00000000001783B0 loc_1783B0:                             ; CODE XREF: gXftm3iswpkVgBNDUp+2BB↑j
.text:00000000001783B0                 mov     eax, [rbp+var_1460__MustBeEq_3AAF5351]
.text:00000000001783B6                 sub     eax, 3AAF5351h
.text:00000000001783BB                 mov     [rbp+var_14A4], eax
.text:00000000001783C1                 jz      AndroidSecurityCheck ; "хорошее" состояние
.text:00000000001783C7                 jmp     $+5
```

```cpp
if (state == 0x3AAF5351) {
    goto AndroidSecurityCheck;
}
```


---

если все окей, и устройство безопасно 
то у нас `var_145C_Android_IsAndroidSafe` становится равным `F05E28BF`
а через часть пролога `var_145C_Android_IsAndroidSafe` копируется в `var_1460__MustBeEq_3AAF5351`



---

```
.text:0000000000178340 loc_178340:                             ; CODE XREF: gXftm3iswpkVgBNDUp+24B↑j
.text:0000000000178340                 mov     eax, [rbp+var_1460__MustBeEq_3AAF5351]
.text:0000000000178346                 sub     eax, 15ABABB3h
.text:000000000017834B                 mov     [rbp+var_1494], eax
.text:0000000000178351                 jz      ValidFlagResult_1 ; NOPE
.text:0000000000178357                 jmp     $+5

.text:0000000000178308 loc_178308:                             ; CODE XREF: gXftm3iswpkVgBNDUp+213↑j
.text:0000000000178308                 mov     eax, [rbp+var_1460__MustBeEq_3AAF5351]
.text:000000000017830E                 sub     eax, 32138F5h
.text:0000000000178313                 mov     [rbp+var_148C], eax
.text:0000000000178319                 jz      ValidFlagResult_0 ; NOPE
.text:000000000017831F                 jmp     $+5
---


---
.text:000000000017AA98
.text:000000000017AA98 ValidFlagResult_0:                      ; CODE XREF: gXftm3iswpkVgBNDUp+229↑j
.text:000000000017AA98                 mov     eax, 15ABABB3h
.text:000000000017AA9D                 mov     ecx, 0D368D7C3h
.text:000000000017AAA2                 mov     dl, 1
.text:000000000017AAA4                 xor     esi, esi
.text:000000000017AAA6                 mov     rdi, cs:y_90_ptr
.text:000000000017AAAD                 mov     r8, cs:x_89_ptr
.text:000000000017AAB4                 mov     [rbp+var_4C1_KeyValidFlag], 23h ; '#'
.text:000000000017AABB                 mov     r9d, [r8]
.text:000000000017AABE                 mov     r10d, [rdi]
.text:000000000017AAC1                 sub     esi, 1
.text:000000000017AAC4                 mov     r11d, r9d
.text:000000000017AAC7                 add     r11d, esi
.text:000000000017AACA                 imul    r9d, r11d
.text:000000000017AACE                 and     r9d, 1
.text:000000000017AAD2                 cmp     r9d, 0
.text:000000000017AAD6                 setz    bl
.text:000000000017AAD9                 cmp     r10d, 0Ah
.text:000000000017AADD                 setl    r14b
.text:000000000017AAE1                 mov     r15b, bl
.text:000000000017AAE4                 xor     r15b, 0FFh
.text:000000000017AAE8                 mov     r12b, r14b
.text:000000000017AAEB                 xor     r12b, 0FFh
.text:000000000017AAEF                 xor     dl, 1
.text:000000000017AAF2                 mov     r13b, r15b
.text:000000000017AAF5                 and     r13b, 0FFh
.text:000000000017AAF9                 and     bl, dl
.text:000000000017AAFB                 mov     sil, r12b
.text:000000000017AAFE                 and     sil, 0FFh
.text:000000000017AB02                 and     r14b, dl
.text:000000000017AB05                 or      r13b, bl
.text:000000000017AB08                 or      sil, r14b
.text:000000000017AB0B                 xor     r13b, sil
.text:000000000017AB0E                 or      r15b, r12b
.text:000000000017AB11                 xor     r15b, 0FFh
.text:000000000017AB15                 or      dl, 1
.text:000000000017AB18                 and     r15b, dl
.text:000000000017AB1B                 or      r13b, r15b
.text:000000000017AB1E                 test    r13b, 1
.text:000000000017AB22                 cmovnz  eax, ecx
.text:000000000017AB25                 mov     [rbp+var_145C_Android_IsAndroidSafe], eax
.text:000000000017AB2B                 jmp     StateMachineStart

; ...

.text:000000000026356D
.text:000000000026356D loc_26356D:                             ; CODE XREF: gXftm3iswpkVgBNDUp+2ED↑j
.text:000000000026356D                 xor     eax, eax
.text:000000000026356F                 mov     [rbp+var_13EC], 9218h
.text:0000000000263579                 mov     ecx, cs:dword_319010
.text:000000000026357F                 mov     edx, ecx
.text:0000000000263581                 mov     [rbp+var_13F8], rdx
.text:0000000000263588                 mov     ecx, [rbp+var_13EC]
.text:000000000026358E                 shl     ecx, 1
.text:0000000000263591                 mov     rdx, [rbp+var_13F8]
.text:0000000000263598                 movsxd  rsi, ecx
.text:000000000026359B                 shl     rsi, 2
.text:000000000026359F                 add     rdx, rsi
.text:00000000002635A2                 mov     [rbp+var_13F8], rdx
.text:00000000002635A9                 mov     rdx, [rbp+var_13F8]
.text:00000000002635B0                 mov     ecx, [rdx]
.text:00000000002635B2                 mov     edi, [rbp+var_13EC]
.text:00000000002635B8                 sub     eax, 52F0E4E5h
.text:00000000002635BD                 sub     eax, edi
.text:00000000002635BF                 add     eax, 52F0E4E5h
.text:00000000002635C4                 sub     ecx, 82A788F0h
.text:00000000002635CA                 sub     ecx, eax
.text:00000000002635CC                 add     ecx, 82A788F0h
.text:00000000002635D2                 mov     rdx, [rbp+var_13F8]
.text:00000000002635D9                 movsxd  rsi, ecx
.text:00000000002635DC                 shl     rsi, 2
.text:00000000002635E0                 add     rdx, rsi
.text:00000000002635E3                 mov     [rbp+var_13F8], rdx
.text:00000000002635EA                 mov     [rbp+var_13F8], 0
.text:00000000002635F5                 mov     rdx, [rbp+var_13F8]
.text:00000000002635FC                 mov     eax, [rdx]
.text:00000000002635FE                 mov     rdx, [rbp+var_13F8]
.text:0000000000263605                 movsxd  rsi, eax
.text:0000000000263608                 shl     rsi, 2
.text:000000000026360C                 add     rdx, rsi
.text:000000000026360F                 mov     [rbp+var_13F8], rdx
.text:0000000000263616                 mov     rdx, [rbp+var_13F8]
.text:000000000026361D                 mov     eax, [rdx]
.text:000000000026361F                 mov     [rbp+var_145C_Android_IsAndroidSafe], 437E1289h
.text:0000000000263629                 mov     [rbp+var_4094], eax
.text:000000000026362F
.text:000000000026362F StateMachineStart:                      ; CODE XREF: gXftm3iswpkVgBNDUp:loc_178420↑j
.text:000000000026362F                                         ; gXftm3iswpkVgBNDUp+354↑j ...
.text:000000000026362F                 jmp     loc_1781E7
```

---

Согласно заданию у нас между пином и солью есть сепаратор `:`

> There is a master PIN code that generates green tokens (aka r2coins) on the screen. If you see a red r2coin, then this token won't be validated by the community. You need to find out the 4 digits PIN code and the salt employed as well. Flag: r2con{PIN_NUMERIC:SALT_LOWERCASE}

который светится тут
```
2026-09-26 19:03:01.240 [+] 0x0031915B - 0x0031915D ( 2 байт) | byte_31915B
2026-09-26 19:03:01.240     Было (Hex):  9ba1
2026-09-26 19:03:01.240     Стало (Hex): 3a00
2026-09-26 19:03:01.240     Текст:       ":\0"
```



А волшебное трио 
```

.text:00000000001BA906                 lea     rax, [rbp+var_490]
.text:00000000001BA90D                 lea     rcx, byte_31915B
.text:00000000001BA914                 lea     rdx, [rbp+var_460]
```

Вполне смахивает на шаблон `PIN:salt`

```
.text:00000000001BA906 loc_1BA906:                             ; CODE XREF: gXftm3iswpkVgBNDUp+2E31↑j
.text:00000000001BA906                 lea     rax, [rbp+var_490]
.text:00000000001BA90D                 lea     rcx, byte_31915B
.text:00000000001BA914                 lea     rdx, [rbp+var_460]
.text:00000000001BA91B                 mov     [rbp+var_EF8], rdx
.text:00000000001BA922                 mov     [rbp+var_F00], rcx
.text:00000000001BA929                 mov     [rbp+var_F08], rax
.text:00000000001BA930                 mov     rax, [rbp+var_EF8]
.text:00000000001BA937                 mov     [rbp+var_EE8], rax
.text:00000000001BA93E                 mov     [rbp+var_F24], 0CA1D0125h
.text:00000000001BA948
.text:00000000001BA948 loc_1BA948:                             ; CODE XREF: gXftm3iswpkVgBNDUp:loc_1C002B↓j
.text:00000000001BA948                 mov     eax, [rbp+var_F24]
.text:00000000001BA94E                 mov     ecx, eax
.text:00000000001BA950                 sub     ecx, 840D921Bh
.text:00000000001BA956                 mov     [rbp+var_206C], eax
.text:00000000001BA95C                 mov     [rbp+var_2070], ecx
.text:00000000001BA962                 jz      loc_1BBB9E
.text:00000000001BA968                 jmp     $+5

...
.text:000000000019C016 loc_19C016:                             ; CODE XREF: gXftm3iswpkVgBNDUp+2F49↑j
.text:000000000019C016                 lea     rax, [rbp+var_490]
.text:000000000019C01D                 lea     rcx, byte_31915B
.text:000000000019C024                 lea     rdx, [rbp+var_460]
.text:000000000019C02B                 mov     [rbp+var_1058], rdx
.text:000000000019C032                 mov     [rbp+var_1060], rcx
.text:000000000019C039                 mov     [rbp+var_1068], rax
.text:000000000019C040                 mov     rax, [rbp+var_1058]
.text:000000000019C047                 mov     [rbp+var_1048], rax
.text:000000000019C04E                 mov     [rbp+var_1084], 0CA1D0125h
.text:000000000019C058
.text:000000000019C058 loc_19C058:                             ; CODE XREF: gXftm3iswpkVgBNDUp:loc_1A18A3↓j
.text:000000000019C058                 mov     eax, [rbp+var_1084]
.text:000000000019C05E                 mov     ecx, eax
.text:000000000019C060                 sub     ecx, 840D921Bh
.text:000000000019C066                 mov     [rbp+var_1BC8], eax
.text:000000000019C06C                 mov     [rbp+var_1BCC], ecx
.text:000000000019C072                 jz      loc_19D36E
.text:000000000019C078                 jmp     $+5
```

а что если попробовать вывести PIN или SALT (0x460 или 0x490) в поток возврата.     
чтоб нативная обфусцированная функция сама выдала все свои потроха?

что-то одно из них - соль   
т.е. у нас там должно лежать     
1. Переданный юзером PIN - он нам не интересен
2. Соль, которую библиотека сама должна подставить

---

1. Делаем вывод Соли в ответный буффер, фиксируем ее
2. Ищем, а кто жрет эту склейку `0x4{A}0 : 0x4{B}}0`
3. Тот кто жрет склейку - должен быть по идее алгоритмом получения хэша, не?

---

Очередной вектор. Аналогия с отправлением/враппером.

Я проверил libtool-checker.so - это паблик библиотека, со своим репо.
Код в репо польностью повторяет то, что я вижу в IDA. 

Я могу скомпилировать свою версию tool-checker.so, которая, крутясь в адресном пространстве одного процесса (если в android все работает так же как под windows), сможет получить доступ/поменять/пропатчить байты native-lib (которая верифицирует пин/соль). и все это будет крутиться в рантайме. Обходя любые проверки, т.к. приложение само загрузит мой. По классике - реальная полезная нагрузка + яд и мы буквально становимся трояном.

https://github.com/allrg/rootbeerFresh/blob/master/LibRootbeerFresh/src/main/jni/toolChecker.cpp

https://github.com/allrg/rootbeerFresh/

```cpp

// Функция для отправки Toast. 
// Вам понадобится доступ к объекту JNIEnv*.
void showAndroidToast(JNIEnv* env, const std::string& message) {
    // 1. Ищем класс по его полному пути (замените точки на слэши)
    jclass mainActivityClass = env->FindClass("com/example/myapp/MainActivity");
    if (mainActivityClass == nullptr) return;

    // 2. Ищем статический метод. 
    // "(Ljava/lang/String;)V" означает: принимает String, возвращает void (V)
    jmethodID showToastMethod = env->GetStaticMethodID(mainActivityClass, "showToast", "(Ljava/lang/String;)V");
    if (showToastMethod == nullptr) return;

    // 3. Конвертируем C++ std::string в Java jstring
    jstring jmsg = env->NewStringUTF(message.c_str());

    // 4. Вызываем статический метод
    env->CallStaticVoidMethod(mainActivityClass, showToastMethod, jmsg);

    // 5. Освобождаем локальную ссылку на строку
    env->DeleteLocalRef(jmsg);
    env->DeleteLocalRef(mainActivityClass);
}
```

```cpp

/*
import android.widget.Toast;
Toast.makeText(MainActivity.this.getApplicationContext(), "Tokenizing your r2coin...", 0).show();
*/

jclass toast = env->FindClass("android/widget/Toast");

jmethodID makeText =
    env->GetStaticMethodID(
        toast,
        "makeText",
        "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;"
    );
    ```

    ---
```

---

# 1. Запуск эмулятора

```
adb start-server
adb devices

emulator -avd ucl34 -no-snapshot-load -gpu host -memory 4096 &\
```

# 2. Сборка и тесты
```
source ~/.profile
cd tools/scripts/
re-elf-mod.sh all -c ~/dev/sandbox/android-re-study/Study/OWASP/UnCrackable-Level4/elf_mod.config.sh
adb logcat | grep -iE 'rootbeer|Trojan|fatal|error|elf|som33ly|SelfHook'
```

# 3. PIN найден

```
10-01 11:50:44.656  9345  9345 I som33ly-log: JNI_OnLoad vm=0x7696610b3610
10-01 11:50:44.656  9345  9345 I som33ly-log: [*] bypass
10-01 11:50:44.656  9345  9345 I som33ly-log: base=0x769480667000, DoStaf addr=0x7694807df0f0
10-01 11:50:44.656  9345  9345 I som33ly-log: resolved: fn=0x7694807df0f0 class=0x2b8a
10-01 11:50:44.657  9345  9393 I som33ly-log: [W0] start, range 0..2500
10-01 11:50:44.657  9345  9345 I som33ly-log: Started 4 workers, total pins = 10000
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/local/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/local/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/local/xbin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /sbin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /su/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/bin/.ext/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/bin/failsafe/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/sd/xbin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/usr/we-need-root/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/xbin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /cache/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /dev/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /product/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /apex/com.android.runtime/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /apex/com.android.art/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system_ext/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /odm/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /vendor/bin/su Absent :(
10-01 11:50:44.657  9345  9345 I som33ly-log: LOOKING FOR BINARY: /vendor/xbin/su Absent :(
10-01 11:50:44.659  9345  9394 I som33ly-log: [W1] start, range 2500..5000
10-01 11:50:44.659  9345  9345 I som33ly-log: [*] bypass
10-01 11:50:44.659  9345  9345 I som33ly-log: Brute-force already running
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/local/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/local/bin/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/local/xbin/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /sbin/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /su/bin/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/bin/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/bin/.ext/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/bin/failsafe/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/sd/xbin/su Absent :(
10-01 11:50:44.659  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/usr/we-need-root/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system/xbin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /cache/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /data/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /dev/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /product/bin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /apex/com.android.runtime/bin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /apex/com.android.art/bin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /system_ext/bin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /odm/bin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /vendor/bin/su Absent :(
10-01 11:50:44.660  9345  9345 I som33ly-log: LOOKING FOR BINARY: /vendor/xbin/su Absent :(
10-01 11:50:44.662  9345  9396 I som33ly-log: [W3] start, range 7500..10000
10-01 11:50:44.664  9345  9395 I som33ly-log: [W2] start, range 5000..7500
10-01 11:50:44.747  9345  9365 W OpenGLRenderer: Failed to initialize 101010-2 format, error = EGL_SUCCESS
10-01 11:50:44.862   586  3200 W BatteryExternalStatsWorker: error reading modem stats:ERROR_INVALID_INFO_RECEIVED
10-01 11:50:47.326   586  1608 E TaskPersister: File error accessing recents directory (directory doesn't exist?).
10-01 11:51:43.092   586  4876 W BatteryExternalStatsWorker: error reading Bluetooth stats: 11
10-01 11:53:01.362  9345  9393 I som33ly-log: [W0] done=100 last_pin=99 avg=1367.0 ms/call
10-01 11:53:01.722  9345  9396 I som33ly-log: [W3] done=100 last_pin=7599 avg=1370.6 ms/call
10-01 11:53:03.440  9345  9395 I som33ly-log: [W2] done=100 last_pin=5099 avg=1387.8 ms/call
10-01 11:53:03.569  9345  9394 I som33ly-log: [W1] done=100 last_pin=2599 avg=1389.1 ms/call

...

10-01 12:13:26.104  9345  9395 I som33ly-log: === FOUND === pin=00005971 bValue=0xFFFFFFF0
10-01 12:13:26.104  9345  9395 I som33ly-log: === result[0..17]: 23 F4 D5 D2 ...
10-01 12:13:26.104  9345  9395 I som33ly-log: [W2] exit, done=972 in 1361440 ms
10-01 12:13:26.180  9345  9394 I som33ly-log: [W1] exit, done=979 in 1361520 ms
10-01 12:13:27.183  9345  9396 I som33ly-log: [W3] exit, done=978 in 1362519 ms
10-01 12:13:27.274  9345  9393 I som33ly-log: [W0] exit, done=979 in 1362616 ms
```


# 4. Поиск соли

> Метод верификации ввода проверяет по шаблону `PIN:salt`   
> Пин найден, разделитель ":" найден, 

- unk_288160 
- unk_2881D0
- unk_2881F0

соль
```
10-02 08:51:07.147  8529  8529 I som33ly-log: === result[0..17]: 23 : `r2c-f4d5d25ee0566da993c0ab6064b8e95b`
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 00288160 :	F6 97 41 70 D0 0F 60 5D	72 65 2F 70 77 6E 6D 65		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 00288170 :	2F 4D 61 69 6E 41 63 74	69 76 69 74 79 00 40 00		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 00288180 :	25 2E 32 58 20 25 2E 32	58 20 25 2E 32 58 20 25		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 00288190 :	2E 32 58 0A 00 00 00 00	00 00 00 00 00 00 00 00		
>>>>    10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 002881A0 :	4A D8 91 93 4B 99 C3 A0	44 5F 66 AD 76 EA A1 06		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 002881B0 :	B7 0E 29 F6 61 F7 8D AC	F5 41 78 7D F5 9B A2 25		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 002881C0 :	E1 44 62 84 88 B4 6B 4C	60 47 D4 CE D3 8A 3A F7		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 002881D0 :	52 FD FC 07 21 82 65 4F	16 3F 5F 0F 9A 62 1D 72		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 002881E0 :	95 66 C7 4D 10 03 7C 4D	7B BB 04 07 D1 E2 C6 49		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 002881F0 :	4A CB 53 25 24 4C B1 9F	4C 59 BB CB 84 5E A6 75		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 00288200 :	81 78 25 97 07 E1 E6 AF	23 7C C2 74 F1 D2 A5 BF		
10-02 08:51:07.147  8529  8529 I som33ly-log: === data dump 00288210 :	06 00 00 00 05 00 00 00	00 00 00 00 00 00 00 00		
```


# 5. Ответ

PIN:    `5971`  
SLAT:   `4ad891934b99c3a0445f66ad76eaa106`
