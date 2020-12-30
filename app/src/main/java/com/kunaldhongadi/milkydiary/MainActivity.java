package com.kunaldhongadi.milkydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "TAG";

    TextView currentDate, currentDay, cardMessage;
    ImageButton yesBtn, noBtn;
    ImageView toggle;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDate = findViewById(R.id.current_date);
        currentDay = findViewById(R.id.current_day);
        cardMessage = findViewById(R.id.card_message);
        toggle = findViewById(R.id.toggle);
        yesBtn = findViewById(R.id.yes_btn);
        noBtn = findViewById(R.id.no_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        final int day = cal.get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MMMM").format(today);
        int year = cal.get(Calendar.YEAR);

        final String record_id = month + " " +  year;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        currentDate.setText(dateFormat.format(today));
        currentDay.setText(dayFormat.format(today));

        navigationView.setNavigationItemSelectedListener(this);

        // Getting the root document reference
        final DocumentReference documentReference = fStore
                .collection("users").document(userId);

        // Getting the record document reference
        final DocumentReference monthReference = documentReference
                .collection("records").document(record_id);


        buttonStatus(monthReference,day);


        // Toggle to open the navigation drawer
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // If the yes button is clicked.
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noBtn.setHapticFeedbackEnabled(true);

                monthReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            Map<String, Object> record = new HashMap<>();
                            record.put(String.valueOf(day),new Timestamp(new Date()));

                            if (doc.exists()) {
                                // If the month collection exists, add to the current month
                                monthReference.update(record).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Okay, No Milk today!!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // create a new month collection and add the docs in it
                                monthReference.set(record).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Okay, No Milk today!!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            activeOrNot(false);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(
                                    MainActivity.this,
                                    "There was a problem\n" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noBtn.setHapticFeedbackEnabled(true);

                // Remove the 'timestamp' field from the document
                Map<String,Object> updates = new HashMap<>();
                updates.put(String.valueOf(day), FieldValue.delete());

                monthReference.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Okay,Got it!\nIts milk day!",
                                Toast.LENGTH_SHORT).show();
                        activeOrNot(true);
                    }
                });
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.daily_record:
                navigationView.setCheckedItem(R.id.daily_record);
                Intent intent = new Intent(MainActivity.this,DailyRecord.class);
                startActivity(intent);
                break;
            case R.id.monthly_record:
                navigationView.setCheckedItem(R.id.monthly_record);
                Intent intent2 = new Intent(MainActivity.this,MonthlyRecord.class);
                startActivity(intent2);
                break;
            case R.id.log_off:
                Toast.makeText(this, "Logg off btn clicked", Toast.LENGTH_SHORT).show();
                fAuth.signOut();
                Log.v(TAG,"User logged off");
                Intent loginIntent = new Intent(MainActivity.this,Login.class);
                startActivity(loginIntent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void buttonStatus(DocumentReference docRef, final int currentDay){
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        if (doc.get(String.valueOf(currentDay)) != null) {
                            Log.d(TAG,"If no--" + doc.get(String.valueOf(currentDay)));
                            activeOrNot(false);
                            Toast.makeText(MainActivity.this, "No Milk Today", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // This function changes the color of the button of the selected option
    public void activeOrNot(Boolean status) {
        if (!status) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                yesBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorOffWhite)));
                noBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pressedButton)));
            } else {
                yesBtn.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
                noBtn.setBackgroundColor(getResources().getColor(R.color.pressedButton));
            }
            cardMessage.setText("No Milk Today :(");
            cardMessage.setVisibility(View.VISIBLE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                noBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorOffWhite)));
                yesBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pressedButton)));
            } else {
                noBtn.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
                yesBtn.setBackgroundColor(getResources().getColor(R.color.pressedButton));
            }
            cardMessage.setVisibility(View.GONE);
        }
    }

}
