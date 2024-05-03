package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking_management.adapters.dashboard_adapter;
import com.example.parking_management.adapters.spotId_adapter;
import com.example.parking_management.models.spotModel;
import com.example.parking_management.models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Available_slots extends AppCompatActivity {

    String[] item = {"Select",
            "Location 1",
            "Location 2",
            "Location 3"};

    String dropDownItem, userId;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView location;

    LinearLayout count, detail;
    RecyclerView detail_rv;
    TextView counting;

    spotId_adapter adapter;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_slots);

        userId = getIntent().getStringExtra("userId");
        location = findViewById(R.id.location);
        count = findViewById(R.id.slot_count_ly);
        detail = findViewById(R.id.slots_ly);
        counting = findViewById(R.id.slot_count);
        detail_rv = findViewById(R.id.slots_rv);
        detail_rv.setLayoutManager(new LinearLayoutManager(this));

        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_item, item);
        location.setAdapter(adapterItems);

        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedItem(parent.getItemAtPosition(position).toString());
            }
        });

    }

    private void updateSelectedItem(String selectedItem) {
        dropDownItem = selectedItem;
        dbRef = FirebaseDatabase.getInstance().getReference().child("counter").child("Temp_Counter");

        if (counting == null) {
            Toast.makeText(this, "TextView counting is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dropDownItem.equals("Select")) {
            count.setVisibility(View.INVISIBLE);
            detail.setVisibility(View.INVISIBLE);
        } else if (dropDownItem.equals("Location 1")) {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Long value = snapshot.child("Location 1").getValue(Long.class);
                        if (value != null) {
                            counting.setText(String.valueOf(value)); // Display as String
                        } else {
                            counting.setText("0");
                        }
                    } else {
                        counting.setText("Data not available");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(Available_slots.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            });

            runAdapter(dropDownItem);
            count.setVisibility(View.VISIBLE);
            detail.setVisibility(View.VISIBLE);
        } else if (dropDownItem.equals("Location 2")) {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(Available_slots.this, "Finding Data", Toast.LENGTH_SHORT).show();
                        Long value = snapshot.child("Location 2").getValue(Long.class);
                        if (value != null) {
                            counting.setText(String.valueOf(value)); // Display as String
                        } else {
                            counting.setText("0");
                        }
                    } else {
                        counting.setText("Data not available");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(Available_slots.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            });

            runAdapter(dropDownItem);
            count.setVisibility(View.VISIBLE);
            detail.setVisibility(View.VISIBLE);
        } else if (dropDownItem.equals("Location 3")) {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(Available_slots.this, "Finding Data", Toast.LENGTH_SHORT).show();
                        Long value = snapshot.child("Location 3").getValue(Long.class);
                        if (value != null) {
                            counting.setText(String.valueOf(value)); // Display as String
                        } else {
                            counting.setText("0");
                        }
                    } else {
                        counting.setText("Data not available");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(Available_slots.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            });

            runAdapter(dropDownItem);
            count.setVisibility(View.VISIBLE);
            detail.setVisibility(View.VISIBLE);
        }else{
            count.setVisibility(View.INVISIBLE);
            detail.setVisibility(View.INVISIBLE);
        }
    }

    private void runAdapter(String Loc) {

        DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference();
        Query query = userRef.child("qr").child("empty").child(Loc);
        FirebaseRecyclerOptions<spotModel> options =
                new FirebaseRecyclerOptions.Builder<spotModel>()
                        .setQuery(query, spotModel.class)
                        .build();

        adapter = new spotId_adapter(options);
        detail_rv.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
