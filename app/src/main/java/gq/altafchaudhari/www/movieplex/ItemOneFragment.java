package gq.altafchaudhari.www.movieplex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gq.altafchaudhari.www.movieplex.adapter.MoviesAdapter;
import gq.altafchaudhari.www.movieplex.api.Client;
import gq.altafchaudhari.www.movieplex.api.Service;
import gq.altafchaudhari.www.movieplex.model.Movie;
import gq.altafchaudhari.www.movieplex.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemOneFragment extends Fragment{

    private RecyclerView trailerRecyclerView,releaseRecyclerView,upcomingRecyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    //ProgressDialog progressDialog;
    ProgressBar new_release_progress,upcoming_progress;
    private SwipeRefreshLayout swipeContainer;
    View rootView;
    Button showMoreReleasedButton,showMoreUpcomingButton;

    int total_released_pages,total_upcoming_pages;

    public static final String LOG_TAG = MoviesAdapter.class.getName();

    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_item_one, container, false);

        initViews();

        showMoreReleasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowMoreMoviesActivity.class);
                intent.putExtra("movie_type","released");
                intent.putExtra("pages",total_released_pages);
                startActivity(intent);
            }
        });

        showMoreUpcomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowMoreMoviesActivity.class);
                intent.putExtra("movie_type","upcoming");
                intent.putExtra("pages",total_upcoming_pages);
                startActivity(intent);
            }
        });

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_one);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               initViews();
                //Toast.makeText(getActivity(),"Refreshed",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void initViews() {

        //progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Fetching movies...");
        //progressDialog.setCancelable(false);
        //progressDialog.show();


        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recycler_view);
        releaseRecyclerView = (RecyclerView) rootView.findViewById(R.id.released_recycler_view);
        upcomingRecyclerView = (RecyclerView) rootView.findViewById(R.id.upcoming_recycler_view);
        showMoreReleasedButton = rootView.findViewById(R.id.btn_sm_released);
        showMoreUpcomingButton = rootView.findViewById(R.id.btn_sm_upcoming);
        new_release_progress = rootView.findViewById(R.id.new_release_progress);
        upcoming_progress = rootView.findViewById(R.id.upcoming_progress);

        new_release_progress.setVisibility(View.VISIBLE);
        upcoming_progress.setVisibility(View.VISIBLE);


        loadMovieTrailer();
        loadMovieJSON(releaseRecyclerView,"newReleased");
        loadMovieJSON(upcomingRecyclerView,"upComing");

    }


    private void loadMovieTrailer(){
        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(getActivity().getBaseContext(),movieList);
    }

    private void loadMovieJSON(RecyclerView recievedRecyclerView,String requestType){

        final RecyclerView recyclerView = recievedRecyclerView;
        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(getActivity().getBaseContext(),movieList);

        /*if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            releaseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        }
        else
        {
            releaseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        }*/

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        try{
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty())
            {
                Toast.makeText(getActivity(),"No Api Key Found",Toast.LENGTH_SHORT).show();
                new_release_progress.setVisibility(View.INVISIBLE);
                upcoming_progress.setVisibility(View.INVISIBLE);
                //progressDialog.dismiss();
                return;
            }

            Client client = new Client();
            Service apiService = Client.getClient().create(Service.class);


            //Calendar cal = GregorianCalendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date current_date = cal.getTime();
            String current_formatted_date= new SimpleDateFormat("yyyy-MM-dd").format(current_date);

            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -48);
            Date pre_date = cal.getTime();
            String pre_formatted_date= new SimpleDateFormat("yyyy-MM-dd").format(pre_date);
            System.out.println("Old : "+pre_formatted_date);
            System.out.println("New : "+current_formatted_date);

            //Call<MoviesResponse> call = apiService.getNowPlayingMovies("IN",BuildConfig.THE_MOVIE_DB_API_TOKEN);
            //Call<MoviesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            Call<MoviesResponse> call = null;
            
            if(requestType.equals("newReleased")) {
                call = apiService.getNowInTheaterMovies("IN",pre_formatted_date,
                        current_formatted_date,
                        BuildConfig.THE_MOVIE_DB_API_TOKEN);

                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        System.out.println("Response : "+response);

                        try{
                            List<Movie> movies = response.body().getResults();

                            total_released_pages = response.body().getTotalPages();
                            recyclerView.setAdapter(new MoviesAdapter(getActivity().getApplicationContext(),movies));
                            recyclerView.smoothScrollToPosition(0);
                            if(swipeContainer.isRefreshing())
                            {
                                swipeContainer.setRefreshing(false);
                            }
                        }catch (NullPointerException e)
                        {
                            Log.d("Error While Fetch",e.toString());
                            //Toast.makeText(getActivity(),""+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        new_release_progress.setVisibility(View.INVISIBLE);

                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                        //Toast.makeText(getActivity().getApplicationContext(),"Seems Network connection issue\nPlease Refresh Page by Swipe Down\nor Check Network Connectivity!",Toast.LENGTH_SHORT).show();
                        //new_release_progress.setVisibility(View.INVISIBLE);
                        //progressDialog.dismiss();
                    }
                });
            }
            if(requestType.equals("upComing"))
            {
                call = apiService.getUpcomingMovies("IN",
                        current_formatted_date,
                        BuildConfig.THE_MOVIE_DB_API_TOKEN);

                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        System.out.println("Response : "+response);

                        try{
                            List<Movie> movies = response.body().getResults();

                            total_upcoming_pages = response.body().getTotalPages();
                            recyclerView.setAdapter(new MoviesAdapter(getActivity().getApplicationContext(),movies));
                            recyclerView.smoothScrollToPosition(0);
                            if(swipeContainer.isRefreshing())
                            {
                                swipeContainer.setRefreshing(false);
                            }
                        }catch (NullPointerException e)
                        {
                            Log.d("Error While Fetch",e.toString());
                            //Toast.makeText(getActivity(),""+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        upcoming_progress.setVisibility(View.INVISIBLE);
                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                        //Toast.makeText(getActivity(),"Seems Network connection issue\nPlease Refresh Page by Swipe Down\nor Check Network Connectivity!",Toast.LENGTH_SHORT).show();
                        //upcoming_progress.setVisibility(View.INVISIBLE);
                        //progressDialog.dismiss();
                    }
                });
            }

        }catch (Exception e)
        {
            Log.d("Error",e.getMessage());
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}