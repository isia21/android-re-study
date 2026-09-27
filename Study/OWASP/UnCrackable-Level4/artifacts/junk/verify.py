d = open("byte_2E3010.bin","rb").read()
# Проверяем: все ли 96 блоков по 256 байт — это XOR-таблица
ok = all(d[256*b + 16*i + j] == (i ^ j)
         for b in range(96) for i in range(16) for j in range(16))
print("XOR table everywhere:", ok)

s = open("byte_295010.bin", "rb").read()
print("size:", len(s))
print("16 copies?", all(s[i] == s[i % 256] for i in range(4096)))
print("first 16 bytes each block:", [s[256*i] for i in range(16)])

def bin_to_c_header(bin_path, out_path, ctype, name, count):
    data = open(bin_path, "rb").read()
    assert len(data) == count * (1 if ctype=="uint8_t" else 4)
    with open(out_path, "w") as f:
        f.write(f"#include <cstdint>\n")
        f.write(f"alignas(64) const {ctype} {name}[{count}] = {{\n")
        for i in range(0, count, 16):
            chunk = data[i*4:(i+16)*4] if ctype=="uint32_t" else data[i:i+16]
            if ctype == "uint32_t":
                vals = [f"0x{int.from_bytes(chunk[k:k+4],'little'):08X}"
                        for k in range(0, len(chunk), 4)]
            else:
                vals = [f"0x{b:02X}" for b in chunk]
            f.write("  " + ",".join(vals) + ",\n")
        f.write("};\n")

# Пример
bin_to_c_header("byte_295010.bin",  "sbox.cpp", "uint8_t",  "sbox", 256)
bin_to_c_header("dword_297010.bin", "T0.cpp",   "uint32_t", "T0",   9*16*256)
bin_to_c_header("dword_2BB010.bin", "T1.cpp",   "uint32_t", "T1",   9*16*256)