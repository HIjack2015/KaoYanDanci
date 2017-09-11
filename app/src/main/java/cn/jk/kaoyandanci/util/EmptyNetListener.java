package cn.jk.kaoyandanci.util;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2017/7/22.
 */

public class EmptyNetListener {
    public static Response.Listener emptyResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        }
    };
    public static Response.ErrorListener emptyErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };

}
