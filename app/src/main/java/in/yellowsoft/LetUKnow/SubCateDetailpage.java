package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 7/26/2016.
 */
public class SubCateDetailpage extends Fragment {
    ImageView cover_image,ch_image;
    in.yellowsoft.LetUKnow.MyTextView member_count,ch_title,label;
    in.yellowsoft.LetUKnow.MyTextView follow_btn;
    ListView listView;
    ArrayList<News> news;
    News_listAdapter news_list_adapter;
    FragmentTouchListner mCallBack;
    String url = Session.SERVER_URL+"feeds.php?";
    DatabaseHandler db;
    Categories.Chanel chanel;
    String parent = "";
    String parent_name="";

    int ch_count=0;
    public interface FragmentTouchListner{
        public void newsclicked(News news);
        public void setselected(String index);
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
        return inflater.inflate(R.layout.subcatdetailfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
                View view = getView();
        chanel = (Categories.Chanel)getArguments().getSerializable("chanel");
        parent = getArguments().getString("parent_id");
        parent_name = getArguments().getString("parent_name");
        db = new DatabaseHandler(getActivity());
                 mCallBack.setselected("4");
                customs_in_header = (LinearLayout) view.findViewById(R.id.custom_header_layout);
                listView=(ListView)view.findViewById(R.id.listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // mCallBack.newsclicked(news.get(position));
                }
            });
                news = new ArrayList<>();
                news_list_adapter=new News_listAdapter(getActivity(), news);
        listView.setAdapter(news_list_adapter);

                ArrayList<String> tab_names = new ArrayList<>();
                tab_names.add("sports news");
                tab_names.add("source");

               // display_custom(tab_names);

        cover_image = (ImageView) view.findViewById(R.id.cover_image);
        Glide.with(getActivity()).load(chanel.cover_image).into(cover_image);
        ch_image = (ImageView)view.findViewById(R.id.ch_logo);
        ch_title = (in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.ch_title);
        label = (in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.label);
        label.setText(parent_name);
        Glide.with(getActivity()).load(chanel.ch_image).into(ch_image);
        ch_title.setText(chanel.ch_title);
        member_count = (in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.member_count);
        member_count.setText(chanel.count);
        ch_count = Integer.parseInt(chanel.count);
        get_news("chanels=" + chanel.ch_id);

        follow_btn=(in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.follow_btn);
        follow_btn.setText(db.is_following(chanel.ch_id) ? Html.fromHtml("&#10004;") : "Add");
        if(db.is_following(chanel.ch_id)) {
            follow_btn.setBackgroundResource(R.drawable.border_full_for_add);
            follow_btn.setTextColor(Color.parseColor("white"));
        }
        else {
            follow_btn.setBackgroundResource(R.drawable.border_empty_for_add);
            follow_btn.setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
        }
        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.is_following(chanel.ch_id)) {

                    db.deletePlaylist(chanel.ch_id);
                    ch_count--;
                    member_count.setText(String.valueOf(ch_count));

                } else {

                    db.addPlaylist(chanel.ch_id, parent);
                    ch_count++;
                    member_count.setText(String.valueOf(ch_count));


                }
                follow_btn.setText(db.is_following(chanel.ch_id) ? Html.fromHtml("&#10004;") : "Add");
                if (db.is_following(chanel.ch_id)) {
                    follow_btn.setBackgroundResource(R.drawable.border_full_for_add);
                    follow_btn.setTextColor(Color.parseColor("white"));
                } else {
                    follow_btn.setBackgroundResource(R.drawable.border_empty_for_add);
                    follow_btn.setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                }

            }

        });

        ImageView back_btn = (ImageView)view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.back();
            }
        });

    }

    ArrayList<in.yellowsoft.LetUKnow.MyTextView> custom_names;
    LinearLayout customs_in_header;

    public void display_custom(final ArrayList<String> jsonArray) {

        custom_names = new ArrayList<>();
        customs_in_header.removeAllViewsInLayout();
        customs_in_header.setVisibility(View.VISIBLE);
        for (int i=0;i<jsonArray.size();i++){
            final  in.yellowsoft.LetUKnow.MyTextView temp = new in.yellowsoft.LetUKnow.MyTextView(getActivity());
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
                    tabclicked(finalI2);

                }
            });

            custom_names.add(temp);
            customs_in_header.addView(temp);
        }
        custom_names.get(0).setBackgroundResource(R.drawable.border_full_appcolor);
        custom_names.get(0).setTextColor(Color.parseColor("white"));

    }

    private void get_news(String url_append){
        news.clear();
        Log.e("url",url+url_append);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please_wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+url_append, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response", jsonArray.toString());
                if(progressDialog!=null)
                    progressDialog.dismiss();

                for(int i=0;i<jsonArray.length();i++){

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("json", jsonObject.toString());
                        news.add(new News(jsonObject));

                    } catch (JSONException e) {
                        news_list_adapter.notifyDataSetChanged();
                        e.printStackTrace();
                    }
                    if(jsonArray.length()==0){
                        Toast.makeText(getActivity(),"no feeds to display",Toast.LENGTH_SHORT).show();
                    }
                }

                news_list_adapter=new News_listAdapter(getActivity(),news);
                listView.setAdapter(news_list_adapter);

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

    private void tabclicked(int index){

        switch (index){
            case 0:
                get_news("category="+MainActivity.sports_id);
                break;
            case 1:
                get_news("chanels="+db.selected_channels(MainActivity.sports_id));
                break;

        }
    }


}
