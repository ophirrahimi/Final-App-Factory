package com.appfactory.kaldi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StoreProfileActivity extends AppCompatActivity
{
    Intent myFileIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);
        Button menuItem =  (Button) findViewById(R.id.menuItem);
        menuItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), AddMenuItemActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                String storeName = getIntent().getStringExtra("storeName");
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                myIntent.putExtra("storeName", storeName);
                startActivityForResult(myIntent, 0);
            }
        });
        Button businessLicense =  (Button) findViewById(R.id.businessLicense);
        businessLicense.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent, 10);

            }
        });

        Button menu =  (Button) findViewById(R.id.getMenu);
        menu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), MenuActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                String storeName = getIntent().getStringExtra("storeName");
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                myIntent.putExtra("businessTitle", storeName);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if((resultCode == RESULT_OK) && (data != null))
                {
                    String path = data.getData().getPath();
                    Log.d("path", "" + path);
                }
                break;
        }
    }
}
