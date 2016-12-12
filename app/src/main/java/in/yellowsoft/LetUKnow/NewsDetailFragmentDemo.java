package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by HP on 7/26/2016.
 */
public class NewsDetailFragmentDemo extends Fragment {

    FragmentTouchListner mCallBack;

    public interface FragmentTouchListner{
        public void setselected(String index);
        public void back();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallBack = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LogoutUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.newsdetailfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        View view = getView();
        mCallBack.setselected(getArguments().getString("parent"));

        ImageView imageView = (ImageView) view.findViewById(R.id.news_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ImageZoomActivity.class);
                startActivity(intent);
            }
        });
        ImageView back_img = (ImageView) view.findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.back();
            }
        });
        }



    }
