package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by HP on 7/26/2016.
 */
public class SportsFragment extends Fragment implements AbsListView.OnScrollListener {
    ListView listView;
    GridView gridView;
    int start=0,end=10;
    LinearLayout progressBar;
    ArrayList<News> news;
    ArrayList<News> latest_news;
    News_listAdapter news_list_adapter;
    FragmentTouchListner mCallBack;
    String url = Session.SERVER_URL+"feeds.php?";
    String url_appednd;
    String url_appednd_search="";
    LinearLayout search_view,cancel,add_source_ll;
    EditText search_edit;
    ImageView sesrch_close;
    MyTextView cancel_tv,no_source,add_source,pop_up_label;
    DatabaseHandler db;
    int temp=0,posi=0;
    ViewFlipper viewFlipper;
   // ViewFlipper viewFlipper;
    LinearLayout pop_up_layout,search_view_back;
    int selected =0;
    int pre_selected =0;
    LinearLayout new_feeds_btn_ll;
    in.yellowsoft.LetUKnow.MyTextView label;
    ImageView search_btn,settings_btn;
    TextView new_feeds_btn;
    public interface FragmentTouchListner{
        public void newsclicked(News news);
        public void setselected(String index);
        public  void setttings_btn_clicked();
        public  void source();
        public  void search();
        public void present(String index);

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
        return inflater.inflate(R.layout.worldfragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
                View view = getView();
        mCallBack.setselected("2");
        mCallBack.present("2");
        db = new DatabaseHandler(getActivity());
        new_feeds_btn_ll = (LinearLayout) view.findViewById(R.id.new_feeds_btn_ll);
        new_feeds_btn = (TextView) view.findViewById(R.id.new_feeds_btn);
        new_feeds_btn_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.smoothScrollToPosition(0);
                new_feeds_btn_ll.setVisibility(View.GONE);
//                t=0;
                set_refresh_timer();
            }
        });

        label = (in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.lable);
        viewFlipper=(ViewFlipper)view.findViewById(R.id.viewFlipper);
        no_source = (MyTextView) view.findViewById(R.id.no_source);
        no_source.setText(Session.getword(getActivity(),"error_no_channels_subscribed"));
        add_source = (MyTextView) view.findViewById(R.id.add_source_tv);
        add_source.setText(Session.getword(getActivity(),"title_add_source"));
        pop_up_label = (MyTextView) view.findViewById(R.id.pop_up_label);
        pop_up_label.setText(Session.getword(getActivity(),"title_select_sources"));
        add_source_ll = (LinearLayout) view.findViewById(R.id.add_source_ll);
        pop_up_layout = (LinearLayout) view.findViewById(R.id.pop_up_layout);
        pop_up_layout.setVisibility(View.GONE);
        label.setText(Session.getword(getActivity(), "title_sports"));
        cancel = (LinearLayout) view.findViewById(R.id.cancel_source_ll);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up_layout.setVisibility(View.GONE);
                for (int j = 0; j < custom_names.size(); j++) {
                    custom_names.get(j).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                    custom_names.get(j).setBackgroundResource(R.drawable.border_empty_appcolor);
                }
                //  custom_names.get(pre_selected).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                // custom_names.get(pre_selected).setBackgroundResource(R.drawable.border_empty_appcolor);

                custom_names.get(pre_selected).setBackgroundResource(R.drawable.border_full_appcolor);
                custom_names.get(pre_selected).setTextColor(Color.parseColor("white"));
                selected = pre_selected;

            }
        });
        cancel_tv = (MyTextView) view.findViewById(R.id.cancel_source_tv);
        cancel_tv.setText(Session.getword(getActivity(), "cancel"));
        progressBar = (LinearLayout) view.findViewById(R.id.progressBar);
        search_btn = (ImageView) view.findViewById(R.id.search_btn);
        settings_btn = (ImageView) view.findViewById(R.id.set_btn);
        search_view = (LinearLayout) view.findViewById(R.id.search_view);
        search_view_back = (LinearLayout) view.findViewById(R.id.search_view_back);
        sesrch_close = (ImageView) view.findViewById(R.id.search_close);
        sesrch_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_appednd_search = "";
                search_view.setVisibility(View.GONE);
            }
        });
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        search_edit = (EditText) view.findViewById(R.id.search_edit);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() > 2)
                        get_news("key=" + URLEncoder.encode(s.toString(), "utf-8"), true,true);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.setttings_btn_clicked();
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.search();
//                if(search_view.getVisibility()==View.VISIBLE) {
//                    search_view.setVisibility(View.GONE);
//                    search_view_back.setVisibility(View.VISIBLE);
//                    url_appednd_search="";
//                    get_news(url_appednd,true);
//                }
//                else {
//                    search_view.setVisibility(View.VISIBLE);
//                    search_view_back.setVisibility(View.GONE);
//
//                }
            }
        });


        //viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
        gridView = (GridView)view.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    posi=position;
                    get_news("chanels=" + chanels.get(position).ch_id, true,true);
                    temp=1;
            }
        });


                customs_in_header = (LinearLayout) view.findViewById(R.id.custom_header_layout);
                listView=(ListView)view.findViewById(R.id.listView);
                listView.setOnScrollListener(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(news.get(position).type.equals("news")){
                        mCallBack.newsclicked(news.get(position));
                    }else{
                        if (news.get(position).video.equals("") && news.get(position).mp4.equals("")) {
                            Intent intent = new Intent(getActivity(), WebviewActivity.class);
                            intent.putExtra("link", news.get(position).link);
                            startActivity(intent);
                        }else{
                            if(!news.get(position).mp4.equals("")){
                                Intent intent = new Intent(getActivity(), AndroidVideoPlayerActivity.class);
                                intent.putExtra("video", news.get(position).mp4);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(getActivity(), YoutubePlayer.class);
                                intent.putExtra("video", news.get(position).video);
                                startActivity(intent);
                            }
                        }
                    }

                }
            });
                news = new ArrayList<>();
                latest_news = new ArrayList<>();

                news_list_adapter=new News_listAdapter(getActivity(),news);
                listView.setAdapter(news_list_adapter);

                ArrayList<String> tab_names = new ArrayList<>();
        tab_names.add(Session.getword(getActivity(),"latest"));
                tab_names.add(Session.getword(getActivity(),"source"));
                display_custom(tab_names);

        if(db.selected_channels(MainActivity.sports_id).equals("0")) {
//            Toast.makeText(getActivity(),"You have not selected any sources",Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(1);
            add_source_ll.setVisibility(View.VISIBLE);
            no_source.setText(Session.getword(getActivity(), "error_no_channels_subscribed"));
        }
        else {
            viewFlipper.setDisplayedChild(0);
            get_news("channels=" + db.selected_channels(MainActivity.sports_id),true,true);
        }
        add_source_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.source();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (pop_up_layout.getVisibility() == View.VISIBLE ) {
                        pop_up_layout.setVisibility(View.GONE);
                        for (int j = 0; j < custom_names.size(); j++) {
                            custom_names.get(j).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                            custom_names.get(j).setBackgroundResource(R.drawable.border_empty_appcolor);
                        }
                        //  custom_names.get(pre_selected).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                        // custom_names.get(pre_selected).setBackgroundResource(R.drawable.border_empty_appcolor);

                        custom_names.get(pre_selected).setBackgroundResource(R.drawable.border_full_appcolor);
                        custom_names.get(pre_selected).setTextColor(Color.parseColor("white"));
                        selected=pre_selected;

                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
        set_refresh_timer();
        }

    ArrayList<in.yellowsoft.LetUKnow.MyTextView1  > custom_names;
    LinearLayout customs_in_header;

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

                    custom_names.get(finalI).setBackgroundResource(R.drawable.border_full_appcolor);
                    custom_names.get(finalI).setTextColor(Color.parseColor("white"));
                    if(finalI2==selected && selected!=custom_names.size()-1){
                        listView.smoothScrollToPosition(0);
                        Log.e("same","clicked");
                    } else{
                        tabclicked(finalI2, true);
                        pre_selected=selected;
                        selected=finalI2;


                    }

                }
            });

            custom_names.add(temp);

        }
        custom_names.get(0).setBackgroundResource(R.drawable.border_full_appcolor);
        custom_names.get(0).setTextColor(Color.parseColor("white"));

        for(int r=0;r<custom_names.size();r++)
            customs_in_header.addView(custom_names.get(r));


    }
    String temp_url = "";

    private void get_news(String url_append,boolean clear_data,boolean show_progress){
        if(show_progress)
        progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
        if(clear_data)
            news.clear();
        start=news.size();
        pop_up_layout.setVisibility(View.GONE);
        String temp_url = "";

        if(news.size()==0){
            if(url_append.equals("")) {
                temp_url = url + url_append + url_appednd_search;
            }
            else {
                temp_url = url + url_append + "&" + url_appednd_search;
            }

        }else{
            if(url_append.equals("")) {
                temp_url = url + url_append + url_appednd_search + "last_id=" + news.get(news.size()-1).id ;
            }
            else {
                temp_url = url + url_append + "&" + url_appednd_search + "last_id=" + news.get(news.size()-1).id ;
            }

        }

        Log.e("url", temp_url);
        url_appednd = url_append;
//        temp_url=temp_url
        // final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("please_wait");
        //progressDialog.show();
        //progressDialog.setCancelable(false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(temp_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response", jsonArray.toString());
              /*  if(progressDialog!=null)
                    progressDialog.dismiss();
*/                  progressBar.setVisibility(View.GONE);
                for(int i=0;i<jsonArray.length();i++){

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("json", jsonObject.toString());
                        news.add(new News(jsonObject));

                    } catch (JSONException e) {
                        news_list_adapter.notifyDataSetChanged();
                        e.printStackTrace();
                    }
                    if(news.size()==0){
                        viewFlipper.setDisplayedChild(1);
                        no_source.setText(Session.getword(getActivity(), "no_feeds"));
                        add_source_ll.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(),"no feeds to display",Toast.LENGTH_SHORT).show();
                    }else{
                        viewFlipper.setDisplayedChild(0);
                    }
                }
                if(start==0) {
                 //   news_list_adapter = new News_listAdapter(getActivity(), news);
                    listView.setAdapter(news_list_adapter);
                    news_list_adapter.notifyDataSetChanged();
                }else{
                    int x = listView.getScrollX();
                    news_list_adapter.notifyDataSetChanged();
                    listView.setSelection(start);
                    listView.setScrollX(x);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               /* if(progressDialog!=null)
                    progressDialog.dismiss();
               */
                progressBar.setVisibility(View.GONE);
                Log.e("error", volleyError.toString());
                Toast.makeText(getActivity(),volleyError.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    private void tabclicked(int index,boolean clear_data){

        switch (index){
            case 0:
                temp=0;
                if(db.selected_channels(MainActivity.sports_id).equals("0")) {
//                    Toast.makeText(getActivity(),"You have not selected any sources",Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1);
                    add_source_ll.setVisibility(View.VISIBLE);
                    no_source.setText(Session.getword(getActivity(), "error_no_channels_subscribed"));
                }else

                    get_news("channels="+db.selected_channels(MainActivity.sports_id),clear_data,true);

                break;

            case 1:
//                pop_up_layout.setVisibility(View.VISIBLE);
                    get_chanels(db.selected_channels(MainActivity.sports_id));

                //    get_news("chanels=" + chanels.get(posi).ch_id, false);

                break;


        }
    }

    ArrayList<Categories> categories;
    ArrayList<Chanel> chanels;
    PopupListAdapter categoryAdapter ;
    PopupListAdapterChanels categoryAdapterChanels ;

    private void get_categories(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please_wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Session.SERVER_URL+"categories.php?type=country";
        Log.e("url",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response",jsonArray.toString());
                if(progressDialog!=null)
                    progressDialog.dismiss();
                categories =new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categories.add(new Categories(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                categoryAdapter = new PopupListAdapter(getActivity(),categories);
                gridView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
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

    private void get_chanels(String list){
        /*final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please_wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
       */
        progressBar.setVisibility(View.VISIBLE);
        String url = Session.SERVER_URL+"channels.php?chanels="+list;
        Log.e("url", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response",jsonArray.toString());
                /*if(progressDialog!=null)
                    progressDialog.dismiss();
                */
                progressBar.setVisibility(View.GONE);
                chanels =new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        chanels.add(new Chanel(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                categoryAdapterChanels = new PopupListAdapterChanels(getActivity(),chanels);
                gridView.setAdapter(categoryAdapterChanels);
                categoryAdapterChanels.notifyDataSetChanged();
                if(chanels.size()==0){
                    pop_up_layout.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(),"You have not selected any sources",Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1);
                    add_source_ll.setVisibility(View.VISIBLE);
                    no_source.setText(Session.getword(getActivity(), "error_no_channels_subscribed"));
//                    for (int j = 0; j < custom_names.size(); j++) {
//                        custom_names.get(j).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
//                        custom_names.get(j).setBackgroundResource(R.drawable.border_empty_appcolor);
//                    }
//                    //  custom_names.get(pre_selected).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
//                    // custom_names.get(pre_selected).setBackgroundResource(R.drawable.border_empty_appcolor);
//
//                    custom_names.get(pre_selected).setBackgroundResource(R.drawable.border_full_appcolor);
//                    custom_names.get(pre_selected).setTextColor(Color.parseColor("white"));
//                    selected=pre_selected;

                }else
                    pop_up_layout.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /*if(progressDialog!=null)
                    progressDialog.dismiss();
                */
                pop_up_layout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Log.e("error",volleyError.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

    private int preLast=0;
// Initialization stuff.
//    yourListView.setOnScrollListener(this);

// ... ... ...

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        
    }

    @Override
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount)
    {

        switch(lw.getId())
        {
            case R.id.listView:

                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount-4)
                {
                    if(preLast!=lastItem)
                    {
                        //to avoid multiple calls for last item
                        Log.d("Last", "Last");
                        preLast = lastItem;
                        start=news.size();
                        if(selected==0)
                            tabclicked(selected,false);
                        else
                            get_news("chanels=" + chanels.get(posi).ch_id, true,false);



                    }
                }
        }
    }

    final Handler h = new Handler();
    final int delay = 1000*60; //milliseconds

    final Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.e("time","ticked");
            h.postDelayed(this, delay);
            // tabclicked(selected,true);
            get_latest_news();
        }
    };

    private void set_refresh_timer(){

        h.postDelayed(r, delay);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
          h.removeCallbacks(r);
        Log.e("timer","removed");
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        h.removeCallbacks(r);
        h.postDelayed(r,delay);
        Log.e("timer","added");
    }

    private void get_latest_news() {

        try {
            pop_up_layout.setVisibility(View.GONE);
            temp_url = url + url_appednd + "&first_id=" + news.get(0).id;

            Log.e("url", temp_url);
            // final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.setMessage("please_wait");
            //progressDialog.show();
            //progressDialog.setCancelable(false);
//        temp_url=temp_url
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(temp_url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    Log.e("response", jsonArray.toString());
              /*  if(progressDialog!=null)
                    progressDialog.dismiss();
*/
                    progressBar.setVisibility(View.GONE);
                    latest_news.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.e("json", jsonObject.toString());
                            latest_news.add(new News(jsonObject));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("lates_news",String.valueOf(latest_news.size()));
                        if(latest_news.size()>0){
                            new_feeds_btn.setText(latest_news.size()+" New Feeds");
//                            t=1;
                            new_feeds_btn_ll.setVisibility(View.VISIBLE);
                            for(int j=latest_news.size()-1;j>=0;j--){
                                try {
                                    news.add(0,latest_news.get(j));
                                }catch (Exception ex){
                                    break;
                                }
                            }
                            int t1=listView.getMaxScrollAmount();
                            int x=listView.getScrollX();
                            int scroll_posi=t1-x;
                            news_list_adapter.notifyDataSetChanged();
//                                listView.smoothScrollToPosition(0);
                            listView.setScrollX(listView.getMaxScrollAmount() - scroll_posi);
//                                swipeRefreshLayout.setRefreshing(false);
                            set_refresh_timer();
                        }

                        //news_list_adapter.notifyDataSetChanged();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
               /* if(progressDialog!=null)
                    progressDialog.dismiss();
               */
                    progressBar.setVisibility(View.GONE);
                    Log.e("error", volleyError.toString());
                    Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();

                }
            });

            AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
