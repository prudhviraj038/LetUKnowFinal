package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 7/26/2016.
 */
public class LiveTvFragment extends Fragment {

    GridView listView;
    ArrayList<LiveChannels> categories;
    LiveTvAdapter categoryAdapter ;
    FragmentTouchListner mCallBack;
    MyTextView1 label;
    ImageView settings_btn;
    ArrayList<String> tab_names;
    public interface FragmentTouchListner{
        public void livetvselected(LiveChannels liveChannels);
        public void setselected(String index);
        public void present(String index);
        public void back();
        public  void setttings_btn_clicked();

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
        return inflater.inflate(R.layout.livetvfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

                View view = getView();
        mCallBack.setselected("3");
        mCallBack.present("3");

        categories = new ArrayList<>();
        get_categories();
        settings_btn = (ImageView) view.findViewById(R.id.set_btn_live);
        customs_in_header = (LinearLayout) view.findViewById(R.id.custom_header_layout);
        listView=(GridView)view.findViewById(R.id.listView);
        tab_names = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // mCallBack.livetvselected(categories.get(position));
                if(!categories.get(selected).chanels.get(position).youtube.equals("")) {
                    Intent intent = new Intent(getActivity(), YoutubePlayer.class);
                    intent.putExtra("video", categories.get(selected).chanels.get(position).youtube);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), JWPlayerActivity.class);
                    intent.putExtra("jw_url", categories.get(selected).chanels.get(position).link);
                    startActivity(intent);
                }
            }
        });

//        listView.setAdapter(categoryAdapter);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.setttings_btn_clicked();
            }
        });
        ImageView back_img = (ImageView) view.findViewById(R.id.back_btn);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.back();
            }
        });
        in.yellowsoft.LetUKnow.MyTextView tittle = (in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.label2);
        tittle.setText(Session.getword(getActivity(), "title_tv_live"));

    }

    ArrayList<in.yellowsoft.LetUKnow.MyTextView1> custom_names;
    LinearLayout customs_in_header;
    int selected=0;
    public void display_custom(final ArrayList<String> jsonArray) {

        custom_names = new ArrayList<>();
        customs_in_header.removeAllViewsInLayout();
        customs_in_header.setVisibility(View.VISIBLE);
        for (int i=0;i<jsonArray.size();i++){
            final  in.yellowsoft.LetUKnow.MyTextView1 temp = new in.yellowsoft.LetUKnow.MyTextView1(getActivity());
            try {
                temp.setText(jsonArray.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            temp.setLayoutParams(params);
            temp.setSingleLine(true);
            temp.setGravity(Gravity.CENTER);

            temp.setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
            temp.setBackgroundResource(R.drawable.border_empty_appcolor);
            final int finalI = i;
            final int finalI1 = i;
            final int finalI2 = i;
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < jsonArray.size(); j++) {
                        custom_names.get(j).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                        custom_names.get(j).setBackgroundResource(R.drawable.border_empty_appcolor);

                    }

                    temp.setBackgroundResource(R.drawable.border_full_appcolor);
                    temp.setTextColor(Color.parseColor("white"));
                   // customizeFagment.custom_item_selected(finalI1);
                    categoryAdapter = new LiveTvAdapter(getActivity(),categories.get(finalI2));
                    selected=finalI2;
                    listView.setAdapter(categoryAdapter);


                }
            });

            custom_names.add(temp);
            customs_in_header.addView(temp);
        }
        custom_names.get(0).setBackgroundResource(R.drawable.border_full_appcolor);
        custom_names.get(0).setTextColor(Color.parseColor("white"));
        custom_names.get(0).performClick();
    }

    private void get_categories(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please_wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Session.SERVER_URL+"tv-full.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response",jsonArray.toString());
                if(progressDialog!=null)
                    progressDialog.dismiss();

                for(int i=0;i<jsonArray.length();i++){

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categories.add(new LiveChannels(jsonObject));
                        Log.e("names", jsonObject.getString("title"));
                        tab_names.add(jsonObject.getString("title" + Session.get_append(getActivity())));
                        Log.e("names",tab_names.get(i));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if(categories.size()==0){
//
//                }else{
//                    for(int i=0;i<categories.size();i++) {
//                        tab_names.add(categories.get(i).get_title(getActivity()));
//                    }
//                }
                display_custom(tab_names);

//                categoryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("error",volleyError.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

}
