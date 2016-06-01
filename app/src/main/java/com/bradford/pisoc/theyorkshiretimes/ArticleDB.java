package com.bradford.pisoc.theyorkshiretimes;



import android.provider.BaseColumns;

/**
 * Created by Matthew on 31/05/2016.
 */
public class ArticleDB {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ArticleDB() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Articles";
        public static final String ID = "ID";
        public static final String COLUMN_NAME_LINK = "entryid";
        public static final String COLUMN_NAME_PUBDATE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "subtitle";
        public static final String COLUMN_NAME_GUIDE = "guide";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
        public static final String COLUMN_NAME_TEXT = "text";

        private static final String TEXT_TYPE = "TEXT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_PUBDATE + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_GUIDE + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }

}






