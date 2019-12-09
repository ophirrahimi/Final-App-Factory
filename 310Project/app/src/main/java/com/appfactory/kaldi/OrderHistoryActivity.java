package com.appfactory.kaldi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import java.io.Serializable;
import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity implements Serializable {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__history);

        //LOOP THROUGH STORED TRIPS FOR THE USER AND CREATE NEW BUTTONS FOR EACH ONE
//        Button newTrip = new Button(this);
//        newTrip.setText(trip.name);

        String currentUser = getIntent().getStringExtra("currentUser");
        Boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);

        System.out.println("_______User: " + currentUser);

        ArrayList<Order> currOrderHistory = new ArrayList<Order>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        Query search = database.child("drinkers").orderByKey().equalTo(currentUser);
        search.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(currentUser))
                    {
                        Drinker drinker = snapshot.getValue(Drinker.class);
                        System.out.println("_________FOUND USER");
                        if(drinker != null)
                        {

                            drinker.id = snapshot.getKey();
                            for(Order o : drinker.orderHistory){
                                currOrderHistory.add(o);
                                System.out.println("_________ORDER: " + o.fromStore);
                            }

                            addHistoryItems(currOrderHistory);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void addHistoryItems(ArrayList<Order> currOrderHistory){
        LinearLayout layout = (LinearLayout) findViewById(R.id.historyLayout);
        for(Order o : currOrderHistory) {
            System.out.println("________LOOPING THROUGH ORDERS");
            String dateOfOrder = o.dateOfOrder;
            String fromStore = o.fromStore;
            Button newOrder = new Button(this);
            String buttonText = "";
            buttonText += fromStore;
            buttonText += "   Date: ";
            buttonText += dateOfOrder;

            newOrder.setText(buttonText);
            newOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentUser = getIntent().getStringExtra("currentUser");
                    Intent myIntent = new Intent(getApplicationContext(), ViewOrder.class);
                    myIntent.putExtra("currentUser", currentUser);
                    myIntent.putExtra("orderID", o);
                    startActivity(myIntent);
                }
            });

            System.out.println("________SETTING BUTTON: " + buttonText);
            layout.addView(newOrder);
        }
    }
}
