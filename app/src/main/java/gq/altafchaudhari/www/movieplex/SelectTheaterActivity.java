package gq.altafchaudhari.www.movieplex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gq.altafchaudhari.www.movieplex.adapter.TheaterAdapter;
import gq.altafchaudhari.www.movieplex.model.Theater;

public class SelectTheaterActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    //TextView movie_name,movie_image;
    String movie_name="",movie_image_url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theater);
        //movie_name = findViewById(R.id.movie_title);
        recyclerView = findViewById(R.id.recycler_view);

        Intent getExtra =  getIntent();
        //movie_name.setText(getExtra.getExtras().getString("movie_name",null));
        movie_name = getExtra.getExtras().getString("movie_name",null);
        movie_image_url = getExtra.getExtras().getString("movie_image",null);
        loadData();

    }


    public void loadData()
    {
        TheaterAdapter myAdapter;
        List<Theater> theater ;
        theater = new ArrayList<>();
        ProgressDialog mProgress;
        mProgress = new ProgressDialog(getBaseContext());
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        //mProgress.show();
        //String movie_name_text = movie_name.getText().toString();
        theater.add(new Theater(movie_name,"INOX Reliance","Aurangabad",movie_image_url,R.id.theater_image));
        theater.add( new Theater(movie_name,"INOX Tapadia","Aurangabad",movie_image_url,R.id.theater_image));
        myAdapter = new TheaterAdapter(getBaseContext(), theater);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),1,false));
        recyclerView.setAdapter(myAdapter);
        mProgress.dismiss();
    }


    public void gotoPreviousActivity(View v)
    {
        finish();
    }

}
