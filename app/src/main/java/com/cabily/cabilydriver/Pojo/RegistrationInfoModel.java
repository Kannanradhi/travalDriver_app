package com.cabily.cabilydriver.Pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CAS64 on 8/20/2018.
 */

public class RegistrationInfoModel implements Serializable {

    private String driverName="";
    private String driverEmail="";
    private String driverPassword="";
    private String driverAddress="";
    private String driverCity="";
    private String driverState="";
    private String driverZipCode="";
    private String selectedLocationId="";
    private String selectedCateId="";
    private String selectedTypeId="";
    private String selectedMakerId="";

    private String selectedModelId="";
    private String selectedYear="";
    private Boolean isVehicleInfoLoaded=false;
    private ArrayList<String> locationIdArray=new ArrayList<>();
    private ArrayList<String> locationCityArray=new ArrayList<>();
    private ArrayList<String> mainTypeNameArray=new ArrayList<>();
    private ArrayList<String> mainTypecateArray=new ArrayList<>();
    private ArrayList<String> mainTypeIdArray=new ArrayList<>();
    private ArrayList<String> mainTypeNameMakerArray=new ArrayList<>();
    private ArrayList<String> makerNameArray=new ArrayList<>();
    private ArrayList<String> makerIdArray=new ArrayList<>();
    private ArrayList<String> mainTypeNameModelArray=new ArrayList<>();
    private ArrayList<String> makerNameModelArray=new ArrayList<>();



    private ArrayList<String> modelNameArray=new ArrayList<>();
    private ArrayList<String> modelIdArray=new ArrayList<>();
    private ArrayList<String> mainTypeNameYearArray=new ArrayList<>();
    private ArrayList<String> makerNameYearArray=new ArrayList<>();
    private ArrayList<String> modelNameYearArray=new ArrayList<>();
    private ArrayList<String> mainTypeNameCat=new ArrayList<>();

    public String getSelectedTypeId() {
        return selectedTypeId;
    }

    public void setSelectedTypeId(String selectedTypeId) {
        this.selectedTypeId = selectedTypeId;
    }

    public String getSelectedMakerId() {
        return selectedMakerId;
    }

    public void setSelectedMakerId(String selectedMakerId) {
        this.selectedMakerId = selectedMakerId;
    }

    public String getSelectedModelId() {
        return selectedModelId;
    }

    public void setSelectedModelId(String selectedModelId) {
        this.selectedModelId = selectedModelId;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }


    public ArrayList<String> getMainTypeNameArray() {
        return mainTypeNameArray;
    }

    public void setMainTypeNameArray(ArrayList<String> mainTypeNameArray) {
        this.mainTypeNameArray = mainTypeNameArray;
    }

    public ArrayList<String> getMainTypecateArray() {
        return mainTypecateArray;
    }

    public void setMainTypecateArray(ArrayList<String> mainTypecateArray) {
        this.mainTypecateArray = mainTypecateArray;
    }

    public ArrayList<String> getMainTypeIdArray() {
        return mainTypeIdArray;
    }

    public void setMainTypeIdArray(ArrayList<String> mainTypeIdArray) {
        this.mainTypeIdArray = mainTypeIdArray;
    }

    public ArrayList<String> getMainTypeNameMakerArray() {
        return mainTypeNameMakerArray;
    }

    public void setMainTypeNameMakerArray(ArrayList<String> mainTypeNameMakerArray) {
        this.mainTypeNameMakerArray = mainTypeNameMakerArray;
    }

    public ArrayList<String> getMakerNameArray() {
        return makerNameArray;
    }

    public void setMakerNameArray(ArrayList<String> makerNameArray) {
        this.makerNameArray = makerNameArray;
    }

    public ArrayList<String> getMakerIdArray() {
        return makerIdArray;
    }

    public void setMakerIdArray(ArrayList<String> makerIdArray) {
        this.makerIdArray = makerIdArray;
    }

    public ArrayList<String> getMainTypeNameModelArray() {
        return mainTypeNameModelArray;
    }

    public void setMainTypeNameModelArray(ArrayList<String> mainTypeNameModelArray) {
        this.mainTypeNameModelArray = mainTypeNameModelArray;
    }

    public ArrayList<String> getMakerNameModelArray() {
        return makerNameModelArray;
    }

    public void setMakerNameModelArray(ArrayList<String> makerNameModelArray) {
        this.makerNameModelArray = makerNameModelArray;
    }

    public ArrayList<String> getModelNameArray() {
        return modelNameArray;
    }

    public void setModelNameArray(ArrayList<String> modelNameArray) {
        this.modelNameArray = modelNameArray;
    }

    public ArrayList<String> getModelIdArray() {
        return modelIdArray;
    }

    public void setModelIdArray(ArrayList<String> modelIdArray) {
        this.modelIdArray = modelIdArray;
    }

    public ArrayList<String> getMainTypeNameYearArray() {
        return mainTypeNameYearArray;
    }

    public void setMainTypeNameYearArray(ArrayList<String> mainTypeNameYearArray) {
        this.mainTypeNameYearArray = mainTypeNameYearArray;
    }

    public ArrayList<String> getMakerNameYearArray() {
        return makerNameYearArray;
    }

    public void setMakerNameYearArray(ArrayList<String> makerNameYearArray) {
        this.makerNameYearArray = makerNameYearArray;
    }

    public ArrayList<String> getModelNameYearArray() {
        return modelNameYearArray;
    }

    public void setModelNameYearArray(ArrayList<String> modelNameYearArray) {
        this.modelNameYearArray = modelNameYearArray;
    }

    public ArrayList<String> getYearArray() {
        return yearArray;
    }

    public void setYearArray(ArrayList<String> yearArray) {
        this.yearArray = yearArray;
    }

    private ArrayList<String> yearArray;
    private ArrayList<RegistrationCountryPojo> countryArray=new ArrayList<>();

    public ArrayList<String> getLocationIdArray() {
        return locationIdArray;
    }

    public void setLocationIdArray(ArrayList<String> mainTypeNameArray, ArrayList<String> mainTypecateArray, ArrayList<String> mainTypeIdArray, ArrayList<String> mainTypeNameMakerArray, ArrayList<String> makerNameArray, ArrayList<String> makerIdArray, ArrayList<String> mainTypeNameModelArray, ArrayList<String> makerNameModelArray, ArrayList<String> modelNameArray, ArrayList<String> modelIdArray, ArrayList<String> mainTypeNameYearArray, ArrayList<String> makerNameYearArray, ArrayList<String> modelNameYearArray, ArrayList<String> yearArray, ArrayList<String> mainTypeNameCat) {
        this.mainTypeNameArray = mainTypeNameArray;
        this.mainTypecateArray = mainTypecateArray;
        this.mainTypeIdArray = mainTypeIdArray;
        this.mainTypeNameMakerArray = mainTypeNameMakerArray;
        this.makerNameArray = makerNameArray;
        this.makerIdArray = makerIdArray;
        this.mainTypeNameModelArray = mainTypeNameModelArray;
        this.makerNameModelArray = makerNameModelArray;
        this.modelNameArray = modelNameArray;
        this.modelIdArray = modelIdArray;
        this.mainTypeNameYearArray = mainTypeNameYearArray;
        this.makerNameYearArray = makerNameYearArray;
        this.modelNameYearArray = modelNameYearArray;
        this.yearArray = yearArray;
        this.mainTypeNameCat=mainTypeNameCat;
    }



    public void setLoginDetailsAttr(String driverName, String driverEmail, String driverPassword)
    {
        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.driverPassword = driverPassword;
    }

    public void setDriverDetailsAttr(String driverAddress, String driverCity, String driverState, String driverZipCode)
    {
        this.driverAddress = driverAddress;
        this.driverCity = driverCity;
        this.driverZipCode = driverState;
        this.driverZipCode = driverZipCode;
    }


    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverCity() {
        return driverCity;
    }

    public void setDriverCity(String driverCity) {
        this.driverCity = driverCity;
    }

    public String getDriverState() {
        return driverState;
    }

    public void setDriverState(String driverState) {
        this.driverState = driverState;
    }

    public String getDriverZipCode() {
        return driverZipCode;
    }

    public void setDriverZipCode(String driverZipCode) {
        this.driverZipCode = driverZipCode;
    }


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public ArrayList<String> getLocationCityArray() {
        return locationCityArray;
    }

    public void setLocationCityArray(ArrayList<String> locationCityArray) {
        this.locationCityArray = locationCityArray;
    }

    public ArrayList<RegistrationCountryPojo> getCountryArray() {
        return countryArray;
    }

    public void setCountryArray(ArrayList<RegistrationCountryPojo> countryArray) {
        this.countryArray = countryArray;
    }

    public String getSelectedLocationId() {
        return selectedLocationId;
    }

    public void setSelectedLocationId(String selectedLocationId) {
        this.selectedLocationId = selectedLocationId;
    }

    public String getSelectedCateId() {
        return selectedCateId;
    }

    public void setSelectedCateId(String selectedCateId) {
        this.selectedCateId = selectedCateId;
    }

    public ArrayList<String> getMainTypeNameCat() {
        return mainTypeNameCat;
    }

    public void setMainTypeNameCat(ArrayList<String> mainTypeNameCat) {
        this.mainTypeNameCat = mainTypeNameCat;
    }

    public Boolean getVehicleInfoLoaded() {
        return isVehicleInfoLoaded;
    }

    public void setVehicleInfoLoaded(Boolean vehicleInfoLoaded) {
        isVehicleInfoLoaded = vehicleInfoLoaded;
    }
}
