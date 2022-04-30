无符号数的识别:
1.(123*abc+def/99.2+9.9E+c)：实验测试用例；
2.(10+E+2+3)：字符’E’前无数字；数字3之后无字符
3.3.(e3.14E-6+6.18E-6.6+)：首字符非数字；指数为负数且尾数为浮点数；指数为小数；


Thompson方法的实现：
1.[a]:单个字符的情况
2.[ab]：与运算
3.[a|b]：或运算
4.[a*]：克林闭包
5.[a|(b|c)*de+]：多种运算结合，需要较少状态进行表示
6.[Go(((Java)|(Python))MySQL)*((Huang)+|(Wei)+)]：多运算结合并且需要多种状态表示

字符串的识别：
1.[GoJavaMySQLJavaMySQL]：缺少正闭包运算"+"的情况
2.[GoJavaMySQLJavaMySQLHuangHuang]：能够识别
3.[GoJavaMySQLJavaMySQLHuangWei]：不能识别，正闭包的或运算同时出现
4.[GoJavaMySQLPythonMySQLWei]：能够识别，两种或同时出现
5.[GoJavaMySQLPythonHuangHuang]：不能识别，与运算中缺少后半部分
6.[GoJavaMySQLMySQLHuangHuang]：不能识别，与运算中缺少前半部分
7.[GoWei]：能够识别，克林闭包获得空串的情况