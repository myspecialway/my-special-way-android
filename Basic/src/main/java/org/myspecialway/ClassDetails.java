package org.myspecialway;

public class ClassDetails {

    private String classLat;
    private String classLon;
    private String className;
    private String classFloor;

    public ClassDetails(String mClassLat, String mClassLon, String mClassName, String classFloor){
        this.classLat = mClassLat;
        this.classLon = mClassLon;
        this.className = mClassName;
        this.classFloor = classFloor;
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

    public String getClassFloor() {
        return classFloor;
    }
}
