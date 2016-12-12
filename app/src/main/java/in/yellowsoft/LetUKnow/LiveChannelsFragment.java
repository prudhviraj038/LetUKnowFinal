package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HP on 7/26/2016.
 */
public class LiveChannelsFragment extends Fragment {
    FragmentTouchListner mCallBack;

    public interface FragmentTouchListner{
        public void newsclicked();
        public void setselected(String index);
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
        return inflater.inflate(R.layout.test_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        mCallBack.setselected("3");
        }

    }
