package com.bradford.pisoc.theyorkshiretimes;

//import android.content.res.AssetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
//import android.app.Activity;
import android.content.Context;
//import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    String EXTRA_MESSAGE;
    private ListView artList;
    private ArticleAdapter mAdapter;
    private String links;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TEST BROWSER
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        //startActivity(browserIntent);


        //Getting a reference to our list view
        artList = (ListView) findViewById(R.id.article_list);
        Log.e("Articless", "OnCreate");

        //We test the availability of network, if it exists we download the file
        //if not we search to see if there is a file saved previously

        if (isNetworkAvailable()) {


            Log.i("Articles", "starting download Task");
            articleDownloadTask download = new articleDownloadTask();
            download.execute();
            List<articleInf> articles = new ArrayList();
            articles = YTXmlPullParser.getArticlesFromFile(MainActivity.this);
            articles = format(articles);
            //Scraping the links from the homepage source
           DownloadHTML dl = new DownloadHTML();
            dl.execute();
            Log.e("Articless", "links: " + links);

            mAdapter = new ArticleAdapter(MainActivity.this, -1, articles);
            artList.setAdapter(mAdapter);

            Log.e("Articless", "adapter size = " + mAdapter.getCount());
            onSelection();

        } else {
            List<articleInf> articles = new ArrayList();
            articles = YTXmlPullParser.getArticlesFromFile(MainActivity.this);
            articles = format(articles);

            mAdapter = new ArticleAdapter(getApplicationContext(), -1, articles);
            artList.setAdapter(mAdapter);
            onSelection();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void getLinks() throws IOException, NetworkOnMainThreadException {

        HtmlParser parser = new HtmlParser();
        links = parser.scrape("http://www.yorkshiretimes.co.uk");
        String test = parser.scrape("http://www.yorkshiretimes.co.uk");
        Log.e("Articless", "links: " + test);
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


//  *Optional override stated in the tutorial not sure if this is needed, may be useful later on(look it up at some point)*
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//       //
//
//        return super.onOptionsItemSelected(item);
//    }

    public void onSelection() {
        artList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> mAdapter, View view, int position, long arg3) {

                Object selected = mAdapter.getItemAtPosition(position);
                articleInf article = new articleInf();
                article = (articleInf) selected;
                String description = article.getDescription();
                String link = article.getLink();
                String title = article.getTitle();
                //EXTRA IMAGE VARIABLE ADDED
                String image = article.getImageUrl();
                goToArticle(description, link, title, image);

            }

        });


    }


    public List<articleInf> format(List<articleInf> articles) {

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
            if (found == false) {
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.bradford.pisoc.theyorkshiretimes/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.bradford.pisoc.theyorkshiretimes/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class DownloadHTML extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try{
                getLinks();
            }
           catch (IOException e){
               e.printStackTrace();
           }
            return  null;
        }

        }

        private class articleDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            Log.e("Articless", "doInBackground");


            //make new instance of downloader
            //make sure you have the entire path name

            Downloader.DownloadFromUrl("http://www.yorkshiretimes.co.uk/rss", MainActivity.this.getFilesDir().getPath().toString() + "/Articles.xml");


            return null;


        }


        @Override
        protected void onPostExecute(Void result) {

            //setup our Adapter and set it to the ListView.

        }

        ;
    }

    public void goToArticle(String description, String link, String title, String image) {
        Intent intent = new Intent(MainActivity.this, DisplayArticle.class);
        intent.putExtra("DESC", description);
        intent.putExtra("LINK", link);
        intent.putExtra("TITLE", title);

        //EXTRA IMAGE VARIABLE ADDED
        intent.putExtra("IMAGE", image);

        startActivity(intent);
    }

}

