# Android Reverse Engineering Junkyard

## 1. Test OWASP

### 1.1. Android Uncrackable L1
||||
|-|-|-|   
Цель/Задача | | https://mas.owasp.org/crackmes/Android/#android-uncrackable-l1  
Заметки | | [/Study/OWASP/UnCrackable-Level1/artifacts/note.md](/Study/OWASP/UnCrackable-Level1/artifacts/note.md)   
Ответ | `I want to believe` | [/Study/OWASP/UnCrackable-Level1/sol/result.txt](/Study/OWASP/UnCrackable-Level1/sol/result.txt)    
Решение | `OWASP::UCL1` |  [/Utils/OWASP_1/src/Source.cpp](/Utils/OWASP_1/src/Source.cpp)

### 1.2. Android Uncrackable L2
||||
|-|-|-|   
Цель/Задача | | https://mas.owasp.org/crackmes/Android/#android-uncrackable-l2  
Заметки | | [/Study/OWASP/UnCrackable-Level2/artifacts/note.md](/Study/OWASP/UnCrackable-Level2/artifacts/note.md)   
Ответ | `Thanks for all the fish` | [/Study/OWASP/UnCrackable-Level2/sol/result.txt](/Study/OWASP/UnCrackable-Level2/sol/result.txt)    
Решение |  | Чистая статика + IDA, ход в заметках.

### 1.3. Android Uncrackable L3
||||
|-|-|-|   
Цель/Задача | | https://mas.owasp.org/crackmes/Android/#android-uncrackable-l3  
Заметки | | [/Study/OWASP/UnCrackable-Level3/artifacts/note.md](/Study/OWASP/UnCrackable-Level3/artifacts/note.md)   
Ответ | `making owasp great again` | [/Study/OWASP/UnCrackable-Level3/sol/result.txt](/Study/OWASP/UnCrackable-Level3/sol/result.txt)    
Решение | `OWASP::UCL3::UCL3` |  [/Utils/OWASP_1/src/Source.cpp](/Utils/OWASP_1/src/Source.cpp)

### 1.4. Android Uncrackable L4
||||
|-|-|-|   
Цель/Задача | `v1.0` | https://mas.owasp.org/crackmes/Android/#android-uncrackable-l4  
Заметки | `...` | [/Study/OWASP/UnCrackable-Level4/artifacts/note.md](/Study/OWASP/UnCrackable-Level4/artifacts/note.md)   
Ответ | PIN: `5971`, Salt: `4ad891934b99c3a0445f66ad76eaa106` | [/Study/OWASP/UnCrackable-Level4/sol/result.md](/Study/OWASP/UnCrackable-Level4/sol/result.md)    
Решение | hijck  `libtool-checker.so` |  [.../rootbeerlib/src/main/cpp/toolChecker.cpp](/Study/OWASP/UnCrackable-Level4/elf_mod/rootbeer/rootbeerlib/src/main/cpp/toolChecker.cpp)

 
### 1.5. Android License Validator
||||
|-|-|-|   
Цель/Задача |  | https://mas.owasp.org/crackmes/Android/#android-license-validator
Заметки |  | [/Study/OWASP/LicenseValidator/note.md](/Study/OWASP/LicenseValidator/note.md)   
Ответ |  | ` [i] => result[i] == pInputData[i*2] ^ pInputData[i*2+1];`      
Решение | генератор на py |  [/Study/OWASP/LicenseValidator/lic_gen.py](/Study/OWASP/LicenseValidator/lic_gen.py)

  