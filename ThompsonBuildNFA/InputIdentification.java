package ThompsonBuildNFA;

import java.util.*;

public class InputIdentification {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Thompson thompson =  new Thompson("Go(((Java)|(Python))MySQL)*((Huang)+|(Wei)+)");
        thompson.getNfa().getGraph();
        /* No --> GoJavaMySQLJavaMySQL【?????】 --->缺少【(Huang)+】|【(Wei)+】 */
        thompson.getNfa().identification("GoJavaMySQLJavaMySQL");
        /* YES */
        thompson.getNfa().identification("GoJavaMySQLJavaMySQLHuangHuang");
        /* NO ---> GoJavaMySQLJavaMySQL【HuangWei】  ---> 应该为或运算,Huang与Wei不能同时出现 */
        thompson.getNfa().identification("GoJavaMySQLJavaMySQLHuangWei");
        /* YES */
        thompson.getNfa().identification("GoJavaMySQLPythonMySQLWei");
        /* NO ---> GoJavaMySQLPython【?????】HuangHuang ---> 缺少【MySQL】 */
        thompson.getNfa().identification("GoJavaMySQLPythonHuangHuang");
        /* NO ---> GoJavaMySQL【????】MySQLHuangHuang ---> 缺少【Java】|【Python】 */
        thompson.getNfa().identification("GoJavaMySQLMySQLHuangHuang");
        /* YES */
        thompson.getNfa().identification("GoWei");

    }
}
