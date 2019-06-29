package com.example.bookreviewer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<BookData> listData = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_viewholder, parent, false);
        this.context = parent.getContext();

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(BookData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView booktitle;
        private TextView bookAuthor;
        private ImageView bookImage;
        private BookData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            booktitle = itemView.findViewById(R.id.booktitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookImage = itemView.findViewById(R.id.bookImage);
        }

        void onBind(BookData data) {
            this.data = data;

            booktitle.setText(data.getTitle());
            bookAuthor.setText(data.getAuthor());
            bookImage.setImageResource(data.getResId());

            itemView.setOnClickListener(this);
            booktitle.setOnClickListener(this);
            bookAuthor.setOnClickListener(this);
            bookImage.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent= new Intent(context,SearchResultActivity.class);
            intent.putExtra("book_title",data.getTitle());
            intent.putExtra("book_author",data.getAuthor());
            intent.putExtra("book_price",data.getPrice());
            intent.putExtra("book_image",data.getResId());
            intent.putExtra("book_review",data.getReview());
            switch (v.getId()) {
                case R.id.linearItem:
                    Toast.makeText(context, "TITLE : " + data.getTitle() + "\nContent : " + data.getAuthor(), Toast.LENGTH_SHORT).show();
                case R.id.booktitle:
                    context.startActivity(intent);
                    Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.bookAuthor:
                    context.startActivity(intent);
                    Toast.makeText(context, data.getAuthor(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.bookImage:
                    context.startActivity(intent);
                    Toast.makeText(context, data.getTitle() + " 이미지 입니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
