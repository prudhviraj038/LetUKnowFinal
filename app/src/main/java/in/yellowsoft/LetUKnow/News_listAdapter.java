package in.yellowsoft.LetUKnow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class News_listAdapter extends BaseAdapter{
    Context context;
    ArrayList<News> newses;
    SharedPreferences sharedPreferences;
    String user_lan = "en";
    private static LayoutInflater inflater=null;
    public News_listAdapter(Context mainActivity,ArrayList<News> newses) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        this.newses = newses;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.e("adapter_size",String.valueOf(newses.size()));
        user_lan=Session.get_user_language(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return newses.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        in.yellowsoft.LetUKnow.MyTextView time,time2,urgent;
        in.yellowsoft.LetUKnow.MyTextView1 ch_title,title,title_ad;
        ImageView news_img,logo,share_btn,video_hint,video_hint_ad;
        LinearLayout is_urgent;
        GifImageView ad_gif;
        SquareImageview img_ad;
    }
    String convertDate(String inputDate) {
        //2016-08-03 04:00:09
        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch(Exception exception) {
            // Generic catch. Do what you want.
        }

        theDateFormat = new SimpleDateFormat("dd MMM ");

        return theDateFormat.format(date);
    }
    public void refill(ArrayList<News> newses) {
        this.newses.clear();
        this.newses.addAll(newses);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        if (newses.get(position).type.equals("news")) {
            rowView = inflater.inflate(R.layout.news_item, null);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            holder.ch_title = (in.yellowsoft.LetUKnow.MyTextView1) rowView.findViewById(R.id.news_ch_title);
            holder.title = (in.yellowsoft.LetUKnow.MyTextView1) rowView.findViewById(R.id.news_title);
            holder.time = (in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.news_timee);
            holder.time2 = (in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.news_time2);
            holder.urgent = (in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.is_urgent_tv);
            holder.video_hint = (ImageView) rowView.findViewById(R.id.video_hint);
            holder.urgent.setText(Session.getword(context, "urgent"));
            holder.is_urgent = (LinearLayout) rowView.findViewById(R.id.is_urgent);
            holder.news_img = (ImageView) rowView.findViewById(R.id.news_img);

            holder.share_btn = (ImageView) rowView.findViewById(R.id.share_btn_item);
            holder.logo = (ImageView) rowView.findViewById(R.id.logo);
            holder.title.setTextSize(sharedPreferences.getInt("font_sizee", 17));
            holder.ch_title.setText(newses.get(position).chanels.get_ch_title(context));
            holder.title.setText(Html.fromHtml(newses.get(position).title));
            Log.e("title", newses.get(position).title);
            holder.time.setText(newses.get(position).get_time(context));
            holder.time2.setText(newses.get(position).get_time(context));

            if (newses.get(position).image.equals(""))
                holder.news_img.setVisibility(View.GONE);

            else {
                ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
                        .getImageLoader();
                imageLoader.get(newses.get(position).image, ImageLoader.getImageListener(holder.news_img,
                        R.drawable.loading, R.drawable
                                .app_icon));
//            Glide.with(context).load(newses.get(position).image).error(R.mipmap.app_icon).placeholder(R.drawable.logonaqsh).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.news_img);
                holder.news_img.setVisibility(View.VISIBLE);
            }
            Log.e("ch_image", newses.get(position).chanels.ch_image);
            Glide.with(context).load(newses.get(position).chanels.ch_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.logo);
            if (newses.get(position).is_urgent.equals("1")) {
                holder.is_urgent.setVisibility(View.VISIBLE);
                holder.time2.setVisibility(View.VISIBLE);
                holder.time.setVisibility(View.GONE);
            } else {
                holder.is_urgent.setVisibility(View.GONE);
                holder.time.setVisibility(View.VISIBLE);
                holder.time2.setVisibility(View.GONE);

            }
            holder.share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, newses.get(position).link);
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });

            if (newses.get(position).video.equals("") && newses.get(position).mp4.equals("")) {
                holder.video_hint.setVisibility(View.GONE);
            } else {
                holder.video_hint.setVisibility(View.VISIBLE);
            }
            return rowView;
        } else if(newses.get(position).type.equals("google_add")) {
            rowView = inflater.inflate(R.layout.news_item_google_ad, null);
            NativeExpressAdView mAdView = (NativeExpressAdView)rowView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            return rowView;
        }else{
            rowView = inflater.inflate(R.layout.news_item_ad, null);
                holder.img_ad = (SquareImageview) rowView.findViewById(R.id.news_img_ad);
                holder.ad_gif=(GifImageView)rowView.findViewById(R.id.ad_gif);
                holder.video_hint_ad= (ImageView) rowView.findViewById(R.id.video_hint_ad);
                holder.title_ad = (in.yellowsoft.LetUKnow.MyTextView1) rowView.findViewById(R.id.news_title_ad);
                holder.title_ad.setText(Session.getword(context, "Sponsered"));
                if (newses.get(position).video.equals("") && newses.get(position).mp4.equals("")) {
                    holder.video_hint_ad.setVisibility(View.GONE);
                } else {
                    holder.video_hint_ad.setVisibility(View.VISIBLE);
                }
                if (newses.get(position).image.endsWith("gif")) {
                    Log.e("ch_image", newses.get(position).image);
                    holder.img_ad.setVisibility(View.VISIBLE);
                    Glide.with(context).load(newses.get(position).image).asGif().error(R.mipmap.app_icon).placeholder(R.drawable.app_icon).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.img_ad);
//                ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
//                        .getImageLoader();
//                imageLoader.get(newses.get(position).image, ImageLoader.getImageListener(holder.ad_gif,
//                        R.drawable.loading, R.drawable
//                                .app_icon));
                    //            Glide.with(context).load(newses.get(position).image).error(R.mipmap.app_icon).placeholder(R.drawable.logonaqsh).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.news_img);
                    holder.ad_gif.setVisibility(View.GONE);
                }else {
                    holder.ad_gif.setVisibility(View.GONE);
//                ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
//                        .getImageLoader();
//                    imageLoader.get(newses.get(position).image, ImageLoader.getImageListener(holder.img_ad,
//                            R.drawable.loading, R.drawable
//                                    .app_icon));
                    Glide.with(context).load(newses.get(position).image).error(R.mipmap.app_icon).placeholder(R.drawable.app_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_ad);
                    holder.img_ad.setVisibility(View.VISIBLE);

                }
                return rowView;
            }
    }


    private String get_different_dates(String date) {
        String temp = "Now";


            long seconds = Long.parseLong(date);
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;


                if(days == 0) {
                    if(hours==0) {
                        temp = String.valueOf(minutes) + (minutes <= 1 ? " minute" : " minutes");
                    }
                    else
                        temp = String.valueOf(hours) + (hours == 1 ?" hour":" hours");
                }
                else if(days<7)
                    temp = days<=1? "1 day": String.valueOf(days)+" days";
                else if(days < 365)
                    temp = String.valueOf(days/7) + (days/7==1?" week":" weeks");
                    else if(days < 365)
                      temp = String.valueOf(days/30) + (days/30==1?" month":" months");
                else
                    temp = String.valueOf(days/365) + (days/365==1?" year":" years");
                return temp + " Ago";

    }



}