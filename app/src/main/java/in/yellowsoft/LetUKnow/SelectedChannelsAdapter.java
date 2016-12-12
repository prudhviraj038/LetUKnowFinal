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

import java.util.ArrayList;

public class SelectedChannelsAdapter extends BaseAdapter{
    Context context;

    ArrayList<Chanel> chanels;
    DatabaseHandler db;
    private static LayoutInflater inflater=null;
    public SelectedChannelsAdapter(Activity mainActivity,  ArrayList<Chanel> chanels) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        this.chanels = chanels;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHandler(context);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return chanels.size();

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
        MyTextView tv;
        MyTextView follow_btn;
        ImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.sub_category_item, null);
        holder.tv=(MyTextView) rowView.findViewById(R.id.cat_name);
        holder.tv.setText(Html.fromHtml(chanels.get(position).ch_title));
        holder.img=(ImageView) rowView.findViewById(R.id.cat_img);
        Glide.with(context).load(chanels.get(position).ch_image).into(holder.img);
        holder.follow_btn=(MyTextView) rowView.findViewById(R.id.follow_btn);
        holder.follow_btn.setText(Html.fromHtml(db.is_following(chanels.get(position).ch_id) ? Session.getword(context,"unfollow") : Session.getword(context,"follow")));
        if(db.is_following(chanels.get(position).ch_id)) {
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

                if (db.is_following(chanels.get(position).ch_id)) {

                    db.deletePlaylist(chanels.get(position).ch_id);
                        chanels.remove(position);
                        notifyDataSetChanged();
                } else {

                    holder.follow_btn.setText(Html.fromHtml(db.is_following(chanels.get(position).ch_id) ? Session.getword(context,"unfollow") : Session.getword(context,"follow")));
                    if(db.is_following(chanels.get(position).ch_id)) {
                        holder.follow_btn.setBackgroundResource(R.drawable.border_full_for_add);
                        holder.follow_btn.setTextColor(Color.parseColor("white"));
                    }
                    else {
                        holder.follow_btn.setBackgroundResource(R.drawable.border_empty_for_add);
                        holder.follow_btn.setTextColor(context.getResources().getColor(R.color.aa_app_blue));
                    }

                }


            }
        });

        return rowView;
    }



}