import ida_bytes
import ida_name
import ida_idaapi
import idc

# Список правил декодирования: (default_ea, length, mask1, mask2, xor_key, label_name)
# Формула декодирования: ((b & m1) | (~b & m2)) ^ xor_k
DECODE_CHUNKS = [
    (0x319138, 8,  0xC7, 0x38, 0xF2, "byte_319138"),
    (0x319140, 19, 0xC0, 0x3F, 0x00, "byte_319140"),
    (0x319153, 8,  0x3E, 0xC1, 0x3A, "byte_319153"),
    (0x31915B, 2,  0x8F, 0x70, 0xD1, "byte_31915B"),
    (0x31915D, 18, 0x89, 0x76, 0xD2, "byte_31915D"),
    (0x31916F, 10, 0x38, 0xC7, 0x42, "byte_31916F"),
    (0x319179, 17, 0xEC, 0x13, 0x1B, "aXzgkMdnNl"),
    (0x31918A, 14, 0x5B, 0xA4, 0x00, "byte_31918A"),
    (0x319198, 10, 0x4A, 0xB5, 0x00, "byte_319198"),
    (0x3191A2, 2,  0xC4, 0x3B, 0x81, "byte_3191A2"),
    (0x3191A4, 3,  0x25, 0xDA, 0x00, "byte_3191A4"),
    (0x3191A7, 16, 0xEF, 0x10, 0x37, "byte_3191A7"),
    (0x3191B7, 26, 0xD8, 0x27, 0x00, "byte_3191B7"),
    (0x3191D1, 12, 0x05, 0xFA, 0x00, "byte_3191D1"),
    (0x3191DD, 6,  0x36, 0xC9, 0x00, "byte_3191DD"),
    (0x3191E3, 25, 0xB4, 0x4B, 0x00, "byte_3191E3"),
    (0x3191FC, 32, 0xD2, 0x2D, 0x00, "byte_3191FC"),
    (0x31921C, 21, 0x65, 0x9A, 0xE5, "byte_31921C"),
    (0x319231, 22, 0xF5, 0x0A, 0x09, "byte_319231"),
    (0x319247, 23, 0x16, 0xE9, 0x00, "byte_319247"),
    (0x31925E, 30, 0xBA, 0x45, 0x64, "byte_31925E"),
    (0x31927C, 21, 0x58, 0xA7, 0xD2, "byte_31927C"),
    (0x319291, 37, 0x91, 0x6E, 0x00, "byte_319291"),
    (0x3192B6, 29, 0x04, 0xFB, 0x98, "byte_3192B6"),
    (0x3192D3, 22, 0xE7, 0x18, 0x34, "byte_3192D3"),
    (0x3192E9, 26, 0x2F, 0xD0, 0x00, "byte_3192E9"),
    (0x319303, 38, 0x9E, 0x61, 0x00, "byte_319303"),
    (0x319329, 16, 0x41, 0xBE, 0x00, "byte_319329"),
    (0x319339, 23, 0x34, 0xCB, 0x00, "format"),
    (0x319350, 4,  0xAE, 0x51, 0x00, "byte_319350"),
    (0x319354, 11, 0x23, 0xDC, 0xDB, "aK"),
    (0x31935F, 9,  0x85, 0x7A, 0x00, "byte_31935F"),
    (0x319368, 9,  0xA3, 0x5C, 0x00, "byte_319368"),
    (0x319371, 7,  0xAA, 0x55, 0x00, "byte_319371"),
    (0x319378, 17, 0xF3, 0x0C, 0x5F, "byte_319378"),
    (0x319389, 15, 0x6E, 0x91, 0x49, "byte_319389"),
    (0x319398, 19, 0xA4, 0x5B, 0xA9, "byte_319398"),
    (0x3193AB, 20, 0x6F, 0x90, 0x00, "byte_3193AB"),
    (0x3193BF, 9,  0x10, 0xEF, 0x30, "byte_3193BF"),
    (0x3193C8, 11, 0xE6, 0x19, 0x00, "byte_3193C8"),
    (0x3193D3, 15, 0xF7, 0x08, 0x00, "byte_3193D3"),
    (0x3193E2, 20, 0x2F, 0xD0, 0x00, "byte_3193E2"),
    (0x3193F6, 24, 0xEF, 0x10, 0x00, "byte_3193F6"),
    (0x31940E, 19, 0x3A, 0xC5, 0x00, "byte_31940E"),
    (0x319421, 28, 0x32, 0xCD, 0x00, "byte_319421"),
    (0x31943D, 16, 0x57, 0xA8, 0xDC, "asc_31943D"),
    (0x31944D, 10, 0x86, 0x79, 0x48, "byte_31944D"),
    (0x319457, 9,  0xEF, 0x10, 0x63, "byte_319457"),
    (0x319460, 8,  0x67, 0x98, 0x00, "byte_319460"),
    (0x319468, 8,  0x9D, 0x62, 0x00, "byte_319468"),
    (0x319470, 12, 0xED, 0x12, 0x8E, "byte_319470"),
    (0x31947C, 13, 0xBF, 0x40, 0xDE, "byte_31947C"),
    (0x319489, 13, 0x35, 0xCA, 0x00, "byte_319489"),
    (0x319496, 12, 0x97, 0x68, 0x00, "byte_319496"),
    (0x3194A2, 6,  0x06, 0xF9, 0x00, "byte_3194A2"),
    (0x3194A8, 5,  0xB3, 0x4C, 0xC1, "byte_3194A8"),
]

def format_string_repr(byte_arr):
    """Преобразует байты в читаемый вид (ASCII / экранированные символы)"""
    res = []
    for b in byte_arr:
        if 0x20 <= b <= 0x7E:
            res.append(chr(b))
        elif b == 0:
            res.append("\\0")
        elif b == 0x0A:
            res.append("\\n")
        elif b == 0x0D:
            res.append("\\r")
        elif b == 0x09:
            res.append("\\t")
        else:
            res.append(f"\\x{b:02X}")
    return "".join(res)

def run_decoder():
    print("=" * 80)
    print("[*] Запуск декодирования datadiv...")

    # Проверяем rebase / смещение адресов по первому символу
    first_sym = DECODE_CHUNKS[0][5]
    named_ea = idc.get_name_ea_simple(first_sym)
    base_delta = 0

    if named_ea != idc.BADADDR:
        base_delta = named_ea - DECODE_CHUNKS[0][0]
        if base_delta != 0:
            print(f"[*] Обнаружен ребейз базы: дельта = 0x{base_delta:X}")
    else:
        print("[*] Имена меток не найдены, используются абсолютные адреса из псевдокода.")

    total_patched = 0

    for raw_ea, length, m1, m2, xor_k, name in DECODE_CHUNKS:
        ea = raw_ea + base_delta

        if not ida_bytes.is_loaded(ea):
            print(f"[!] Ошибка: адрес 0x{ea:X} ({name}) не загружен в память базы IDA!")
            continue

        orig_bytes = bytearray()
        decoded_bytes = bytearray()

        for i in range(length):
            cur_ea = ea + i
            b = ida_bytes.get_byte(cur_ea) & 0xFF
            orig_bytes.append(b)

            # Формула: (b & m1 | ~b & m2) ^ xor_k
            dec_b = (((b & m1) | ((~b & 0xFF) & m2)) ^ xor_k) & 0xFF
            decoded_bytes.append(dec_b)

            # Патчим память в базе IDA
            ida_bytes.patch_byte(cur_ea, dec_b)

        total_patched += length

        # Формируем читаемый лог
        hex_orig = orig_bytes.hex()
        hex_dec = decoded_bytes.hex()
        str_view = format_string_repr(decoded_bytes)

        print(f"\n[+] 0x{ea:08X} - 0x{ea+length:08X} ({length:2d} байт) | {name}")
        print(f"    Было (Hex):  {hex_orig}")
        print(f"    Стало (Hex): {hex_dec}")
        print(f"    Текст:       \"{str_view}\"")

    print("\n" + "=" * 80)
    print(f"[+] Готово! Всего успешно декодировано и пропатчено байт: {total_patched}")
    print("=" * 80)

run_decoder()