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


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


    /*
    * Custom Adapter class that is responsible for holding the list of Articles after they
    * get parsed out of XML and building row views to display them on the screen.
    */

public class ArticleAdapter extends ArrayAdapter<articleInf> {

    ImageLoader imageLoader;

    //toset options on imageloader we need a DisplayImageOptions variable
    DisplayImageOptions options;


    public ArticleAdapter(Context context, int resource, List<articleInf> articles) {
        super(context, resource, articles);

        //We want to set up our image loader for displaying the images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //Setting options for imageLoader so caching will occur
        options = new DisplayImageOptions.Builder().cacheInMemory().cacheInMemory().build();


    }



        /*
        * (non-Javadoc)
        * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
        *
        * This method is responsible for creating row views out of a articleInf object that can be put
        * into our ListView -> basically where we populate each row view
        */

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        //ConvertView -> a view that is no longer on the screen
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

        final ImageView artImg = (ImageView)row.findViewById(R.id.article_pic);
        final ProgressBar indicator = (ProgressBar)row.findViewById(R.id.progress);

        //To begni with we want a progress bar and then when the image is loaded we see the actual
        // image so initially artImg will be invisible

        indicator.setVisibility(View.VISIBLE);
        artImg.setVisibility(View.INVISIBLE);

        //We now need a Listener to tell us when the image is loaded so we can swap it with the
        //loaded image

        ImageLoadingListener listener =  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted() {

            }

            @Override
            public void onLoadingFailed(FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(Bitmap bitmap) {

                indicator.setVisibility(View.INVISIBLE);
                artImg.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingCancelled() {

            }
        };

        //Load the image and use our options so that the caching occurs

        imageLoader.displayImage(getItem(pos).getImageUrl(),artImg,options,listener);

        //Set the relevant text in our TextViews

        titleTxt.setText(getItem(pos).getTitle());
        pubDateTxt.setText(getItem(pos).getPubDate());
        desTxt.setText(getItem(pos).getDescription());
        guidTxt.setText(getItem(pos).getGuid());
        authorTxt.setText(getItem(pos).getAuthor());

        return row;

    }


}
