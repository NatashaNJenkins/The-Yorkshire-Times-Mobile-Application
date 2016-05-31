package com.bradford.pisoc.theyorkshiretimes; /**
 * Created by charmstrange on 01/06/15.
 */

import android.nfc.Tag;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/*
* Helper class for downloading a file.
*/
public class Downloader {

    //Tag for Log statements


    private static String myTag = "Article";

    //helper method to find start of useful rss feed
    public static Boolean isFirstItemTag(String Tag) {

        return Tag.matches("<item>");

    }

    //helper method to find the end of the rss feed
    public static Boolean isLastItemTag(String Tag) {

        return Tag.matches("</channel>");

    }

    //static final int POST_PROGRESS = 1;

    /**
     * *********************************************
     * Download a file from the Internet and store it locally
     *
     * @param artURL - the url of the file to download
     * @param file   - a FileOutputStream to save the downloaded file to.
     *               **********************************************
     */

    public static void DownloadFromUrl(String artURL, String file) { //this isContext the downloader method
        try {


            URL url = new URL(artURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            Log.i(myTag, "Opened Connection");

            //if file name as passed into the method doesn't already exist locally the create one
            if (!(new File(file).exists())) {
                (new File(file)).createNewFile();
            }

            FileWriter fw = new FileWriter((new File(file)).getAbsoluteFile());
            BufferedWriter out = new BufferedWriter(fw);

            String inputLine;
            Boolean after = false;


            //The following loop means that we retrieve only the essencial from the rss feed
            // i.e only the bits that will be used to form ArticleInf objects. (Basically cuts off
            // the unneeded beginning and end to the rss feed)
            while ((inputLine = in.readLine()) != null) {

                if (isFirstItemTag(inputLine)) {
                    after = true;
                }

                if (isLastItemTag(inputLine)) {
                    after = false;
                }

                if (after == true) {
                    System.out.println(inputLine);
                    out.write(inputLine);
                }

            }

            out.flush();
            out.close();

//          *Original code from the tutorial there as a reference*
//          URL url = new URL(URL); //URL of the file
//
//                                    //keep the start time so we can display how long it took to the Log.
//
//            long startTime = System.currentTimeMillis();
//            Log.d(myTag, "download begining");
//
//            /* Open a connection to that URL. */
//
//            URLConnection ucon = url.openConnection();
//
//            // this will be useful so that you can show a tipical 0-100% progress bar
//            //int lenghtOfFile = ucon.getContentLength();
//
//            Log.i(myTag, "Opened Connection");
//
//            /************************************************
//            * Define InputStreams to read from the URLConnection.
//            ************************************************/
//
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//            Log.i(myTag, "Got InputStream and BufferedInputStream");
//
//            /************************************************
//            * Define OutputStreams to write to our file.
//            ************************************************/
//
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            Log.i(myTag, "Got FileOutputStream and BufferedOutputStream");
//
//            /************************************************
//            * Start reading the and writing our file.
//            ************************************************/
//
//            byte data[] = new byte[1024];
//
//            //long total = 0;
//
//            int count;
//
//            //loop and read the current chunk
//
//            while ((count = bis.read(data)) != -1) {
//
//            //keep track of size for progress.
//            //total += count;
//            //write this chunk
//
//                bos.write(data, 0, count);
//            }
//
//            //Have to call flush or the file can get corrupted.
//
//            bos.flush();
//            bos.close();
//            Log.d(myTag, "download ready in "
//                    + ((System.currentTimeMillis() - startTime))
//                    + " milisec");
        } catch (IOException e) {
            Log.d(myTag, "Error: " + e);
        }
    }
}
