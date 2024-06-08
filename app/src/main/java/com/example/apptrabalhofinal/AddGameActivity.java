package com.example.apptrabalhofinal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGameActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etGameName, etGameDescription, etGameRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        dbHelper = new DatabaseHelper(this);

        etGameName = findViewById(R.id.etGameName);
        etGameDescription = findViewById(R.id.etGameDescription);
        etGameRating = findViewById(R.id.etGameRating);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBack = findViewById(R.id.btnBack);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGame();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveGame() {
        String name = etGameName.getText().toString();
        String description = etGameDescription.getText().toString();
        String rating = etGameRating.getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("rating", rating);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert("games", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Game added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error adding game", Toast.LENGTH_SHORT).show();
        }
    }
}
