package com.example.oberin.imagemacao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oberin on 02/06/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "wordManager",
    TABLE_WORS = "words",
    KEY_ID = "id",
    KEY_WORD = "word",
    KEY_CATEGORY = "category";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE" + TABLE_WORS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_WORD + " TEXT," + KEY_CATEGORY + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORS);
        onCreate(db);
    }

    public void createWord(Word word){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_WORD, word.getWord());
        values.put(KEY_CATEGORY, word.get_category());

        db.insert(TABLE_WORS, null, values);
        db.close();
    }

    public Word getWord(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORS,new String[]{KEY_ID,KEY_WORD,KEY_CATEGORY}, KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor!= null)
            cursor.moveToFirst();

        Word word = new Word(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
        cursor.close();
        db.close();
        return word;
    }

    public void deleteWord(Word word){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_WORS, KEY_ID + "=?", new String[]{String.valueOf(word.getid())});
        db.close();
    }

    public int getWordCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM" + TABLE_WORS, null);
        int wordCount = cursor.getCount();
        cursor.close();
        db.close();
        return wordCount;
    }
    public int updateWord(Word word){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(KEY_WORD, word.getWord());
        values.put(KEY_CATEGORY, word.get_category());
        int rowsAffected = db.update(TABLE_WORS,values,KEY_ID + "=?", new String[]{String.valueOf(word.getid())});
        db.close();
        return rowsAffected;
    }

    public List<Word> getAllWords(){
        List<Word> words = new ArrayList<Word>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM" + TABLE_WORS, null);

        if (cursor.moveToFirst()){
            do {
                words.add(new Word(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return words;
    }
}
