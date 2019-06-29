package com.example.bookreviewer;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout llBottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    ImageButton login_btn;
    EditText searchView;
    RecyclerView recyclerView,recyclerView_yes24,recyclerView_aladin,
                 recyClerView_interpark, recyClerView_bandi;
    CircularImageView user_profile;
    String keyword;
    ListView listView;
    ArrayList<String> arraylist = new ArrayList<>();
    ArrayList<String> linklist = new ArrayList<>();
    TextView textView;

    ImageButton clear_btn;
    //tag start
    ListView tagView;
    String[] taglist={"","","","",""};
    //tag end

    //private MainRecommendAdapter adapter;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llBottomSheet = findViewById(R.id.bottom_sheet);
        searchView = findViewById(R.id.search);
        listView = findViewById(R.id.searchView);
        listView.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recommend_recycler);
        recyclerView_yes24 = findViewById(R.id.recommend_recycler_yes24);
        recyclerView_aladin = findViewById(R.id.recommend_recycler_aladin);
        recyClerView_interpark = findViewById(R.id.recommend_recycler_interpark);
        recyClerView_bandi = findViewById(R.id.recommend_recycler_bandi);
        textView = findViewById(R.id.main_bookmark);
        textView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_5 = new Intent(MainActivity.this,BookmarkActivity.class);
                startActivity(intent_5);
            }
        });

        final ScrollView scrollview = findViewById(R.id.scrollview);


        clear_btn = findViewById(R.id.clear_button);
        clear_btn.setVisibility(View.GONE);

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
            }
        });
        // tag start


        String[] names = {"저자","목차","출판사","isbn"};
        ArrayList<Item> item_list = new ArrayList<>();
        for(int i=0;i<4;++i)
            item_list.add(new Item(names[i]));
        tagView = findViewById((R.id.tagView));
        ImageButton tagbutton = findViewById(R.id.tag);
        tagView.setVisibility(View.GONE);
        final ArrayAdapter tagadapter = new ItemAdapter(this, R.layout.list_tag,  item_list);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist);
        tagView.setAdapter(tagadapter);
        listView.setAdapter(adapter);
        tagbutton.setOnClickListener(new View.OnClickListener() {
            int c = 1;
            @Override
            public void onClick(View v) {
                tagView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                for(int i=0;i<4;++i) {
                    Item titem = ((ItemAdapter) tagadapter).getItem(i);
                    taglist[i] = titem.mValue;
                }
                scrollview.setVisibility(View.GONE);
                c++;
                if(c%2==0) {
                    listView.setVisibility(View.VISIBLE);
                    if(listView.getAdapter().isEmpty())
                        scrollview.setVisibility(View.VISIBLE);
                    tagView.setVisibility(View.GONE);
                    search();
                    return;
                }
            }
        });

        //tag end
        new Description().execute();
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setPeekHeight(150);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        login_btn = findViewById(R.id.login_button);
        login_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent_1, 3000);
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                    clear_btn.setVisibility(View.VISIBLE);
                else
                    clear_btn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = searchView.getText().toString();
                if (keyword.length() == 0) {
                    listView.setVisibility(View.GONE);
                    tagView.setVisibility(View.GONE);
                    llBottomSheet.setVisibility(View.VISIBLE);
                    scrollview.setVisibility(View.VISIBLE);
                }
                else {
                    listView.setVisibility(View.VISIBLE);
                    tagView.setVisibility(View.GONE);
                    llBottomSheet.setVisibility(View.GONE);
                    scrollview.setVisibility(View.GONE);
                    search();
                }
            }
        });






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = (linklist.get(position));
                Intent intent_2 = new Intent(MainActivity.this, SearchResultActivity.class);
                intent_2.putExtra("naverlink", link);
                startActivity(intent_2);
            }
        });
        //splash

        user_profile = findViewById(R.id.user_profile1);

        //splash
        Intent intent_4 = new Intent(this, LoadingActivity.class);
        startActivity(intent_4);
    }
        //프로필 사진 띄우기
        Thread thread_naver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str = getNaverSearch(keyword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    //tag start
    private class Item {
        public String mName, mValue;
        public TextWatcher mTextWatcher;

        public Item(String name) {
            mName = name;
            mValue = "";

            //EditText 변경 리스너 생성
            mTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //변경된 값을 저장한다
                }

                @Override
                public void afterTextChanged(Editable s) {

                    mValue = s.toString();
                }
            };
        }
    }

    private class ItemAdapter extends ArrayAdapter<Item> {

        private int mResource;

        public ItemAdapter(Context context, int resource, ArrayList<Item> list) {
            super(context, resource, list);
            mResource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout itemView;

            if (convertView == null) {
                itemView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(mResource, itemView, true);
            }
            else
                itemView = (LinearLayout) convertView;
            Item item = getItem(position);
            ((TextView)itemView.findViewById(R.id.NameTextView)).setText(item.mName);
            EditText editText = (EditText)itemView.findViewById(R.id.ValueEditText);
            //예전 리스너를 삭제한다
            clearTextChangedListener(editText);

            //값을 불려오고 해당 리스너를 적용한다
            editText.setText(item.mValue);
            editText.addTextChangedListener(item.mTextWatcher);

            return itemView;
        }

        private void clearTextChangedListener(EditText editText) {
            //리스트 목록의 모든 리스너를 대상으로 검사하여 삭제해 준다
            int count = getCount();
            for (int i = 0 ; i < count ; i++)
                editText.removeTextChangedListener(getItem(i).mTextWatcher);
        }

    }
    //tag end

    public void search() {
        arraylist = new ArrayList<>();
        linklist = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist);
        listView.setAdapter(adapter);
        if (thread_naver.getState() == Thread.State.NEW)
            thread_naver.start();
        thread_naver.run();
        adapter.notifyDataSetChanged();
    }



    private ArrayList<ItemObject> list = new ArrayList<>();
    private ArrayList<ItemObject> list_yes24 = new ArrayList<>();
    private ArrayList<ItemObject> list_aladin = new ArrayList<>();
    private ArrayList<ItemObject> list_interpark = new ArrayList<>();
    private ArrayList<ItemObject> list_bandi = new ArrayList<>();

    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;
        //@InjectView(R.id.elastic_download_view)
        //ElasticDownloadView mElasticDownloadView;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //mElasticDownloadView.startIntro();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("정보를 수집하는 중입니다.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params){
            try {
                Document doc = Jsoup.connect("http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?orderClick=d79").userAgent("google robot").get();
                Elements mElementData = doc.select("ul[class=list_type01]").select("li");
                int mElementsize = mElementData.size(); //목록의 갯수

                for (Element elem : mElementData) {
                    //li 에서 다시 원하는 데이터를 추출해 낸다.
                    String img_kyobo = elem.select("div[class=cover] a img").attr("src");
                    String title_kyobo = elem.select("div[class=title] a strong").text();
                    String author_kyobo = elem.select("div[class=author]").text();
                    author_kyobo = author_kyobo.split("\\|")[0];
                    String url_kyobo = elem.select("div[class=title] a").attr("href");
                    String siteName_kyobo = "교보문고";
                    String star_kyobo = elem.select("div[class=review] img").attr("alt");
                    if(star_kyobo.equals("5점 만점에 5점")){
                        star_kyobo = "10";
                        Log.e("스타교보",star_kyobo);
                    }
                    else if(star_kyobo.equals("5점 만점에 4점")){
                        star_kyobo = "8";
                    }
                    else if(star_kyobo.equals("5점 만점에 3점")){
                        star_kyobo = "6";
                    }
                    else if(star_kyobo.equals("5점 만점에 2점")){
                        star_kyobo = "4";
                    }
                    else if(star_kyobo.equals("5점 만점에 1점")){
                        star_kyobo = "2";
                    }
                    else if(star_kyobo.equals("5점 만점에 0점")){
                        star_kyobo = "0";
                    }
                    else star_kyobo = "0";
                    String price_kyobo = elem.select("div[class=price] strong[class=book_price]").text();
                    list.add(new ItemObject(img_kyobo, url_kyobo, author_kyobo,
                            Float.parseFloat(star_kyobo),
                            title_kyobo, price_kyobo, siteName_kyobo));
                }

                Document doc_yes24 = Jsoup.connect("http://www.yes24.com/24/category/bestseller?CategoryNumber=001&sumgb=09").get();
                Elements mElementData_yes24 = doc_yes24.select("table#category_layout tbody").select("tr");
                for(Element elem : mElementData_yes24){
                    String img_yes24 = elem.select("div[class=goodsImgW] a img").attr("src");
                    String title_yes24 = elem.select("td[class=goodsTxtInfo] p a").text();
                    String siteName_yes24 = "yes 24";
                    String author_yes24 = elem.select("div[class=aupu] a").text();
                    Float star_yes24 = Float.parseFloat("5.0");
                    String price = elem.select("span[class=priceB]").text();
                    String url_yes24 = elem.select("td[class=goodsTxtInfo] p a").attr("href");
                    url_yes24 = "http://www.yes24.com" + url_yes24;

                    list_yes24.add(new ItemObject(img_yes24, url_yes24, author_yes24, star_yes24, title_yes24, price, siteName_yes24));
                }

                Document doc_aladin = Jsoup.connect("https://www.aladin.co.kr/shop/common/wbest.aspx?BranchType=1").get();
                Elements mElementData_aladin = doc_aladin.select("form#Myform").select("div[class=ss_book_box]");
                for(Element elem : mElementData_aladin){
                    String img_aladin = elem.select("div[style=position:relative;] a img").attr("src");
                    String title_aladin = elem.select("a[class=bo3] b").text();
                    String siteName_aladin = "알라딘";
                    String url_aladin = elem.select("a[class=bo3]").attr("href");
                    Float star_aladin = Float.parseFloat("5.0");
                    String price = elem.select("span[class=ss_p2] b span").text();
                    String author_aladin = elem.select("div[class=ss_book_list] ul li").next().next().text();
                    author_aladin = author_aladin.split("\\|")[0];

                    list_aladin.add(new ItemObject(img_aladin, url_aladin, author_aladin, star_aladin, title_aladin, price, siteName_aladin));
                }

                Document doc_interpark = Jsoup.connect("http://book.interpark.com/display/collectlist.do?_method=BestsellerHourNew201605&bestTp=1&dispNo=028").get();
                Elements mElementData_interpark = doc_interpark.select("div[class=rankBestContentList] ol li");
                for(Element elem : mElementData_interpark){
                    String img_interpark = elem.select("div[class=coverImage] label a img").attr("src");
                    String title_interpark = elem.select("div[class=itemName]").text();
                    String siteName_interpark = "interpark";
                    String url_interpark = elem.select("a[target=_blank]").attr("href");
                    url_interpark = "http://book.interpark.com" + url_interpark;
                    String star_interpark = elem.select("span[clss=starRateWrap] span").text();
                    if(star_interpark == null || star_interpark.equals("")){
                        star_interpark = "0";
                    }
                    String price_interpark = elem.select("div[class=priceWrap] span[class=price] em").text();
                    String author_interpark  = elem.select("div[class=itemMeta] span[class=author]").text();

                    list_interpark.add(new ItemObject(img_interpark, url_interpark, author_interpark, Float.parseFloat(star_interpark), title_interpark, price_interpark, siteName_interpark));
                }

                Document doc_bandi = Jsoup.connect("http://www.bandinlunis.com/front/display/listBest.do").get();
                Elements mElementData_bandi = doc_bandi.select("div[class=prod_list_type prod_best_type] ul").select("li");
                for(Element elem : mElementData_bandi){
                    String img_bandi = elem.select("div[class=prod_thumb_img] a img").attr("src");
                    String title_bandi = elem.select("dl[class=prod_info] dt a").text();
                    String url_bandi = elem.select("dl[class=prod_info] dt a").attr("href");
                    url_bandi = "http://www.bandinlunis.com" + url_bandi;
                    String siteName_bandi = "BANDI N LUNIS";
                    String star_bandi = elem.select("dd[class=txt_desc] strong").text();
                    String price_bandi = elem.select("dd[class=mt5] p span[class=txt_price] strong em").text();
                    String author_bandi = elem.select("dd[class=txt_block]").text();
                    author_bandi = author_bandi.split("\\|")[0];

                    list_bandi.add(new ItemObject(img_bandi, url_bandi, author_bandi, Float.parseFloat(star_bandi), title_bandi, price_bandi, siteName_bandi));
                }

                Log.d("debug :", "List " + mElementData);
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //어댑터와 연결
            MainAct_Adapter myAdapter = new MainAct_Adapter(list);
            MainAct_Adapter myAdapter_yes24 = new MainAct_Adapter(list_yes24);
            MainAct_Adapter myAdapter_aladin = new MainAct_Adapter(list_aladin);
            MainAct_Adapter myAdapter_interpark = new MainAct_Adapter(list_interpark);
            MainAct_Adapter myAdapter_bandi = new MainAct_Adapter(list_bandi);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager layoutManager_yes24 = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager layoutManager_aladin = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager layoutManager_interpark = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager layoutManager_bandi = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
            recyclerView_yes24.setLayoutManager(layoutManager_yes24);
            recyclerView_yes24.setAdapter(myAdapter_yes24);
            recyclerView_aladin.setLayoutManager(layoutManager_aladin);
            recyclerView_aladin.setAdapter(myAdapter_aladin);
            recyClerView_interpark.setLayoutManager(layoutManager_interpark);
            recyClerView_interpark.setAdapter(myAdapter_interpark);
            recyClerView_bandi.setLayoutManager(layoutManager_bandi);
            recyClerView_bandi.setAdapter(myAdapter_bandi);
            ((LinearLayoutManager)layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
            ((LinearLayoutManager)layoutManager_yes24).setOrientation(LinearLayoutManager.HORIZONTAL);
            ((LinearLayoutManager)layoutManager_aladin).setOrientation(LinearLayoutManager.HORIZONTAL);
            ((LinearLayoutManager)layoutManager_interpark).setOrientation(LinearLayoutManager.HORIZONTAL);
            ((LinearLayoutManager)layoutManager_bandi).setOrientation(LinearLayoutManager.HORIZONTAL);
            //mElasticDownloadView.success ();
            progressDialog.dismiss();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3000)
        {
            Toast.makeText(MainActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK) {
            final String geturi = data.getStringExtra("result");
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(geturi);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (MalformedURLException ee) {
                        ee.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();
            try {
                mThread.join();
                user_profile.setImageBitmap(bitmap);
            } catch (
                    InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public String getNaverSearch(String keyword) {
        String clientID = "Sr5TKDOqyCbl51P5Yy_E";
        String clientSecret = "_nD64QTiiq";
        StringBuffer sb = new StringBuffer();
        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?" +
                    "&d_titl="  + text + "&d_auth=" +taglist[0] + "&d_cont=" + taglist[1] +
                    "&d_publ=" + taglist[2] + "&d_isbn=" + taglist[3] + "&display=10" + "&start=1";
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




    /*
    private void getData() {
        List<Integer> listResId = Arrays.asList(

        );
        for (int i = 0; i < listResId.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            BookData data = new BookData();
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
    */