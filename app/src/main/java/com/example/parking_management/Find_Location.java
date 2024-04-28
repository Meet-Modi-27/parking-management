package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Find_Location extends AppCompatActivity {


    String item[] = {"Select"
            , "Location 1"
            , "Location 2"
            , "Location 3"
    };
    String dropDownItem;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView location;
    ImageView image;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        location = findViewById(R.id.location);
        image = findViewById(R.id.finder_image);
        text = findViewById(R.id.finder_text);

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

        if ("Location 1".equals(dropDownItem)) {
            image.setImageResource(R.drawable.parking_logo);
            text.setText("In front of Arch gate");
            image.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        } else if ("Location 2".equals(dropDownItem)) {
            image.setImageResource(R.drawable.qr_code_scan_icon);
            text.setText("In front of Tech Park");
            image.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        } else if ("Location 3".equals(dropDownItem)) {
            image.setImageResource(R.drawable.parking_svgrepo_com);
            text.setText("Between Architecture and Biotech Building");
            image.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
        }
    }
}