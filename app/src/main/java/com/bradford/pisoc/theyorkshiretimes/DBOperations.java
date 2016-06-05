package com.bradford.pisoc.theyorkshiretimes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Matthew on 01/06/2016.
 */
public class DBOperations extends SQLiteOpenHelper{
    public static final int DBVersion = 1;
    public String CREATE_QUERY = "CREATE TABLE " + ArticleDB.FeedEntry.TABLE_NAME +"("
            + ArticleDB.FeedEntry.COLUMN_NAME_LINK +" TEXT,"
            + ArticleDB.FeedEntry.COLUMN_NAME_PUBDATE + " DATE,"
            + ArticleDB.FeedEntry.COLUMN_NAME_DESCRIPTION + " TEXT,"
            + ArticleDB.FeedEntry.COLUMN_NAME_AUTHOR + " TEXT,"
            + ArticleDB.FeedEntry.COLUMN_NAME_GUIDE + " TEXT,"
            + ArticleDB.FeedEntry.COLUMN_NAME_IMAGE_URL + " TEXT,"
            + ArticleDB.FeedEntry.COLUMN_NAME_TEXT + " TEXT,"
            + ArticleDB.FeedEntry.COLUMN_NAME_SEEN + " BOOL,"
            + ArticleDB.FeedEntry.COLUMN_NAME_TITLE + " TEXT );";

    public DBOperations(Context context) {
        super(context, ArticleDB.FeedEntry.TABLE_NAME, null, DBVersion);
        Log.d("Database operations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    sqLiteDatabase.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DELETE * FROM" + ArticleDB.FeedEntry.TABLE_NAME);
        onCreate(db);
    }

    public void insertArticle(DBOperations dbop,String link, String pubDate, String description, String author, String guide, String imgUrl, String text, boolean seen){
        SQLiteDatabase db = dbop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_LINK,link);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_PUBDATE,pubDate);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_DESCRIPTION,description);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_AUTHOR,author);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_GUIDE,guide);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_IMAGE_URL,imgUrl);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_TEXT,text);
        cv.put(ArticleDB.FeedEntry.COLUMN_NAME_SEEN,seen);
        long k = db.insert(ArticleDB.FeedEntry.TABLE_NAME,null,cv);
        Log.d("Database operations", "Row Inserted");
    }

    public void dltMostRecent(DBOperations dbop){
        SQLiteDatabase sqldb = dbop.getReadableDatabase();
        String Query = "WITH T AS ( SELECT TOP 1 * FROM " + ArticleDB.FeedEntry.TABLE_NAME + " ORDER BY " + ArticleDB.FeedEntry.COLUMN_NAME_PUBDATE + " ) " +
                "DELETE FROM T";
        Cursor cursor = sqldb.rawQuery(Query, null);
        cursor.close();
    }

    public void clearDB(DBOperations dbop){
        SQLiteDatabase sqldb = dbop.getWritableDatabase();
        String Query = "Delete * from " + ArticleDB.FeedEntry.TABLE_NAME;
        Cursor cursor = sqldb.rawQuery(Query, null);
        cursor.close();
    }
    public int getCount(DBOperations dbop){
        SQLiteDatabase sqldb = dbop.getWritableDatabase();
        String Query = "SELECT * FROM " + ArticleDB.FeedEntry.TABLE_NAME;
        Cursor cursor = sqldb.rawQuery(Query, null);
        int count = cursor.getCount();
        cursor.close();
        return  count;
    }


}


