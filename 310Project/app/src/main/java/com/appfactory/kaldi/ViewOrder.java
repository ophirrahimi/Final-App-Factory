package com.appfactory.kaldi;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class ViewOrder extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        String currentUser = getIntent().getStringExtra("currentUser");

        Order target = (Order) getIntent().getSerializableExtra("orderID");

        LinearLayout layout = (LinearLayout) findViewById(R.id.itemLayout);
        for(Item i : target.items) {
            TextView newItem = new TextView(this);
            String itemText = "";

            itemText += i.name;
            itemText += "     Caffeine: ";
            itemText += i.caffeine;
            itemText += "     Price: $";
            itemText += i.price;

            newItem.setText(itemText);


            System.out.println("________SETTING BUTTON: " + itemText);
            layout.addView(newItem);
        }
        Button back = (Button) findViewById(R.id.backToOrderHistory);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OrderHistoryActivity.class);
                myIntent.putExtra("currentUser", currentUser);
                startActivityForResult(myIntent, 0);
            }
        });
    }

}
