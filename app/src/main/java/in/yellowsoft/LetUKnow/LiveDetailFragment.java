package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by HP on 7/26/2016.
 */
public class LiveDetailFragment extends Fragment {

    FragmentTouchListner mCallBack;
    String news;
    public interface FragmentTouchListner{
//        public void setselected(String index);
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
        return inflater.inflate(R.layout.livedetailfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        View view = getView();
        news = (String)getArguments().getSerializable("link");
        WebView webView=(WebView)view.findViewById(R.id.webView2);
        ImageView back_img = (ImageView) view.findViewById(R.id.back_img_live);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.back();
            }
        });
        Toast.makeText(getActivity(),Session.getword(getActivity(),"please_wait"),Toast.LENGTH_SHORT).show();
        webView.loadUrl(news);
        }
    }
