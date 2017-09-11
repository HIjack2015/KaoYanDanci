package cn.jk.kaoyandanci.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import cn.jk.kaoyandanci.model.CommonQuestion;


/**
 * Created by Administrator on 2017/4/20.
 */

public class AssetsUtil {
    public static String loadStringFromAsset(String fileName, Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            json = FileUtil.streamToString(is);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<CommonQuestion> loadCardFromFile(Context context, String filePath) {
        String allCollectibleCard = loadStringFromAsset(filePath, context);
        Type typeOfListOfFoo = new TypeToken<List<CommonQuestion>>() {
        }.getType();
        List<CommonQuestion> jsonObjectList = new Gson().fromJson(allCollectibleCard, typeOfListOfFoo);
        return jsonObjectList;
    }
}
