package com.vbes.util.secret;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vbe on 2019/12/30.
 */
public class Key {
    private String k = "";
    private String p = "";

    Key(String[] keys) {
        if (keys.length == 5) {
            this.p = keys[4].substring(3) + keys[3];
            this.k = keys[0] + keys[1] + keys[2] + keys[4].substring(0, 3);
        }
    }

    private String getl() throws Exception {
        Dec d = new Dec(this.p);
        return d.decrypt(this.k);
    }

    public boolean invoke() throws Exception {
        SimpleDateFormat simp = new SimpleDateFormat("yyMMdd");
        return Integer.parseInt(this.getl()) - Integer.parseInt(simp.format(new Date())) >= 0;
    }

    public boolean verify() {
        try {
            this.getl();
            return true;
        } catch (Exception var2) {
            return false;
        }
    }
}
