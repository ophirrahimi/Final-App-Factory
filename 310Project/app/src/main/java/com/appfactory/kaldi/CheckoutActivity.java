package com.appfactory.kaldi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity
{
    ArrayList<String> bag;
    String bagString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        TextView textview = (TextView) findViewById(R.id.bagText);
        for(int i = 0; i < bag.size(); i++)
        {
            bagString += bag.get(i);
            bagString += "\n";
        }
        textview.setText(bagString);
    }
}
