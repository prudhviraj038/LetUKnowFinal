package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by sriven on 6/7/2016.
 */
public class WebviewActivity extends Activity {
    ImageView back;
    WebView webView;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);
        back=(ImageView)findViewById(R.id.wv_back_img);
        webView=(WebView)findViewById(R.id.webView4);
        link=getIntent().getStringExtra("link");
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new WebAppInterface(this), "app");
        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new MyWebViewClient());
        webView.loadUrl(link);
        back.setOnClickListener(new View.OnClickListener() {
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
