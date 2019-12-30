package com.vbes.util.secret;

/**
 * Created by Vbe on 2019/12/30.
 */
class k {
    static String A = "45d0944561452f75";//MD2
    static String B = "ca055ff0843f3fa6";//MD5
    static String C = "2bd50e45a6ee3ad4";//SHA-1
    static String D = "fd6320f1bc148b12";//SHA-256
    static String E = "a4b8d3aa4fd94946";//SHA-384
    static String F = "d8ba150c087b6b8a";//sha-512
    static String P = "fdd40c3010ade995";
    static String Q = "3b9fca216d553265";

    static String h(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            sb.append(Dec.HEX_DIGITS[(aB & 0xf0) >>> 4]);
            sb.append(Dec.HEX_DIGITS[aB & 0x0f]);
        }
        return sb.toString();
    }
    static String s(byte[] s, String d) {
        try {
            if (s != null) {
                m.g(d);
                m.a(s);
                return h(m.s());
            }
        } catch (Exception e){
            return P;
        }
        return Q;
    }
}
