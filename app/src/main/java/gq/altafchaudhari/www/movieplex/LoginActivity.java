package gq.altafchaudhari.www.movieplex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton loginButton;
    private AccessToken accessToken ;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor();


        MyApplication myApplication =(MyApplication)getApplication();
        SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);
        if(sp.getBoolean("isLogin", false)){
            gotoMainActivity();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = loginResult.getAccessToken();
                getUserProfile(accessToken);
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this,"Login Process Canceled",Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this,"Error : "+exception,Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //to fetch user info
                            //object.getJSONObject("data").getString("url");
                            String img_url = object.getJSONObject("picture").toString();
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            Profile profile = Profile.getCurrentProfile();
                            MyApplication myApplication = (MyApplication)getApplication();
                            //saveDataInSp(id,name,email);
                            new DownloadImage(id).execute(profile.getProfilePictureUri(200,200).toString());
                            saveDataInSp(profile,email);
                            gotoMainActivity();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void gotoMainActivity()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void skipSignIn(View v)
    {
        System.out.println("Saving Data to Preference with Skip");
        SharedPreferences.Editor editor;
        MyApplication myApplication = (MyApplication)getApplication();
        String sp_name = myApplication.SP_NAME;
        SharedPreferences sp = getApplicationContext().getSharedPreferences(sp_name, 0);
        System.out.println("Shared Preference : "+sp_name);
        editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.putString("id", "skipped");
        editor.putString("name", "skipped");
        editor.putString("email", "skipped");
        editor.putString("profile_pic", "skipped");
        editor.commit();

        gotoMainActivity();
    }

    //private void saveDataInSp(String id,String name,String email)
    private void saveDataInSp(Profile profile,String email)
    {
        /*System.out.println("Saving Data to Preference");
        SharedPreferences.Editor editor;
        MyApplication myApplication = (MyApplication)getApplication();
        String sp_name = myApplication.SP_NAME;
        SharedPreferences sp = getApplicationContext().getSharedPreferences(sp_name, 0);
        System.out.println("Shared Preference : "+sp_name);
        editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.commit();*/

        System.out.println("Saving Data to Preference");
        SharedPreferences.Editor editor;
        MyApplication myApplication = (MyApplication)getApplication();
        String sp_name = myApplication.SP_NAME;
        SharedPreferences sp = getApplicationContext().getSharedPreferences(sp_name, 0);
        System.out.println("Shared Preference : "+sp_name);
        editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.putString("id", profile.getId());
        editor.putString("name", profile.getName());
        editor.putString("email", email);
        editor.putString("profile_pic", profile.getProfilePictureUri(220,256).toString());
        editor.commit();
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

        //change bottom bar to transparent....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //to use any drawable add this two lines in style xml
        //<item name="android:windowDrawsSystemBarBackgrounds">true</item>
        //<item name="android:statusBarColor">@color/primary_dark</item>
        //change bottom bar to transperen end....
    }


    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        String imageName;

        public DownloadImage(String imageName){
            this.imageName = imageName;
        }
        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try{
                System.out.println("Image Downloading");
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Bitmap result){
            //bmImage.setImageBitmap(result);
            System.out.println("Image Downloaded Successful");
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("profile_image", Context.MODE_PRIVATE);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File image_path = new File(directory, imageName+".png");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(image_path);
                System.out.println("FIle Outputstram path "+image_path);
                result.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                Log.e("SAVE_IMAGE", e.getMessage(), e);
            }
        }

    }

}
