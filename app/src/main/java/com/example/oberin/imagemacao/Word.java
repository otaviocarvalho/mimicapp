package com.example.oberin.imagemacao;

/**
 * Created by oberin on 02/06/15.
 */
public class Word{

    private String _word,_category;
    private int _id;
    public Word (int id,String word, String category){
        _id = id;
        _word = word;
        _category = category;

    }

    public int getid(){
        return _id;
    }

    public String getWord(){
        return _word;
    }

    public String get_category(){
        return _category;
    }
}
