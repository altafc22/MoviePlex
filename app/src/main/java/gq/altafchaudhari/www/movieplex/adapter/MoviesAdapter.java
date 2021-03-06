package gq.altafchaudhari.www.movieplex.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gq.altafchaudhari.www.movieplex.MovieDetailsActivity;
import gq.altafchaudhari.www.movieplex.R;
import gq.altafchaudhari.www.movieplex.model.Movie;
import gq.altafchaudhari.www.movieplex.model.MoviesResponse;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> movieList;
    String genereation = "";
    public MoviesAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_small_card_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder viewHolder, int i){

        String rDate = movieList.get(i).getReleaseDate();
        String releaseDate="";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = format.parse(rDate);
            SimpleDateFormat your_format = new SimpleDateFormat("dd/MM/yyyy");
            releaseDate = your_format.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.title.setText(movieList.get(i).getTitle());
        viewHolder.releaseDate.setText(releaseDate);
        try{
            int gen = 0;
            if(movieList.get(i).getGenereIds().size()!=0){
                gen = movieList.get(i).getGenereIds().get(0);
                System.out.println("genre........"+getGenOneByOne(gen));
                viewHolder.tv_genre.setText(getGenOneByOne(gen));
            }
            else
            {
                viewHolder.tv_genre.setText("N/A");
            }

        }catch (ArrayIndexOutOfBoundsException ex)
        {
            viewHolder.tv_genre.setText("Drama");
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        requestOptions.error(R.drawable.ic_broken_image_black_24dp);

        String poster_path = "http://image.tmdb.org/t/p/w185"+movieList.get(i).getPosterPath();

        Glide.with(mContext)
                .load(poster_path)
                .apply(RequestOptions.placeholderOf(R.drawable.loading).error(R.drawable.loading))
                .into(viewHolder.thumbnail);

    }

    @Override
    public int getItemCount()
    {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,releaseDate,tv_genre;
        public ImageView thumbnail;
        CardView small_card;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            releaseDate = itemView.findViewById(R.id.releasedate);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            tv_genre = itemView.findViewById(R.id.genre);
            small_card = itemView.findViewById(R.id.small_card);

            small_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION)
                    {
                        Movie clickDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                        intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                        intent.putExtra("title",movieList.get(pos).getTitle());
                        intent.putExtra("adult",movieList.get(pos).getPosterPath());
                        intent.putExtra("overview",movieList.get(pos).getOverview());
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        List<Integer> genre_list = movieList.get(pos).getGenereIds();
                        for (int i=0;i<genre_list.size();i++)
                        {
                            String new_gen =  getGenOneByOne(movieList.get(pos).getGenereIds().get(i));
                            if(new_gen != "" && new_gen.length() > 0) {
                                genereation += "| " + new_gen+" ";

                            }
                            //else
                             //   genereation = "";
                        }

                        System.out.println("dasasd "+genereation);
                        intent.putExtra("genre",genereation);
                        intent.putExtra("popularity",movieList.get(pos).getPopularity());
                        intent.putExtra("popularity",movieList.get(pos).getVoteAverage());
                        intent.putExtra("video_url",movieList.get(pos).getVideo());
                        intent.putExtra("vote_count",movieList.get(pos).getVoteCount());
                        intent.putExtra("vote_average",Double.toString(movieList.get(pos).getVoteAverage()));
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        genereation = "";
                    }
                }
            });
        }
    }



    public String getGenOneByOne(int id){
        String jsonData;
        String genre = "";

        try {
            InputStream inputStream = mContext.getAssets().open("generes.json");

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            jsonData = new String(buffer, "UTF-8");
            //JSONArray genereArray = new JSONArray(jsonData);
            //System.out.println(genereArray);
            JSONObject rawObj = new JSONObject(jsonData);
            //System.out.println(rawObj);
            JSONArray genreArray = rawObj.getJSONArray("genres");

                for(int j =0;j<genreArray.length();j++)
                {
                    JSONObject genObj = genreArray.getJSONObject(j);
                    int gen_id = genObj.getInt("id");
                    String genre_type = genObj.getString("name");
                    if(gen_id == id)
                    {
                        genre = genre_type;
                        break;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(genre!=null)
            return genre;
        else
            return "";
    }

}
