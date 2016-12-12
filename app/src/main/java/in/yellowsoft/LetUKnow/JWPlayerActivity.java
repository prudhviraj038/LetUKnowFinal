package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.os.Bundle;

import com.longtailvideo.jwplayer.JWPlayerFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

/**
 * Created by mac on 10/19/16.
 */
public class JWPlayerActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the Activity's content view.
        String jw_url = getIntent().getStringExtra("jw_url");
        setContentView(R.layout.jw_player_fragment);

        // Get a handle to the JWPlayerFragment
        JWPlayerFragment fragment = (JWPlayerFragment) getFragmentManager().findFragmentById(R.id.playerFragment);

        // Get a handle to the JWPlayerView
        JWPlayerView playerView = fragment.getPlayer();

        // Create a PlaylistItem
        PlaylistItem video = new PlaylistItem(jw_url);

        // Load a stream into the player

        playerView.load(video);

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
