package a06.csie.cjcu.rssreaderui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText RSS_Edit; //輸入網址
    private Button RSS_Send;
    private Button RSS_Clear;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList myTitle = new ArrayList();
    private ArrayList myLink = new ArrayList();
    private ArrayList mycategory = new ArrayList();
    private List<HashMap<String , String>> list = new ArrayList<>();
    private String title;
    private String link;
    private String pubDate;
    private TextView RSS_Display; //結果測試
    private RequestQueue resquextQueue;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        RSS_Edit = (EditText)findViewById(R.id.RSS_Edit);
        RSS_Send = (Button)findViewById(R.id.RSS_Sent);
        RSS_Clear = (Button)findViewById(R.id.btn_clear);

        resquextQueue = Volley.newRequestQueue(this);


        RSS_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTitle.clear();
                myLink.clear();
                listView.setAdapter(null);
            }
        });

        RSS_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = RSS_Edit.getText().toString(); //網址輸入

                url = "http://192.168.130.2:8080/mypro3/mypro3Servlet?param="+content;
                Log.d("cylog", "不好");

                final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {

                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Info = response.getJSONObject(i);
                                title = Info.getString("title");
                                link = Info.getString("link");
                                pubDate = Info.getString("pubDate");
                                myTitle.add("文章 : " + title + "　日期 : " + pubDate);
                                myLink.add(link);

                                Log.d("cylog", title);
                                Log.d("cylog", pubDate);
                                Log.d("cylog", link);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }

                );
                resquextQueue.add(jsonObjectRequest);
                listView.setAdapter(listAdapter);
            }//按鈕到這1
        });//按鈕到這2

        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myTitle);

        Log.d("cylog", "你好");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 連結網頁
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object list[] = myLink.toArray();
                String[] stringArray = Arrays.copyOf(list, list.length, String[].class);
                String url = stringArray[position];
                Intent intent = new Intent();  //切網頁
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
            }


    }



