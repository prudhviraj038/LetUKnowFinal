package in.yellowsoft.LetUKnow;

/**
 * Created by HP on 8/22/2016.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SlidingImageAdapter extends PagerAdapter {


    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, ArrayList<String > IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final TouchImageView imageView = (TouchImageView) imageLayout
                .findViewById(R.id.image);


       // imageView.setImageResource(IMAGES.get(position));

//        ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
//                .getImageLoader();
//        imageLoader.get(IMAGES.get(position), ImageLoader.getImageListener(imageView,
//                R.drawable.nwessss, android.R.drawable
//                        .ic_dialog_alert));
//        imageView.setImageUrl(IMAGES.get(position), imageLoader);


        Glide.with(context).load(IMAGES.get(position)).into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}