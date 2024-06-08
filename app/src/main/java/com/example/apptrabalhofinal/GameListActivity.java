package com.example.apptrabalhofinal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ArrayList<String> gameList;
    private ArrayAdapter<String> adapter;
    private static final int REQUEST_ADD_GAME = 1;
    private static final int REQUEST_EDIT_GAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        dbHelper = new DatabaseHelper(this);
        gameList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, R.layout.list_item_game, R.id.textViewGame, gameList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundResource(R.drawable.item_background);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long gameId = getGameIdByName(gameList.get(position));
                        Intent intent = new Intent(GameListActivity.this, GameDetailsActivity.class);
                        intent.putExtra("gameId", gameId);
                        startActivityForResult(intent, REQUEST_EDIT_GAME);
                    }
                });
                return view;
            }
        };

        ListView lvGames = findViewById(R.id.lvGames);
        lvGames.setAdapter(adapter);

        Button btnAddGame = findViewById(R.id.btnAddGame);
        btnAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GameListActivity.this, AddGameActivity.class), REQUEST_ADD_GAME);
            }
        });

        loadGames();
    }

    private void loadGames() {
        gameList.clear();
        Cursor cursor = dbHelper.getReadableDatabase().query("games", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                gameList.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private long getGameIdByName(String name) {
        Cursor cursor = dbHelper.getReadableDatabase().query("games", new String[]{"id"}, "name = ?", new String[]{name}, null, null, null);
        long gameId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            gameId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        return gameId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadGames();
        }
    }
}
