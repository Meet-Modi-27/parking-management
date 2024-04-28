package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parking_management.models.vehicleModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class new_vehicle extends AppCompatActivity {

    String item[] = {"2-Wheeler", "4-Wheeler"};
    String dropDownItem;
    Button add;

    AutoCompleteTextView vehicle_type;

    ArrayAdapter<String> adapterItems;

    TextInputEditText make,model,numberPlate;
    TextInputLayout number;

    String NumberPlatePattern = "[A-Z]{2}-[0-9]{2}-[A-Z]{2}-[0-9]{4}";
    ProgressBar progressBar;
    String userId;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vehicle);

        add = findViewById(R.id.add_vehcile_btn);
        make= findViewById(R.id.vehicle_make);
        model= findViewById(R.id.vehicle_model);
        numberPlate= findViewById(R.id.vehicle_number);
        number= findViewById(R.id.vehicle_number_layout);

        userId = getIntent().getStringExtra("userId");

        progressBar = findViewById(R.id.progress_bar);
        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("users");

        vehicle_type = findViewById(R.id.vehicle_type);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        vehicle_type.setAdapter(adapterItems);
        vehicle_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedItem(parent.getItemAtPosition(position).toString());
            }
        });
    }
    private void updateSelectedItem(String selectedItem) {
        dropDownItem = selectedItem;
        if (dropDownItem!=null){
        }
    }
    private void performRegistration() {
        String Make = make.getText().toString();
        String Model = model.getText().toString();
        String NumberPlate = numberPlate.getText().toString();
        String Selector = dropDownItem;

        if (Make.isEmpty()){
            make.setError("Enter the Make.");
        } else if(Model.isEmpty()){
            model.setError("Enter the Model.");
        } else if (!NumberPlate.matches(NumberPlatePattern)){
            number.setError("Enter Correct Number Plate.\nFormat: XX-XX-XX-XXXX");
        } else if (Selector.isEmpty()){
            vehicle_type.setError("Select the vehicle Type");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            UpdateDataToFirebase(Make,Model,NumberPlate,Selector);
            progressBar.setVisibility(View.INVISIBLE);
            SendUserToNextActivity();
            Toast.makeText(new_vehicle.this, "Vehicle Data Added!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateDataToFirebase(String MAKE, String MODEL, String NUMBERPLATE, String SELECTOR) {
        DatabaseReference userRef = db.child("Reg_vehicles").child(userId);
        String vehicleId = userRef.push().getKey();
        vehicleModel vehicleModel = new vehicleModel(MAKE, MODEL, NUMBERPLATE, SELECTOR, userId);
        userRef.child(vehicleId).setValue(vehicleModel);

    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(new_vehicle.this, vehicle_details.class);
        intent.putExtra("userId",userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}