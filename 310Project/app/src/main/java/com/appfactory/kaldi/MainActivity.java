package com.appfactory.kaldi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable
{
    private int id;
    boolean userFound;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView emailInput = (TextView) findViewById(R.id.email);
                TextView passwordInput = (TextView) findViewById(R.id.password);

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

//                if (!validateEmail(email))
//                {
//                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid email!", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();
//                }
                if (password.isEmpty())
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password is empty!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                    int radioButtonId = radioGroup.getCheckedRadioButtonId();
                    if (radioButtonId != -1)
                    {
                        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonId);
                        if (radioButton != null) ;
                        {
                            id = (Integer.parseInt((String) radioButton.getTag()));
                            DatabaseReference database;
                            if (id == 1)
                                database = FirebaseDatabase.getInstance().getReference("users").child("drinkers");
                            else
                                database = FirebaseDatabase.getInstance().getReference("users").child("merchants");
                            database.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                    {
                                        if (snapshot.exists())
                                        {
                                            Drinker drinker;
                                            if (id == 1)
                                                drinker = snapshot.getValue(Drinker.class);
                                            else
                                                drinker = snapshot.getValue(Merchant.class);
                                            drinker.id = snapshot.getKey();
                                            String drinkerEmail = drinker.email.toLowerCase();
                                            String userEmail = email.toLowerCase();

                                            if (drinkerEmail.equals(userEmail))
                                            {
                                                userFound = true;
                                                if (!drinker.password.equals(password))
                                                {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Password is incorrect!", Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                                    toast.show();
                                                }
                                                //Log in
                                                else {
                                                    Intent myIntent;
                                                    if (id == 1) {
                                                        myIntent = new Intent(view.getContext(), DrinkerMainActivity.class);
                                                        myIntent.putExtra("isDrinker", true);
                                                    } else {
                                                        myIntent = new Intent(view.getContext(), MerchantMainActivity.class);
                                                        myIntent.putExtra("isDrinker", false);

                                                    }
                                                    System.out.println("_____________LOGGING IN");
                                                    myIntent.putExtra("currentUser", drinker.id);
                                                    myIntent.putExtra("currentUserName", drinker.name);

                                                    System.out.println("MAIN ACTIVITY NAME: " + drinker.name);
                                                    startActivity(myIntent);
                                                    break;
                                                }
                                            }
                                        }
                                        else{
                                            System.out.println("----------  Account does not exist!");
                                            Toast toast = Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                            toast.show();
                                        }
                                    }
                                    if(userFound != true){
                                        System.out.println("----------  Account does not exist!");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                        toast.show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });
                        }
                    }
                }
            }
        });
        Button button2 = (Button) findViewById(R.id.regButton);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivity(myIntent);
            }

        });
    }

    /**
     * Validates that the input given is in the correct format.
     *
     * @param email
     * @return
     */
    public static boolean validateEmail(String email)
    {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return false;
        }
        return true;
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