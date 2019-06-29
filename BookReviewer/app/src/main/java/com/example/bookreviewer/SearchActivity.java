package com.example.bookreviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    String keyword;
    EditText editText;
    ListView listView;
    ArrayList<String> arraylist = new ArrayList<>();
    ArrayList<String> linklist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        editText = (EditText) findViewById(R.id.searchText);
        listView = (ListView) findViewById(R.id.searchView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }
            @Override
            public void afterTextChanged(Editable s) {
                keyword = editText.getText().toString();
                search(keyword);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = (linklist.get(position));
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class );
                intent.putExtra("naverlink",link);
                startActivity(intent);
            }
        });
    }

    public void search(String charText) {
        arraylist = new ArrayList<>();
        linklist = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist);
        listView.setAdapter(adapter);
        if(thread.getState() == Thread.State.NEW)
            thread.start();
        thread.run();
        adapter.notifyDataSetChanged();
    }

    Thread thread = new Thread(new Runnable(){
        @Override
        public void run() {
            try {
                String str = getNaverSearch(keyword);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public String getNaverSearch(String keyword) {
        String clientID = "Sr5TKDOqyCbl51P5Yy_E";
        String clientSecret = "_nD64QTiiq";
        StringBuffer sb = new StringBuffer();
        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text + "&display=10" + "&start=1";
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;
            //inputStream으로부터 xml값 받기
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            int eventType = xpp.getEventType();
            int count = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기
                        if (tag.equals("item")) ; //첫번째 검색 결과
                        else if (tag.equals("title")){
                            if(count == 0){
                                count++;
                                break;
                            }
                            sb.append("제목 : ");
                            xpp.next();
                            arraylist.add(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            //sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            //sb.append("\n");
                        } else if (tag.equals("link")) {
                            if(count == 1){
                                count++;
                                break;
                            }
                            sb.append("link : ");
                            xpp.next();
                            linklist.add(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } /*else if (tag.equals("publisher")) {
                            sb.append("출판사 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        }
                        */
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }
}