package org.myspecialway;

public class AgendaDetails {

    String className;
    int classIcon;
    String classTime;

    public AgendaDetails(String className, int classIcon, String classTime) {
        this.className = className;
        this.classIcon = classIcon;
        this.classTime = classTime;
    }

    public String getClassName() {
        return className;
    }

    public int getClassIcon() {
        return classIcon;
    }

    public String getClassTime() {
        return classTime;
    }

}
