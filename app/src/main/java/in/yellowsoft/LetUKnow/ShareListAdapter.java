package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShareListAdapter extends BaseAdapter{
    Context context;
    ArrayList<Integer> images;
    ArrayList<String> titles;
    private static LayoutInflater inflater=null;
    public ShareListAdapter(Activity mainActivity, ArrayList<String> titles, ArrayList<Integer> images) {
        // TODO Auto-generated constructor stu
        context=mainActivity;
        this.images=images;
        this.titles=titles;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.size();
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
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        if(convertView==null)
            rowView = inflater.inflate(R.layout.news_detail_share_item, parent,false);
        else
            rowView = convertView;
        holder.tv=(TextView)rowView.findViewById(R.id.share_item_tv);
        holder.img=(ImageView) rowView.findViewById(R.id.share_item_img);
        holder.tv.setText(titles.get(position));
        holder.img.setImageResource(images.get(position));
        return rowView;
    }

}