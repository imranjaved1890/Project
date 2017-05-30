package com.imran.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnProcess;
    TextView txtResult;
    Button importImage;
    private static final int PICK_IMAGE = 1;
    Bitmap bitmap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView)findViewById(R.id.image_view);
        btnProcess = (Button)findViewById(R.id.button_process);
        txtResult = (TextView)findViewById(R.id.textview_result);
        importImage = (Button)findViewById(R.id.import_image);

        //final Bitmap bitmap = BitmapFactory.decodeResource( getApplicationContext().getResources(), R.drawable.number_plate2);
        //imageView.setImageBitmap(bitmap);

        /*btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational())
                    Log.e("ERROR","Detector dependencies are not yet available");
                else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0;i<items.size();++i)
                    {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    txtResult.setText(stringBuilder.toString());
                }
            }
        });*/
    }


    public void meth(View view) {
        //final Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.number_plate2);
            if(bitmap==null){
                Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show();
            }else {

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational())
                    Log.e("ERROR", "Detector dependencies are not yet available");
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    txtResult.setText(stringBuilder.toString());
                }
            }
    }

    public void dispatchTakePictureIntent(View view) {

        if (view.getId() == R.id.picture) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
            startActivityForResult(intent, 1);
            //onActivityResult(1,1,intent);
        }
    }

    public void openGallery(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
        //onActivityResult(1,1,gallery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        else
            Toast.makeText(this, "can not load the image", Toast.LENGTH_LONG).show();*/
       if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            //Toast.makeText(this, "Loading image", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
            //Toast.makeText(this, "loaded", Toast.LENGTH_LONG).show();
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(bitmap);

        }
    }
}
