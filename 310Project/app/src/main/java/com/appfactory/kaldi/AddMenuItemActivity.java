package com.appfactory.kaldi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddMenuItemActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        TextView itemInput = (TextView) findViewById(R.id.itemName);
        TextView caffeineInput = (TextView) findViewById(R.id.caffeine);
        TextView costInput = (TextView) findViewById(R.id.costInput);
        Button addMenuItem =  (Button) findViewById(R.id.addButton);
        addMenuItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String businessName = getIntent().getStringExtra("storeName");
                String userName = getIntent().getStringExtra("currentUser");
                String itemName = itemInput.getText().toString();
                String caffeine = caffeineInput.getText().toString();
                String cost = costInput.getText().toString();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                Query search = database.child("merchants").orderByKey().equalTo(userName);
                search.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            if (snapshot.getKey().equals(userName))
                            {
                                Merchant merchant = snapshot.getValue(Merchant.class);
                                if(merchant != null)
                                {
                                    merchant.id = snapshot.getKey();
                                    for (int i = 0; i < merchant.stores.size(); i++)
                                    {
                                        if (merchant.stores.get(i).storeName.equals(businessName))
                                        {
                                            merchant.stores.get(i).menu.addItem(new Item(itemName, Integer.parseInt(caffeine), Double.parseDouble(cost)));
                                            merchant.submitToDatabase();
                                        }
                                        break;
                                    }
                                    Intent myIntent = myIntent = new Intent(getApplicationContext(), MerchantMainActivity.class);
                                    String currentUser = getIntent().getStringExtra("currentUser");
                                    boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                                    myIntent.putExtra("currentUser", currentUser);
                                    myIntent.putExtra("isDrinker", isDrinker);
                                    startActivityForResult(myIntent, 0);
                                    Toast toast = Toast.makeText(getApplicationContext(), "New item Added!", Toast.LENGTH_LONG);
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

    /**
     *
     */
    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
