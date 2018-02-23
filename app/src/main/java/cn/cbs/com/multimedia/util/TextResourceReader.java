package cn.cbs.com.multimedia.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by cbs on 2018/2/23.
 */

public class TextResourceReader {

    public static String readTextFileFromRes(Context context, int resID) {
        StringBuilder sb = new StringBuilder();


        InputStreamReader isr = new InputStreamReader(context.getResources().openRawResource(resID));
        BufferedReader br = new BufferedReader(isr);

        String nextLine;

        try {
            while ((nextLine = br.readLine()) != null) {
                sb.append(nextLine);
                sb.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
