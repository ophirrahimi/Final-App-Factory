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

public class ManageProfileActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__profile);

        TextView nameInput = (TextView) findViewById(R.id.name);
        TextView emailInput = (TextView) findViewById(R.id.email);
        TextView passwordInput = (TextView) findViewById(R.id.password);

        String name = passwordInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        String currentUserName = getIntent().getStringExtra("currentUserName");
        TextView profileHeader = (TextView) findViewById(R.id.textView6);
        String header = "";
        if(currentUserName!=null) {
            header += currentUserName;
            header += "'s Profile";
        }else{
            header += "Edit Profile";
        }
        profileHeader.setText(header);

        Button checkout =  (Button) findViewById(R.id.submit);
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
                                if (!name.isEmpty())
                                    drinker.name = name;
                                if (!email.isEmpty())
                                    drinker.email = email;
                                if (!password.isEmpty())
                                    drinker.password = password;
                                drinker.submitToDatabase();

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

                                Toast toast = Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_LONG);
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
}
