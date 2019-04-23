package gq.altafchaudhari.www.movieplex;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gq.altafchaudhari.www.movieplex.adapter.MoreMoviesAdapter;
import gq.altafchaudhari.www.movieplex.adapter.MoviesAdapter;
import gq.altafchaudhari.www.movieplex.api.Client;
import gq.altafchaudhari.www.movieplex.api.Service;
import gq.altafchaudhari.www.movieplex.model.Movie;
import gq.altafchaudhari.www.movieplex.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMoreMoviesActivity extends AppCompatActivity {

    TextView action_bar_title;
    String type;
    int pages;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    ProgressBar main_progress;

    private MoreMoviesAdapter adapter;
    private List<Movie> movieList;
    boolean isLoading = false;
    int current_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more_movies);

        initViews();

        Intent intent = getIntent();

        if (intent.hasExtra("movie_type") && intent.hasExtra("pages")) {
            type = getIntent().getExtras().getString("movie_type");
            pages = getIntent().getExtras().getInt("pages");

            if (type.equals("released"))
            {
                action_bar_title.setText("New Released Movies");
            }
            if (type.equals("upcoming"))
            {
                action_bar_title.setText("Upcoming Movies");
            }
            getMovies(type,1);
            initAdapter();
            initScrollListener();
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                //Toast.makeText(getActivity(),"Refreshed",Toast.LENGTH_SHORT).show();
                getMovies(type,1);
            }
        });
    }


    private void initViews() {
        recyclerView = findViewById(R.id.main_recycler);
        swipeContainer = findViewById(R.id.swipe_container);
        action_bar_title = findViewById(R.id.activity_title);
        main_progress = findViewById(R.id.main_progress);

    }

    private void initAdapter() {
        movieList = new ArrayList<>();

        adapter = new MoreMoviesAdapter(ShowMoreMoviesActivity.this,movieList);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("Scrolingggggggg ");
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == movieList.size() - 1) {
                        //bottom of list!

                        if(current_page<=pages) {
                            getMovies(type, current_page);
                            current_page++;
                            isLoading = true;
                        }
                        else
                        {
                            isLoading = false;
                        }
                    }
                }
            }
        });
    }

 /*   private void loadMore(final String movie_type, final int page) {


        movieList.add(null);
        adapter.notifyItemInserted(movieList.size() - 1);

                movieList.remove(movieList.size() - 1);
                int scrollPosition = movieList.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    //movieList.add("Item " + currentSize);
                    getMovies(movie_type,page);
                    currentSize++;
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
                current_page++;
    }
**/
    private void getMovies(String requestType, int page) {
        adapter = new MoreMoviesAdapter(ShowMoreMoviesActivity.this,movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Service apiService = Client.getClient().create(Service.class);
        Calendar cal = Calendar.getInstance();
        Date current_date = cal.getTime();
        String current_formatted_date = new SimpleDateFormat("yyyy-MM-dd").format(current_date);

        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -48);
        Date pre_date = cal.getTime();
        String pre_formatted_date = new SimpleDateFormat("yyyy-MM-dd").format(pre_date);
        Call<MoviesResponse> call = null;

        if (requestType.equals("released"))
            call = apiService.getNowInTheaterMoviesPage("IN", pre_formatted_date,
                    current_formatted_date,page,
                    BuildConfig.THE_MOVIE_DB_API_TOKEN);
        if (requestType.equals("upcoming")) {
            call = apiService.getUpcomingMoviesPage("IN",
                    current_formatted_date, page,
                    BuildConfig.THE_MOVIE_DB_API_TOKEN);
        }

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                System.out.println("Response : " + response);

                try {
                    List<Movie> movies = response.body().getResults();

                    ///total_released_pages = response.body().getTotalPages();
                    recyclerView.setAdapter(new MoreMoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                } catch (NullPointerException e) {
                    Log.d("Error While Fetch", e.toString());
                    //Toast.makeText(getActivity(),""+e.toString(),Toast.LENGTH_SHORT).show();
                }
                main_progress.setVisibility(View.INVISIBLE);

                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                //Toast.makeText(getActivity().getApplicationContext(),"Seems Network connection issue\nPlease Refresh Page by Swipe Down\nor Check Network Connectivity!",Toast.LENGTH_SHORT).show();
                main_progress.setVisibility(View.INVISIBLE);
                //progressDialog.dismiss();
            }
        });
    }

    public void gotoPreviousActivity (View v)
    {
        finish();
    }


}