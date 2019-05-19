package gq.altafchaudhari.www.movieplex.Fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;
import gq.altafchaudhari.www.movieplex.LoginActivity;
import gq.altafchaudhari.www.movieplex.MyApplication;
import gq.altafchaudhari.www.movieplex.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserAccountFragment extends Fragment {

    Button logoutButton;
    TextView tv_name,tv_id,tv_email;
    CircleImageView profile_pic;
    String id,name,email,loginType;
    GoogleSignInClient mGoogleSignInClient;

    public static UserAccountFragment newInstance() {
        UserAccountFragment fragment = new UserAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_account, container, false);

        logoutButton = rootView.findViewById(R.id.logout_button);
        tv_id = rootView.findViewById(R.id.tv_id);
        tv_name = rootView.findViewById(R.id.tv_user_name);
        tv_email = rootView.findViewById(R.id.tv_email);
        profile_pic = rootView.findViewById(R.id.profile_pic);

        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        id = sp.getString("id", null);
        loginType = sp.getString("loginType", null);

        tv_id.setText(id);
        name = sp.getString("name", null);
        String new_name;
        //if(name!=null)
            new_name = name.replace(" ", "\n");
        //else
        //    new_name = "";
        tv_name.setText(new_name);
        tv_email.setText(sp.getString("email", null));
        String profile_image_url = sp.getString("profile_pic","");

        Glide.with(getActivity())
                .load(profile_image_url)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).error(R.drawable.ic_user_placeholder))
                .into(profile_pic);

        /*Bitmap profile_image = loadProfileImage(id);
        if (profile_image == null) {
            Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_placeholder);
            profile_pic.setImageBitmap(defaultImage);
        } else
        {
            profile_pic.setImageBitmap(profile_image);
        }*/

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginType.equals("gmail"))
                    signOutGmail();
                if(loginType.equals("facebook"))
                    LoginManager.getInstance().logOut();

                logoutUser();
            }
        });
        return rootView;
    }

    private void signOutGmail() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
    }

  /*  private Bitmap loadProfileImage(String imageName)
    {
        Bitmap bitmap;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile_image", Context.MODE_PRIVATE);
        File image_path = new File(directory, imageName+".png");
        if (!directory.exists()) {
           return null;
        }
        try {
            FileInputStream is =  new FileInputStream(image_path);
            try {
                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } finally {
                is.close();
            }
        } catch (Exception e) {
            bitmap = null;
        }
        return bitmap;
    }*/

    public void logoutUser(){
        LoginManager.getInstance().logOut();

        SharedPreferences.Editor editor;
        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);
        editor = sp.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}