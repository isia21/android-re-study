/* sols/ucl_1.js */
console.log("[*] Script loaded! Initializing hooks for UCL1...");

Java.perform(function () {
    console.log("[+] Java runtime hooked!");

    // 1. Обход детектов
    try {

        // -- Детект отладки --
        /* JADX INFO: renamed from: sg.vantagepoint.a.b */
        const DebugDetectClass = Java.use("sg.vantagepoint.a.b");
        /* JADX INFO: renamed from: a */
        //  public static boolean m1a(Context context) {
        //      return (context.getApplicationContext().getApplicationInfo().flags & 2) != 0;
        //  }
        DebugDetectClass.a.implementation= function () {
            console.log("[*] sg.vantagepoint.a.b() [IsDebug] -> spoofed false");
            return false;
        };

        
        // -- Детект рута --
        /* JADX INFO: renamed from: sg.vantagepoint.a.c */
        const RootCheckClass = Java.use("sg.vantagepoint.a.c");
        /* JADX INFO: renamed from: a */
        //  public static boolean m2a() {
        //      for (String str : System.getenv("PATH").split(":")) {
        //          if (new File(str, "su").exists()) {
        //              return true;
        //          }
        //      }
        //      return false;
        //  }
        RootCheckClass.a.implementation= function () {
            console.log("[*] sg.vantagepoint.a.c() [IsFileExist_su] -> spoofed false");
            return false;
        };

        /* JADX INFO: renamed from: b */
        //  public static boolean m3b() {
        //      String str = Build.TAGS;
        //      return str != null && str.contains("test-keys");
        //  }
        RootCheckClass.b.implementation= function () {
            console.log("[*] sg.vantagepoint.a.c() [IsBuildTagTestKeys] -> spoofed false");
            return false;
        };

        /* JADX INFO: renamed from: c */
        //  public static boolean m4c() {
        //      for (String str : new String[]{"/system/app/Superuser.apk", "/system/xbin/daemonsu", "/system/etc/init.d/99SuperSUDaemon", "/system/bin/.ext/.su", "/system/etc/.has_su_daemon", "/system/etc/.installed_su_daemon", "/dev/com.koushikdutta.superuser.daemon/"}) {
        //          if (new File(str).exists()) {
        //              return true;
        //          }
        //      }
        //      return false;
        //  }
        RootCheckClass.c.implementation= function () {
            console.log("[*] sg.vantagepoint.a.c() [IsRootApksInstalled] -> spoofed false");
            return false;
        };
    } catch (err) {
        console.error("[-] RootCheck hook failed: " + err);
    }

    // 2. Зная, что дешифр эталон строка сверяется в string.Equals, хукаем именно его
    // Идея в том, чтоб передать в метод сравнения наше ключ-значение, с которым apk сравнивает декод строку-эталон
    try {
        const 
        JavaLangStringClass                 = Java.use('java.lang.String'), 
        objectClass                         = 'java.lang.Object';

        JavaLangStringClass.equals.overload(objectClass).implementation = function(obj) {
            var response = JavaLangStringClass.equals.overload(objectClass).call(this, obj);
            if (obj) {

                if (obj.toString() == "someely" || this.toString() == "someely") {
                    console.log("[*] java.lang.String() dst: `" + JavaLangStringClass.toString.call(this) + "`\t\tsrc: `" + JavaLangStringClass.toString.call(obj) + "` ");
                    
                }
            }
            return response;
        }
    } catch (err) {
        console.error("[-] java.lang.String hook failed: " + err);
    }
});