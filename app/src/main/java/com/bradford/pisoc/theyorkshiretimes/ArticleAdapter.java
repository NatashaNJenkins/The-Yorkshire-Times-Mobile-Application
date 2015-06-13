package com.bradford.pisoc.theyorkshiretimes;

/**
 * Created by charmstrange on 08/06/15.
 */


import android.content.Context;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


    /*
    * Custom Adapter class that is responsible for holding the list of Articles after they
    * get parsed out of XML and building row views to display them on the screen.
    */

public class ArticleAdapter extends ArrayAdapter<articleInf> {

    public ArticleAdapter(Context context, int resource, List<articleInf> articles) {
        super(context, resource, articles);
    }

        /*
        * (non-Javadoc)
        * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
        *
        * This method is responsible for creating row views out of a articleInf object that can be put
        * into our ListView
        */

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        RelativeLayout row = (RelativeLayout) convertView;

        Log.i("Articles", "getView pos = " + pos);

        if (null == row) {

            //No recycled View, we have to inflate one.

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout) inflater.inflate(R.layout.article_row, null);

        }

        //Get our View References

        TextView titleTxt = (TextView) row.findViewById(R.id.title_text);
        TextView pubDateTxt = (TextView) row.findViewById(R.id.pubDate_text);
        TextView desTxt = (TextView) row.findViewById(R.id.description_text);
        TextView guidTxt = (TextView) row.findViewById(R.id.guid_text);
        TextView authorTxt = (TextView) row.findViewById(R.id.author_text);

        //Set the relevant text in our TextViews

        titleTxt.setText(getItem(pos).getTitle());
        pubDateTxt.setText(getItem(pos).getPubDate());
        desTxt.setText(getItem(pos).getDescription());
        guidTxt.setText(getItem(pos).getGuid());
        authorTxt.setText(getItem(pos).getAuthor());

        return row;

    }


}
