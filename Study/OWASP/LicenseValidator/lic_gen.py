import os
import base64

def generate(xorKey=None):
    if xorKey is None:
        xorKey = os.urandom(1)[0]

    mask_chars = b'LOLZ!'

    barr = bytearray(10)

    # 1. Заполняем нечётные позиции (ключи для каждой пары)
    barr[1] = xorKey
    for idx in (3, 5, 7, 9):
        barr[idx] = os.urandom(1)[0]

    # 2. Вычисляем чётные позиции под их соответствующие ключи barr[idx + 1]
    for i, idx in enumerate([0, 2, 4, 6, 8]):
        barr[idx] = mask_chars[i] ^ barr[idx + 1]

    # Base32-энкод (с паддингом до 16 символов)
    serial = base64.b32encode(bytes(barr)).decode()
    return serial, barr, xorKey


serial, barr, key = generate()
print(f"xorKey = 0x{key:02X}")
print(f"barr   = {barr.hex()}")
print(f"serial = {serial}")