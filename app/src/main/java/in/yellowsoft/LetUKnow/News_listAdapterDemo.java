package in.yellowsoft.LetUKnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class News_listAdapterDemo extends BaseAdapter{
    int price;
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public News_listAdapterDemo(Context mainActivity) {
        // TODO Auto-generated constructor stub
        context=mainActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 10;
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
        TextView title,time,description;
        ImageView news_img,logo;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.news_item, null);
        holder.title=(TextView) rowView.findViewById(R.id.news_title);
        holder.time=(TextView) rowView.findViewById(R.id.news_time);
        holder.description=(TextView) rowView.findViewById(R.id.news_descri);
        holder.news_img=(ImageView) rowView.findViewById(R.id.news_img);
        holder.logo=(ImageView) rowView.findViewById(R.id.logo);
        return rowView;
    }

}