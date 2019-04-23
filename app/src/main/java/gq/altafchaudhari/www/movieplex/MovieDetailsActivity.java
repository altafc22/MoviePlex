package gq.altafchaudhari.www.movieplex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codesgood.views.JustifiedTextView;

public class MovieDetailsActivity extends AppCompatActivity {

    String toolbar_title;
    ImageView imageView;
    VideoView videoView;
    TextView movie_title, release_date,vote_average,total_vote,genre;
    JustifiedTextView overview;
    String thumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeStatusBarColor();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolBar();

        imageView = findViewById(R.id.thumbnail_image_header);
        videoView = findViewById(R.id.video_view);
        movie_title = findViewById(R.id.movie_title);
        release_date = findViewById(R.id.release_date);
        vote_average = findViewById(R.id.vote_average);
        total_vote = findViewById(R.id.total_vote);
        genre = findViewById(R.id.genre);
        overview = findViewById(R.id.overview);
        System.out.println("trying to get intent");
        Intent intent = getIntent();
        if(intent.hasExtra("title"))
        {
            movie_title.setText(getIntent().getExtras().getString("title"));
            release_date.setText(getIntent().getExtras().getString("release_date"));
            Double avg = Double.parseDouble(getIntent().getExtras().getString("vote_average"));
            avg = avg*10;
            vote_average.setText(String.valueOf(avg)+"%");
            total_vote.setText(getIntent().getExtras().getString("vote_count")+" votes");
            String gen = getIntent().getExtras().getString("genre");
            //gen.replaceAll("null ", "");
            //if(gen.equals(""))
            //    gen = "Drama";
            genre.setText(gen);
            overview.setText(getIntent().getExtras().getString("overview"));

            System.out.println("Intent Data Null "+getIntent().getExtras().getString("title"));

            thumbnail = getIntent().getExtras().getString("poster_path");
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.loading);
            requestOptions.error(R.drawable.ic_broken_image_black_24dp);
            //backdrop = "http://image.tmdb.org/t/p/w780"+backdrop;
            thumbnail = "http://image.tmdb.org/t/p/w500"+thumbnail;

            Glide.with(this)
                    .load(thumbnail)
                    .apply(RequestOptions.placeholderOf(R.drawable.loading).error(R.drawable.loading))
                    .into(imageView);

            toolbar_title = getIntent().getExtras().getString("original-title");

        }
        else
        {
            Toast.makeText(this,"No data found",Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolBar() {

        final View yourView = findViewById(R.id.main_content);

        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + i ==0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (yourView != null) {
                            yourView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }
                    }

                }else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow =false;
                    yourView.setSystemUiVisibility(0);
                }
            }
        });
    }

    public void initiateBooking(View v)
    {
        System.out.println("seeeeending thumbnail url "+thumbnail);
        Intent intent = new Intent(MovieDetailsActivity.this,SelectTheaterActivity.class);
        intent.putExtra("movie_name",movie_title.getText().toString());
        intent.putExtra("movie_image",thumbnail);
        startActivity(intent);
    }

    /**
     * Making notification bar and bottom bar transparent
     */
    private void changeStatusBarColor() {


        //change notification icon color...
        View yourView = findViewById(R.id.splash_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (yourView != null) {
                yourView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        //If you want to reset the changes, clear the flag like this:
        //yourView.setSystemUiVisibility(0);
        //change notification icon color end ...


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        // making notification bar transparent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        // making notification bar transparent end .....


        /*//change bottom bar to transperen....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //to use any drawable add this two lines in style xml
        //<item name="android:windowDrawsSystemBarBackgrounds">true</item>
        //<item name="android:statusBarColor">@color/primary_dark</item>

        //change bottom bar to transperen end....
        */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
