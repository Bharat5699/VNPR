package org.doubango.ultimateAlpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Vnpr extends AppCompatActivity {
    TextView rg,eRegistration_No,eOwner_Name,eRegistration_Date,eChassis_No,eVehicle_Class,eModel ,eEngine_No,eFuel, eRC_Status;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpr);
        rg=findViewById(R.id.rg);
        eRegistration_No=findViewById(R.id.Registration_No);
        eOwner_Name=findViewById(R.id.Owner_name);
        eRegistration_Date=findViewById(R.id.Registration_Date);
        eChassis_No=findViewById(R.id.Chassis_No);
        eVehicle_Class=findViewById(R.id.Vehicle_Class);
        eModel=findViewById(R.id.Model);
        eEngine_No=findViewById(R.id.Engine_No);
        eFuel=findViewById(R.id.Fuel);
        eRC_Status=findViewById(R.id.Rc_status);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Registered_Vehicles");
        Intent i=getIntent();
        String  Registration_No=i.getStringExtra("1");
        String Owner_Name=i.getStringExtra("2");
        String Registration_date=i.getStringExtra("3");
        String Chassis_No=i.getStringExtra("4");
        String Vehicle_Class=i.getStringExtra("5");
        String Model=i.getStringExtra("6");
        String Fuel=i.getStringExtra("7");
        String Engine_No=i.getStringExtra("8");
        String Rc_Status=i.getStringExtra("9");
        rg.setText(Registration_No);
        eRegistration_No.setText(Registration_No);
        eOwner_Name.setText(Owner_Name);
        eRegistration_Date.setText(Registration_date);
        eChassis_No.setText(Chassis_No);
        eVehicle_Class.setText(Vehicle_Class);
        eModel.setText(Model);
        eFuel.setText(Fuel);
        eEngine_No.setText(Engine_No);
        eRC_Status.setText(Rc_Status);


    }

}
