package com.bradford.pisoc.theyorkshiretimes;

/**
 * Created by charmstrange on 27/05/15.
 * <p/>
 * Class to hold ArticleInf objects containing all the in data in the rss stream for particular
 * article
 */
public class ArticleInf {

    //Simple article class to set and get the variables when parsing and displaying respectively

    private String title;
    private String link;
    private String pubDate;
    private String description;
    private String guid;
    private String author;
    private String imageUrl;
    private String articleText;
    private  boolean seen;

    public  ArticleInf(){
        seen = false;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSeen(){seen = true;}

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setArticleText(String text){articleText = text;}

    public String getArticleText(){return  articleText;}

    @Override
    public String toString(){
        return title;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isSeen(){return seen;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
