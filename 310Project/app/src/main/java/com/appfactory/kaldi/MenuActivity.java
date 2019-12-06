package com.appfactory.kaldi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.List;

public class MenuActivity extends AppCompatActivity
{
    private Button newItem;
    private Order bag = new Order();
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
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child("merchants");
        database.addValueEventListener(new ValueEventListener()
        {
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
            public void onClick(View view)
            {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                Query search;
                if (getIntent().getBooleanExtra("isDrinker", true))
                    search = database.child("drinkers").orderByKey();
                else
                    search = database.child("merchants").orderByKey();
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(getIntent().getStringExtra("currentUser"))) {
                                Drinker drinker;
                                if (getIntent().getBooleanExtra("isDrinker", true))
                                    drinker = snapshot.getValue(Drinker.class);
                                else
                                    drinker = snapshot.getValue(Merchant.class);
                                drinker.id = snapshot.getKey();
                                drinker.orderHistory.add(bag);
                                drinker.submitToDatabase();

                                //Intent myIntent = new Intent(view.getContext(), CheckoutActivity.class); Might want to bring this page back
                                Intent myIntent;
                                if (getIntent().getBooleanExtra("isDrinker", true))
                                  myIntent = new Intent(view.getContext(), DrinkerMainActivity.class);
                                else
                                    myIntent = new Intent(view.getContext(), MerchantMainActivity.class);
                                String currentUser = getIntent().getStringExtra("currentUser");
                                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                                myIntent.putExtra("currentUser", currentUser);
                                myIntent.putExtra("isDrinker", isDrinker);
                                startActivityForResult(myIntent, 0);

                                Toast toast = Toast.makeText(getApplicationContext(), "Order placed!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                                break;
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
    public void addMenuItem(Item item)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);
        String itemContent = item.name + "             " + item.caffeine + "mg" + "            " + "$" + item.price;
        newItem = new Button(this);
        newItem.setText(itemContent);
        layout.addView(newItem);
        newItem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                bag.items.add(item);
            }
        });
    }
}
