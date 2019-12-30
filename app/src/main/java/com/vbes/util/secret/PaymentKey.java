package com.vbes.util.secret;

/**
 * Created by Vbe on 2019/7/1.
 */
public class PaymentKey {
    private String k;
    public static PaymentKey getInstance(String k) {
        return new PaymentKey(k);
    }
    private PaymentKey(String p) {
        k = p;
    }
    public String a() {
        return a.k(k);
    }
    public String b() {
        return b.k(k);
    }
    public String c() {
        return c.k(k);
    }
    public String d() {
        return d.k(k);
    }
    public String e() {
        return e.k(k);
    }
    public String f() {
        return f.k(k);
    }
    public String p() {
        return p.k(k);
    }
    public String q() {
        return q.k(k);
    }
    public String ab() {
        return a()+b();
    }
    public String ac() {
        return a()+c();
    }
    public String ad() {
        return a()+d();
    }
    public String ae() {
        return a()+e();
    }
    public String af() {
        return a()+f();
    }
    public String bc() {
        return b()+c();
    }
    public String bd() {
        return b()+d();
    }
    public String be() {
        return b()+e();
    }
    public String bf() {
        return b()+f();
    }
    public String cd() {
        return c()+d();
    }
    public String ce() {
        return c()+d();
    }
    public String cf() {
        return c()+f();
    }
    public String de() {
        return d()+e();
    }
    public String df() {
        return d()+f();
    }
    public String ef() {
        return e()+f();
    }
    public String pq() {
        return p()+q();
    }
}
