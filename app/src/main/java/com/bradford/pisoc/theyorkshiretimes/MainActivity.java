package com.bradford.pisoc.theyorkshiretimes;

//import android.content.res.AssetManager;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
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
                String description = article.getDescription();
                String link = article.getLink();
                String title = article.getTitle();
                    //EXTRA IMAGE VARIABLE ADDED
                    String image = article.getImageUrl();
                goToArticle(description, link, title, image);

            }

        });


    }


    public List<articleInf> format(List<articleInf> articles){

        String temp;
        for(int i = 0; i < articles.size();i++){
            temp = articles.get(i).getPubDate();
            temp = temp.substring(0,16);
            articles.get(i).setPubDate(temp);
        }
        for(int i = 0; i < articles.size(); i ++){
            temp = articles.get(i).getDescription();

            int index = 0;
            String tempSub;
            Boolean found = false;
            while (!found && (index < temp.length()-1)){
                tempSub = temp.substring(index, index + 2);
                if (tempSub.matches(">[A-Z]|> ")){
                    found = true;
                }
                else{
                    index++;
                }

            }
            if(found == false){
                temp = "Description not found...";
            }
            else {
                temp = temp.substring(index + 1, temp.length());
            }
            index = 0;
            found = false;
            while (!found && (index < temp.length()-1)){
                tempSub = temp.substring(index, index + 3);
                if (tempSub.matches("(\\.\\.\\.)")){
                    found = true;
                }
                else{
                    index++;
                }

            }
            temp = temp.substring(0, index + 3);

           articles.get(i).setDescription(temp);

       }


        return articles;
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

        public void goToArticle(String description, String link, String title, String image){
            Intent intent = new Intent(MainActivity.this, DisplayArticle.class);
            intent.putExtra("DESC",description);
            intent.putExtra("LINK", link);
            intent.putExtra("TITLE", title);

                //EXTRA IMAGE VARIABLE ADDED
                intent.putExtra("IMAGE",image);

            startActivity(intent);
        }
    }

