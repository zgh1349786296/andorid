package com.example.music.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;
//使用litepal建立关系映射 建立数据库
public class SongBean extends LitePalSupport {
    private int id;
    @Column(unique = true)
    private String name;
    private int songSheetId;

    public SongBean(String name, int songSheetId) {
        this.name = name;
        this.songSheetId = songSheetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongSheetId() {
        return songSheetId;
    }

    public void setSongSheetId(int songSheetId) {
        this.songSheetId = songSheetId;
    }
}
