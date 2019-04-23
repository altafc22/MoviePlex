package gq.altafchaudhari.www.movieplex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PaymentActivity extends AppCompatActivity {


    TextView movie_name,theater_name,theater_city,tv_total_amt,currency_symbol,show_time,seats;
    ImageView movie_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        movie_name = findViewById(R.id.movie_title);
        theater_name = findViewById(R.id.theater_name);
        theater_city = findViewById(R.id.theater_city);
        tv_total_amt = findViewById(R.id.tv_total_amt);
        show_time = findViewById(R.id.show_time);
        seats = findViewById(R.id.seats);
        movie_image = findViewById(R.id.thumbnail);
        currency_symbol = findViewById(R.id.currency_symbol);

        Intent getExtra =  getIntent();
        movie_name.setText(getExtra.getExtras().getString("movie",null));
        theater_name.setText(getExtra.getExtras().getString("theater",null));
        theater_city.setText(getExtra.getExtras().getString("city",null));
        show_time.setText(getExtra.getExtras().getString("time",null));
        seats.setText(getExtra.getExtras().getString("seats",null));
        tv_total_amt.setText(getExtra.getExtras().getString("amount",null));
        String poster_path = getExtra.getExtras().getString("movie_image",null);

        Glide.with(this)
                .load(poster_path)
                .apply(RequestOptions.placeholderOf(R.drawable.loading).error(R.drawable.loading))
                .into(movie_image);

    }

    public void gotoPreviousActivity(View v)
    {
        finish();
    }
}
