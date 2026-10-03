/* sols/ucl_2.js */
console.log("[*] Script loaded! Initializing hooks for UCL2...");

Java.perform(function () {
    console.log("[+] Java runtime hooked!");

    // 1. Обход детектов java
    try {

        // -- Отключим вывод сообщения с завершением процесса  --
        /* JADX INFO: renamed from: sg.vantagepoint.a.b */
        const 
        MainActivityClass                   = Java.use("sg.vantagepoint.uncrackable2.MainActivity"), 
        objectClass                         = 'java.lang.String';;
        /* JADX INFO: renamed from: a */
        //public void m2178a(String str) {
        //    AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
        //    alertDialogCreate.setTitle(str);
        //    alertDialogCreate.setMessage("This is unacceptable. The app is now going to exit.");
        //    alertDialogCreate.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: sg.vantagepoint.uncrackable2.MainActivity.1
        //        @Override // android.content.DialogInterface.OnClickListener
        //        public void onClick(DialogInterface dialogInterface, int i) {
        //            System.exit(0);
        //        }
        //    });
        //    alertDialogCreate.setCancelable(false);
        //    alertDialogCreate.show();
        //}
        
        //  MainActivityClass.a.implementation = function (obj/*string input*/) {
        //      var strError = obj.toString();
        //      console.log("[*] sg.vantagepoint.uncrackable2.MainActivity() [ErrorExitMessageBox] disable. Call reason: " + strError);
        //      //return false;
        //  };

        MainActivityClass.a.overload(objectClass).implementation = function(obj/*string input*/) {
            var strError = obj.toString();
            console.log("[*] sg.vantagepoint.uncrackable2.MainActivity() [ErrorExitMessageBox] disable. Call reason: " + strError);
            //return false;
        };

        // -- Детект отладки --
        const AndroidOsDebugClass = Java.use("android.os.Debug");
        AndroidOsDebugClass.isDebuggerConnected.implementation= function () {
            //закомментированно, т.к. флудит в консоль каждые 100мс
            //console.log("[*] android.os.Debug.isDebuggerConnected() disable. ");
            return false;
        };

    } catch (err) {
        console.error("[-] DebugRootCheck hook failed: " + err);
    }

});



// 2. Обход детектов нативки (из libfoo.so)
console.log("[+] Native runtime hooked!");

/*
https://armconverter.com/?code=MOV+x0,+%230x01%0ARET
целевая архитектура Infinix HOT 11S NFC -- arm64
Мы можем занупить 
.text:0000000000000D94                 BL              sub_918

Но лучше, я просто отключу метод проверки потоков/атача через return 1;

__asm arm64 {
    MOV x0, #0x01
    RET
}

И пропатчу байты по адресу (внутри lib/arm64-v8a/libfoo.so) 
.text:0000000000000918
как
; помещаем в arm регистр возврата `x0 = 1`
200080D2    ;MOV x0, #0x01
; сразу делаем return
C0035FD6    ;RET

примечание: это должно сработать, т.к. нативный метод Java_sg_vantagepoint_uncrackable2_MainActivity_init вызывается вне конструктора
*/


function hookDlopenAndPatchBatch(targetSo, patchesList, onPatchedCallback) {
    const dlopenExt = Module.findExportByName(null, "android_dlopen_ext") 
                   || Module.findExportByName(null, "dlopen");

    if (!dlopenExt) {
        console.error("[-] Не удалось найти dlopen / android_dlopen_ext в линкере!");
        return;
    }

    let isApplied = false;

    Interceptor.attach(dlopenExt, {
        onEnter: function (args) {
            this.path = args[0].readCString();
        },
        onLeave: function (retval) {
            // Если уже применили патчи для этого модуля — пропускаем повторные вызовы dlopen
            if (isApplied) return;

            if (this.path && this.path.indexOf(targetSo) !== -1) {
                console.log(`[+] Обнаружена загрузка ${this.path}`);
                
                const base = Module.findBaseAddress(targetSo);
                if (!base) {
                    console.error(`[-] База для ${targetSo} не найдена!`);
                    return;
                }

                console.log(`[+] Базовый адрес ${targetSo}: ${base}`);
                console.log(`[*] Накладываем серию патчей (${patchesList.length} шт.)...`);

                patchesList.forEach(([offset, bytes], index) => {
                    const targetAddr = base.add(offset);
                    const patchSize = bytes.length;

                    try {
                        // 1. Снимаем защиту на запись (RWX)
                        Memory.protect(targetAddr, patchSize, 'rwx');

                        // 2. Пишем сырые байты
                        Memory.writeByteArray(targetAddr, bytes);

                        console.log(`    [#${index + 1}] Патч по RVA 0x${offset.toString(16)} (ABS: ${targetAddr}) -> [${bytes.map(b => '0x' + b.toString(16).padStart(2, '0')).join(', ')}] OK!`);
                    } catch (err) {
                        console.error(`    [-] Ошибка патча по RVA 0x${offset.toString(16)}: ${err.message}`);
                    }
                });

                isApplied = true;

                // Вызываем callback
                if (typeof onPatchedCallback === "function") {
                    onPatchedCallback(targetSo);
                }
            }
        }
    });
}

const KEY_TRIGER  = "someely";

// 3. Хукаем strncmp - сверка переданной и эталонной строки
// Абсурдно, т.к. все чисто видно в IDA обычной строкой, но все же
function hookStringCompare(targetSo)  {
    const moduleImportsList = Module.enumerateImports(targetSo);


    for (let importItem of moduleImportsList) {
        if (importItem.name === "strncmp") {

            console.log("[+] Найден точный импорт strncmp внутри libfoo.so по адресу: " + importItem.address);
            Interceptor.attach(importItem.address, {
                onEnter: function (args) {
                    const s1 = args[0].readCString();
                    const s2 = args[1].readCString();
                    const n = args[2].toInt32(); 
                    // Ищем наш маркерный ввод "someely"
                    // console.log("[★] strncmp(`"+s1+"`,`"+s2+"`,"+n+"");
                    if (s1.indexOf(KEY_TRIGER) != -1 || s2.indexOf(KEY_TRIGER) != -1) {
                        console.log("\n[★] =========================================");
                        console.log("[★] СЕКРЕТ ПЕРЕХВАЧЕН В STRNCMP!");
                        console.log("[★] User Input : `" + s1 + "`");
                        console.log("[★] Target Flag: `" + s2 + "`");
                        console.log("[★] Size       : " + n);
                        console.log("[★] =========================================\n");
                    }
                }
            });
        }
    }
}


// Задаем список патчей в формате: [offset_rva, [байты]]
const myPatches = [
    // 1. Заглушить init/ptrace (RVA 0x0918 -> MOV X0, #1; RET)
    // .text:0000000000000918                 SUB             SP, SP, #0x30
    [0x0000000000000918, [0x20, 0x00, 0x80, 0x52, 0xc0, 0x03, 0x5f, 0xd6]],

    // 2. Запатчить проверку длины буфера (RVA 0x0E4C -> NOP)
    // .text:0000000000000E48                 CMP             W0, #0x17
    // .text:0000000000000E4C                 B.NE            loc_E64
    // .text:0000000000000E4C                 NOP => 1F 20 03 D5
    [0x0000000000000E4C, [0x1F, 0x20, 0x03, 0xD5]]
];

// Запуск:
hookDlopenAndPatchBatch(
    "libfoo.so", 
    myPatches, 
    function (moduleName) {
        console.log(`[+] Все патчи для ${moduleName} наложены! Hook на strncmp.. `);
        hookStringCompare(moduleName);
    });

//hookDlopenAndPatch("libfoo.so", 0x0918, [0x20, 0x00, 0x80, 0x52, 0xc0, 0x03, 0x5f, 0xd6]);