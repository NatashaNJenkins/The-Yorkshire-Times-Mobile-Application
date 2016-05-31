package com.bradford.pisoc.theyorkshiretimes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DisplayArticle extends ActionBarActivity {
String link; String article = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);

        Intent intent = getIntent();
        String description = intent.getStringExtra("DESC");
        link = intent.getStringExtra("LINK");
        String title = intent.getStringExtra("TITLE");
        String image = intent.getStringExtra("IMAGE");

        final TextView textView = (TextView) findViewById(R.id.textViewID);



        new Thread() {
            public void run() {
                try {
                    String newArticle = getArticle();
                    article = newArticle;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            public String getArticle() throws IOException, NetworkOnMainThreadException {
              String html = Jsoup.connect(link).maxBodySize(0).get().html();
                Document doc = Jsoup.connect(link).maxBodySize(0).get();
                Element text = doc.select("div.articlebody").first();
                Log.e("pls work", text.toString());
                return text.toString();
            }

        }.start();
        while(article == ""){
            boolean waiting = true;
        }
        article = format(article);
        textView.setText(article);

    }

    public String format(String input){
        String onlyBr = input.replaceAll("<html>|<div.*|<body>|<img.*|<\\/div.*|<head.*|<\\/.*","");
        Log.e("output:1", onlyBr);
        String output = onlyBr.replaceAll("<br>","");
        Log.e("output:2", output);
        Log.e("raw", input);
        return  output;
    }

        /*
        Takes the LINK string, and uses it to download the HTML straight from the url
         */

        // Takes the link and retrieves the article source


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
