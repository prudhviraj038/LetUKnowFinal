package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

public class LiveTvAdapter extends BaseAdapter{
    Context context;
    LiveChannels categoriess;
    String user_language = "en";
    private static LayoutInflater inflater=null;
    public LiveTvAdapter(Activity mainActivity, LiveChannels categories) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        this.categoriess=categories;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        user_language = Session.get_user_language(context);
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
        SquareImageview img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.livetv_item, null);
        holder.tv=(in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.cat_name);
//        if(user_language.equals("fr"))
//         holder.tv.setText(categoriess.get(position).title_fr);
//        else  if(user_language.equals("ar"))
//            holder.tv.setText(categoriess.get(position).title_ar);
//        else
//            holder.tv.setText(categoriess.get(position).title);
        holder.tv.setText(categoriess.chanels.get(position).get_ch_title(context));
        holder.img=(SquareImageview) rowView.findViewById(R.id.cat_img);
        Glide.with(context).load(categoriess.chanels.get(position).ch_image).into(holder.img);
        return rowView;
    }

}