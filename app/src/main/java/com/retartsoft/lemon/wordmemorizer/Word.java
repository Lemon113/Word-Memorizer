package com.retartsoft.lemon.wordmemorizer;

import java.util.UUID;

/**
 * Created by Lemon on 05.08.2017.
 */

public class Word {
    public String eng;
    public String rus;
    public UUID id;
    public String comment;

    public void Word() {
        id = UUID.randomUUID();
        comment = "";
        eng = "";
        rus = "";
    }

    public void setRus(String val) {
        rus = val;
    }

    public void setEng(String val) {
        eng = val;
    }

    public String getRus() {
        return rus;
    }

    public String getEng() {
        return eng;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) { id = uuid; }

    public void setComment(String s) { comment = s; }

    public String getComment(String s) { return comment; }
}

