package com.appfactory.kaldi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ManageStoreActivity extends AppCompatActivity
{
    private Button newItem;
    private String userName;
    private boolean isDrinker;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__store);
        userName = getIntent().getStringExtra("currentUser");
        isDrinker = getIntent().getBooleanExtra("isDrinker", true);
        getStores(userName);
    }
    public void getStores(String userName)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child("merchants");
        database.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    if (postSnapshot.getKey().equals(userName))
                    {
                        Merchant merchant = postSnapshot.getValue(Merchant.class);
                        for (int i = 0; i < merchant.stores.size(); i++) {
                            String storeName = merchant.stores.get(i).storeName;
                            addStoreItem(storeName);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    public void addStoreItem(String storeName)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);
        newItem = new Button(this);
        newItem.setText(storeName);
        layout.addView(newItem);
        newItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), StoreProfileActivity.class);
                myIntent.putExtra("currentUser", userName);
                myIntent.putExtra("isDrinker", isDrinker);
                myIntent.putExtra("storeName", storeName);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
