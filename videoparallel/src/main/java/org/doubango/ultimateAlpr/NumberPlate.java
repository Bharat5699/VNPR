package org.doubango.ultimateAlpr;


public class NumberPlate {
    private  String Registration_No;
    private String Owner_Name;
    private String Registration_Date;
    private String Chassis_No;
    private String Vehicle_Class;
    private String Model;
    private String Engine_No;
    private String Fuel;
    private String RC_Status;

    public NumberPlate(String registration_No, String owner_Name, String registration_Date, String chassis_No, String vehicle_Class, String model, String engine_No, String fuel, String RC_Status) {
        Registration_No = registration_No;
        Owner_Name = owner_Name;
        Registration_Date = registration_Date;
        Chassis_No = chassis_No;
        Vehicle_Class = vehicle_Class;
        Model = model;
        Engine_No = engine_No;
        Fuel = fuel;
        this.RC_Status = RC_Status;
    }

    public NumberPlate() {
    }

    public String getRegistration_No() {
        return Registration_No;
    }

    public void setRegistration_No(String registration_No) {
        Registration_No = registration_No;
    }

    public String getOwner_Name() {
        return Owner_Name;
    }

    public void setOwner_Name(String owner_Name) {
        Owner_Name = owner_Name;
    }

    public String getRegistration_Date() {
        return Registration_Date;
    }

    public void setRegistration_Date(String registration_Date) {
        Registration_Date = registration_Date;
    }

    public String getChassis_No() {
        return Chassis_No;
    }

    public void setChassis_No(String chassis_No) {
        Chassis_No = chassis_No;
    }

    public String getVehicle_Class() {
        return Vehicle_Class;
    }

    public void setVehicle_Class(String vehicle_Class) {
        Vehicle_Class = vehicle_Class;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getEngine_No() {
        return Engine_No;
    }

    public void setEngine_No(String engine_No) {
        Engine_No = engine_No;
    }

    public String getFuel() {
        return Fuel;
    }

    public void setFuel(String fuel) {
        Fuel = fuel;
    }

    public String getRC_Status() {
        return RC_Status;
    }

    public void setRC_Status(String RC_Status) {
        this.RC_Status = RC_Status;
    }
}
