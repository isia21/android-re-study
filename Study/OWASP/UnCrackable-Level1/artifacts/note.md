# 1. Entry Point
```
    // ~ MessageBox n force quit wo err code
    private void a(String str) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
        alertDialogCreate.setTitle(str);
        alertDialogCreate.setMessage("This is unacceptable. The app is now going to exit.");
        alertDialogCreate.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: sg.vantagepoint.uncrackable1.MainActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        alertDialogCreate.setCancelable(false);
        alertDialogCreate.show();
    }
    // root/debug check
    protected void onCreate(Bundle bundle) {
        if (c.a() || c.b() || c.c()) {
            a("Root detected!");
        }
        if (b.a(getApplicationContext())) {
            a("App is debuggable!");
        }
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
    }
```
> 1. Приложение детектит рутированные телефоны и если находится под отладкой?  
> 2. Если задача просто найти "строку", нет необходимости ковыряться в рантайме, рутировать/хукать/дебажить.   
> 3. Видимо, в силу `Level1` или специфику Android`а строки чистые/топорные - гуд.

---

# 2. Верификация строки
```
    public void verify(View view) {
        String str;
        String string = ((EditText) findViewById(R.id.edit_text)).getText().toString();
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
        if (a.a(string)) {
            alertDialogCreate.setTitle("Success!");
            str = "This is the correct secret.";
        } else {
            alertDialogCreate.setTitle("Nope...");
            str = "That's not it. Try again.";
        }
        alertDialogCreate.setMessage(str);
        alertDialogCreate.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: sg.vantagepoint.uncrackable1.MainActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogCreate.show();
    }
```
> Снова удобно, строки открыты, напрямую видим err/pass state

Значит `sg.vantagepoint.uncrackable1.a.a` верифицирует введенную строку.
```
public class a {
    public static boolean a(String str) {
        byte[] bArrA;
        byte[] bArr = new byte[0];
        try {
            bArrA = sg.vantagepoint.a.a.a(b("8d127684cbc37c17616d806cf50473cc"), Base64.decode("5UJiFctbmgbDoLXmpL12mkno8HT4Lv8dlat8FxR2GOc=", 0));
        } catch (Exception e) {
            Log.d("CodeCheck", "AES error:" + e.getMessage());
            bArrA = bArr;
        }
        return str.equals(new String(bArrA));
    }

    public static byte[] b(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
```

> Сложно ориентироваться, т.к. много псевдо-обфускации функций классов a/b/c    
> В JADX приведу к именам на свое усмотрение, благо хоткеи Xref Name работают аналогично IDA

---

# 3. Решение
[Ответ по задаче](..\sol\result.txt)    
[Решение (OWASP::UCL1)](..\..\..\..\Utils\OWASP_1\src\Source.cpp)

Будь то win32, я бы сделал одно из:
- Хук string::Equals(string this, string in), чтоб вытащить готовый ключ    
`return struserInput.equals(new String(strCorrectAnswer));`
- Патч-байт топорного байпаса   
`return true; //struserInput.equals(new String(strCorrectAnswer));`