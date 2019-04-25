package gq.altafchaudhari.www.movieplex;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

public class SelectTimingAndSeatActivity extends AppCompatActivity implements View.OnClickListener {

    TextView movie_name,theater_name,theater_city,tv_total_amt,currency_symbol,show_time;
    ImageView movie_image;
    String poster_path;
    Locale locale;
    Currency currency;
    int base_fare = 100;

    ViewGroup layout;

    int total_rows = 0;
    int total_seat_booking = 0;
    double total_amount=0;

    // U = Not Available
    // A = Available
    // R = Reserved
     String seats;
     String seats_tapadia = "AA_AAUURRAA_AA/"
                  + "UU_AAAUUUAA_AA/"
                  + "UU_AAAAAAAA_UU/"
                  + "______________/"
                  + "UU_UUUARRRR_AA/"
                  + "AA_AAAARRAA_UU/"
                  + "AA_AARRUUUU_RR/"
                  + "AA_UUAAUURR_RR/"
                  + "______________/"
                  + "UU_UUUARRRR_AA/"
                  + "AA_AAAARRAA_UU/"
                  + "AA_AARRUUUU_RR/"
                  + "AA_UUAAUURR_RR/"
                  + "______________/";

    String seats_reliance = "AA_AAUU_RRAA_AA/"
            + "AA_AAAA_UUAA_AA/"
            + "AA_AAAA_AAAA_UU/"
            + "_______________/"
            + "UU_UUUA_RRRR_AA/"
            + "AA_AAAA_RRAA_UU/"
            + "AA_AARR_UUUU_RR/"
            + "AA_UUAA_AARR_RR/"
            + "_______________/"
            + "UU_UUUA_RRRR_AA/"
            + "AA_AAAA_RRAA_UU/"
            + "AA_UURR_UUUU_RR/"
            + "AA_RRAA_UURR_RR/"
            + "_______________/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 75;
    int seatGaping = 5;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timing_and_seat);

        movie_name = findViewById(R.id.movie_title);
        theater_name = findViewById(R.id.theater_name);
        theater_city = findViewById(R.id.theater_city);
        movie_image = findViewById(R.id.thumbnail);
        tv_total_amt = findViewById(R.id.tv_total_amt);
        show_time = findViewById(R.id.show_time);
        currency_symbol = findViewById(R.id.currency_symbol);

        locale = getResources().getConfiguration().locale;
        currency = Currency.getInstance(locale);
        //currency_symbol.setText(currency.getSymbol(locale)+".");

        Intent getExtra =  getIntent();
        movie_name.setText(getExtra.getExtras().getString("movie_name",null));
        theater_name.setText(getExtra.getExtras().getString("theater_name",null));
        theater_city.setText(getExtra.getExtras().getString("theater_city",null));
        show_time.setText("12:00 PM");
        poster_path = getExtra.getExtras().getString("movie_image",null);
        String theater = getExtra.getExtras().getString("theater_name",null);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        requestOptions.error(R.drawable.ic_broken_image_black_24dp);


        Glide.with(this)
                .load(poster_path)
                .apply(RequestOptions.placeholderOf(R.drawable.loading).error(R.drawable.loading))
                .into(movie_image);


        if(theater.contains("Reliance")){
            seats = seats_reliance;
            base_fare = base_fare + 25;
        }
        if(theater.contains("Tapadia")) {
            seats = seats_tapadia;
            base_fare = base_fare + 30;
        }

        layout = findViewById(R.id.layoutSeat);
        seats = "/" + seats;
        //String[] rows = seats.split("/");

        LinearLayout layoutSeat = new LinearLayout(this);

        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;


        int count = 0;
        int[] slash_id = new int[seats.length()];
        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setId(total_rows);
                layoutSeat.addView(layout);

                if(seats.charAt(index) != '_')
                {
                    slash_id[total_rows] = index;
                    total_rows++;
                }

                /*if(!is_path(seats,index))
                    total_rows++;
                else
                    total_rows--;*/
                //slash_id[total_rows] = index;

            } else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                //view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setPadding(0, 0, 0, 0);
                view.setId(count);
                view.setTag(total_rows);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_booked);
                view.setTextColor(Color.WHITE);
                view.setTextAppearance(this, R.style.seatFont);
                view.setTag(STATUS_BOOKED);
                view.setHint(String.valueOf(total_rows));
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 0);
                view.setId(count);
                view.setTag(total_rows);
                view.setHint(String.valueOf(total_rows));
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                view.setTextColor(Color.GRAY);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'R') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 0);
                view.setId(count);
                view.setTag(total_rows);
                view.setHint(String.valueOf(total_rows));
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_reserved);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_RESERVED);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
    }

    @Override
    public void onClick(View view) {

        if ((int) view.getTag() == STATUS_AVAILABLE) {

            TextView tv = (TextView)  view;
            int row_number = Integer.parseInt((String) tv.getHint());

            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
                total_amount -= get_fare(row_number,base_fare);
                total_seat_booking--;
                tv_total_amt.setText(String.valueOf(total_amount));
                System.out.println("Total Seats: "+total_seat_booking+"\nTotal Amount: "+total_amount);
            }
            else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
                total_amount += get_fare(row_number,base_fare);
                total_seat_booking++;
                tv_total_amt.setText(String.valueOf(total_amount));
                System.out.println("Total Seats: "+total_seat_booking+"\nTotal Amount: "+total_amount);
            }
            System.out.println("Total Booking "+total_seat_booking);
        } else if ((int) view.getTag() == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        } else if ((int) view.getTag() == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }




    private int get_fare(int selected_row,int theater_fare) {

        int seat_fare = 0;

        //System.out.println("Row = "+selected_row);
        //System.out.println("old fare : "+theater_fare);

        if(selected_row>0&&selected_row<4)
            seat_fare = theater_fare;
        else if(selected_row>4&&selected_row<9)
            seat_fare = base_fare+30;
        else if(selected_row>9)
            seat_fare = base_fare+70;

        System.out.println("seat_fare : "+seat_fare);
        return seat_fare;
    }


    public void gotoPreviousActivity(View v)
    {
        finish();
    }

    public void gotoPayment(View v)
    {
        String movie,theater,city, time,amount,selected_seats = "";
        movie =  movie_name.getText().toString();
        theater = theater_name.getText().toString();
        city = theater_city.getText().toString();
        time = show_time.getText().toString();
        amount = tv_total_amt.getText().toString();

        try {
            selected_seats = selectedIds.substring(0, selectedIds.length() - 1);

            System.out.println("......."+selected_seats+".......");
            if (seats.length() > 0 ) {
                Intent intent = new Intent(SelectTimingAndSeatActivity.this, PaymentActivity.class);
                intent.putExtra("movie", movie);
                intent.putExtra("theater", theater);
                intent.putExtra("city", city);
                intent.putExtra("time", time);
                intent.putExtra("seats", selected_seats);
                intent.putExtra("amount", amount);
                intent.putExtra("movie_image", poster_path);
                startActivity(intent);
            } else {
                Toast.makeText(SelectTimingAndSeatActivity.this, "Please Select Seats", Toast.LENGTH_LONG).show();
            }
        }catch (StringIndexOutOfBoundsException ex)
        {
            Toast.makeText(SelectTimingAndSeatActivity.this, "Please Select Seats", Toast.LENGTH_LONG).show();
        }
    }


    /*private boolean is_path(String seats,int index)
    {
        boolean flag = false;
        String[] rows = seats.split("/");
        String path = "";
        for(int i = 0;i<rows.length;i++)
        {
            for(int j = 0; j<rows[i].length();j++)
            {
                path += "_";
                break;
            }
            break;
        }
        //System.out.println("Paaaaaaaaathhhh : "+path);
        System.out.println("paaaaaaath "+path);
        String str = seats.substring(0,index);
        String[] seat_rows = str.split("/");

        for (int i = 0; i < seat_rows.length; i++) {
            //for (int j = i + 1 ; j < seat_rows.length; j++) {
                if (seat_rows[i].equals(path)) {
                    flag = true;
                    System.out.println("paath found....");
                }
            //}
        }
        return flag;
    }*/

}
