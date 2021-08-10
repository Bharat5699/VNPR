package org.doubango.ultimateAlpr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;

public class detect_number_plate extends AppCompatActivity {
    EditText mResultEt;
    ImageView mPreviewIv;
    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    FirebaseDatabase database;
    DatabaseReference ref;
    String Registration_No;
    String Owner_Name;
    String Registration_date;
    String Chassis_No;
    String Vehicle_Class;
    String Model;
    String Fuel;
    String Engine_No;
    String Rc_Status;
    String validate="invalidate";
    HIstory_DB history_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_number_plate);
        mResultEt = findViewById(R.id.result);
        mPreviewIv = findViewById(R.id.imageView);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Registered_Vehicles");

        history_db=new HIstory_DB(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage1) {
            if (!checkCameraPermission()) {
                requestCameraPermission();
            } else {
                pickCamera();
            }
        }
        if (id == R.id.addImage) {

            if (!checkStoragePermission()) {
                requestStoragePermission();
            } else {
                pickGallery();
            }

        }
        if (id == R.id.Video) {
            Intent intent=new Intent(this,AlprVideoParallelActivity.class);
            startActivity(intent);

        }
        if (id == R.id.history) {
            Intent intent=new Intent(this,Number_plate_History.class);
            startActivity(intent);
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }


    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK_GALLERY_CODE) {
            CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if (requestCode == IMAGE_PICK_CAMERA_CODE) {
            CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resulturi = result.getUri();
                mPreviewIv.setImageURI(resulturi);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    String inputString=sb.toString();
                    inputString = inputString.replaceAll("\t", "");
                    inputString = inputString.replaceAll("\n", "");
                    inputString = inputString.replaceAll(" ", "");
                    mResultEt.setText(inputString);
                    Toast.makeText(this,inputString , Toast.LENGTH_SHORT).show();

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getDetails(View view) {

if(validate.equals("validate")) {
validate="invalidate";
    Intent i = new Intent(this, Vnpr.class);
    i.putExtra("1", Registration_No);
   i.putExtra("2", Owner_Name);
    i.putExtra("3", Registration_date);
    i.putExtra("5", Vehicle_Class);
    i.putExtra("6", Model);
    i.putExtra("4", Chassis_No);
    i.putExtra("7", Fuel);
    i.putExtra("8", Engine_No);
    i.putExtra("9", Rc_Status);
    startActivity(i);
}
else{

    Toast.makeText(this, "Please Check the Number Plate", Toast.LENGTH_SHORT).show();

}



    }

    public void check(View view) {
        /*ref.child(mResultEt.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NumberPlate numberPlate = dataSnapshot.getValue(NumberPlate.class);

                Registration_No=numberPlate.getRegistration_No();
                Owner_Name=numberPlate.getOwner_Name();
                Registration_date=numberPlate.getRegistration_Date();
                Chassis_No=numberPlate.getChassis_No();
                Vehicle_Class=numberPlate.getVehicle_Class();
                Model=numberPlate.getModel();
                Fuel=numberPlate.getFuel();
                Engine_No=numberPlate.getEngine_No();
                Rc_Status=numberPlate.getRC_Status();
                validate="validate";



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

       String rg=mResultEt.getText().toString().toUpperCase();
        rg = rg.replaceAll("\t", "");
       rg = rg.replaceAll("\n", "");
        rg = rg.replaceAll(" ", "");
        String finalRg = rg;
        ref.child(rg).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                            NumberPlate numberPlate = dataSnapshot.getValue(NumberPlate.class);

                           Registration_No=numberPlate.getRegistration_No();
                            Owner_Name=numberPlate.getOwner_Name();
                            Registration_date=numberPlate.getRegistration_Date();
                            Chassis_No=numberPlate.getChassis_No();
                            Vehicle_Class=numberPlate.getVehicle_Class();
                            Model=numberPlate.getModel();
                            Fuel=numberPlate.getFuel();
                            Engine_No=numberPlate.getEngine_No();
                            Rc_Status=numberPlate.getRC_Status();
                            history_db.insertData(finalRg,"ACTIVE");
                    Toast.makeText(detect_number_plate.this, "valid", Toast.LENGTH_SHORT).show();
                            validate="validate";





                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(detect_number_plate.this);
                    alertDialogBuilder.setMessage("Number plate is Invalid");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                           boolean res= history_db.insertData(finalRg,"INACTIVE");
                                           if(res){
                                               Toast.makeText(detect_number_plate.this,"Ok",Toast.LENGTH_LONG).show();
                                           }
                                        }
                                    });



                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
