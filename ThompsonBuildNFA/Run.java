import ThompsonBuildNFA.NFA;
import ThompsonBuildNFA.Thompson;

import java.io.File;

public class Run{

    public static void main(String[] args) {
        NFA nfa = Thompson.buildNFA("Go(((Java)|(Python))MySQL)*((Huang)+|(Wei)+)");
        nfa.drawWithMarkdown();
        /* No --> GoJavaMySQLJavaMySQL【?????】 --->缺少【(Huang)+】|【(Wei)+】 */
        nfa.identification("GoJavaMySQLJavaMySQL");
        /* YES */
        nfa.identification("GoJavaMySQLJavaMySQLHuangHuang");
        /* NO ---> GoJavaMySQLJavaMySQL【HuangWei】  ---> 应该为或运算,Huang与Wei不能同时出现 */
        nfa.identification("GoJavaMySQLJavaMySQLHuangWei");
        /* YES */
        nfa.identification("GoJavaMySQLPythonMySQLWei");
        /* NO ---> GoJavaMySQLPython【?????】HuangHuang ---> 缺少【MySQL】 */
        nfa.identification("GoJavaMySQLPythonHuangHuang");
        /* NO ---> GoJavaMySQL【????】MySQLHuangHuang ---> 缺少【Java】|【Python】 */
        nfa.identification("GoJavaMySQLMySQLHuangHuang");
        /* YES */
        nfa.identification("GoWei");
    }
}