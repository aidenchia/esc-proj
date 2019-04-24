package algorithm;

import java.util.ArrayList;

public class Subject {
    protected String name;
    protected int id;
    protected SubjectType type;
//    protected Professor[] courseLead;
    protected int term;
    protected int studNumPerCohort; // 50 students each class
    protected int numOfCohort;
    protected int totalEnrollNum;
    protected GenericClass[] classComponent;
    protected int numOfSession;

    Subject() {

    }

    Subject(String name, int id, SubjectType type, int term    ,
            int studNumPerCohort, int numOfCohort, GenericClass[] classComponent) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.term = term;
        this.studNumPerCohort = studNumPerCohort;
        this.numOfCohort = numOfCohort;
        this.totalEnrollNum = this.numOfCohort * this.studNumPerCohort;
        this.classComponent = classComponent;
        this.numOfSession = classComponent.length;
    }

    public GenericClass[] getClassComponent() {
        return classComponent;
    }

    public int getNumOfCohort() {
        return numOfCohort;
    }

    public int getNumOfSession() {
        return numOfSession;
    }

    public String getName() {
        return name;
    }

    public int getTerm() {
        return term;
    }

    public int getId() {
        return id;
    }
}

enum SubjectType {
    CORE, ELECTIVE
}


enum ClassType {
    LEC, CBL, LAB
}

/*
 * Construction Policy:
 *
 * 1. If CBL or LAB, no need set cohorts since every cohorts and each time only one
 * 2. if LEC, need to set cohorts since may not every cohort takes at one time
 *
 */
class GenericClass {
    private double duration;
    private ClassType classType;
    private Classroom[] possibleRoomSet = null;
    private ArrayList<Integer> cohorts = null;

    GenericClass(ClassType classType, double duration, Classroom[] classroom) {
        this.classType = classType;
        this.duration = duration;
        this.possibleRoomSet = classroom;
    }

    GenericClass(ClassType classType, double duration, Classroom[] classroom, ArrayList<Integer> cohorts) {
        this.classType = classType;
        this.duration = duration;
        this.possibleRoomSet = classroom;
        this.cohorts = cohorts;
    }

    public Classroom[] getClassroom() {
        return possibleRoomSet;
    }

    public ClassType getClassType() {
        return classType;
    }

    public double getDuration() {
        return duration;
    }

    public ArrayList<Integer> getCohorts() {
        return cohorts;
    }

    public Classroom[] getPossibleRoomSet() {
        return possibleRoomSet;
    }
}

class SpecificClass {
    private ClassType type;
    private double duration;
    private Classroom classroom;
    private Classroom[] possibleRoomSet;
    private ArrayList<Integer> cohortNo; // lecture can have multiple cohorts
    private int session;
    private Subject subject;
    private String specialType; // HASS or 5-th Row

    // below will be set after scheduling
    private int weekday = 0;
    private int startTime = 0;

    // for printing
    private boolean printed = false;

    // for generate ics
    private ArrayList<Professor> professors = new ArrayList<>();
    private  ArrayList<StudentGroup> studentGroups = new ArrayList<>();

    SpecificClass(GenericClass gclass, int session, int cohortNo,
                  Subject subject, Classroom room) {
        this.cohortNo = new ArrayList<>();
        if (gclass.getCohorts() == null) {
            this.cohortNo.add(cohortNo);
        }else {
            this.cohortNo = gclass.getCohorts();
        }
        this.type = gclass.getClassType();
        this.duration = gclass.getDuration();
//        if (gclass.getClassroom().length == 0) {
//            this.classroom = room;
//        }else {
//            this.classroom = gclass.getClassroom()[0];
//        }
        if(room == null) {
            possibleRoomSet = gclass.getClassroom();
        }else {
//            possibleRoomSet = new Classroom[]{room};
            possibleRoomSet = gclass.getClassroom();
        }
        this.session = session;
        this.subject = subject;
    }

    SpecificClass(String specialType) {
        this.specialType = specialType;
    }

    public void setTimeAndPos(int weekday, int startTime, Classroom room) {
        this.weekday = weekday;
        this.startTime = startTime;
        if (this.classroom == null) {
            this.classroom = room;
        }else {
            assert(this.classroom == room);
        }
    }

    public int getStartTime() {
        return startTime;
    }

    public int getWeekday() {
        return weekday;
    }

    public int getSession() {
        return session;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public Classroom[] getPossibleRoomSet() {
        return possibleRoomSet;
    }

    public ClassType getType() {
        return type;
    }

    public double getDuration() {
        return duration;
    }

    public ArrayList<Integer> getCohortNo() {
        return cohortNo;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setProfessor(Professor professor) {
        this.professors.add(professor);
    }

    public ArrayList<Professor> getProfessor() {
        return professors;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroups.add(studentGroup);
    }

    public ArrayList<StudentGroup> getStudentGroup() {
        return studentGroups;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void printInfo() {
        String profs = "";
        for (Professor p: this.getProfessor()) {
            profs += p.getName() + " ";
        }
        if (specialType == null) {
            System.out.println("name: " + this.subject.name + "; cohort: " + this.cohortNo + "; session: " + this.session
                    + "; Weekday: " + this.weekday + "; startTime: " + this.startTime + " Classroom: " + this.classroom.getName()
            + " Prof: " + profs);
        }
    }
    public void printInfoWithoutRoom() {
        String profs = "";
        for (Professor p: this.getProfessor()) {
            profs += p.getName() + " ";
        }
        if (specialType == null) {
            System.out.println("name: " + this.subject.name + "; cohort: " + this.cohortNo + "; session: " + this.session
                    + "; Weekday: " + this.weekday + "; startTime: " + this.startTime + " Prof: " + profs);
        }
    }

    public boolean isHass() {
        if (specialType != null) {
            return specialType == "HASS";
        }else {
            return false;
        }
    }
}
