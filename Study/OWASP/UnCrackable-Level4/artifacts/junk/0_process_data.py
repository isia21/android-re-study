
import struct

s = open("byte_295010.bin","rb").read()
ss = open("byte_2E3010.bin","rb").read()
d = open("dword_297010.bin","rb").read()

# =============================================================
# Проверяем: все ли 96 блоков по 256 байт — это XOR-таблица
ok = all(ss[256*b + 16*i + j] == (i ^ j)
         for b in range(96) for i in range(16) for j in range(16))
print("XOR table everywhere:", ok)

# =============================================================
vals = struct.unpack(f"<{len(d)//4}I", d)
# Round 0, position 0, byte 0..255
Tblk = vals[0:256]
for byte in range(4):
    t = Tblk[byte]
    print(f"T0[0][{byte}] = 0x{t:08X}", 
          "bytes:", [hex((t>>24)&0xFF), hex((t>>16)&0xFF), hex((t>>8)&0xFF), hex(t&0xFF)],
          "sbox0[byte]:", hex(s[byte]))

# =============================================================
# 256 блоков по 256. Проверим первые 16 на перестановочность
for b in range(16):
    block = s[256*b:256*b+256]
    if len(set(block)) != 256:
        print(f"block {b}: NOT a permutation")
        break
else:
    print("blocks 0..15 are all permutations (likely S-boxes)")

# Проверим, отличаются ли блоки
print("block0[:8]:", s[0:8])
print("block1[:8]:", s[256:264])
print("block15[:8]:", s[256*15:256*15+8])

# Сколько блоков (из 256) перестановки?
perm_blocks = [b for b in range(256) if len(set(s[256*b:256*b+256])) == 256]
print("permutation blocks:", len(perm_blocks), "/256")
# Уникальные блоки
blocks = [bytes(s[256*b:256*b+256]) for b in range(256)]
print("unique blocks:", len(set(blocks)))

# =============================================================
vals = struct.unpack(f"<{len(d)//4}I", d)

# Раунд 0, позиция 0, значения для byte = 0..7
print("round0 col0:", [hex(v) for v in vals[0:8]])
# Раунд 0, позиция 1
print("round0 col1:", [hex(v) for v in vals[256:264]])

# =============================================================
def bin_to_c_header(bin_path, out_path, ctype, name, count):
    with open(bin_path, "rb") as f:
        data = f.read(count * (1 if ctype == "uint8_t" else 4))
    with open(out_path, "w") as f:
        f.write("#include <cstdint>\n")
        f.write(f"alignas(64) const {ctype} {name}[{count}] = {{\n")
        step = 16
        for i in range(0, count, step):
            if ctype == "uint32_t":
                chunk = data[i*4:(i+step)*4]
                vals = [f"0x{int.from_bytes(chunk[k:k+4],'little'):08X}"
                        for k in range(0, len(chunk), 4)]
            else:
                chunk = data[i:i+step]
                vals = [f"0x{b:02X}" for b in chunk]
            f.write("  " + ",".join(vals) + ",\n")
        f.write("};\n")
    print(f"[+] {name}: {count} элементов -> {out_path}")

# ВАЖНО: используем правильные count!
bin_to_c_header("byte_295010.bin",  "sbox.cpp", "uint8_t",  "sbox", 4096)      # 16*256
bin_to_c_header("byte_2E3010.bin",  "xorT.cpp", "uint8_t",  "xorT", 24576*9)
bin_to_c_header("dword_297010.bin", "T0.cpp",   "uint32_t", "T0",   9*16*256)  # 36864
bin_to_c_header("dword_2BB010.bin", "T1.cpp",   "uint32_t", "T1",   9*16*256)  # 36864