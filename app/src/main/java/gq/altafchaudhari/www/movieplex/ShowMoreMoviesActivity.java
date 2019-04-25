package gq.altafchaudhari.www.movieplex;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gq.altafchaudhari.www.movieplex.adapter.PaginationAdapter;
import gq.altafchaudhari.www.movieplex.api.Client;
import gq.altafchaudhari.www.movieplex.api.Service;
import gq.altafchaudhari.www.movieplex.model.Movie;
import gq.altafchaudhari.www.movieplex.model.MoviesResponse;
import gq.altafchaudhari.www.movieplex.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMoreMoviesActivity extends AppCompatActivity {

    TextView action_bar_title;
    String type;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    ProgressBar main_progress;

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;

    private Service movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more_movies);

        recyclerView = findViewById(R.id.main_recycler);
        swipeContainer = findViewById(R.id.swipe_container);
        action_bar_title = findViewById(R.id.activity_title);
        main_progress = findViewById(R.id.main_progress);

        Intent intent = getIntent();

        if (intent.hasExtra("movie_type") && intent.hasExtra("pages")) {
            type = getIntent().getExtras().getString("movie_type");
            TOTAL_PAGES = getIntent().getExtras().getInt("pages");

            if (type.equals("released"))
            {
                action_bar_title.setText("New Released Movies");
            }
            if (type.equals("upcoming"))
            {
                action_bar_title.setText("Upcoming Movies");
            }
        }


        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getActivity(),"Refreshed",Toast.LENGTH_SHORT).show();
                loadFirstPage(1);
            }
        });


        adapter = new PaginationAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ShowMoreMoviesActivity.this,3);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage(currentPage);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        movieService = Client.getClient().create(Service.class);

        loadFirstPage(1);


    }


    private void loadFirstPage(int page) {
        Log.d(TAG, "loadFirstPage: ");

        callMoviesApi(type,page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                List<Movie> results = fetchResults(response);
                main_progress.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }

    /**
     * @param response extracts List<{@link Movie>} from response
     * @return
     */
    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse response1 = response.body();
        return response1.getResults();
    }


    private void loadNextPage(int page) {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callMoviesApi(type,page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Movie> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }


    private Call<MoviesResponse> callMoviesApi(String type, int page) {

        Calendar cal = Calendar.getInstance();
        Date current_date = cal.getTime();
        String current_formatted_date= new SimpleDateFormat("yyyy-MM-dd").format(current_date);

        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -48);
        Date pre_date = cal.getTime();
        String pre_formatted_date= new SimpleDateFormat("yyyy-MM-dd").format(pre_date);
        System.out.println("Old : "+pre_formatted_date);
        System.out.println("New : "+current_formatted_date);

        if(type.equals("released"))
        {
            return movieService.getNowInTheaterMoviesPage("IN",pre_formatted_date,
                    current_formatted_date,
                    page,
                    BuildConfig.THE_MOVIE_DB_API_TOKEN);
        }
        else
        {
            return movieService.getUpcomingMoviesPage("IN",
                    current_formatted_date,
                    page,
                    BuildConfig.THE_MOVIE_DB_API_TOKEN);
        }

    }

    public void gotoPreviousActivity (View v)
    {
        finish();
    }


}