package com.buxingzhe.pedestrian;

import android.content.Context;

/**
 * 全局初始化
 * Created by quanjing on 2017/2/5.
 */
public class PDConfig {
    private static PDConfig s_instance;
    private Context _context;

    private PDConfig() {
    }

    public static PDConfig getInstance() {
        if (s_instance == null) {
            s_instance = new PDConfig();
        }
        return s_instance;
    }

    public void init(Context context) {
        _context = context;
    }

    public Context getContext() {
        return _context;
    }

}
