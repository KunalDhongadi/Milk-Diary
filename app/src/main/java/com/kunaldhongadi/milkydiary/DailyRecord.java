package com.kunaldhongadi.milkydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DailyRecord extends AppCompatActivity {
    public static final String TAG = "TAG";

    TextView day,date;
    ImageView statusTick;
    RecyclerView recyclerView;
    MaterialToolbar toolbar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

//    RecyclerViewAdapter recyclerViewAdapter;
//    private ArrayList<Record> records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_record);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.top_toolBar);

        final ArrayList<String> monthList = new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July",
                "August","September","October","November","December"));

        recyclerView = findViewById(R.id.recycler_view);

//        ---------------------------------------
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting the current timestamp
        Date today = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        final int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);

        final String month = new SimpleDateFormat("MMMM").format(today);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

        final int totalDays  = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);

        final String record_id = month + " " +  year;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DailyRecord.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        final ArrayList<Month> months = new ArrayList<>();

        final CollectionReference collectionReference = fStore.collection("users").document(userId).
                collection("records");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String heading = document.getId();

                        ArrayList<String> crossedDays = new ArrayList<>();
                        ArrayList<Days> days = new ArrayList<>();

                        // Getting the crossed out dates
                        crossedDays.addAll(document.getData().keySet());
                        Log.d(TAG,"the crossed out list is" + crossedDays.toString());

                        // Getting the last day of the month
                        String loopedMonth = document.getId().replaceAll("[^a-zA-Z]+", "");
                        String year = document.getId().replaceAll("[^0-9]", "");

                        Calendar calendar = Calendar.getInstance();;
                        int looped_month = monthList.indexOf(loopedMonth);
                        int looped_year = Integer.parseInt(year);
                        calendar.set(looped_year,looped_month,1);
                        int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                        Log.d(TAG,document.getId() + "---" + document.getData());

                        // Iterating through the complete month
                        for (int i = 1; i <= totalDaysInMonth; i++) {

                            // Getting the current looped timestamp
                            Calendar c = Calendar.getInstance();
                            c.set(looped_year,looped_month, i);
                            Date d = c.getTime();

                            // Getting the string formats for the looped dates to print
                            String dateString = dateFormat.format(d);
                            String dayString = dayFormat.format(d);

                            Log.d(TAG,"Looped month-" + looped_year + "," + looped_month +  "," + i);
                            Log.d(TAG,"Current-" + currentYear + "," + currentMonth + "," + currentDay);

                            // Getting all the days up to current day
                            if(looped_year == currentYear  && looped_month == currentMonth && i == currentDay + 1){
                                Log.d(TAG,"Stopped");
                                break;
                            }

                            boolean mark = false;
                            // Iterating through the crossed out dates
                            for (String day : crossedDays) {
                                Log.d(TAG,"Compare- i:" + i + "/day:" + Integer.parseInt(day));
                                if (i == Integer.parseInt(day)) {
                                    days.add(new Days(dateString,dayString,R.drawable.ic_closed));
                                    mark = true;
                                }
                            }
                            if(!mark) {
                                days.add(new Days(dateString,dayString,R.drawable.ic_checked));
                            }

                        }
                        months.add(new Month(heading,days));
                    }
                    DaysAdapter adapter = new DaysAdapter(months);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(DailyRecord.this,
                            "There was a problem.", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        ArrayList<Days> days = new ArrayList<>();
//        days.add(new Days("12/03/2009","monday",R.drawable.ic_wrong_icon));
//        days.add(new Days("12/03/2009","monday",R.drawable.ic_baseline_check_24));
//        days.add(new Days("12/03/2009","monday",R.drawable.ic_wrong_icon));
//        days.add(new Days("12/03/2009","monday",R.drawable.ic_wrong_icon));
//        days.add(new Days("12/03/2009","monday",R.drawable.ic_baseline_check_24));
//
//        Month jan = new Month("January", days);
//        months.add(jan);
//
//        ArrayList<Days> days2 = new ArrayList<>();
//        days2.add(new Days("12/03/2009","Tuesday",R.drawable.ic_wrong_icon));
//        days2.add(new Days("12/03/2009","Tuesday",R.drawable.ic_baseline_check_24));
//        days2.add(new Days("12/03/2009","Tuesday",R.drawable.ic_wrong_icon));
//        days2.add(new Days("12/03/2009","Tuesday",R.drawable.ic_wrong_icon));
//
//        Month feb = new Month("February",days2);
//        months.add(feb);




    }

}


