package com.abc.pointofsale;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    static String r;
    static String price = "100" ;
    int c1=0;
    private static String url = "https://diagonal-misalineme.000webhostapp.com/show.php";
    private static String inserturl = "https://diagonal-misalineme.000webhostapp.com/insert.php";
    com.android.volley.RequestQueue requestQueue;
    ImageView add1;
    ImageView add2,add3,add4;
    ImageView bgapp, clover;

    Animation frombottom1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frombottom1 = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);

        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        add1=(ImageView)findViewById(R.id.imageView5);
        add1.setOnClickListener(this);
        add2=(ImageView)findViewById(R.id.imageView6);
        add3=(ImageView)findViewById(R.id.imageView8);
        add4=(ImageView)findViewById(R.id.imageView2);

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoac2();
            }
        });

        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoac3();
            }
        });

        add4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoac4();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"Result Not Found", Toast.LENGTH_SHORT).show();
            }
            else{

                r=result.getContents();



                JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray students = response.getJSONArray("students");

                            for(int i=0; i< students.length();i++) {

                                JSONObject student= students.getJSONObject(i);
                                String id = student.getString("id");

                                if(r.equals(id))
                                {
                                    c1++;
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(JsonObjectRequest);

                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

                dialog.setMessage("Creating Product...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();

                long delayInMillis = 2000;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, delayInMillis);


                if(c1==0) {
                    StringRequest request = new StringRequest(Request.Method.POST, inserturl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("name", r);
                            parameters.put("price", price);

                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                    Toast.makeText(this,"Product Created", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(this,"Product Already Exists ", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanow(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Barcode");
        integrator.initiateScan();
    }

    @Override
    public void onClick(View v) {
        scanow();

    }



    public void gotoac2() {
       Intent intent = new Intent (this, Main2Activity.class);
       startActivity(intent);
    }
    public void gotoac3() {
        Intent intent = new Intent (this, Main3Activity.class);
        startActivity(intent);
    }
    public void gotoac4() {
        Intent intent = new Intent (this, Activity4.class);
        startActivity(intent);
    }


}


