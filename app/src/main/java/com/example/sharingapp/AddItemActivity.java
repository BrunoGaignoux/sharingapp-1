package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Add a new item
 */
public class AddItemActivity extends AppCompatActivity {

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;

    private ImageView photo;
    private Bitmap image;
    private int REQUEST_CODE = 1;

    private ItemList item_list = new ItemList();
    private ItemListController item_list_controller = new ItemListController(item_list);

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);

        title = findViewById(R.id.title);
        maker = findViewById(R.id.maker);
        description = findViewById(R.id.description);
        length = findViewById(R.id.length);
        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        photo = findViewById(R.id.image_view);

        photo.setImageResource(android.R.drawable.ic_menu_gallery);

        context = getApplicationContext();
        item_list_controller.loadItems(context);
    }

    public void saveItem (View view) {

        if (!validateInput()) {
            return;
        }

        Item item = new Item(
                title.getText().toString(),
                maker.getText().toString(),
                description.getText().toString(),
                image,
                null);

        ItemController item_controller = new ItemController(item);
        item_controller.setDimensions(
                length.getText().toString(),
                width.getText().toString(),
                height.getText().toString()
        );

        // Add item
        boolean success = item_list_controller.addItem(item, context);
        if (!success) {
            return;
        }

        // End AddItemActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent intent){
        if (request_code == REQUEST_CODE && result_code == RESULT_OK){
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
        }
    }
}
