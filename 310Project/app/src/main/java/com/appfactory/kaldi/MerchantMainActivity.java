package com.appfactory.kaldi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MerchantMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant__main);

        Button manageStore =  (Button) findViewById(R.id.manageStore);
        manageStore.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), ManageStoreActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                startActivityForResult(myIntent, 0);
            }
        });

        Button drinkerProfile =  (Button) findViewById(R.id.drinkerProfile);
        drinkerProfile.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), DrinkerMainActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                startActivityForResult(myIntent, 0);
            }
        });

        Button AddStore =  (Button) findViewById(R.id.addStore);
        AddStore.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), AddBusinessActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                startActivityForResult(myIntent, 0);
            }
        });

    }
}
