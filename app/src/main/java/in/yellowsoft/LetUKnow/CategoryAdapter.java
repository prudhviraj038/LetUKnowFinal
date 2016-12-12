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

public class CategoryAdapter extends BaseAdapter{
    Context context;
    ArrayList<Categories> categoriess;
    private static LayoutInflater inflater=null;
    public CategoryAdapter(Activity mainActivity, ArrayList<Categories> categories) {
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
        rowView = inflater.inflate(R.layout.category_item, null);
        holder.tv=(in.yellowsoft.LetUKnow.MyTextView) rowView.findViewById(R.id.cat_name);

        if(Session.get_user_language(context).equals("fr"))
        holder.tv.setText(categoriess.get(position).title);
        else if(Session.get_user_language(context).equals("ar"))
            holder.tv.setText(categoriess.get(position).title_ar);
        else
            holder.tv.setText(categoriess.get(position).title);

        holder.img=(CircleImageView) rowView.findViewById(R.id.cat_img);
        Glide.with(context).load(categoriess.get(position).image).into(holder.img);
        return rowView;
    }

}