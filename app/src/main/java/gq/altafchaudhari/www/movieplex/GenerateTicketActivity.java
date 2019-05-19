package gq.altafchaudhari.www.movieplex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class GenerateTicketActivity extends AppCompatActivity {

    String custid,orderId,amt,theater,movie,city,time,seats,mobile,email;
    ImageView img_qrcode;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);
        img_qrcode = findViewById(R.id.img_qrcode);

        getIntentData();
        String text2Qr = orderId+":"+custid+":"+amt+":"+movie+":"+theater+":"+time+":"+seats;
        generateQR(text2Qr);
    }


    private void getIntentData()
    {
        Intent intent = getIntent();
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

    private void generateQR(String text)
    {

        /*String currentString = "Fruit: they taste good";
        String[] separated = currentString.split(":");
        separated[0]; // this will contain "Fruit"
        separated[1]; // this will contain " they taste good"
        */

        text2Qr = text;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img_qrcode.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
    }

    public void gotoMainActivity(View v){
     startActivity(new Intent(GenerateTicketActivity.this,MainActivity.class));
     finish();
    }


    public void saveTicket(View v){
        saveToSdCard(getTicketBitmap());
        startActivity(new Intent(GenerateTicketActivity.this,MainActivity.class));
        finish();

    }



    public Bitmap getTicketBitmap() {
        Layout_to_Image layout_to_image;
        FrameLayout ticketFrame;
        Bitmap bitmap;
        ticketFrame=(FrameLayout) findViewById(R.id.ticketFrame);
        layout_to_image=new Layout_to_Image(GenerateTicketActivity.this,ticketFrame);
        bitmap=layout_to_image.convert_layout();

        return bitmap;
    }


    private void saveToSdCard(Bitmap bitmap){
        try {

            FileOutputStream output =   new FileOutputStream(Environment.getExternalStorageDirectory() + "/MoviePlex/tickets/"+orderId+".png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
            Toasty.success(GenerateTicketActivity.this,"Ticket Saved in Gallery",Toasty.LENGTH_LONG,true).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toasty.error(GenerateTicketActivity.this,"Ticket Not Saved",Toasty.LENGTH_LONG,true).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toasty.error(GenerateTicketActivity.this,"Ticket Not Saved",Toasty.LENGTH_LONG,true).show();
        }
    }


}
