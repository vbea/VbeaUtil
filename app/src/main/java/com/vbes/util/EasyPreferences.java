package com.vbes.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 简单化的SharedPreferences实现
 * @author Created by vbe on 2018/9/18.
 */

public class EasyPreferences {
    private static SharedPreferences spf;
    private static SharedPreferences.Editor edt;

    private EasyPreferences() {}

    public static void init(Context context) {
        init(context, "setting");
    }

    public static void init(Context context, String name) {
        if (spf == null)
            spf = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static String getString(String var1, String var2) {
        return spf.getString(var1, var2);
    }

    public static String getString(String var1) {
        return getString(var1, "");
    }

    public static int getInt(String var1, int var2) {
        return spf.getInt(var1, var2);
    }

    public static long getLong(String var1, long var2) {
        return spf.getLong(var1, var2);
    }

    public static float getFloat(String var1, float var2) {
        return spf.getFloat(var1, var2);
    }

    public static boolean getBoolean(String var1, boolean var2) {
        return spf.getBoolean(var1, var2);
    }

    public static boolean contains(String var1) {
        return spf.contains(var1);
    }

    public static void putString(String var1, String var2) {
        getEditor().putString(var1, var2);
    }

    public static void putInt(String var1, int var2) {
        getEditor().putInt(var1, var2);
    }

    public static void putLong(String var1, long var2) {
        getEditor().putLong(var1, var2);
    }

    public static void putFloat(String var1, float var2) {
        getEditor().putFloat(var1, var2);
    }

    public static void putBoolean(String var1, boolean var2) {
        getEditor().putBoolean(var1, var2);
    }

    public static boolean commit() {
        return getEditor().commit();
    }

    public static void apply() {
        getEditor().apply();
    }

    private static SharedPreferences.Editor getEditor() {
        if (edt == null)
            edt = spf.edit();
        return edt;
    }
}
