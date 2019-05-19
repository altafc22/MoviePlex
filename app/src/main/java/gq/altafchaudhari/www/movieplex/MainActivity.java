package gq.altafchaudhari.www.movieplex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;

import gq.altafchaudhari.www.movieplex.Fragments.MovieListFragment;
import gq.altafchaudhari.www.movieplex.Fragments.TheaterListFragment;
import gq.altafchaudhari.www.movieplex.Fragments.UserAccountFragment;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Menu menu;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);


        bottomNavigationView.setItemIconTintList(null);
        menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.action_item1).setIcon(R.drawable.ic_start_color);
        bottomNavigationView.setItemTextAppearanceActive(R.color.color_red_icon);
        bottomNavigationView.setItemTextAppearanceInactive(R.color.colorGray);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        //Menu menu = bottomNavigationView.getMenu();
                        bottomNavigationView.setItemIconTintList(null);
                        menu.findItem(R.id.action_item1).setIcon(R.drawable.ic_clapperboard);
                        menu.findItem(R.id.action_item2).setIcon(R.drawable.ic_theatre);
                        menu.findItem(R.id.action_item3).setIcon(R.drawable.ic_user);

                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = MovieListFragment.newInstance();
                                item.setIcon(R.drawable.ic_clapperboard_color);
                                changeBottomNavigationTextColor(bottomNavigationView,Color.parseColor("#3F51B5"));
                                break;
                            case R.id.action_item2:
                                selectedFragment = TheaterListFragment.newInstance();
                                item.setIcon(R.drawable.ic_theatre_color);
                                changeBottomNavigationTextColor(bottomNavigationView,Color.parseColor("#FFB300"));
                                break;
                            case R.id.action_item3:

                                MyApplication myApplication = (MyApplication) MainActivity.this.getApplication();
                                SharedPreferences sp = getApplicationContext().getSharedPreferences(myApplication.SP_NAME, 0);
                                //String id = sp.getString("id", null);
                                String loginType = sp.getString("loginType", null);
                                if(loginType!=null && loginType.equals("skipped"))
                                {
                                    selectedFragment = MovieListFragment.newInstance();
                                    item.setIcon(R.drawable.ic_user_color);
                                    changeBottomNavigationTextColor(bottomNavigationView,Color.parseColor("#3F51B5"));

                                    new TTFancyGifDialog.Builder(MainActivity.this)
                                            .setTitle("Login")
                                            .setMessage("You need to login.")
                                            .setPositiveBtnText("Ok")
                                            .setPositiveBtnBackground("#22b573")
                                            .setNegativeBtnText("Cancel")
                                            .setNegativeBtnBackground("#c1272d")
                                            .setGifResource(R.drawable.login)      //pass your gif, png or jpg
                                            .isCancellable(true)
                                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                                @Override
                                                public void OnClick() {
                                                    MyApplication myApplication = (MyApplication)getApplication();
                                                    SharedPreferences.Editor editor;
                                                    String sp_name = myApplication.SP_NAME;
                                                    SharedPreferences sp = getApplicationContext().getSharedPreferences(sp_name, 0);
                                                    System.out.println("Shared Preference : "+sp_name);
                                                    editor = sp.edit();
                                                    editor.putBoolean("isLogin", false);
                                                    editor.commit();
                                                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                                }
                                            })
                                            .build();

                                }
                                else
                                {
                                    selectedFragment = UserAccountFragment.newInstance();
                                    item.setIcon(R.drawable.ic_user_color);
                                    changeBottomNavigationTextColor(bottomNavigationView,Color.parseColor("#3F51B5"));
                                }
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MovieListFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }



    private void changeBottomNavigationTextColor(BottomNavigationView navigationView, int active_color) {
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = Color.parseColor("#9B9B9B");

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        active_color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );
        navigationView.setItemTextColor(navMenuTextList);
    }


}