package gq.altafchaudhari.www.movieplex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class PostPaymentActivity extends AppCompatActivity {

    String custid="", orderId="", mid="",amt="";
    String theater,movie,city,time,seats,mobile,email,paymentStatus;

    LottieAnimationView animationView;
    TextView tv_status,tv_message;
    LinearLayout next_btn_layout,cancel_button_layout;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_payment);


        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        tv_status =  findViewById(R.id.tv_status);
        tv_message = findViewById(R.id.tv_message);
        next_btn_layout = findViewById(R.id.next_btn_layout);
        cancel_button_layout = findViewById(R.id.cancel_button_layout);

        Intent intent = getIntent();

        paymentStatus = intent.getExtras().getString("status");


        if(paymentStatus.equals("success"))
        {
            next_btn_layout.setVisibility(View.VISIBLE);
            animationView.setAnimation("payment_success.json");
            animationView.loop(true);
            animationView.playAnimation();

            tv_status.setTextColor(R.color.color_green);
            tv_status.setText(R.string.payment_success_status);
            tv_message.setText(R.string.payment_success_message);

            orderId = intent.getExtras().getString("orderid");
            custid = intent.getExtras().getString("custid");
            amt = intent.getExtras().getString("amount");
            theater = intent.getExtras().getString("theater",null);
            city = intent.getExtras().getString("city",null);
            movie = intent.getExtras().getString("movie",null);
            time = intent.getExtras().getString("time",null);
            seats = intent.getExtras().getString("seats",null);
            mobile = intent.getExtras().getString("mobile",null);
            email = intent.getExtras().getString("email",null);
        }
        else
        {
            cancel_button_layout.setVisibility(View.VISIBLE);
            animationView.setAnimation("payment_failed.json");
            animationView.loop(true);
            animationView.playAnimation();
            tv_status.setTextColor(R.color.color_red_icon);
            tv_status.setText(R.string.payment_failed_status);
            tv_message.setText(R.string.payment_failed_message);
        }
    }

    public void gotoBarcodeActivity(View v)
    {
        Intent intent = new Intent(PostPaymentActivity.this, GenerateTicketActivity.class);
        intent.putExtra("status", "success");
        intent.putExtra("orderid", orderId);
        intent.putExtra("custid", custid);
        intent.putExtra("amount", amt);
        intent.putExtra("movie",movie);
        intent.putExtra("theater",theater);
        intent.putExtra("city",city);
        intent.putExtra("time",time);
        intent.putExtra("seats",seats);
        intent.putExtra("mobile",mobile);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();

    }

    public void gotoPreviousActivity(View v)
    {
        finish();
    }
}
