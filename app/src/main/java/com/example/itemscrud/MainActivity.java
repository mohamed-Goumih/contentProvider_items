package com.example.itemscrud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private static final int REQUEST_ADD_ITEM = 1;
    private static final int REQUEST_EDIT_ITEM = 2;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        loadItems();

        findViewById(R.id.button_add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                startActivityForResult(intent, REQUEST_ADD_ITEM);
            }
        });
    }

    private void loadItems() {
        itemList.clear();
        Cursor cursor = getContentResolver().query(ItemContract.ItemEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_DESCRIPTION));
                itemList.add(new Item(id, title, description));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Item item = itemList.get(position);
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        startActivityForResult(intent, REQUEST_EDIT_ITEM);
    }

    @Override
    public void onDeleteClick(int position) {
        Item item = itemList.get(position);
        Uri uri = Uri.withAppendedPath(ItemContract.ItemEntry.CONTENT_URI, item.getId());
        int rowsDeleted = getContentResolver().delete(uri, null, null);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Item supprimé", Toast.LENGTH_SHORT).show();
            loadItems();
        } else {
            Toast.makeText(this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadItems();
        }
    }
}
