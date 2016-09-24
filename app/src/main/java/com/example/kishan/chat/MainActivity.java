package com.example.kishan.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
EditText ip;
    String ipadress;
    Intent intent;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip=(EditText)findViewById(R.id.ipadress);
        connect=(Button)findViewById(R.id.connect);
        load();
        connect.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        ipadress=ip.getText().toString();
        intent= new Intent(getApplicationContext(),Main2.class);
        intent.putExtra("IP_AD",ipadress);
        startActivity(intent);
    }
    public void save(){
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("IP_AD",ipadress);
        editor.commit();

    }
    public void load(){
        sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
        ipadress= sharedPreferences.getString("IP_AD","192");

        if(ipadress == "192" ){

        }
        else
        {
            ip.setText(ipadress);

        }

    }

}
