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
    private TextView queryResultTextView, addResultTextView, updateResultTextView, deleteResultTextView;

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

        queryResultTextView = findViewById(R.id.queryResultTextView);
        addResultTextView = findViewById(R.id.addResultTextView);
        updateResultTextView = findViewById(R.id.updateResultTextView);
        deleteResultTextView = findViewById(R.id.deleteResultTextView);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        MaterialButton buttonQuery = findViewById(R.id.buttonQuery);
        MaterialButton buttonUpdate = findViewById(R.id.buttonUpdate);
        MaterialButton buttonDelete = findViewById(R.id.buttonDelete);

        MaterialButton buttonToggleAddSection = findViewById(R.id.buttonToggleAddSection);
        MaterialButton buttonToggleQuerySection = findViewById(R.id.buttonToggleQuerySection);
        MaterialButton buttonToggleUpdateSection = findViewById(R.id.buttonToggleUpdateSection);
        MaterialButton buttonToggleDeleteSection = findViewById(R.id.buttonToggleDeleteSection);

        // Set up Add button listener
        buttonAdd.setOnClickListener(v -> addLocation());

        // Set up Query button listener
        buttonQuery.setOnClickListener(v -> queryLocation());

        // Set up Update button listener
        buttonUpdate.setOnClickListener(v -> updateLocation());

        // Set up Delete button listener
        buttonDelete.setOnClickListener(v -> deleteLocation());

        // Set up Toggle button listeners to expand/collapse each section
        buttonToggleAddSection.setOnClickListener(v -> toggleSectionVisibility(addSection));
        buttonToggleQuerySection.setOnClickListener(v -> toggleSectionVisibility(querySection));
        buttonToggleUpdateSection.setOnClickListener(v -> toggleSectionVisibility(updateSection));
        buttonToggleDeleteSection.setOnClickListener(v -> toggleSectionVisibility(deleteSection));

//        addSampleLocations();

    }
//    private void addSampleLocations() {
//        String[] locations = {
//                "Oshawa,43.8971,-78.8658",
//                "Ajax,43.8509,-79.0204",
//                "Pickering,43.8384,-79.0868",
//                "Scarborough,43.7731,-79.2578",
//                "Downtown Toronto,43.6510,-79.3470",
//                "Mississauga,43.5890,-79.6441",
//                "Brampton,43.7315,-79.7624",
//                "Markham,43.8561,-79.3370",
//                "Vaughan,43.8363,-79.4985",
//                "Richmond Hill,43.8828,-79.4403",
//                "Etobicoke,43.6435,-79.5650",
//                "North York,43.7615,-79.4111",
//                "York,43.6896,-79.4875",
//                "Thornhill,43.8130,-79.4205",
//                "Maple,43.8534,-79.5071",
//                "Woodbridge,43.7875,-79.6077",
//                "Concord,43.8012,-79.4982",
//                "King City,43.9248,-79.5287",
//                "Nobleton,43.9337,-79.6528",
//                "Aurora,44.0065,-79.4504",
//                "Newmarket,44.0592,-79.4613",
//                "Bradford,44.1146,-79.5590",
//                "Stouffville,43.9707,-79.2442",
//                "Uxbridge,44.1092,-79.1205",
//                "Brooklin,43.9635,-78.9576",
//                "Whitby,43.8975,-78.9429",
//                "Port Perry,44.1001,-78.9442",
//                "Clarington,43.9356,-78.6074",
//                "Bowmanville,43.9126,-78.6870",
//                "Courtice,43.9112,-78.7975",
//                "Newcastle,43.9237,-78.5944",
//                "Georgina,44.2964,-79.4274",
//                "Keswick,44.2230,-79.4596",
//                "Sutton,44.3045,-79.3633",
//                "Pefferlaw,44.3153,-79.2029",
//                "Mount Albert,44.1363,-79.3148",
//                "Ballantrae,43.9768,-79.3184",
//                "Unionville,43.8622,-79.3104",
//                "Cornell,43.8665,-79.2277",
//                "Box Grove,43.8537,-79.2191",
//                "Milliken,43.8253,-79.3009",
//                "Buttonville,43.8592,-79.3720",
//                "Cathedraltown,43.8693,-79.3676",
//                "Bayview Glen,43.8333,-79.3765",
//                "Cachet,43.8772,-79.3496",
//                "Victoria Square,43.8903,-79.3677",
//                "Berczy Village,43.8961,-79.3064",
//                "Greensborough,43.9112,-79.2566",
//                "Rouge Park,43.8103,-79.1329",
//                "Guildwood,43.7553,-79.1968",
//                "West Hill,43.7678,-79.1771",
//                "Port Union,43.7852,-79.1320",
//                "Highland Creek,43.7873,-79.1845",
//                "Morningside,43.7993,-79.2156",
//                "Woburn,43.7701,-79.2318",
//                "Malvern,43.8067,-79.2297",
//                "Agincourt,43.7879,-79.2676",
//                "Milliken Mills,43.8315,-79.3168",
//                "Middlefield,43.8361,-79.2701",
//                "Cedarwood,43.8383,-79.2586",
//                "Armour Heights,43.7371,-79.4267",
//                "Bathurst Manor,43.7544,-79.4564",
//                "Bayview Village,43.7697,-79.3750",
//                "Clanton Park,43.7490,-79.4395",
//                "Don Valley Village,43.7807,-79.3494",
//                "Downsview,43.7436,-79.4905",
//                "Glen Park,43.7066,-79.4537",
//                "Humber Summit,43.7667,-79.5622",
//                "Jane and Finch,43.7610,-79.4961",
//                "Kingsview Village,43.7030,-79.5546",
//                "Pelmo Park,43.7050,-79.5156",
//                "Rexdale,43.7277,-79.5563",
//                "Smithfield,43.7481,-79.5937",
//                "The Elms,43.7168,-79.5351",
//                "West Humber,43.7261,-79.5924",
//                "Woodbine Gardens,43.7045,-79.3095",
//                "O'Connor-Parkview,43.7051,-79.3151",
//                "The Beaches,43.6764,-79.2933",
//                "Riverdale,43.6667,-79.3477",
//                "East York,43.7045,-79.3275",
//                "Leaside,43.7095,-79.3631",
//                "Rosedale,43.6780,-79.3802",
//                "Yorkville,43.6708,-79.3948",
//                "Annex,43.6697,-79.4075",
//                "Davisville,43.7047,-79.3834",
//                "Forest Hill,43.6972,-79.4145",
//                "Chaplin Estates,43.7024,-79.4095",
//                "Deer Park,43.6900,-79.3960",
//                "Moore Park,43.6908,-79.3772",
//                "Casa Loma,43.6785,-79.4095",
//                "Summerhill,43.6826,-79.3912",
//                "Kensington Market,43.6548,-79.4023",
//                "Little Italy,43.6541,-79.4195",
//                "Chinatown,43.6528,-79.3984",
//                "Harbourfront,43.6387,-79.3825",
//                "Liberty Village,43.6396,-79.4225",
//                "Parkdale,43.6415,-79.4308",
//                "Swansea,43.6505,-79.4755"
//        };
//
//        for (int i = 0; i < locations.length; i++) {
//            String loc = locations[i];
//            String[] parts = loc.split(",");
//            String address = parts[0];
//            double latitude = Double.parseDouble(parts[1]);
//            double longitude = Double.parseDouble(parts[2]);
//
//            String id = String.valueOf(i + 1); // Assign sequential IDs starting from 1
//            Location location = new Location(id, address, latitude, longitude);
//            databaseReference.child(id).setValue(location);
//        }
//    }



    private void toggleSectionVisibility(View section) {
        if (section.getVisibility() == View.GONE) {
            section.setVisibility(View.VISIBLE);
        } else {
            section.setVisibility(View.GONE);
        }
    }

    private void addLocation() {
        String address = ((TextView) findViewById(R.id.inputAddress)).getText().toString().trim();
        String latitudeStr = ((TextView) findViewById(R.id.inputLatitude)).getText().toString().trim();
        String longitudeStr = ((TextView) findViewById(R.id.inputLongitude)).getText().toString().trim();

        if (address.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            addResultTextView.setText("Please fill in all fields");
            return;
        }

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);
        String id = databaseReference.push().getKey();

        Location location = new Location(id, address, latitude, longitude);
        if (id != null) {
            databaseReference.child(id).setValue(location).addOnSuccessListener(aVoid -> {
                addResultTextView.setText("Location added successfully");
                clearInputFields();
            }).addOnFailureListener(e -> {
                addResultTextView.setText("Failed to add location: " + e.getMessage());
            });
        }
    }

    private void queryLocation() {
        String address = ((TextView) findViewById(R.id.inputQueryAddress)).getText().toString().trim().toLowerCase();

        if (address.isEmpty()) {
            queryResultTextView.setText("Please enter an address to search");
            return;
        }

        databaseReference.orderByChild("address").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Location location = snapshot.getValue(Location.class);
                        if (location != null) {
                            queryResultTextView.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                        }
                    }
                } else {
                    queryResultTextView.setText("No location found with that address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                queryResultTextView.setText("Failed to query location: " + databaseError.getMessage());
            }
        });
    }

    private void updateLocation() {
        String address = ((TextView) findViewById(R.id.inputUpdateAddress)).getText().toString().trim();
        String latitudeStr = ((TextView) findViewById(R.id.inputUpdateLatitude)).getText().toString().trim();
        String longitudeStr = ((TextView) findViewById(R.id.inputUpdateLongitude)).getText().toString().trim();

        if (address.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            updateResultTextView.setText("Please fill in all fields");
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
                    updateResultTextView.setText("Location updated successfully");
                    clearInputFields();
                } else {
                    updateResultTextView.setText("No location found with that address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                updateResultTextView.setText("Failed to update location: " + databaseError.getMessage());
            }
        });
    }

    private void deleteLocation() {
        String address = ((TextView) findViewById(R.id.inputDeleteAddress)).getText().toString().trim();

        if (address.isEmpty()) {
            deleteResultTextView.setText("Please enter the address to delete");
            return;
        }

        databaseReference.orderByChild("address").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                            deleteResultTextView.setText("Location deleted successfully");
                            clearInputFields();
                        }).addOnFailureListener(e -> {
                            deleteResultTextView.setText("Failed to delete location: " + e.getMessage());
                        });
                    }
                } else {
                    deleteResultTextView.setText("No location found with that address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                deleteResultTextView.setText("Error while deleting: " + databaseError.getMessage());
            }
        });
    }

    private void clearInputFields() {
        ((TextView) findViewById(R.id.inputAddress)).setText("");
        ((TextView) findViewById(R.id.inputLatitude)).setText("");
        ((TextView) findViewById(R.id.inputLongitude)).setText("");
        ((TextView) findViewById(R.id.inputQueryAddress)).setText("");
        ((TextView) findViewById(R.id.inputUpdateAddress)).setText("");
        ((TextView) findViewById(R.id.inputUpdateLatitude)).setText("");
        ((TextView) findViewById(R.id.inputUpdateLongitude)).setText("");
        ((TextView) findViewById(R.id.inputDeleteAddress)).setText("");
    }
}
