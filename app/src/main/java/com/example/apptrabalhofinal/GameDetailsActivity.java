package com.example.apptrabalhofinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameDetailsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etGameName, etGameDescription, etGameRating;
    private Button btnEdit, btnSave, btnDelete, btnBack;
    private long gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        dbHelper = new DatabaseHelper(this);

        etGameName = findViewById(R.id.etGameName);
        etGameDescription = findViewById(R.id.etGameDescription);
        etGameRating = findViewById(R.id.etGameRating);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        gameId = intent.getLongExtra("gameId", -1);

        if (gameId != -1) {
            loadGameDetails(gameId);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGameDetails();
                enableEditing(false);
                setResult(RESULT_OK);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enableEditing(false);
    }

    private void loadGameDetails(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("games", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            etGameName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etGameDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            etGameRating.setText(cursor.getString(cursor.getColumnIndexOrThrow("rating")));
            cursor.close();
        }
    }

    private void enableEditing(boolean enabled) {
        etGameName.setEnabled(enabled);
        etGameDescription.setEnabled(enabled);
        etGameRating.setEnabled(enabled);
        btnSave.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnEdit.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    private void saveGameDetails() {
        String name = etGameName.getText().toString();
        String description = etGameDescription.getText().toString();
        String rating = etGameRating.getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("rating", rating);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.update("games", values, "id = ?", new String[]{String.valueOf(gameId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Game details updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating game details", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteGame() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("games", "id = ?", new String[]{String.valueOf(gameId)});

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Game deleted successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error deleting game", Toast.LENGTH_SHORT).show();
        }
    }
}
