package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parking_management.models.spotModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class qr_code extends AppCompatActivity {

    ImageView qr;
    String key;
    DatabaseReference dbRef;
    String item[] = {"Location 1"
            , "Location 2"
            , "Location 3"
    };
    String item1[] = {"2-Wheeler"
            , "4-Wheeler"
    };
    String dropDownItem;
    String dropDownItem1;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems1;
    AutoCompleteTextView location;
    AutoCompleteTextView vehicle;
    Button generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        qr = findViewById(R.id.qr_code);

        location = findViewById(R.id.loc_type);
        vehicle = findViewById(R.id.vehicle_type);
        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_item, item);
        location.setAdapter(adapterItems);
        adapterItems1 = new ArrayAdapter<>(this, R.layout.dropdown_item, item1);
        vehicle.setAdapter(adapterItems1);
        generate = findViewById(R.id.Generate);
        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedItem(parent.getItemAtPosition(position).toString());
            }
        });
        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedItem(parent.getItemAtPosition(position).toString());
            }
        });

        vehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedItem1(parent.getItemAtPosition(position).toString());
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushData(dropDownItem,dropDownItem1);
            }
        });
    }
    private void updateSelectedItem(String selectedItem) {
        dropDownItem = selectedItem;
    }private void updateSelectedItem1(String selectedItem) {
        dropDownItem1 = selectedItem;
    }

    private void pushData(String loc1, String vehicle1) {
        // Get a reference to the counter node for the specific location in your Firebase Database
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference().child("counter").child(loc1);

        // Increment the counter value for the specific location
        counterRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long currentValue = mutableData.getValue(Long.class);
                if (currentValue == null) {
                    // Initialize the counter if it doesn't exist
                    mutableData.setValue(1);
                } else {
                    // Increment the counter
                    mutableData.setValue(currentValue + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    // Handle any errors that occurred during the transaction
                    Toast.makeText(qr_code.this, "Counter transaction failed.", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the new counter value after incrementing
                    Long counterValue = dataSnapshot.getValue(Long.class);
                    if (counterValue != null) {
                        // Use the new counter value as the key, along with the location
                        String newKey = loc1 + "_" + counterValue;

                        // Get a reference to the 'qr' node using the location
                        DatabaseReference qrRef = FirebaseDatabase.getInstance().getReference().child("qr");

                        // Create a new spotModel instance with the updated key
                        spotModel SpotModel = new spotModel(loc1, vehicle1, newKey, true);

                        // Push the data to Firebase
                        qrRef.child(newKey).setValue(SpotModel);

                        // Generate QR code using the new key
                        generateQRCode(newKey);
                    } else {
                        Toast.makeText(qr_code.this, "Counter value is null.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void generateQRCode(String key) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(key, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}