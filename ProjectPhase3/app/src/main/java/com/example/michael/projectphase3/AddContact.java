
package com.example.michael.projectphase3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

    int ID;
    EditText editFirst, editMiddle, editLast, editPhone, editBirth, editDate, editAddress, editCity, editState, editZip;
    Button btnAddData;
    Button btnMap;
    Button btnDelete;
    Button btnviewUpdate;
    Database myDb;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create a new database
        myDb = new Database(this);

        editFirst = (EditText) findViewById(R.id.editText_First);
        editLast = (EditText) findViewById(R.id.editText_Last);
        editMiddle = (EditText) findViewById(R.id.editText_Middle);
        editPhone = (EditText) findViewById(R.id.editText_Phone);
        editBirth = (EditText) findViewById(R.id.editText_Birth);
        editDate = (EditText) findViewById(R.id.editText_Date);
        editAddress = (EditText) findViewById(R.id.editText_Address);
        editCity = (EditText) findViewById(R.id.editText_City);
        editState = (EditText) findViewById(R.id.editText_State);
        editZip = (EditText) findViewById(R.id.editText_Zip);


        // This button looks at the information and deletes it
        btnDelete = (Button) findViewById(R.id.button_delete);
        // Just for now, look at save instead of update.
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        // This is the button for the map.
        btnMap = (Button)findViewById(R.id.button_map);

        bundle = getIntent().getExtras();
        String temp = bundle.getString("FirstName");
        String[] temp1= temp.split("\\s+");
        editFirst.setText(temp1[0]);


        Cursor myCursor = myDb.getFirstData(temp1[0]);
        if (myCursor.getCount() == 0) {
            Toast.makeText(AddContact.this, "The Database is empty", Toast.LENGTH_LONG).show();
        } else {
            myCursor.moveToFirst();
            editFirst.setText(myCursor.getString(1));
            editLast.setText(myCursor.getString(2));
            editMiddle.setText(myCursor.getString(3));
            editPhone.setText(myCursor.getString(4));
            editBirth.setText(myCursor.getString(5));
            editDate.setText(myCursor.getString(6));
            editAddress.setText(myCursor.getString(7));
            editCity.setText(myCursor.getString(8));
            editState.setText(myCursor.getString(9));
            editZip.setText(myCursor.getString(10));
        }

        UpdateData();
        DeleteData();
        OpenMap();

    }

    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editFirst.getText().toString());
                        if (deletedRows > 0) {
                            Toast.makeText(AddContact.this, "Data Deleted", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddContact.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(AddContact.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(Integer.toString(ID),editFirst.getText().toString(),
                                editLast.getText().toString(),
                                editMiddle.getText().toString(), editPhone.getText().toString(),
                                editBirth.getText().toString(), editDate.getText().toString(),
                                editAddress.getText().toString(), editCity.getText().toString(),
                                editState.getText().toString(), editZip.getText().toString());
                        if (isUpdate == true) {
                            Toast.makeText(AddContact.this, "Data Update", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddContact.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(AddContact.this, "Data not Updated", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void OpenMap() {
        btnMap.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String AddressIn = editAddress.getText().toString() + " " +
                                editCity.getText().toString() + " " + editZip.getText().toString();
                        Intent intent = new Intent(AddContact.this, MapsActivity.class);
                        intent.putExtra("Address", AddressIn);
                        startActivity(intent);
                    }

                });

    }


}
