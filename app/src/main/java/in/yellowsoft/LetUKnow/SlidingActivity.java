package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

/**
 * Created by HP on 19-Sep-16.
 */
public class SlidingActivity extends Activity {
    ViewFlipper viewFlipper;
    private static ViewPager mPager;
    ImageView close_slider;
    ArrayList<String> img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_activity);
        img=new ArrayList<>();
        img=getIntent().getStringArrayListExtra("images");
        mPager=(ViewPager)findViewById(R.id.pager);
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper4);
        mPager.setAdapter(new SlidingImageAdapter(this, img));
        close_slider=(ImageView)findViewById(R.id.close_slider);
        close_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        try {
            AppController.getInstance().cancelPendingRequests();
            Session.set_minimizetime(this);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        try {
            Session.get_minimizetime(this);
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }
}
