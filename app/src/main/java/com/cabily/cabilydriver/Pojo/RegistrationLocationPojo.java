package com.cabily.cabilydriver.Pojo;

import java.util.ArrayList;

/**
 * Created by CAS64 on 8/24/2018.
 */

public class RegistrationLocationPojo {
    private String locationId;

    public RegistrationLocationPojo(String locationId, String locationCity, ArrayList<RegistrationCategoryPojo> registrationCategoryPojos) {
        this.locationId = locationId;
        this.locationCity = locationCity;
        this.registrationCategoryPojos = registrationCategoryPojos;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public ArrayList<RegistrationCategoryPojo> getRegistrationCategoryPojos() {
        return registrationCategoryPojos;
    }

    public void setRegistrationCategoryPojos(ArrayList<RegistrationCategoryPojo> registrationCategoryPojos) {
        this.registrationCategoryPojos = registrationCategoryPojos;
    }

    private String locationCity;
    public ArrayList<RegistrationCategoryPojo> registrationCategoryPojos;




}
