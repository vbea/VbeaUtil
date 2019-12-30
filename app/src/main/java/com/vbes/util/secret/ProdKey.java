package com.vbes.util.secret;

import com.vbes.util.secret.Dec;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.vbea.secret.SecretKey;
import com.vbes.util.secret.Key;

/**
 * Created by Vbe on 2019/12/30.
 */
public class ProdKey {
    private String[] k;

    public ProdKey(String _k) {
        this.k = _k.split("-");
    }

    public Key getInstance() {
        return new Key(this.k);
    }
}
