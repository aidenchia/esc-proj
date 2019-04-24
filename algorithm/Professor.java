package algorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class Professor {
    private String name;
    private int id;
    private HashMap<Subject, ArrayList<StudentGroup>> courseTable; // key is the name of subject
    private ArrayList<SpecificClass> sClassSet; // is used to check conflict

    Professor(String name, int id) {
        this.id = id;
        this.name = name;
        this.courseTable = new HashMap<>();
        this.sClassSet = new ArrayList<>();
    }

    public void addSubject(Subject subject, StudentGroup sg) {
        if (courseTable.containsKey(subject)) {
            courseTable.get(subject).add(sg);
        }else {
            courseTable.put(subject, new ArrayList<StudentGroup>());
            courseTable.get(subject).add(sg);
        }
    }

    public void setsClassSet(SpecificClass[][][] input3D) {
        this.sClassSet.clear();
        int subjectNum = input3D.length;
        int cohortNum;
        int sessionNum;
        ArrayList<StudentGroup> e;
        for (int i = 0; i < subjectNum; i++) {
            if (this.courseTable.containsKey(input3D[i][0][0].getSubject())) {
                cohortNum = input3D[i].length;
                for (int j = 0; j < cohortNum; j++) {
                    sessionNum = input3D[i][j].length;
                    for (int k = 0; k < sessionNum; k++) {
                        if (input3D[i][j][k] != null) {
                            for (StudentGroup sg: courseTable.get(input3D[i][0][0].getSubject())) {
                                if (input3D[i][0][0].getSubject().getTerm() == sg.getTerm()) {
                                    if (input3D[i][j][k].getCohortNo().contains(sg.getCohort())) {
                                        if (!sClassSet.contains(input3D[i][j][k])) {
                                            sClassSet.add(input3D[i][j][k]);
                                            if (!input3D[i][j][k].getProfessor().contains(this)) {
                                                input3D[i][j][k].setProfessor(this);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                continue;
            }
        }
    }

    // pre-condition: assume after scheduling
    // If detects one conflict, result++
    public int checkConflict() {
        int[][] timeTable = new int[5][20];
        int startTime;
        int timeSlotNum;
        int weekday;
        int result = 0;

        for (SpecificClass c: sClassSet) {
            weekday = c.getWeekday();
            startTime = c.getStartTime();
            timeSlotNum = (int)(c.getDuration()/0.5);

            for (int sln = 0; sln < timeSlotNum; sln++) {
                if (weekday != -1 && timeTable[weekday][startTime + sln] == 0) { //empty in slot
                    timeTable[weekday][startTime + sln] = 1;
                }else {
                    for(int i = 0; i < sln; i++) {
                        timeTable[weekday][startTime + i] = 0;
                    }
//                    c.printInfo();
//                    for (int i = 0; i < 5; i++) {
//                        for (int j = 0; j < 20; j++) {
//                            System.out.print(timeTable[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println("-------------");
                    result++;
                    break;
                }
            }
        }
        return result;
    }
    private void addSpecificClass(SpecificClass sClass) {
        if(sClassSet.contains(sClass)) {
            System.out.println("Error: Alr contain this sClass");
        }else {
            sClassSet.add(sClass);
        }
    }

    private void addSpecificClass(SpecificClass[] sCSet) {
        for (SpecificClass c: sCSet) {
            if (!sClassSet.contains(c)) {
                sClassSet.add(c);
            }else{
                System.out.println("Error: Alr contain this sClass");
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<SpecificClass> getClassSet() {
        return sClassSet;
    }

    public HashMap<Subject, ArrayList<StudentGroup>> getCourseTable() {
        return courseTable;
    }

}
