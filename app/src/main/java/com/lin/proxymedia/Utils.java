package com.lin.proxymedia;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by linchen on 2018/4/1.
 * mail: linchen@sogou-inc.com
 */

public class Utils {
public static String readAssertResource(Context context, String strAssertFileName) {
    AssetManager assetManager = context.getAssets();
    String strResponse = "";
    try {
        InputStream ims = assetManager.open(strAssertFileName);
        strResponse = getStringFromInputStream(ims);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return strResponse;
}

    private static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }
}
