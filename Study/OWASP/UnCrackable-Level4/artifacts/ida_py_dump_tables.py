import ida_bytes
import idc
import os

OUT_DIR = r"F:\Reverse\Android\Targets\Study\OWASP\UnCrackable-Level4\artifacts\junk"
os.makedirs(OUT_DIR, exist_ok=True)

# (имя, адрес, размер в байтах)
tables = [
    ("byte_295010",  0x295010,  256 * 256),            # 65536
    ("byte_2E3010",  0x2E3010,  24576 * 9),            # 221184
    ("dword_297010", 0x297010,  4096 * 256 * 9 * 4),   # 37748736
    ("dword_2BB010", 0x2BB010,  4096 * 256 * 9 * 4),   # 37748736
]

for name, addr, size in tables:
    data = ida_bytes.get_bytes(addr, size)
    if data is None:
        print(f"[!] {name}: не удалось прочитать {size} байт по 0x{addr:X}")
        continue
    if len(data) != size:
        print(f"[!] {name}: прочитано {len(data)} из {size}")
    path = os.path.join(OUT_DIR, name + ".bin")
    with open(path, "wb") as f:
        f.write(data)
    print(f"[+] {name}: {len(data)} байт -> {path}")