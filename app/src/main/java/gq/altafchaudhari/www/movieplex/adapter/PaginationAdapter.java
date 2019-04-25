package gq.altafchaudhari.www.movieplex.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gq.altafchaudhari.www.movieplex.MovieDetailsActivity;
import gq.altafchaudhari.www.movieplex.R;
import gq.altafchaudhari.www.movieplex.model.Movie;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Movie> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    String genereation = "";

    public List<Movie> getMovies() {
        return movieResults;
    }

    public void setMovies(List<Movie> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.movie_card_item, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Movie result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                String rDate = movieResults.get(position).getReleaseDate();
                String releaseDate="";
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date dt = format.parse(rDate);
                    SimpleDateFormat your_format = new SimpleDateFormat("dd/MM/yyyy");
                    releaseDate = your_format.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                movieVH.title.setText(movieResults.get(position).getTitle());
                movieVH.releaseDate.setText(releaseDate);
                try{
                    int gen = 0;
                    if(movieResults.get(position).getGenereIds().size()!=0){
                        gen = movieResults.get(position).getGenereIds().get(0);
                        System.out.println("genre........"+getGenOneByOne(gen));
                        movieVH.tv_genre.setText(getGenOneByOne(gen));
                    }
                    else
                    {
                        movieVH.tv_genre.setText("N/A");
                    }

                }catch (ArrayIndexOutOfBoundsException ex)
                {
                    movieVH.tv_genre.setText("Drama");
                }

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.loading);
                requestOptions.error(R.drawable.ic_broken_image_black_24dp);

                String poster_path = "http://image.tmdb.org/t/p/w185"+movieResults.get(position).getPosterPath();

                Glide.with(context)
                        .load(poster_path)
                        .apply(RequestOptions.placeholderOf(R.drawable.loading).error(R.drawable.loading))
                        .into(movieVH.thumbnail);

                break;

            case LOADING:
//                Do nothing
                break;


        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Movie r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void remove(Movie r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movieResults.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        public TextView title,releaseDate,tv_genre;
        public ImageView thumbnail;
        CardView small_card;

        public MovieVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            releaseDate = itemView.findViewById(R.id.releasedate);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            tv_genre = itemView.findViewById(R.id.genre);
            small_card = itemView.findViewById(R.id.movie_card_view);

           small_card.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   int pos = getAdapterPosition();
                   if(pos!=RecyclerView.NO_POSITION)
                   {
                       Movie clickDataItem = movieResults.get(pos);
                       Intent intent = new Intent(context, MovieDetailsActivity.class);
                       intent.putExtra("poster_path",movieResults.get(pos).getPosterPath());
                       intent.putExtra("title",movieResults.get(pos).getTitle());
                       intent.putExtra("adult",movieResults.get(pos).getPosterPath());
                       intent.putExtra("overview",movieResults.get(pos).getOverview());
                       intent.putExtra("release_date",movieResults.get(pos).getReleaseDate());
                       List<Integer> genre_list = movieResults.get(pos).getGenereIds();
                       for (int i=0;i<genre_list.size();i++)
                       {
                           String new_gen =  getGenOneByOne(movieResults.get(pos).getGenereIds().get(i));
                           if(new_gen != "" && new_gen.length() > 0) {
                               genereation += "| " + new_gen+" ";

                           }
                           //else
                           //   genereation = "";
                       }

                       System.out.println("dasasd "+genereation);
                       intent.putExtra("genre",genereation);
                       intent.putExtra("popularity",movieResults.get(pos).getPopularity());
                       intent.putExtra("popularity",movieResults.get(pos).getVoteAverage());
                       intent.putExtra("video_url",movieResults.get(pos).getVideo());
                       intent.putExtra("vote_count",movieResults.get(pos).getVoteCount());
                       intent.putExtra("vote_average",Double.toString(movieResults.get(pos).getVoteAverage()));
                       //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);
                       genereation = "";
                   }

               }
           });
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    public String getGenOneByOne(int id){
        String jsonData;
        String genre = "";

        try {
            InputStream inputStream = context.getAssets().open("generes.json");

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