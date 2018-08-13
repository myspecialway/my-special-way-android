package org.myspecialway.android.schedule.gateway;

import java.util.List;

public class ScheduleResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data{
        private ClassById classById;

        public ClassById getClassById() {
            return classById;
        }
    }

    public static class ClassById{
        private List<Schedule> schedule;

        public List<Schedule> getSchedule() {
            return schedule;
        }
    }

    public static class Schedule{
        private String index;
        private Location location;
        private Lesson lesson;

        public String getIndex() {
            return index;
        }

        public Location getLocation() {
            return location;
        }

        public Lesson getLesson() {
            return lesson;
        }
    }

    public static class Lesson{
        private String title;
        public String icon;

        public String getTitle() {
            return title;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static class Location{
        private String name;
        private boolean disabled;

        public String getName() {
            return name;
        }

        public boolean isDisabled() {
            return disabled;
        }
    }
}