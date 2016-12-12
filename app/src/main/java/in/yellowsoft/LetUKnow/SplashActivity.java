package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

/**
 * Created by sriven on 6/7/2016.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2235475769630197~3389506060");
       // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d( "Refreshed token: " , refreshedToken);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        ImageView imageView=(ImageView)findViewById(R.id.splash);
        Session.set_user_language(SplashActivity.this, "en");
       // imageView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation));

        get_language_words();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Session.sendRegistrationToServer(this,refreshedToken);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
////                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
////                //intent.putExtra("goto",getIntent().getStringExtra("goto"));
////               // intent.putExtra("data",getIntent().getStringExtra("data"));
////                startActivity(intent);
////                finish();
//
//            }
//        }, 500);
    }

    private void get_language_words(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        String url = Session.SERVER_URL+"words-json.php";

        Log.e("url", url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                Session.set_user_language_words(SplashActivity.this, jsonObject.toString());
//                if(Session.get_user_language1(SplashActivity.this).equals("-1")){
//                    Intent intent = new Intent(SplashActivity.this, LanguageActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("feed_id", "0");
                    startActivity(intent);
                    finish();
//                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("error",error.toString());
                Toast.makeText(SplashActivity.this, Session.getword(SplashActivity.this,"no_network"), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);


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
