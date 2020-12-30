package com.kunaldhongadi.milkydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MonthlyRecord extends AppCompatActivity {
    public static final String TAG = "TAG";

    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;

    private ArrayList<Bill> bills = new ArrayList<>();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    MaterialToolbar toolbar;

    ArrayList<String> monthList = new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July",
        "August","September","October","November","December"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_record);

        recyclerView2 = findViewById(R.id.monthly_rec_recycler_view);

        toolbar = findViewById(R.id.m_top_toolBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        // Today's Date
        Date today = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        final int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);

        // Intent to MainActivity when pressed back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonthlyRecord.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        final CollectionReference collectionReference = fStore.collection("users").document(userId).
                collection("records");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   for (DocumentSnapshot document: task.getResult()){
                       // To get the count of total days for the month
                       // For the current month ,get the current date
                       int count;

                       // Getting the last day of the month
                       String loopedMonth = document.getId().replaceAll("[^a-zA-Z]+", "");
                       String year = document.getId().replaceAll("[^0-9]", "");
                       Calendar calendar = Calendar.getInstance();
                       int loopedYearInt = Integer.parseInt(year);
                       int loopedMonthInt = monthList.indexOf(loopedMonth);
                       calendar.set(loopedYearInt,loopedMonthInt,1);

                       if (loopedMonthInt == currentMonth && loopedYearInt == currentYear) {
                           count = currentDay - document.getData().size();
                           bills.add(new Bill(document.getId() + " (until today)",count,count * 58));
                       } else {
                           int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                           count = totalDaysInMonth - document.getData().size();
                           bills.add(new Bill(document.getId(),count,count * 58));
                       }


                   }
                   initializeRecyclerView();
               } else {
                   Toast.makeText(MonthlyRecord.this, "There was an error\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           }
        });
    }

    public void initializeRecyclerView() {
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewAdapter2 = new RecyclerViewAdapter2(getApplicationContext(),bills);
        recyclerView2.setAdapter(recyclerViewAdapter2);
    }
}