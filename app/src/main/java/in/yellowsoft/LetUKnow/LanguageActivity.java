package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by sriven on 6/7/2016.
 */
public class LanguageActivity extends Activity {
    MyTextView choose_lan;
    LinearLayout eng,arabic,french;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_screen);
        choose_lan=(MyTextView)findViewById(R.id.choose_lan);
        choose_lan.setText(Session.getword(this,"choose_lan"));
        eng=(LinearLayout)findViewById(R.id.en_ll);
        arabic=(LinearLayout)findViewById(R.id.ar_ll);
        french=(LinearLayout)findViewById(R.id.fr_ll);
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.set_user_language(LanguageActivity.this,"en");
                Intent intent = new Intent(LanguageActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.set_user_language(LanguageActivity.this,"ar");
                Intent intent = new Intent(LanguageActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.set_user_language(LanguageActivity.this,"fr");
                Intent intent = new Intent(LanguageActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
