package com.appfactory.kaldi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class RegisterMerchantActivity extends AppCompatActivity implements Serializable
{
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__merchant);
        Button registerButton = (Button)findViewById(R.id.addBusiness);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView nameInput = findViewById(R.id.adminInput);
                TextView storeNameInput = findViewById(R.id.storeInput);
                TextView emailInput = findViewById(R.id.emailInput);
                TextView passwordInput = findViewById(R.id.passwordInput);
                TextView confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
                TextView addressInput = findViewById(R.id.addressInput);
                TextView initialItemInput = findViewById(R.id.initialItemInput);
                TextView caffeineInput = findViewById(R.id.caffeineInput);
                TextView costInput = findViewById(R.id.costInput);

                String name = nameInput.getText().toString();
                String storeName = storeNameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();
                String address = addressInput.getText().toString();
                String initialItem = initialItemInput.getText().toString();
                String price = costInput.getText().toString();

                String caffeine = caffeineInput.getText().toString();

                if (!password.equals(confirmPassword))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                    Query search = database.child("merchants").orderByChild("email").equalTo(email);
                    search.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            boolean emailExists = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                if (snapshot.getValue(Merchant.class).email.equals(email))
                                {
                                    emailExists = true;
                                    Toast toast = Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                    break;
                                }
                            }
                            if (!emailExists)
                            {
                                Merchant merchant = new Merchant(name, password, email, storeName, address, new Menu(new Item(initialItem, Integer.parseInt(caffeine), Double.parseDouble(price))));
                                merchant.submitToDatabase();

                                //Update Page
                                Intent myIntent = new Intent(view.getContext(), MerchantMainActivity.class);
                                myIntent.putExtra("currentUser", merchant.id);
                                myIntent.putExtra("isDrinker", false);
                                startActivity(myIntent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }
        });
    }
}
