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
import java.time.LocalDate;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements Serializable
{
    private Button newItem;
    private Order bag = new Order();
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        String businessTitle = getIntent().getStringExtra("businessTitle");
        getMenu(businessTitle);

    }

    public void getMenu(String businessTitle)
    {
        storeName = businessTitle;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child("merchants");
        database.addValueEventListener(new ValueEventListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Merchant merchant = postSnapshot.getValue(Merchant.class);
                    if(merchant.stores != null)
                    {
                        for (int i = 0; i < merchant.stores.size(); i++)
                        {
                            if (merchant.stores.get(i).storeName.equals(businessTitle))
                            {
                                List<Item> menu = merchant.stores.get(i).menu.menu;
                                for (Item item : menu) {
//                                    LocalDate localDate = LocalDate.now();
//                                    System.out.println("______________Local date: " + localDate);
//                                    item.dateOfOrder = localDate.toString();
//                                    item.fromStore = storeName;
                                    addMenuItem(item);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button checkout =  (Button) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view)
            {
                System.out.println("____________________CLIKED");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                System.out.println("____________________went in to database");
//                Query search;
//                if (getIntent().getBooleanExtra("isDrinker", true))
//                    search = database.child("drinkers").orderByKey();
//                else
//                    search = database.child("merchants").orderByKey();









                Intent myIntent = new Intent(view.getContext(), CheckoutActivity.class);

                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                myIntent.putExtra("bag", bag);
                startActivityForResult(myIntent, 0);

                Toast toast = Toast.makeText(getApplicationContext(), "Order placed!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                System.out.println("____________________HEREEEEEEE");
//                search.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        System.out.println("____________________Entered the function");
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            if (snapshot.getKey().equals(getIntent().getStringExtra("currentUser"))) {
//                                Drinker drinker;
//                                if (getIntent().getBooleanExtra("isDrinker", true))
//                                    drinker = snapshot.getValue(Drinker.class);
//                                else
//                                    drinker = snapshot.getValue(Merchant.class);
//                                drinker.id = snapshot.getKey();
//                                drinker.orderHistory.add(bag);
//                                System.out.println("____________________Added to bag");
//                                drinker.submitToDatabase();
//
//                                System.out.println("____________________GOING TO CHECKOUT ACTIVITY");
//
//                                Intent newIntent = new Intent(view.getContext(), CheckoutActivity.class);
////                                Intent myIntent;
////                                if (getIntent().getBooleanExtra("isDrinker", true))
////                                  myIntent = new Intent(view.getContext(), DrinkerMainActivity.class);
////                                else
////                                    myIntent = new Intent(view.getContext(), MerchantMainActivity.class);
////                                String currentUser = getIntent().getStringExtra("currentUser");
////                                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
////                                newIntent.putExtra("currentUser", currentUser);
////                                newIntent.putExtra("isDrinker", isDrinker);
//                                //newIntent.putExtra("bag", bag);
//
//                                System.out.println("____________________PASSED VALUES");
//
//                                //startActivityForResult(myIntent, 0);
//
//                                System.out.println("____________________STARTING NEXT ACTIVITY");
//
//                                Toast toast = Toast.makeText(getApplicationContext(), "Order placed!", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//                                toast.show();
//                                startActivity(newIntent);
//                                System.out.println("____________________INTENT STARTED");
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
            }
        });
    }
    public void addMenuItem(Item item)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);
        String itemContent = item.name + "             " + item.caffeine + "mg" + "            " + "$" + item.price;
        newItem = new Button(this);
        newItem.setText(itemContent);
        layout.addView(newItem);
        newItem.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view)
            {
                bag.items.add(item);
                LocalDate localDate = LocalDate.now();
                System.out.println("______________Local date: " + localDate);
                bag.dateOfOrder = localDate.toString();
                bag.fromStore = storeName;
                System.out.println("______________added item: " + item.name);
            }

        });
    }
}
