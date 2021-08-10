package org.doubango.ultimateAlpr;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {
    String Registration_No;
    String Owner_Name;
    String Registration_date;
    String Chassis_No;
    String Vehicle_Class;
    String Model;
    String Fuel;
    String Engine_No;
    String Rc_Status;
    HIstory_DB history_db;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_active, container, false);
        history_db=new HIstory_DB(getContext());
        ArrayList arrayList= history_db.getAllActiveData();
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Registered_Vehicles");
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setEmptyView(view.findViewById(R.id.empty));

        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);


        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rg_no= (String) arrayList.get(position);
                getDetails(rg_no);

            }
        });
        return view;
    }
    public void getDetails(String rg_no){

        ref.child(rg_no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NumberPlate numberPlate = dataSnapshot.getValue(NumberPlate.class);

                    Registration_No = numberPlate.getRegistration_No();
                    Owner_Name = numberPlate.getOwner_Name();
                    Registration_date = numberPlate.getRegistration_Date();
                    Chassis_No = numberPlate.getChassis_No();
                    Vehicle_Class = numberPlate.getVehicle_Class();
                    Model = numberPlate.getModel();
                    Fuel = numberPlate.getFuel();
                    Engine_No = numberPlate.getEngine_No();
                    Rc_Status = numberPlate.getRC_Status();

                    Intent i = new Intent(getContext(), Vnpr.class);
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
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
