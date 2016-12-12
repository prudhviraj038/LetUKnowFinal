package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
public class SearchFragment extends Fragment implements AbsListView.OnScrollListener {

    ListView listView;
    LinearLayout progressBar;
    ArrayList<News> newses;
    News_listAdapter news_list_adapter;
    FragmentTouchListner mCallBack;
    MyTextView label,no_source;
    ImageView back_btn;
    String temp="";
    EditText search_edit;
    String url = Session.SERVER_URL+"feeds.php?";
    int start=0,end=10;


    public interface FragmentTouchListner{
        public void back();
        public void newsclicked(News news);
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
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getView();
        newses = new ArrayList<>();
        back_btn = (ImageView) view.findViewById(R.id.search_back_btn);
        listView = (ListView) view.findViewById(R.id.listView3);
        listView.setOnScrollListener(this);
        label = (MyTextView) view.findViewById(R.id.search_title);
        no_source=(MyTextView)view.findViewById(R.id.no_result_search);
        progressBar = (LinearLayout) view.findViewById(R.id.progressBar34);
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mCallBack.newsclicked(newses.get(position));
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.back();
            }
        });
        news_list_adapter=new News_listAdapter(getActivity(),newses);
        listView.setAdapter(news_list_adapter);
         search_edit = (EditText) view.findViewById(R.id.et_search);
        search_edit.setHint(Session.getword(getActivity(), "empty_search"));
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() > 2)
                        temp=s.toString();
                        get_news("key=" + URLEncoder.encode(s.toString(), "utf-8"), true);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    String temp_url = "";
    private void get_news(String url_append,boolean clear_data){
        progressBar.setVisibility(View.VISIBLE);
        if(clear_data) {
            newses.clear();
            start = newses.size();
            Log.e("start1", String.valueOf(start));
        }else {
            start = newses.size();
            Log.e("start2",String.valueOf(start));
        }
            temp_url = url + url_append+"&start=" + start + "&count=" + end;
        Log.e("url", temp_url);
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
                        newses.add(new News(jsonObject));

                    } catch (JSONException e) {
                        news_list_adapter.notifyDataSetChanged();
                        e.printStackTrace();
                    }

                    if(newses.size() == 0 ) {
                        no_source.setText(Session.getword(getActivity(), "no_feeds"));
                    }

                }
                if(start==0) {

                    //  news_list_adapter = new News_listAdapter(getActivity(), news);
                    listView.setAdapter(news_list_adapter);
                    news_list_adapter.notifyDataSetChanged();
                }else{
                    news_list_adapter.notifyDataSetChanged();
                    listView.setSelection(start);
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
//                Toast.makeText(getActivity(),volleyError.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    private int preLast;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        //to avoid multiple calls for last item
                        Log.d("Last", "Last");
                        preLast = lastItem;
                        start=newses.size();
                        get_news("key=" + temp,false);
                    }
                }

    }
}
