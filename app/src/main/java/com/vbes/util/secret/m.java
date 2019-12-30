package com.vbes.util.secret;

import java.security.MessageDigest;

class m {
    private static MessageDigest m;
    static void a(byte[] s) {
        m.update(s);
    }
    static byte[] s() {
        return m.digest();
    }
    static void g(String s) throws Exception {
        m = MessageDigest.getInstance(new Dec().decrypt(s));
    }
}
