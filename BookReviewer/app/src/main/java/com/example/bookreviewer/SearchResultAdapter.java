package com.example.bookreviewer;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<itemObject_detail> mList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private RatingBar RatingBar_star;
        private TextView textView_title, textView_author, textView_review, textView_price, textView_site;
        private final View mView;
        private itemObject_detail item;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageView_img = (ImageView) itemView.findViewById(R.id.result_bookImage);
            textView_title = (TextView) itemView.findViewById(R.id.result_booktitle);
            textView_author = (TextView) itemView.findViewById(R.id.result_bookAuthor);
            RatingBar_star = (RatingBar) itemView.findViewById(R.id.result_bookstar);
            textView_review = (TextView) itemView.findViewById(R.id.result_bookreview);
            textView_price = (TextView) itemView.findViewById(R.id.result_bookprice);
            textView_site = (TextView) itemView.findViewById(R.id.site);
        }
    }

    //생성자
    public SearchResultAdapter(ArrayList<itemObject_detail> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchresult_viewholder, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, final int position) {

        holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
        holder.textView_author.setText(String.valueOf(mList.get(position).getAuthor()));
        GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                .override(300,400)
                .into(holder.imageView_img);
        holder.RatingBar_star.setRating(Float.valueOf(mList.get(position).getStar()));
        holder.textView_site.setText(String.valueOf(mList.get(position).getSite_name()));
        holder.textView_price.setText(String.valueOf(mList.get(position).getPrice()));
        holder.textView_review.setText(String.valueOf(mList.get(position).getReview()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context,InformationActivity.class);
                intent.putExtra("URL",mList.get(position).getUrl());
                intent.putExtra("siteName",mList.get(position).getSite_name());
                intent.putExtra("coverImage",mList.get(position).getImg_url());
                intent.putExtra("author",mList.get(position).getAuthor());
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("star",mList.get(position).getStar());
                intent.putExtra("price",mList.get(position).getPrice());
                intent.putExtra("code",1);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}