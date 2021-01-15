package com.vbes.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络检测工具
 * @author Created by Vbe on 2019/3/27.
 */
public class NetUtil {
    private static Context mContext;

    /**
     * 初始化
     * @param context Application context
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 检测网络是否连接
     * @return 网络状态
     */
    public static boolean isConnected() {
        if (mContext == null) {
            Log.i("VbeUtil.NetUtil", "You must init NetUtil first.");
            return false;
        }
        ConnectivityManager systemService = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService == null) {
            return false;
        } else {
            NetworkInfo[] info = systemService.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
