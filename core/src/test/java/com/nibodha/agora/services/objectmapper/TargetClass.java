package com.nibodha.agora.services.objectmapper;

import java.util.ArrayList;
import java.util.List;

public class TargetClass {
    private String fullName;
    private Integer years;
    private ArrayList<TargetClass> colleagues;
    private SourceClass engineer;
    private String customTargetValue;

    public List<Integer> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<Integer> mobiles) {
        this.mobiles = mobiles;
    }

    private List<Integer> mobiles;

    public TargetClass(String fullName, Integer years, ArrayList<TargetClass> colleagues, SourceClass engineer, List<Integer> mobiles) {
        this.fullName = fullName;
        this.years = years;
        this.colleagues = colleagues;
        this.engineer = engineer;
        this.mobiles = mobiles;
    }

    public TargetClass() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public ArrayList<TargetClass> getColleagues() {
        return colleagues;
    }

    public void setColleagues(ArrayList<TargetClass> colleagues) {
        this.colleagues = colleagues;
    }

    public SourceClass getEngineer() {
        return engineer;
    }

    public void setEngineer(SourceClass engineer) {
        this.engineer = engineer;
    }

    public String getCustomTargetValue() {
        return customTargetValue;
    }

    public void setCustomTargetValue(final String customTargetValue) {
        this.customTargetValue = customTargetValue;
    }
}

