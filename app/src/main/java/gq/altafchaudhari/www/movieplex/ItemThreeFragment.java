package gq.altafchaudhari.www.movieplex;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ItemThreeFragment extends Fragment {

    Button logoutButton;
    TextView tv_name,tv_id,tv_email;
    CircleImageView profile_pic;
    String id,name,email;
    public static ItemThreeFragment newInstance() {
        ItemThreeFragment fragment = new ItemThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_item_three, container, false);

        logoutButton = rootView.findViewById(R.id.logout_button);
        tv_id = rootView.findViewById(R.id.tv_id);
        tv_name = rootView.findViewById(R.id.tv_user_name);
        tv_email = rootView.findViewById(R.id.tv_email);
        profile_pic = rootView.findViewById(R.id.profile_pic);

        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);

        id = sp.getString("id",null);
        tv_id.setText(id);
        name = sp.getString("name",null);
        String new_name = name.replace(" ", "\n");
        tv_name.setText(new_name);
        tv_email.setText(sp.getString("email",null));

        Bitmap profile_image = loadProfileImage(id);
        profile_pic.setImageBitmap(profile_image);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        return rootView;
    }

    private Bitmap loadProfileImage(String imageName)
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
    }

    public void logoutUser(){

        LoginManager.getInstance().logOut();

        SharedPreferences.Editor editor;
        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);
        editor = sp.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}