package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopupListAdapterChanels extends BaseAdapter{
    Context context;
    ArrayList<Chanel> categoriess;
    private static LayoutInflater inflater=null;
    public PopupListAdapterChanels(Activity mainActivity, ArrayList<Chanel> categories) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        this.categoriess=categories;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categoriess.size();
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
        CircleImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.popup_item, null);
        holder.tv=(in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.cat_name);
        holder.tv.setText(categoriess.get(position).get_ch_title(context));
        holder.img=(CircleImageView) rowView.findViewById(R.id.cat_img);
        Glide.with(context).load(categoriess.get(position).ch_image).into(holder.img);
        return rowView;
    }

}