package com.zq.widget.lrc;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class Lrc {

    private String title;

    private String artist;

    private String album;

    private String makeBy;

    private int offset;

    private List<Item> itemList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getMakeBy() {
        return makeBy;
    }

    public void setMakeBy(String makeBy) {
        this.makeBy = makeBy;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public static class Item{

        private int minute;

        private int second;

        private int milliSecond;

        private int time;

        private String text;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getMilliSecond() {
            return milliSecond;
        }

        public void setMilliSecond(int milliSecond) {
            this.milliSecond = milliSecond;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
