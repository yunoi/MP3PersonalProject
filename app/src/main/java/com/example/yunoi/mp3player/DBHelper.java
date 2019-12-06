package com.example.yunoi.mp3player;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "myMusicDB", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE myMusicTBL (singer CHAR(20), title CHAR(50), janre CHAR(20), rate INTEGE, CONSTRAINT myMusic_key PRIMARY KEY (singer, title));");
        db.execSQL("CREATE TABLE favMusicTBL (id CHAR(50), singer CHAR(20), title CHAR(50), albumArt CHAR(50), CONSTRAINT favMusic_key PRIMARY KEY (singer, title));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS myMusicTBL;");
        db.execSQL("DROP TABLE IF EXISTS favMusicTBL;");
        onCreate(db);
    }
}
