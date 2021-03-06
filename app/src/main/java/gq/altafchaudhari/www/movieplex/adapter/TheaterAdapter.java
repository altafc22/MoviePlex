package gq.altafchaudhari.www.movieplex.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import gq.altafchaudhari.www.movieplex.Interfaces.CardViewOnClickListener;
import gq.altafchaudhari.www.movieplex.MyApplication;
import gq.altafchaudhari.www.movieplex.R;
import gq.altafchaudhari.www.movieplex.SelectTimingAndSeatActivity;
import gq.altafchaudhari.www.movieplex.model.Theater;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Theater> mData ;

    private CardViewOnClickListener cardViewOnClickListener;


    public TheaterAdapter(Context mContext, List<Theater> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.theater_card_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.theater_name.setText(String.valueOf(mData.get(position).theater_name));
        holder.theater_city.setText(mData.get(position).theater_city);
        holder.theater_city.setText(mData.get(position).theater_city);
        //holder.theater_image.setBackgroundResource(mData.get(position).getTheater_image());

        //String profile_image_url = mData.get(position).getTheater_image();
        //Glide.with(mContext).load(profile_image_url).into(holder.theater_image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewOnClickListener.onClick(v,position);
            }
        });

    }

    //Set method of OnItemClickListener object
    public void setOnClickListener(CardViewOnClickListener cardViewOnClickListener){
        this.cardViewOnClickListener = cardViewOnClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView theater_name,theater_city,movie_name,movie_image;
        ImageView theater_image;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            theater_name = (TextView) itemView.findViewById(R.id.theater_name);
            theater_city = (TextView) itemView.findViewById(R.id.theater_city);
            movie_name = (TextView) itemView.findViewById(R.id.movie_title);
            movie_image = (TextView) itemView.findViewById(R.id.movie_image);
            theater_image =  itemView.findViewById(R.id.theater_image);

            cardView = (CardView) itemView.findViewById(R.id.card_view);


        }
    }

}