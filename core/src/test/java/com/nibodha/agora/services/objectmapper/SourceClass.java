package com.nibodha.agora.services.objectmapper;

import java.util.List;

public class SourceClass {
    private String name;
    private String age;
    private List<SourceClass> friends;
    private String[] telephones;
    private String customSourceValue;

    public TargetClass getDeveloper() {
        return developer;
    }

    public void setDeveloper(TargetClass developer) {
        this.developer = developer;
    }

    private TargetClass developer;

    public SourceClass(String name, String age, List<SourceClass> friends, TargetClass developer, String[] telephones) {
        this.name = name;
        this.age = age;
        this.friends = friends;
        this.developer = developer;
        this.telephones = telephones;
    }

    public SourceClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setFriends(List<SourceClass> friends) {
        this.friends = friends;
    }

    public List<SourceClass> getFriends() {
        return friends;
    }

    public String[] getTelephones() {
        return telephones;
    }

    public void setTelephones(final String[] telephones) {
        this.telephones = telephones;
    }

    public String getCustomSourceValue() {
        return customSourceValue;
    }

    public void setCustomSourceValue(final String customSourceValue) {
        this.customSourceValue = customSourceValue;
    }
}
