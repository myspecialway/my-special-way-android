package org.myspecialway;

public class ClassCoordinates {

    private String classLat;
    private String classLon;
    private String className;

    public ClassCoordinates(String mClassLat, String mClassLon, String mClassName){
        this.classLat = mClassLat;
        this.classLon = mClassLon;
        this.className = mClassName;
    }

    public String getClassLat() {
        return classLat;
    }

    public String getClassLon() {
        return classLon;
    }

    public String getClassName() {
        return className;
    }
}
