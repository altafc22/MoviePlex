package gq.altafchaudhari.www.movieplex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 3000;
    private static final int STORAGE_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        changeStatusBarColor();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIMEOUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            //createAppDirectory();
            createAppDirectory(getString(R.string.app_name));
            createAppDirectory(getString(R.string.app_name)+"/Tickets");
        }
        else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat. requestPermissions(SplashActivity.this,permissions,STORAGE_PERMISSION_CODE);
        }
        else if(!checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            ActivityCompat. requestPermissions(SplashActivity.this,permissions,STORAGE_PERMISSION_CODE);
        }
    }

    private boolean checkPermission(String permission)
    {
        //String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = SplashActivity.this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public void createAppDirectory(String dir){
        String myfolder=Environment.getExternalStorageDirectory()+"/"+dir;
        File f=new File(myfolder);
        if(!f.exists()) {
            if (!f.mkdir()) {
                //Toast.makeText(this, myfolder + " can't be created.", Toast.LENGTH_SHORT).show();
                System.out.println(myfolder + " can't be created.");
            } else {
                //Toast.makeText(this, myfolder + " can be created.", Toast.LENGTH_SHORT).show();
                System.out.println(myfolder+" can be created.");
            }
        }
         else {
            //Toast.makeText(this, myfolder + " already exits.", Toast.LENGTH_SHORT).show();
            System.out.println(myfolder + " already exits.");
        }
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

        //change bottom bar to transperen....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //to use any drawable add this two lines in style xml
        //<item name="android:windowDrawsSystemBarBackgrounds">true</item>
        //<item name="android:statusBarColor">@color/primary_dark</item>

        //change bottom bar to transperen end....


    }
}
