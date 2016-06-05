package com.bradford.pisoc.theyorkshiretimes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    ListView artList;
    ArticleAdapter mAdapter;
    //private String links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TEST BROWSER
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        //startActivity(browserIntent);


        //Getting a reference to our list view
        artList = (ListView) findViewById(R.id.article_list);
        if(!doesDatabaseExist(MainActivity.this, ArticleDB.FeedEntry.TABLE_NAME)){
            Log.e("ERROR", "This should not be reached");
            initialiseDB();

        }

        //We test the availability of network, if it exists we download the file
        //if not we search to see if there is a file saved previously

        if (isNetworkAvailable()) {
            articleDownloadTask download = new articleDownloadTask();
            download.execute();
            List<ArticleInf> articles = YTXmlPullParser.getArticlesFromFile(MainActivity.this);
            articles = format(articles);
            //Scraping the links from the homepage source
            mAdapter = new ArticleAdapter(MainActivity.this, -1, articles);
            artList.setAdapter(mAdapter);
            onSelection();
        } else {
            List<ArticleInf> articles = YTXmlPullParser.getArticlesFromFile(MainActivity.this);
            articles = format(articles);

            mAdapter = new ArticleAdapter(getApplicationContext(), -1, articles);
            artList.setAdapter(mAdapter);
            onSelection();
        }
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onSelection() {
        artList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> mAdapter, View view, int position, long arg3) {

                Object selected = mAdapter.getItemAtPosition(position);
                ArticleInf article = (ArticleInf) selected;
                String description = article.getDescription();
                String link = article.getLink();
                String title = article.getTitle();
                //EXTRA IMAGE VARIABLE ADDED
                String image = article.getImageUrl();
                String pubDate = article.getPubDate();
                String author = article.getAuthor();
                String guide = article.getGuid();

                if (articleExists(title)) {
                    goToArticle(link);
                }
                goToArticle(description, link, title, image, article, pubDate, author, guide);


            }

        });
    }


    public boolean articleExists(String title) {
        DBOperations dbop = new DBOperations(MainActivity.this);
        SQLiteDatabase sqldb = dbop.getReadableDatabase();
        String Query = "Select * from " + ArticleDB.FeedEntry.TABLE_NAME + " where " + ArticleDB.FeedEntry.COLUMN_NAME_TITLE+ " = " + "\"" + title + "\"" ;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void initialiseDB()
    {
        DBOperations dbop = new DBOperations(MainActivity.this);
        SQLiteDatabase db = dbop.getWritableDatabase();
        dbop.onCreate(db);

    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public List<ArticleInf> format(List<ArticleInf> articles) {

        String temp;
        //Iterates through articles and substrings the pubDates
        for (int i = 0; i < articles.size(); i++) {
            temp = articles.get(i).getPubDate();
            temp = temp.substring(0, 16);
            articles.get(i).setPubDate(temp);
        }

        for (int i = 0; i < articles.size(); i++) {
            temp = articles.get(i).getAuthor();
            temp = temp.substring(27, temp.length());
            articles.get(i).setAuthor(temp);
        }
        //Iterates through articles
        for (int i = 0; i < articles.size(); i++) {
            temp = articles.get(i).getDescription();

            int index = 0;
            String tempSub;
            Boolean found = false;
            //Iterates through characters of description until the pattern is matched, or the length of the description is reached
            while (!found && (index < temp.length() - 1)) {
                tempSub = temp.substring(index, index + 2);
                if (tempSub.matches(">[A-Z]|> ")) {
                    found = true;
                } else {
                    index++;
                }

            }
            //If the pattern is not matched, set description
            if (!found) {
                temp = "Description not found...";
            }
            //Substrings using the position the pattern was matched previously
            else {
                temp = temp.substring(index + 1, temp.length());
            }
            index = 0;
            found = false;
            //Iterates through resulting string looking for '...'
            while (!found && (index < temp.length() - 1)) {
                tempSub = temp.substring(index, index + 3);
                if (tempSub.matches("(\\.\\.\\.)")) {
                    found = true;
                } else {
                    index++;
                }

            }
            //Cuts everything off following the '...'
            temp = temp.substring(0, index + 3);

            articles.get(i).setDescription(temp);
        }
        return articles;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private class articleDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            //make new instance of downloader
            //make sure you have the entire path name
            Downloader.DownloadFromUrl("http://www.yorkshiretimes.co.uk/rss", "/Articles.xml");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //setup our Adapter and set it to the ListView.

        }
    }

    /* This method is only ran if an article is accessed for the first time */

    public void goToArticle(String description, String link, String title, String image, ArticleInf article, String pubDate, String author, String guide) {
        Intent intent = new Intent(MainActivity.this, DisplayArticle.class);
        intent.putExtra("TITLE", title);
        //EXTRA IMAGE VARIABLE ADDED
        intent.putExtra("IMAGE", image);
        intent.putExtra("LINK",link);
        intent.putExtra("TEXT","default");
            startActivity(intent);

    }

    public String getArticle(String link) throws IOException, NetworkOnMainThreadException {
        Document doc = Jsoup.connect(link).maxBodySize(0).get();
        Element text = doc.select("div.articlebody").first();
        Log.e("pls work", text.toString());
        return format(text.toString());
    }

    public String format(String input) {
        String onlyBr = input.replaceAll("<html>|<div.*|<body>|<img.*|</div.*|<head.*|", "");
        Log.e("output:1", onlyBr);
        return onlyBr;
    }

    /* This method is ran if an article is returned to */
    public void goToArticle(String link) {
        DBOperations dbop = new DBOperations(MainActivity.this);
        SQLiteDatabase sqldb = dbop.getReadableDatabase();
        String Query = "Select * from " + ArticleDB.FeedEntry.TABLE_NAME + " where " + ArticleDB.FeedEntry.COLUMN_NAME_LINK + " = " + link;
        Cursor cursor = sqldb.rawQuery(Query, null);
        int i = cursor.getColumnIndex(ArticleDB.FeedEntry.COLUMN_NAME_TEXT);
        String article = cursor.getString(i);
        cursor.close();
        Intent intent = new Intent(MainActivity.this,DisplayArticle.class);
        intent.putExtra("ARTICLE",article);
    }
}

    /*public void initialiseDB{
        ArticleDB.FeedEntry fe = new ArticleDB.FeedEntry() {
            SQLiteDatabase myDB = openOrCreateDatabase(TABLE_NAME,MODE_PRIVATE,null);
            myDb.execSQL("dd");
            */