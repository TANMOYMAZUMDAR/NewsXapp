package com.example.newsx;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNewsSourceDetails extends RecyclerView.Adapter<AdapterNewsSourceDetails.HolderNewsSourceDetails> {

    private Context context;
    private ArrayList<ModeNewsSourceDetails> newsSourceDetailsArrayList;

    public AdapterNewsSourceDetails(Context context, ArrayList<ModeNewsSourceDetails> newsSourceDetailsArrayList) {
        this.context = context;
        this.newsSourceDetailsArrayList = newsSourceDetailsArrayList;
    }

    @NonNull
    @Override
    public HolderNewsSourceDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_news_source_details,parent,false);
        return new HolderNewsSourceDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNewsSourceDetails holder, int position) {
       ModeNewsSourceDetails model=newsSourceDetailsArrayList.get(position);
       String content=model.getContent();
       String description=model.getDescription();
       String publishedAt=model.getPublishedAt();
       String title=model.getTitle();
       final String url=model.getUrl();
       String urlToImage=model.getUriToImage();

       holder.titleTv.setText(title);
       holder.descriptionTv.setText(description);
       holder.dataTv.setText(publishedAt);
        Picasso.get().load(urlToImage).into(holder.imageTv);

        //handle click,view complete details of news
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,NewsDetailsActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsSourceDetailsArrayList.size();
    }

    class HolderNewsSourceDetails extends RecyclerView.ViewHolder{

        TextView titleTv,descriptionTv,dataTv;
        ImageView imageTv;
        public HolderNewsSourceDetails(@NonNull View itemView) {
            super(itemView);

            titleTv=itemView.findViewById(R.id.titleTv);
            descriptionTv=itemView.findViewById(R.id.descriptionTv);
            dataTv=itemView.findViewById(R.id.dateTv);
            imageTv=itemView.findViewById(R.id.imageTv);
        }
    }
}
