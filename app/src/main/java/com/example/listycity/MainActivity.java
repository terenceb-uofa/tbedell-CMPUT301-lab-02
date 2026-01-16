package com.example.listycity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private Button deleteButton;
    private Button addButton;
    private Button confirmButton;
    private ListView cityList;

    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;
    LinearLayout inputContainer;

    int selectedIndex= -1; // The index of the selected city (-1 is the "no select" state)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get elements
        initViews();
        //Setup the patched array adapter
        setupAdapter();
        //Input Listeners
        setupListeners();


    }




    private void initViews() {
        //Assign all local view variables their appropriate view

        //Layout
        cityList = findViewById(R.id.city_list);
        inputContainer = findViewById(R.id.city_field_container);

        //Buttons
        deleteButton = findViewById(R.id.delete_city);
        addButton = findViewById(R.id.add_city);
        confirmButton = findViewById(R.id.city_input_confirm);

        //Input field
        cityInput = findViewById(R.id.city_input);
    }


    private  void setupAdapter(){
        String[] cities = {"Edmonton", "Vancouver", "Moscow", "Sidney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"};

        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));

        //Patching the array adapter class on assignment
        cityAdapter = new ArrayAdapter<String>(this, R.layout.content, dataList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the default view object
                View view = super.getView(position, convertView, parent);

                //Patch logic for updating highlights
                if (position == selectedIndex) {
                    view.setBackgroundColor(android.graphics.Color.LTGRAY);
                } else {
                    view.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                }

                return view;
            }
        };

        cityList.setAdapter(cityAdapter);
    }

    private void setupListeners(){
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Set the new index to the position in the list of the element
                selectedIndex = position;
                cityAdapter.notifyDataSetChanged(); //re-render cities (selection proc)

            }
        });
        deleteButton.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                Object cityName =  cityList.getItemAtPosition(selectedIndex).toString();
                Toast.makeText(this, String.format("%s was deleted!", cityName ), Toast.LENGTH_SHORT).show();

                dataList.remove(selectedIndex);
                selectedIndex = -1;              // reset to no selection
                cityAdapter.notifyDataSetChanged(); // re-render cities
            }
        });
        addButton.setOnClickListener(v -> {
            // Toggle visibility
            int currentVisibility = inputContainer.getVisibility();
            int newVisibility = currentVisibility == View.GONE? View.VISIBLE : View.GONE;
            inputContainer.setVisibility(newVisibility);
        });
        confirmButton.setOnClickListener(v -> {
            // get city name input
            String cityName = cityInput.getText().toString();

            //validate name (early exit)
            if (cityName.isBlank()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }
            //valid name
            dataList.add(cityName);            // add city to the list
            cityAdapter.notifyDataSetChanged(); // notify adapter for re-render
            cityInput.setText("");
            String successMessage =  String.format("%s was added!", cityName);
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();


        });
    }
}

