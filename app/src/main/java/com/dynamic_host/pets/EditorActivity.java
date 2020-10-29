package com.dynamic_host.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dynamic_host.pets.data.PetContract.PetEntry;
import com.dynamic_host.pets.data.PetDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText, mBreedEditText, mWeightEditText;
    private Spinner mGenderSpiner;
    private int mGender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);
        mGenderSpiner = findViewById(R.id.spinner_gender);
        setupSpinner();
    }


    private void setupSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter genderSpinneradapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genderSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mGenderSpiner.setAdapter(genderSpinneradapter);
        //Set the int mGender to the constant value
        mGenderSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)) {
                    if(selection.equals("Male"))
                        mGender = PetEntry.GENDER_MALE;  //Male
                    else if(selection.equals("Female"))
                        mGender = PetEntry.GENDER_FEMALE;  //Female
                    else
                        mGender = PetEntry.GENDER_UNKNOWN; //Unknown
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {mGender = 0;}
        });
    }

    private void insertPet(){
        //Use trim to eliminate leading/trailing white space
        String name = mNameEditText.getText().toString().trim();
        String breed = mBreedEditText.getText().toString().trim();
        Integer weight = Integer.parseInt(mWeightEditText.getText().toString().trim());
        //mGender is selected in spinner method

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, name);
        values.put(PetEntry.COLUMN_PET_BREED, breed);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null)
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Pet saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_delete:
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_save:
                //Save data to database
                insertPet();
                //Return to parent activity
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}