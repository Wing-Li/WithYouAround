package com.lyl.myallforyou.im.entity;

import java.io.File;

/**
 * 某个聊天窗口的属性
 * Created by lyl on 2017/9/7.
 */
public class ChatInfo {
    private String id;
    private String title;
    private File icon;
    private int unReadCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getIcon() {
        return icon;
    }

    public void setIcon(File icon) {
        this.icon = icon;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
