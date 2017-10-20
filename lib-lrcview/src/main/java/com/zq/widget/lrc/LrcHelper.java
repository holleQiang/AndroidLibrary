package com.zq.widget.lrc;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class LrcHelper {

    private static final String LRC_PATTERN = "^\\[[0-9]{2}:[0-9]{2}.[0-9]{2,}\\].*";
    private static final String LRC_TIME_PATTERN = "\\[[0-9]{2}:[0-9]{2}.[0-9]{2,}\\]";
    private static final String EXPLAIN_PATTERN = "^\\[[a-z]{2,}:.*]$";

    public static Lrc parseLrc(InputStream inputStream) {

        return parseLrc(inputStream,"gbk");
    }

    public static Lrc parseLrc(InputStream inputStream,String charsetName) {

        if (inputStream == null) {
            return null;
        }

        try {

            Lrc lrc = new Lrc();
            List<Lrc.Item> itemList = new ArrayList<>();
            lrc.setItemList(itemList);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            String lineString;

            while ((lineString = bufferedReader.readLine()) != null) {

                if (lineString.matches(EXPLAIN_PATTERN)) {

                    String[] explainItems = lineString.replace("[", "").replace("]", "").split(":");
                    String key = explainItems[0];
                    String value = explainItems[1];
                    if ("ti".equals(key)) {
                        lrc.setTitle(value);
                    } else if ("ar".equals(key)) {
                        lrc.setArtist(value);
                    } else if ("al".equals(key)) {
                        lrc.setAlbum(value);
                    } else if ("by".equals(key)) {
                        lrc.setMakeBy(value);
                    } else if("offset".equals(key)){
                        lrc.setOffset(Integer.valueOf(value));
                    }
                } else if (lineString.matches(LRC_PATTERN)) {

                    Log.i("Test",lineString+"=================");

                    String lrcStr = lineString.substring(lineString.lastIndexOf(']') + 1);

                    Pattern pattern = Pattern.compile(LRC_TIME_PATTERN);
                    Matcher matcher = pattern.matcher(lineString);
                    while(matcher.find()){

                        Lrc.Item item = new Lrc.Item();

                        String timeStr = matcher.group();
                        String[] timeItems = timeStr.replace("[", "").replace("]", "").split(":");
                        item.setMinute(Integer.valueOf(timeItems[0]));
                        String[] timeItems2 = timeItems[1].split("\\.");
                        item.setSecond(Integer.valueOf(timeItems2[0]));
                        item.setMilliSecond(Integer.valueOf(timeItems2[1]));
                        item.setTime(item.getMinute() * 60 * 1000 + item.getSecond() * 1000 + item.getMilliSecond());

                        item.setText(lrcStr);
                        itemList.add(item);
                    }
                }else{

                    Log.w("Test", "==========未知的歌词===========" + lineString);
                }
            }
            //排序
            Collections.sort(itemList, new Comparator<Lrc.Item>() {
                @Override
                public int compare(Lrc.Item o1, Lrc.Item o2) {
                    return o1.getTime() - o2.getTime();
                }
            });
            return lrc;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
