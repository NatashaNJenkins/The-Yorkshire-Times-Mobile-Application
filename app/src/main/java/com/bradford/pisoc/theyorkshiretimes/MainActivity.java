package com.bradford.pisoc.theyorkshiretimes;

//import android.content.res.AssetManager;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
//import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    String EXTRA_MESSAGE;
    private ListView artList;
    private ArticleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting a reference to our list view
        artList = (ListView) findViewById(R.id.article_list);
        Log.e("Articless", "OnCreate");

        //We test the availability of network, if it exists we download the file
        //if not we search to see if there is a file saved previously

        if (isNetworkAvailable()) {

            Log.i("Articles", "starting download Task");
            articleDownloadTask download = new articleDownloadTask();
            download.execute();
            mAdapter = new ArticleAdapter(MainActivity.this, -1, YTXmlPullParser.getArticlesFromFile(MainActivity.this));
            artList.setAdapter(mAdapter);

            Log.e("Articless", "adapter size = " + mAdapter.getCount());
            onSelection();

        } else {

            mAdapter = new ArticleAdapter(getApplicationContext(), -1, YTXmlPullParser.getArticlesFromFile(MainActivity.this));
            artList.setAdapter(mAdapter);
            onSelection();
        }

    }
    public interface OnCustomClickListener {
        public void OnCustomClick(View aView, int position);
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

    public void onSelection(){
        artList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> mAdapter, View view, int position, long arg3){

                Object  selected =  mAdapter.getItemAtPosition(position);
                articleInf article = new articleInf();
                article = (articleInf) selected;
                String value = article.getDescription();

                goToArticle(value);
                Log.e("Article as .toString()", value);
            }

        });





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
        protected void onPostExecute(Void result)
        {

            //setup our Adapter and set it to the ListView.

            }

        ;}

        public void goToArticle(String description){
            Intent intent = new Intent(MainActivity.this, DisplayArticle.class);
            intent.putExtra("DESC",description);
            startActivity(intent);
        }
    }

