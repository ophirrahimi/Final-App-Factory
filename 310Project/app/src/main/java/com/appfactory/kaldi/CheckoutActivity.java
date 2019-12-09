package com.appfactory.kaldi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity implements Serializable
{
    ArrayList<String> bag;
    String bagString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        System.out.println("_________IN CHECKOUT ACTIVITY");

        String currentUser = getIntent().getStringExtra("currentUser");
        Boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
        System.out.println("_________STILL WORKING");
        Order bag = (Order) getIntent().getSerializableExtra("bag");
        System.out.println("_________BAG SECURED");
        Button newItem;

        Double priceTotal=0.0;
        int caffeineTotal=0;
        TextView totalLayout = (TextView) findViewById(R.id.totalLayout);

        String totalString = "";
        for(Item item : bag.items){

            priceTotal+=item.price;
            caffeineTotal+=item.caffeine;
            System.out.println("adding price " + item.price);
            System.out.println("adding caffeine " + item.caffeine);


            LinearLayout layout = (LinearLayout) findViewById(R.id.bagLayout);
            String itemContent = item.name + "             " + item.caffeine + "mg" + "            " + "$" + item.price;
            newItem = new Button(this);
            newItem.setText(itemContent);
            layout.addView(newItem);
        }
        System.out.println("price total " + priceTotal);
        System.out.println("caffeine total " + caffeineTotal);


        String priceString = "Total Price: $" + priceTotal.toString();
        String caffeineString = "     Total Caffeine: " + caffeineTotal + "mg";
        totalString += priceString;
        totalString += caffeineString;
        totalLayout.setText(totalString);

        Button complete = (Button) findViewById(R.id.completeButton);
        complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //SCAN THROUGH DATABASE AND ADD ORDER TO USERS ORDER HISTORY
                System.out.println("________ENTERED ONCLICK OF CHECKOUT");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                Query search;
                if(isDrinker) {
                    search = database.child("drinkers").orderByKey().equalTo(currentUser);
                }else{
                    search = database.child("merchants").orderByKey().equalTo(currentUser);
                }
                System.out.println("______SEARCH: " + search);


                search.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(currentUser))
                            {
                                Drinker drinker = snapshot.getValue(Drinker.class);
                                System.out.println("_______User: " + currentUser);
                                if(drinker != null)
                                {
                                    drinker.id = snapshot.getKey();
                                    drinker.orderHistory.add(bag);
                                    drinker.submitToDatabase();
                                    Intent myIntent = new Intent(getApplicationContext(), DrinkerMainActivity.class);
                                    String currentUser = getIntent().getStringExtra("currentUser");
                                    boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                                    myIntent.putExtra("currentUser", currentUser);
                                    myIntent.putExtra("isDrinker", isDrinker);
                                    startActivityForResult(myIntent, 0);
                                    Toast toast = Toast.makeText(getApplicationContext(), "Order Completed!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
