package com.example.bookreviewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ItemViewHolder>{
    private ArrayList<Bookmark> listData = new ArrayList<>();
    private Context context;

    private MySQLiteOpenHelper helper;
    String dbName = "st_file.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_viewholder, parent, false);
        this.context = parent.getContext();

        //asdf
        helper = new MySQLiteOpenHelper(
                context,  // 현재 화면의 제어권자
                dbName,// db 이름
                null,  // 커서팩토리-null : 표준커서가 사용됨
                dbVersion);       // 버전
        try {
            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(tag, "데이터베이스를 얻어올 수 없음");
        }
        //

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position), holder);
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Bookmark data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView booktitle;
        private TextView bookAuthor;
        private ImageView bookImage;
        private Bookmark data;
        private ImageButton bookremove;

        ItemViewHolder(View itemView) {
            super(itemView);

            booktitle = itemView.findViewById(R.id.bookmark_title);
            bookAuthor = itemView.findViewById(R.id.bookmark_Author);
            bookImage = itemView.findViewById(R.id.bookmark_Image);
            bookremove = itemView.findViewById(R.id.remove_shop);
        }
        void onBind(Bookmark data, ItemViewHolder holder) {
            this.data = data;
            booktitle.setText(data.getTitle());
            bookAuthor.setText(data.getAuthor());
            GlideApp.with(holder.itemView).load(data.getImage())
                    .override(300,400)
                    .into(holder.bookImage);

            itemView.setOnClickListener(this);
            booktitle.setOnClickListener(this);
            bookAuthor.setOnClickListener(this);
            bookImage.setOnClickListener(this);
            bookremove.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
//            Intent intent= new Intent(context,SearchResultActivity.class);
//            intent.putExtra("book_title",data.getTitle());
//            intent.putExtra("book_author",data.getAuthor());
//            intent.putExtra("book_image",data.getImage());
            Cursor c;

            switch (v.getId()) {
                case R.id.remove_shop:
                    //Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
                    c = db.rawQuery("select * from bookmark;", null);
                    db.delete("bookmark", null, null);
                    while (c.moveToNext()) {
                        Log.e(tag, "hello" + c.getString(1));
                        if(data.getTitle() == c.getString(1)) {
                            //Toast.makeText(context, "TITLE : " + data.getTitle() + "\nContent : " + data.getAuthor(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
                            db.delete("bookmark", "title" + "=" + data.getTitle(), null);
                        }
                    }
//                case R.id.bookmark_Item:
//                    //context.startActivity(intent);
//                    c = db.rawQuery("select * from bookmark;", null);
//                    db.delete("bookmark", null, null);
//                    while (c.moveToNext()) {
//                        if(data.getTitle() == c.getString(1)) {
//                            //Toast.makeText(context, "TITLE : " + data.getTitle() + "\nContent : " + data.getAuthor(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
//                            db.delete("bookmark", "title" + "=" + data.getTitle(), null);
//                        }
//                    }

//                case R.id.bookmark_title:
//                    //context.startActivity(intent);
//                    //Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.bookmark_Author:
//                    //context.startActivity(intent);
//                    //Toast.makeText(context, data.getAuthor(), Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.bookmark_Image:
//                    //context.startActivity(intent);
//                    //Toast.makeText(context, data.getTitle() + " 이미지 입니다.", Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    }
}
