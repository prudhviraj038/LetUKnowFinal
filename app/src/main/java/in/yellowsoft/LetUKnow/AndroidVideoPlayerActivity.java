package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by mac on 10/24/16.
 */
public class AndroidVideoPlayerActivity extends Activity{

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;

    // Insert your Video URL
    String VideoURL = "https://video.twimg.com/amplify_video/789776306493808640/pl/vaLptnddqxjlaieY.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.videodisplay);
        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask
        VideoURL = getIntent().getStringExtra("video");
        // Create a progressbar
        pDialog = new ProgressDialog(this);
        // Set progressbar title
       // pDialog.setTitle("Android Video Streaming Tutorial");
        // Set progressbar message
        pDialog.setMessage(Session.getword(this,"loading"));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if(videoview!=null)
            try {
                videoview.pause();
            } catch (Exception ex){
                ex.printStackTrace();
            }

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
