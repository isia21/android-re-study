# 1. Верификация строки
```
    /* JADX INFO: renamed from: m */
    private CodeCheck m_pCodeCheck;

    static {
        System.loadLibrary("foo");
    }

    ...

    public void verify(View view) {
        String str;
        String string = ((EditText) findViewById(R.id.edit_text)).getText().toString();
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
        if (this.m_pCodeCheck.IsAnswerCorrect(string)) {
            alertDialogCreate.setTitle("Success!");
            str = "This is the correct secret.";
        } else {
            alertDialogCreate.setTitle("Nope...");
            str = "That's not it. Try again.";
        }
        alertDialogCreate.setMessage(str);
        alertDialogCreate.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: sg.vantagepoint.uncrackable2.MainActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogCreate.show();
    }
```
> 1. Приложение подтягивает библиотеку `foo`    
> 2. Верификация строки в этот раз происходит в через метод класса CodeCheck::IsAnswerCorrect

---

# 2. Работа с библиотекой (?)
```
public class CodeCheck {
    private native boolean bar(byte[] bArr);

    /* JADX INFO: renamed from: a */
    public boolean IsAnswerCorrect(String str) {
        return bar(str.getBytes());
    }
}
```
> На этот раз мы обращаемся к нативному методу `bar`, куда передаем нашу строку, как ByteArary  
> В ресурсах проекта/apk я вижу билды libfoo.so (?~dll?).
> В андроид/java библиотеки имеют свой префикс и строгие пути архитектур?
> `lib/` ARM - `arm64-v8a`, `armeabi-v7a`; x86 - `x86`, `x86_64`
> Ради возможности избежать динамического анализа, я бы использовал билды so/dll под x86 семейство в IDA (?)

# 3. IDA + lib/x86/libfoo.so
```
;экспортируем методы CodeCheck::bar и MainActivity::init?
LOAD:0000035A aJavaSgVantagep db 'Java_sg_vantagepoint_uncrackable2_CodeCheck_bar',0
LOAD:0000035A                                         ; DATA XREF: LOAD:00000260↑o
LOAD:0000038A aExit           db '_exit',0            ; DATA XREF: LOAD:00000270↑o
LOAD:00000390 aJavaSgVantagep_0 db 'Java_sg_vantagepoint_uncrackable2_MainActivity_init',0

;   private native void MainActivity::init();
; Со стороны JADX/Java(?) не принимает никаких аргументов, и ничего не возвращает
;   private native boolean CodeCheck::bar(byte[] bArr);
; Со стороны JADX/Java(?) принимает byte[] и возвращает bool как результат

```
## MainActivity::init
```
unsigned int Java_sg_vantagepoint_uncrackable2_MainActivity_init()
{
  unsigned int result; // eax
  //Запуск потока анти-отладки.
  result = sub_720();
  //Установка некого флага в 1.
  g_bSomeFlag = 1;
  return result;
}
```

## CodeCheck::bar
```
_BOOL4 __cdecl Java_sg_vantagepoint_uncrackable2_CodeCheck_bar(JNIEnv *a1, int a2, void *pData)
{
  const char *v3; // esi
  _BOOL4 result; // eax
  char s2[24]; // [esp+0h] [ebp-2Ch] BYREF
  unsigned int v6; // [esp+18h] [ebp-14h]
                                                // a1 -- как я понял является указателем на класс, хоть и непривычно передан, не через ecx, и метод __cdecl, а не __thiscall / __fastcall
                                                // однако 
                                                // a2 -- я не вижу обращения к нему. может аналогично win32 это void * /*<edx>*/
                                                // BOOL as sized 4
                                                // ? BOOL __fastcall CodeCheck::bar(CodeCheck * this, void * /*edx*/, byte * pData); ?
                                                // ДА. но мы не передаем указатель на класс, мы передаем `JNIEnv *` лол.
  v6 = __readgsdword(0x14u);
  result = 0;                                   // Если приложение штатно запущено (флаг инита встал в true)
                                                // Верифицируем строку
  if ( g_bSomeFlag == 1 )
  {
    strcpy(s2, "Thanks for all the fish");
    v3 = (*a1)->GetByteArrayElements(a1, pData, 0);
    if ( (*a1)->GetArrayLength(a1, pData) == 23 && !strncmp(v3, s2, 0x17u) )
      return 1;
  }                                             // Иначе сразу возвращаем false, скипая верификацию
  return result;
}
```

[Ответ по задаче](../sol/result.txt)    