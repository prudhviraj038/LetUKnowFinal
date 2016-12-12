package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Chinni on 18-08-2016.
 */
public class SettingsActivity extends Activity {
    MyTextView submit,label;
    EditText name,email,msg;
    String namee,emaill,msgg,no,id;
    LinearLayout subit_ll;
    ImageView back;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_actvity);
        no=getIntent().getStringExtra("no");
        id=getIntent().getStringExtra("id");
        submit=(MyTextView)findViewById(R.id.sett_submit);
        label=(MyTextView)findViewById(R.id.labell);
        name=(EditText)findViewById(R.id.et_name);
        email=(EditText)findViewById(R.id.et_email);
        msg=(EditText)findViewById(R.id.et_msg);
        back=(ImageView)findViewById(R.id.back_btnn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(no.equals("1")){
            label.setText("Request for New Source");
        }else if(no.equals("2")){
            label.setText("Send Bug Report");
        }else{
            label.setText("Report or Abuse");
        }
        subit_ll=(LinearLayout)findViewById(R.id.submit_ll);
        subit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namee=name.getText().toString();
                emaill=email.getText().toString();
                msgg=msg.getText().toString();
                if (namee.equals(""))
                    Toast.makeText(SettingsActivity.this,Session.getword(SettingsActivity.this,"empty_name"), Toast.LENGTH_SHORT).show();
                else if (emaill.equals(""))
                    Toast.makeText(SettingsActivity.this, Session.getword(SettingsActivity.this, "empty_email"), Toast.LENGTH_SHORT).show();
                else if (!emaill.matches(emailPattern))
                    Toast.makeText(SettingsActivity.this, Session.getword(SettingsActivity.this, "empty_email_valid"), Toast.LENGTH_SHORT).show();
                else if (msgg.equals(""))
                    Toast.makeText(SettingsActivity.this,Session.getword(SettingsActivity.this, "empty_message"), Toast.LENGTH_SHORT).show();
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
                    progressDialog.setMessage("please_wait");
                    progressDialog.show();
                    String url = null;
                    try {
                        if(no.equals("1")) {
                            url = Session.SERVER_URL+"suggestions.php?" +
                                    "name=" + URLEncoder.encode(namee, "utf-8") +
                                    "&email=" + URLEncoder.encode(emaill, "utf-8")
                                    + "&message=" + URLEncoder.encode(msgg, "utf-8");
                        }else if(no.equals("2")){
                            url = Session.SERVER_URL+"bug_reports.php?" +
                                    "name=" + URLEncoder.encode(namee, "utf-8") +
                                    "&email=" + URLEncoder.encode(emaill, "utf-8")
                                    + "&message=" + URLEncoder.encode(msgg, "utf-8");
                        }else {
                            url = Session.SERVER_URL+"report_abuse.php?" +
                                    "name=" + URLEncoder.encode(namee, "utf-8") +
                                    "&email=" + URLEncoder.encode(emaill, "utf-8")
                                    + "&message=" + URLEncoder.encode(msgg, "utf-8")
                                    + "&news_id=" + URLEncoder.encode(id, "utf-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("register url", url);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Log.e("response is", jsonObject.toString());
                            try {
//                                Log.e("response is", jsonObject.getString("response"));
                                String result = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                if (result.equals("Failed")) {
                                    Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("error response is:", error.toString());
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "please try again", Toast.LENGTH_SHORT).show();

                        }
                    });

                    AppController.getInstance().addToRequestQueue(jsObjRequest);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
