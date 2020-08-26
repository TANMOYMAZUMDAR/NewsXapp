package com.example.newsx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER=5000;
   private ImageView backgorundImage;


   //Animation
    Animation sideAnim,bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

           //Hooks
        backgorundImage=findViewById(R.id.background_image);


        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //set Animations on elements
        backgorundImage.setAnimation(sideAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(getApplicationContext(),FrontScreenActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIMER);
    }
}