package com.bradford.pisoc.theyorkshiretimes;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;


public class DisplayArticle extends ActionBarActivity {
String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);

        Intent intent = getIntent();
        String description = intent.getStringExtra("DESC");
        link = intent.getStringExtra("LINK");
        String title = intent.getStringExtra("TITLE");
        String image = intent.getStringExtra("IMAGE");

        TextView textView = (TextView) findViewById(R.id.textViewID);
        textView.setText(description);
        new Thread() {
            public void run() {
                try {
                    String articleHTML = getText();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            public String parseHTML(String html, HtmlParser parser){
               Document doc = Jsoup.parse(html);
                Elements text = doc.select("articlebody");
                String test = text.toString();
               // Log.v("parse","article body: " + test);
                return  test;
            }


            public String getText() throws IOException, NetworkOnMainThreadException {

                HtmlParser parser = new HtmlParser();
                String artHtml = parser.scrape(link);

                Log.v("Articless", "html scraped");

               String article = parseHTML(artHtml,parser);

                return article;
            }

        }.start();



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
