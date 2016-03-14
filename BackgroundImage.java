package com.weebly.docrosby.listtaker;

public class BackgroundImage
{
    private String _png;
    private String name;

    public BackgroundImage(String png, String name){
        this._png = (png);
        this.name = name;
    }

    public String getPng() {
        return this._png;
    }

    public String getName() {
        return this.name;
    }
}
