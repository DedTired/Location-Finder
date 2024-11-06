package com.example.locationfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private LinearLayout addSection, querySection, updateSection, deleteSection;
    private DatabaseReference databaseReference;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");

        // Find views for each section
        addSection = findViewById(R.id.addSection);
        querySection = findViewById(R.id.querySection);
        updateSection = findViewById(R.id.updateSection);
        deleteSection = findViewById(R.id.deleteSection);
        resultTextView = findViewById(R.id.resultTextView);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        MaterialButton buttonQuery = findViewById(R.id.buttonQuery);
        MaterialButton buttonUpdate = findViewById(R.id.buttonUpdate);
        MaterialButton buttonDelete = findViewById(R.id.buttonDelete);

        // Set up Add button listener
        buttonAdd.setOnClickListener(v -> addLocation());

        // Set up Query button listener
        buttonQuery.setOnClickListener(v -> queryLocation());

        // Set up Update button listener
        buttonUpdate.setOnClickListener(v -> updateLocation());

        // Set up Delete button listener
        buttonDelete.setOnClickListener(v -> deleteLocation());
    }

    private void addLocation() {
        String address = ((TextView) findViewById(R.id.inputAddress)).getText().toString().trim();
        String latitudeStr = ((TextView) findViewById(R.id.inputLatitude)).getText().toString().trim();
        String longitudeStr = ((TextView) findViewById(R.id.inputLongitude)).getText().toString().trim();

        if (address.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            resultTextView.setText("Please fill in all fields");
            return;
        }

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);
        String id = databaseReference.push().getKey();

        Location location = new Location(id, address, latitude, longitude);
        if (id != null) {
            databaseReference.child(id).setValue(location).addOnSuccessListener(aVoid -> {
                resultTextView.setText("Location added successfully");
                clearInputFields();
            }).addOnFailureListener(e -> {
                resultTextView.setText("Failed to add location: " + e.getMessage());
            });
        }
    }

    private void queryLocation() {
        String address = ((TextView) findViewById(R.id.inputQueryAddress)).getText().toString().trim();

        if (address.isEmpty()) {
            resultTextView.setText("Please enter an address to search");
            return;
        }

        databaseReference.orderByChild("address").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Location location = snapshot.getValue(Location.class);
                        if (location != null) {
                            TextView queryResultTextView = findViewById(R.id.queryResultTextView);
                            queryResultTextView.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());

                        }
                    }
                } else {
                    resultTextView.setText("No location found with that address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultTextView.setText("Failed to query location: " + databaseError.getMessage());
            }
        });
    }

    private void updateLocation() {
        String address = ((TextView) findViewById(R.id.inputAddress)).getText().toString().trim();
        String latitudeStr = ((TextView) findViewById(R.id.inputLatitude)).getText().toString().trim();
        String longitudeStr = ((TextView) findViewById(R.id.inputLongitude)).getText().toString().trim();

        if (address.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            resultTextView.setText("Please fill in all fields");
            return;
        }

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);

        databaseReference.orderByChild("address").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("latitude").setValue(latitude);
                        snapshot.getRef().child("longitude").setValue(longitude);
                    }
                    resultTextView.setText("Location updated successfully");
                    clearInputFields();
                } else {
                    resultTextView.setText("No location found with that address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultTextView.setText("Failed to update location: " + databaseError.getMessage());
            }
        });
    }

    private void deleteLocation() {
        String address = ((TextView) findViewById(R.id.inputQueryAddress)).getText().toString().trim();

        if (address.isEmpty()) {
            resultTextView.setText("Please enter the address to delete");
            return;
        }

        databaseReference.orderByChild("address").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                            resultTextView.setText("Location deleted successfully");
                            clearInputFields();
                        }).addOnFailureListener(e -> {
                            resultTextView.setText("Failed to delete location: " + e.getMessage());
                        });
                    }
                } else {
                    resultTextView.setText("No location found with that address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultTextView.setText("Error while deleting: " + databaseError.getMessage());
            }
        });
    }

    private void clearInputFields() {
        ((TextView) findViewById(R.id.inputAddress)).setText("");
        ((TextView) findViewById(R.id.inputLatitude)).setText("");
        ((TextView) findViewById(R.id.inputLongitude)).setText("");
        ((TextView) findViewById(R.id.inputQueryAddress)).setText("");
    }
}
