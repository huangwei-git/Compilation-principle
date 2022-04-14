package LexicalAnalysis;

import java.util.HashSet;

public class Sign {
    public final static String INTEGER              = "整数";
    public final static String KEYWORD              = "关键字";
    public final static String ID                   = "标识符";
    public final static String DELIMITER            = "分界符";
    public final static String ASSIGN_OPERATOR      = "赋值运算符";
    public final static String BIT_OPERATOR         = "位运算符";
    public final static String RELATIONAL_OPERATOR  = "关系运算符";
    public final static String LOGICAL_OPERATOR     = "逻辑运算符";
    public final static String ARITHMETIC_OPERATOR  = "算术运算符";
    public final static String ADDRESS_OPERATOR     = "取地址运算符";
    public final static String NOTES                = "注释";

    public static final HashSet<String> keyword_set = new HashSet<String>() {{
        add("short");add("int");add("long");add("float");add("double");
        add("bool");add("byte");add("char");add("void"); add("if");
        add("else");add("switch");add("do");add("while");add("goto");
        add("break");add("continue");add("this");add("super");add("new");
        add("true");add("false");add("using");add("namespace"); add("include");
        add("implements");add("transient");add("extends");add("public");add("private");
        add("protected");add("enum");add("class");add("return");add("NULL");
        add("struct");add("typedef");add("unsigned");

        add("import");add("package");
        add("try");add("catch");add("finally");
        add("static");add("final");
        add("synchronized");add("throw");add("throws");add("null");
    }};
}
