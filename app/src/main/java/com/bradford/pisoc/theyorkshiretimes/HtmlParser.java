package com.bradford.pisoc.theyorkshiretimes;


import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Matthew on 26/05/2016.
 */

public class HtmlParser {

    public String scrape(String url) throws IOException {
        //Scrapes the page from the given url
        Document doc = Jsoup.connect(url).get();

        Elements links = doc.select("a[href]");
        String strLinks  = links.toString();
        System.out.println(strLinks);
        return strLinks;

        }



    }

