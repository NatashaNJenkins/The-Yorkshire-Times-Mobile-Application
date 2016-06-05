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
        public static final String COLUMN_NAME_SEEN = "seen";
        public static final String COLUMN_NAME_TITLE = "title";

    }
}






