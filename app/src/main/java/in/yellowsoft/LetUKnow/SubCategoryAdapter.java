package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCategoryAdapter extends BaseAdapter{
    Context context;
    Categories categoriess;
    DatabaseHandler db;
    String user_lan;
    private static LayoutInflater inflater=null;
    public SubCategoryAdapter(Activity mainActivity, Categories categories) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        this.categoriess=categories;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHandler(context);
        user_lan=Session.get_user_language(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categoriess.chanels.size();

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
        in.yellowsoft.LetUKnow.MyTextView tv;
        in.yellowsoft.LetUKnow.MyTextView follow_btn;
        ImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.sub_category_item, null);
        holder.tv=(in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.cat_name);
        if(user_lan.equals("fr"))
        holder.tv.setText(Html.fromHtml(categoriess.chanels.get(position).ch_title_fr));
       else if(user_lan.equals("ar"))
            holder.tv.setText(Html.fromHtml(categoriess.chanels.get(position).ch_title_ar));
        else
            holder.tv.setText(Html.fromHtml(categoriess.chanels.get(position).ch_title));

        holder.img=(ImageView) rowView.findViewById(R.id.cat_img);
        Glide.with(context).load(categoriess.chanels.get(position).ch_image).into(holder.img);
        holder.follow_btn=(in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.follow_btn);
        holder.follow_btn.setText(Html.fromHtml(db.is_following(categoriess.chanels.get(position).ch_id) ? Session.getword(context,"unfollow") : Session.getword(context,"follow")));

        if(db.is_following(categoriess.chanels.get(position).ch_id)) {
            holder.follow_btn.setBackgroundResource(R.drawable.border_full_for_add);
            holder.follow_btn.setTextColor(Color.parseColor("white"));
        }
        else {
            holder.follow_btn.setBackgroundResource(R.drawable.border_empty_for_add);
            holder.follow_btn.setTextColor(context.getResources().getColor(R.color.aa_app_blue));
        }
        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.is_following(categoriess.chanels.get(position).ch_id)) {

                    db.deletePlaylist(categoriess.chanels.get(position).ch_id);

                } else {

                    db.addPlaylist(categoriess.chanels.get(position).ch_id, categoriess.id);
                    mFirebaseAnalytics.setUserProperty("favorite_channel", categoriess.chanels.get(position).get_ch_title(context));


                }
                holder.follow_btn.setText(Html.fromHtml(db.is_following(categoriess.chanels.get(position).ch_id) ? Session.getword(context,"unfollow") : Session.getword(context,"follow")));
                if(db.is_following(categoriess.chanels.get(position).ch_id)) {
                    holder.follow_btn.setBackgroundResource(R.drawable.border_full_for_add);
                    holder.follow_btn.setTextColor(Color.parseColor("white"));
                }
                else {
                    holder.follow_btn.setBackgroundResource(R.drawable.border_empty_for_add);
                    holder.follow_btn.setTextColor(context.getResources().getColor(R.color.aa_app_blue));
                }

            }
        });

        return rowView;
    }

    private FirebaseAnalytics mFirebaseAnalytics;


}