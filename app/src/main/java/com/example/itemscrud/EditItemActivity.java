package com.example.itemscrud;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditItemActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSave;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonSave = findViewById(R.id.button_save);

        itemId = getIntent().getStringExtra("id");
        if (itemId != null) {
            editTextTitle.setText(getIntent().getStringExtra("title"));
            editTextDescription.setText(getIntent().getStringExtra("description"));
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_NAME_TITLE, title);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_DESCRIPTION, description);

        if (itemId == null) {
            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "Erreur d'ajout", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Item ajouté", Toast.LENGTH_SHORT).show();
            }
        } else {
            Uri uri = Uri.withAppendedPath(ItemContract.ItemEntry.CONTENT_URI, itemId);
            int rowsUpdated = getContentResolver().update(uri, values, null, null);
            if (rowsUpdated == 0) {
                Toast.makeText(this, "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Item mis à jour", Toast.LENGTH_SHORT).show();
            }
        }
        setResult(RESULT_OK);
        finish();
    }
}
