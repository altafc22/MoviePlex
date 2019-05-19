package gq.altafchaudhari.www.movieplex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gujun.android.taggroup.TagGroup;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PaymentActivity extends AppCompatActivity {


    TextView movie_name,theater_name,theater_city,tv_total_amt,currency_symbol,show_time,tv_seats;
    ImageView movie_image;
    EditText et_name,et_mobile,et_email;

    String theater,movie,city,time,seats,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        movie_name = findViewById(R.id.movie_title);
        tv_total_amt = findViewById(R.id.tv_total_amt);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_email = findViewById(R.id.et_email);
        /*theater_name = findViewById(R.id.theater_name);
        theater_city = findViewById(R.id.theater_city);
        show_time = findViewById(R.id.show_time);
        tv_seats = findViewById(R.id.seats);*/
        movie_image = findViewById(R.id.thumbnail);
        currency_symbol = findViewById(R.id.currency_symbol);



        Intent getExtra =  getIntent();
        movie_name.setText(getExtra.getExtras().getString("movie",null));

        theater = getExtra.getExtras().getString("theater",null);
        city = getExtra.getExtras().getString("city",null);
        movie = getExtra.getExtras().getString("movie",null);
        time = getExtra.getExtras().getString("time",null);
        seats = getExtra.getExtras().getString("seats",null);
        amount = getExtra.getExtras().getString("amount",null);



        MyApplication myApplication = (MyApplication) this.getApplication();
        SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);
        String loginType = sp.getString("loginType",null);

        if(loginType.equals("gmail")||loginType.equals("facebook"))
        {
         et_name.setText(sp.getString("name",null));
         et_email.setText(sp.getString("email",null));
        }


        tv_total_amt.setText(getExtra.getExtras().getString("amount",null));
        String poster_path = getExtra.getExtras().getString("movie_image",null);


        TagGroup mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(new String[]{
                  getExtra.getExtras().getString("theater",null)
                , getExtra.getExtras().getString("city",null)
                , getExtra.getExtras().getString("time",null)
                , getExtra.getExtras().getString("seats",null)});


        Glide.with(this)
                .load(poster_path)
                .apply(RequestOptions.placeholderOf(R.drawable.loading).error(R.drawable.loading))
                .into(movie_image);

    }

    public void gotoPreviousActivity(View v)
    {
        finish();
    }

    public void gotoPayment(View v)
    {
        String name = et_name.getText().toString();
        String mobile = et_mobile.getText().toString();
        String email = et_email.getText().toString();
        String orderId = getOrderID();
        amount = "1";

        if(name.length()<1)
        {
            et_name.setError("Enter Name");
        }
        else if(!isValidMobile(mobile))
        {
            et_mobile.setError("Enter Valid Mobile Number");
        }
        else if(!isEmailValid(email))
        {
            et_email.setError("Enter Valid Email");
        }
        else
        {
            Intent intent = new Intent(PaymentActivity.this, PaytmChecksum.class);
            intent.putExtra("orderid", orderId);
            intent.putExtra("custid", name);
            intent.putExtra("amount", amount);
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

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidMobile(String s)
    {
        String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        Matcher m;
        Pattern r = Pattern.compile(pattern);
        m = r.matcher(s.trim());

        if (m.find())
           return true;
        else
           return false;
    }

    private String getOrderID(){
        String new_order = "";
        Random rand = new Random();

        long x = (long)(rand.nextDouble()*100000000000000L);

        new_order = "52" + String.format("%014d", x);
        System.out.println("New Order ID : "+new_order);
        return new_order;
    }
}
