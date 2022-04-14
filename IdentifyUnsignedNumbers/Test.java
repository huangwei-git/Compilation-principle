package IdentifyUnsignedNumbers;

import java.util.*;

public class Test {

    public static void p(int a){
        System.out.println(a);
    }

    public static void q(int a){
        System.out.println(Integer.toBinaryString(a));
    }

    public static void f(int x,int n){
        int a = 0xfffffffe;
        int b = 31 + (~n + 1);//31-n
        int c = a << b;
        int d = ~c;
        int e = x >> n;
        int res = e & d;
        System.out.println(Integer.toHexString(res));
    }

    public static void main(String[] args) {
        int x = 0x80000000;
        int n = 32;
        int a = x >> 31;
        q(a);
        int b = x >> n + (~1 + 1);
        q(b);
        int c = a ^ b;
        q(c);

        int ans = c==0?1:0;
        q(0 & -0);
    }
}
