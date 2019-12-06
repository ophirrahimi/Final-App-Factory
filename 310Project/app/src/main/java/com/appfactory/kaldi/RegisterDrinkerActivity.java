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

public class RegisterDrinkerActivity extends AppCompatActivity implements Serializable
{
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__drinker);
        Button registerButton = (Button)findViewById(R.id.addBusiness);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView nameInput = findViewById(R.id.adminInput);
                TextView emailInput = findViewById(R.id.emailInput);
                TextView passwordInput = findViewById(R.id.passwordInput);
                TextView confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                if (!password.equals(confirmPassword))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                    Query search = database.child("drinkers").orderByChild("email").equalTo(email);
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
                                Drinker drinker = new Drinker(name, password, email);
                                drinker.submitToDatabase();

                                //Update Page
                                Intent myIntent = new Intent(view.getContext(), DrinkerMainActivity.class);
                                myIntent.putExtra("currentUser", drinker.id);
                                myIntent.putExtra("isDrinker", true);
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
