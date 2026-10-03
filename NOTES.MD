1. Прежде использовать статический анализ   
Нам незачем сразы прыгать во frida, байт-патчи и прочее.

2. Приложение может использовать библиотеки
```
    static {
        System.loadLibrary("foo");
    }
    static {
        System.loadLibrary("dll_name");
    }
```
Которые будут лежать в ресурсах
> Ресурсы     
|-Lib   
|--arm64-v8a\lib{dll_name}.so   
|--armeabi-v7a\lib{dll_name}.so     
|--x86\lib{dll_name}.so     
|--x86_64\lib{dll_name}.so  

3. Комфортная среда     
Если есть `x86\lib{dll_name}.so` - бинго, можем идти в иду.

4. Соглашение вызовов (не факт)   
> 1. ! В java/android первым агрументом метода идет не указатель на класс, а указатель сразу на VTABLE `JNIEnv* a1`    
> 2. ? Если метод принадлежит классу/нестатик, вторым аргументом идет jobject

5. Смотри, что именно влияет на результат, а не что делает метод. (L3 неплохо запутал сначала своим развернутым циклом, который нес 0 полезной нагрузки) 

6. Скачать apk файл с googleplay по имени пакета.
> 1. Находим интересующее приложение/игру, напримр: "https://play.google.com/store/apps/details?id=com.rstgames.durak&hl=ru&pli=1"      
> 2. Имя пакета == _GET[`id`]  == `com.rstgames.durak`      
> 3. Идем и скачиваем: https://apkpure.com/search?q=com.rstgames.durak или https://apkpure.com/x/com.rstgames.durak     
> 4. Скачиваем пакет        
