package gq.altafchaudhari.www.movieplex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaytmChecksum extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    String custid="", orderId="", mid="",amt="";

    String theater,movie,city,time,seats,mobile,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        mid = "TUUcxM47514943318406";
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(PaytmChecksum.this);


        String url ="http://192.168.43.230/paytm/generateChecksum.php";//TODO your server's url here (www.xyz/checksumGenerate.php)
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        String CHECKSUMHASH ="";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParse jsonParser = new JSONParse(PaytmChecksum.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custid+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+amt+"&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";

            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {

                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getStagingService();

            custid = custid.replaceAll(" ", "_").toLowerCase();

            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("MID", mid);
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("MOBILE_NO", mobile);
            paramMap.put("EMAIL", email);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amt);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);

            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);

            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);

            Service.startPaymentTransaction(PaytmChecksum.this, true, true,
                    PaytmChecksum.this  );


        }

    }


    @Override
    public void onTransactionResponse(Bundle bundle) {
        Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show();
        Log.e("checksum ", " respon true " + bundle.toString());

        custid = custid.replaceAll("_", " ").toUpperCase();

        Intent intent = new Intent(PaytmChecksum.this, PostPaymentActivity.class);
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

    @Override
    public void networkNotAvailable() {

        Intent intent = new Intent(PaytmChecksum.this, PostPaymentActivity.class);
        intent.putExtra("status", "failed");
        startActivity(intent);
        finish();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Intent intent = new Intent(PaytmChecksum.this, PostPaymentActivity.class);
        intent.putExtra("status", "failed");
        startActivity(intent);
        finish();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }


}