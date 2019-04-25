package algorithm;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.management.remote.SubjectDelegationPermission;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JsonUtils {
    public static void writeToJson(Chromosome c) throws Exception{
        if (c == null) {
            throw new Exception();
        }
        JSONObject json = new JSONObject();
        JSONArray sClassSet = new JSONArray();
        try {
            SpecificClass[] lineC = c.getLineChromosome();
            for(SpecificClass s: lineC) {
                if (s != null) {
                    JSONObject sClass = new JSONObject();
                    sClass.put("subject", s.getSubject().getId());
                    sClass.put("cohort", s.getCohortNo());
                    sClass.put("session", s.getSession());
                    sClass.put("weekday", s.getWeekday());
                    sClass.put("startTime", s.getStartTime());
                    sClass.put("classroom", s.getClassroom().getName());
                    sClass.put("duration", s.getDuration());
                    ArrayList<Integer> profId = new ArrayList<>();
                    for (Professor p: s.getProfessor()) {
                        profId.add(p.getId());
                    }
                    sClass.put("professor", profId);
                    ArrayList<Integer> sgId = new ArrayList<>();
                    for (StudentGroup sg: s.getStudentGroup()) {
                        sgId.add(sg.getId());
                    }
                    sClass.put("studentgroup", sgId);
                    sClassSet.put(sClass);
                }
            }
            json.put("specific class", sClassSet);
        }catch(Exception ex){
            System.out.println("Testing finished!");
        }

        String jsonStr = json.toString(); //将JSON对象转化为字符串
        try{
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File("timetable.json"))));
            writer.write(jsonStr);
            writer.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }

    }

    public static JSONObject writeComponents(int sessionType, double duration,
                                            int[] classrooms, ArrayList<Integer> cohorts) {

        JSONObject component = new JSONObject();
        component.put("sessionType", sessionType); // 0: cohort; 1: lecture; 2: lab
        component.put("duration", duration); // value type is double
        component.put("classroom", classrooms); // every classroom has an integer id; 0 means null
        component.put("cohorts", cohorts);
        return component;
    }
    public static JSONObject writeASubejct(String name, int courseId, int type, int term,
                                            int pillar, int cohortNum, int totalEnrollNumber,
                                            int sessionNumber, JSONArray componets ) {

        JSONObject subject = new JSONObject();
        subject.put("name", name);
        subject.put("courseId", courseId);
        subject.put("type", type); // 0: CORE; 1: ELECTIVE
        subject.put("term", term);
        subject.put("pillar", pillar); // 0: HASS; 1: ASD; 2: EPD; 3: ESD; 4: ISTD
        subject.put("cohortNumber", cohortNum);
        subject.put("totalEnrollNumber", totalEnrollNumber);
        subject.put("sessionNumber", sessionNumber);
        JSONArray compoent = componets;
        subject.put("component", compoent);

        return subject;
    }

    public static JSONArray writeSubjects(){
        JSONArray subjectSet = new JSONArray();
        try {
            JSONObject c;
            ArrayList<Integer> cohorts = new ArrayList<>();
            cohorts.add(0); cohorts.add(1); cohorts.add(2);
            ArrayList<Integer> zeroCohort = new ArrayList<>();

            JSONArray component = new JSONArray();
            c = writeComponents(0, 1.5, new int[]{0,1,2,3,4,5,6,7,8,9}, zeroCohort);
            component.put(c);
            c = writeComponents(0, 1.5, new int[]{0,1,2,3,4,5,6,7,8,9}, zeroCohort);
            component.put(c);
            c = writeComponents(0, 2, new int[]{0,1,2,3,4,5,6,7,8,9}, zeroCohort);
            component.put(c);

            JSONObject subjectDW = writeASubejct("Digital World", 10009,
                    0, 3, 5, 10, 450, 3, component);
            subjectSet.put(subjectDW);

            JSONArray componentCSE = new JSONArray();
            c = writeComponents(0, 1.5, new int[]{10, 11}, zeroCohort);
            componentCSE.put(c);
            c = writeComponents(0, 1.5, new int[]{10, 11}, zeroCohort);
            componentCSE.put(c);
            c = writeComponents(1, 2, new int[]{20}, cohorts);
            componentCSE.put(c);
            JSONObject subjectCSE = writeASubejct("Computer System Engineering", 50005, 0, 5, 4,
                    3, 150, 3, componentCSE);
            subjectSet.put(subjectCSE);
//            subjectCSE.put("name", "Computer System Engineering");
//            subjectCSE.put("courseId", 50005);
//            subjectCSE.put("type", 0); // 0: CORE; 1: ELECTIVE
//            subjectCSE.put("term", 5);
//            subjectCSE.put("pillar", 4); // 0: HASS; 1: ASD; 2: EPD; 3: ESD; 4: ISTD; 5: Freshman
//            subjectCSE.put("cohortNumber", 3);
//            subjectCSE.put("totalEnrollNumber", 150);
//            subjectCSE.put("sessionNumber", 3);
//
//            JSONArray CSEcompoent = new JSONArray();
            JSONObject component1 = new JSONObject();
            component1.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component1.put("duration", 1.5); // value type is double
            component1.put("classroom", new int[]{10,11}); // every classroom has an integer id; 0 means null
            component1.put("cohorts", zeroCohort);
            JSONObject component2 = new JSONObject();
            component2.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component2.put("duration", 1.5); // value type is double
            component2.put("classroom", new int[]{10,11}); // every classroom has an integer id; 0 means null
            component2.put("cohorts", zeroCohort);
//            JSONObject component3 = new JSONObject();
//            component3.put("sessionType", 1); // 0: cohort; 1: lecture; 2: lab
//            component3.put("duration", 2.0); // value type is double
//            component3.put("classroom", new int[]{20}); // every classroom has an integer id; 0 means null
//            component3.put("cohorts", cohorts);
//            CSEcompoent.put(component1);
//            CSEcompoent.put(component2);
//            CSEcompoent.put(component3);
//            subjectCSE.put("component", CSEcompoent);
//            subjectSet.put(subjectCSE);

            JSONObject subjectESC = new JSONObject();
            subjectESC.put("name", "Elements of Software Construction");
            subjectESC.put("courseId", 50003);
            subjectESC.put("type", 0); // 0: CORE; 1: ELECTIVE
            subjectESC.put("term", 5);
            subjectESC.put("pillar", 4); // 0: HASS; 1: ASD; 2: EPD; 3: ESD; 4: ISTD
            subjectESC.put("cohortNumber", 3);
            subjectESC.put("totalEnrollNumber", 150);
            subjectESC.put("sessionNumber", 3);

            JSONArray ESCcompoent = new JSONArray();
            JSONObject component4 = new JSONObject();
            component4.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component4.put("duration", 2.0); // value type is double
            component4.put("classroom", new int[]{10,11}); // every classroom has an integer id; 0 means null
            component4.put("cohorts", zeroCohort);
            ESCcompoent.put(component1);
            ESCcompoent.put(component2);
            ESCcompoent.put(component4);
            subjectESC.put("component", ESCcompoent);
            // every subject
            subjectSet.put(subjectESC);

            JSONObject subjectPROB = new JSONObject();
            subjectPROB.put("name", "Probability and Statistics");
            subjectPROB.put("courseId", 50034);
            subjectPROB.put("type", 0); // 0: CORE; 1: ELECTIVE
            subjectPROB.put("term", 5);
            subjectPROB.put("pillar", 4); // 0: HASS; 1: ASD; 2: EPD; 3: ESD; 4: ISTD
            subjectPROB.put("cohortNumber", 3);
            subjectPROB.put("totalEnrollNumber", 150);
            subjectPROB.put("sessionNumber", 3);

            JSONArray PROBcompoent = new JSONArray();
            JSONObject component5 = new JSONObject();
            component5.put("sessionType", 1); // 0: cohort; 1: lecture; 2: lab
            component5.put("duration", 1.5); // value type is double
            component5.put("classroom", new int[]{20}); // every classroom has an integer id; 0 means null
            component5.put("cohorts", cohorts);
            JSONObject component6 = new JSONObject();
            component6.put("sessionType", 1); // 0: cohort; 1: lecture; 2: lab
            component6.put("duration", 1.5); // value type is double
            component6.put("classroom", new int[]{20}); // every classroom has an integer id; 0 means null
            component6.put("cohorts", cohorts);
            JSONObject component7 = new JSONObject();
            component7.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component7.put("duration", 2.0); // value type is double
            component7.put("classroom", new int[]{10,11}); // every classroom has an integer id; 0 means null\
            component7.put("cohorts", zeroCohort);
            PROBcompoent.put(component5);
            PROBcompoent.put(component6);
            PROBcompoent.put(component7);
            subjectPROB.put("component", PROBcompoent);
            // every subject
            subjectSet.put(subjectPROB);
            // last-single
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return subjectSet;
    }

    public static JSONObject writeARoom(int id, String name, String location, int capacity, int roomType) {
        JSONObject room = new JSONObject();
        room.put("id", id); // -1 means empty
        room.put("name", name);
        room.put("location", location);
        room.put("capacity", capacity);
        room.put("roomType", roomType); //0: cohort; 1: lecture; 2: lab

        return room;
    }

    // 0 - 9: freshman cohort rooms
    // 10, 11: term 5 ISTD cohort room
    // 12, 13, 14: term 7 ISTD electives
    // 15, 16, 17: empty cohort room
    // 18: LT2; 19: LT4; 20: LT5
    // 21: bio lab; 22: physics lab; 23: safety lab
    public static JSONArray writeClassroom() {
        JSONArray classroomSet = new JSONArray();
        JSONObject room;
        room = writeARoom(0, "CC1", "1.301", 50, 0);
        classroomSet.put(room);
        room = writeARoom(1, "CC2", "1.302", 50, 0);
        classroomSet.put(room);
        room = writeARoom(2, "CC3", "2.505", 50, 0);
        classroomSet.put(room);
        room = writeARoom(3, "CC4", "1.301", 50, 0);
        classroomSet.put(room);
        room = writeARoom(4, "CC5", "1.302", 50, 0);
        classroomSet.put(room);
        room = writeARoom(5, "CC6", "2.505", 50, 0);
        classroomSet.put(room);
        room = writeARoom(6, "CC7", "1.301", 50, 0);
        classroomSet.put(room);
        room = writeARoom(7, "CC8", "1.302", 50, 0);
        classroomSet.put(room);
        room = writeARoom(8, "CC9", "2.505", 50, 0);
        classroomSet.put(room);
        room = writeARoom(9, "CC10", "1.301", 50, 0);
        classroomSet.put(room);
        room = writeARoom(10, "CC13", "1.302", 50, 0);
        classroomSet.put(room);
        room = writeARoom(11, "CC14", "2.505", 50, 0);
        classroomSet.put(room);
        room = writeARoom(12, "CC15", "1.301", 50, 0);
        classroomSet.put(room);
        room = writeARoom(13, "CC16", "1.302", 50, 0);
        classroomSet.put(room);
        room = writeARoom(14, "CC17", "2.505", 50, 0);
        classroomSet.put(room);
        room = writeARoom(15, "Empty1", "1.301", 50, 0);
        classroomSet.put(room);
        room = writeARoom(16, "Empty2", "1.302", 50, 0);
        classroomSet.put(room);
        room = writeARoom(17, "Empty3", "2.505", 50, 0);
        classroomSet.put(room);
        room = writeARoom(18, "LT2", "1.302", 150, 1);
        classroomSet.put(room);
        room = writeARoom(19, "LT4", "2.505", 150, 1);
        classroomSet.put(room);
        room = writeARoom(20, "LT5", "1.302", 150, 1);
        classroomSet.put(room);
        room = writeARoom(21, "BioLab", "2.505", 50, 2);
        classroomSet.put(room);
        room = writeARoom(22, "PhysicsLab", "1.302", 50, 2);
        classroomSet.put(room);
        room = writeARoom(23, "SafetyLab", "2.505", 50, 2);
        classroomSet.put(room);

        return classroomSet;
    }

    public static JSONObject writeAStudentGroup(int term, int cohort, String name, int size,
                                                 List<Integer> subjects, int pillar, int id) {
        JSONObject sg = new JSONObject();
        sg.put("term", term);
        sg.put("cohort", cohort);
        sg.put("name", name);
        sg.put("size", size);
        sg.put("subjects", subjects);
        sg.put("pillar", pillar); //0: HASS; 1: ASD; 4: ISTD;
        sg.put("id", id);
        return sg;
    }
    public static JSONArray writeStudentGroup() {
        ArrayList<Integer> subjects = new ArrayList<>();
        subjects.add(50005);
        subjects.add(50003);
        subjects.add(50034);

        List<Integer> freshmore = Arrays.asList(10009);

        JSONArray studentGroupSet = new JSONArray();

        JSONObject sg0 = writeAStudentGroup(3, 0,"t3c1", 50, freshmore, 5, 0);
        studentGroupSet.put(sg0);
        JSONObject sg1 = writeAStudentGroup(3, 1,"t3c2", 50, freshmore, 5, 1);
        studentGroupSet.put(sg1);
        JSONObject sg2 = writeAStudentGroup(3, 2,"t3c3", 50, freshmore, 5, 2);
        studentGroupSet.put(sg2);
        JSONObject sg3 = writeAStudentGroup(3, 3,"t3c4", 50, freshmore, 5, 3);
        studentGroupSet.put(sg3);
        JSONObject sg4 = writeAStudentGroup(3, 4,"t3c5", 50, freshmore, 5, 4);
        studentGroupSet.put(sg4);
        JSONObject sg5 = writeAStudentGroup(3, 5,"t3c6", 50, freshmore, 5, 5);
        studentGroupSet.put(sg5);
        JSONObject sg6 = writeAStudentGroup(3, 6,"t3c7", 50, freshmore, 5, 6);
        studentGroupSet.put(sg6);
        JSONObject sg7 = writeAStudentGroup(3, 7,"t3c8", 50, freshmore, 5, 7);
        studentGroupSet.put(sg7);
        JSONObject sg8 = writeAStudentGroup(3, 8,"t3c9", 50, freshmore, 5, 8);
        studentGroupSet.put(sg8);
        JSONObject sg9 = writeAStudentGroup(3, 9,"t3c10", 50, freshmore, 5, 9);
        studentGroupSet.put(sg9);

        JSONObject studentgroup1 = new JSONObject();
        studentgroup1.put("term", 5);
        studentgroup1.put("cohort", 0);
        studentgroup1.put("name", "t5c1");
        studentgroup1.put("size", 50);
        studentgroup1.put("subjects", subjects);
        studentgroup1.put("pillar", 4); //0: HASS; 1: ASD; 4: ISTD;
        studentgroup1.put("id", 10);
        studentGroupSet.put(studentgroup1);

        JSONObject studentgroup2 = new JSONObject();
        studentgroup2.put("term", 5);
        studentgroup2.put("cohort", 1);
        studentgroup2.put("name", "t5c2");
        studentgroup2.put("size", 50);
        studentgroup2.put("subjects", subjects);
        studentgroup2.put("pillar", 4); //0: HASS; 1: ASD; 4: ISTD;
        studentgroup2.put("id", 11);
        studentGroupSet.put(studentgroup2);

        JSONObject studentgroup3 = new JSONObject();
        studentgroup3.put("term", 5);
        studentgroup3.put("cohort", 2);
        studentgroup3.put("name", "t5c3");
        studentgroup3.put("size", 50);
        studentgroup3.put("subjects", subjects);
        studentgroup3.put("pillar", 4); //0: HASS; 1: ASD; 4: ISTD;
        studentgroup3.put("id", 12);
        studentGroupSet.put(studentgroup3);

        return studentGroupSet;
    }

    public static JSONObject writeAProf(int id, String name, HashMap<Integer, List> courseTable) {
        JSONObject prof = new JSONObject();
        prof.put("id", id);
        prof.put("name", name);
        HashMap<Integer, List> courseTable1 = courseTable;
        prof.put("coursetable",courseTable);

        return prof;
    }

    public static JSONArray writeProfessor() {
        ArrayList<Integer> cohortLs1 = new ArrayList<>();
        cohortLs1.add(0);cohortLs1.add(1);cohortLs1.add(2);
        ArrayList<Integer> cohortLs2 = new ArrayList<>();
        cohortLs2.add(0); cohortLs2.add(1);
        ArrayList<Integer> cohortLs3 = new ArrayList<>();
        cohortLs3.add(2);

        JSONArray professorSet = new JSONArray();

        JSONObject prof0 = new JSONObject();
        prof0.put("id", 0);
        prof0.put("name", "Natalie");
        HashMap<Integer, List> courseTable0 = new HashMap<>();
        courseTable0.put(50005, Arrays.asList(0,1));
        courseTable0.put(10009, Arrays.asList(3,4));
        prof0.put("coursetable",courseTable0);
        professorSet.put(prof0);

        JSONObject prof1 = new JSONObject();
        prof1.put("id", 1);
        prof1.put("name", "Sudipta");
        HashMap<Integer, ArrayList> courseTable1 = new HashMap<>();
        courseTable1.put(50003, cohortLs1);
        prof1.put("coursetable",courseTable1);
        professorSet.put(prof1);

        JSONObject prof2 = new JSONObject();
        prof2.put("id", 2);
        prof2.put("name", "Tony");
        HashMap<Integer, ArrayList> courseTable2 = new HashMap<>();
        courseTable2.put(50034, cohortLs1);
        prof2.put("coursetable",courseTable2);
        professorSet.put(prof2);

        JSONObject prof3 = new JSONObject();
        prof3.put("id", 3);
        prof3.put("name", "David");
        HashMap<Integer, ArrayList> courseTable3 = new HashMap<>();
        courseTable3.put(50005, cohortLs3);
        prof3.put("coursetable",courseTable3);
        professorSet.put(prof3);

        HashMap<Integer, List> courseTable4 = new HashMap<>();
        courseTable4.put(10009, Arrays.asList(0,1));
        JSONObject prof4 = writeAProf(4, "Oka", courseTable4);

        professorSet.put(prof4);

        HashMap<Integer, List> courseTable5 = new HashMap<>();
        courseTable5.put(10009, Arrays.asList(2,5));
        JSONObject prof5 = writeAProf(5, "Gemma", courseTable5);

        professorSet.put(prof5);

        HashMap<Integer, List> courseTable6 = new HashMap<>();
        courseTable6.put(10009, Arrays.asList(6,7));
        JSONObject prof6 = writeAProf(6, "Norman", courseTable6);

        professorSet.put(prof6);

        HashMap<Integer, List> courseTable7 = new HashMap<>();
        courseTable7.put(10009, Arrays.asList(8,9));
        JSONObject prof7 = writeAProf(6, "DuoDuo", courseTable7);

        professorSet.put(prof7);
        return professorSet;
    }

    public static void writeInput() {
        JSONObject input = new JSONObject();
        JSONArray subjectSet = writeSubjects();
        JSONArray classroomSet = writeClassroom();
        JSONArray studentGroupSet = writeStudentGroup();
        JSONArray professorSet = writeProfessor();
        try {
            input.put("subject", subjectSet);
            input.put("classroom", classroomSet);
            input.put("studentGroup", studentGroupSet);
            input.put("professor", professorSet);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        String jsonStr = input.toString(); //将JSON对象转化为字符串
        try{
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File("input.json"))));
            writer.write(jsonStr);
            writer.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static RoomList readJsonRoomList() {
        StringBuilder jsonStr = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("input.json"))); //或者使用Scanner
            String temp = "";
            while((temp = reader.readLine())!= null)
                jsonStr.append(temp);
                reader.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(jsonStr.toString());
            JSONArray jsonRoom = json.getJSONArray("classroom");
            Classroom[] rooms = new Classroom[jsonRoom.length()];
            int lecStart = -1;
            int labStart = -1;
            for (int i = 0; i < jsonRoom.length(); i++) {
                JSONObject room = (JSONObject) jsonRoom.get(i);
                String name = room.getString("name");
                String location = room.getString("location");
                int id = room.getInt("id");
                int roomType = room.getInt("roomType");
                if (lecStart == -1 && roomType == 1) { // first lecture hall
                    lecStart = i;
                }
                if (labStart == -1 && roomType == 2) { // first lecture hall
                    labStart = i;
                }
                ClassType type = ClassType.CBL;
                    switch (roomType) {
                        case 0:
                            type = ClassType.CBL;
                            break;
                        case 1:
                            type = ClassType.LEC;
                            break;
                        case 2:
                            type = ClassType.LAB;
                            break;
                }
                int capacity = room.getInt("capacity");
                Classroom classroom = new Classroom(name, location, capacity, type, id);
                rooms[i] = classroom;
            }
            if (labStart == -1) {
                labStart = rooms.length;
            }
            return new RoomList(rooms, lecStart, labStart);
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Subject> readJsonSubject(RoomList roomList) {
        ArrayList<Subject> subjects = new ArrayList<>();
        StringBuilder jsonStr = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("input.json"))); //或者使用Scanner
            String temp = "";
            while((temp = reader.readLine())!= null)
                jsonStr.append(temp);
            reader.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(jsonStr.toString());
            JSONArray jsonSubject = json.getJSONArray("subject");

            for (int i = 0; i < jsonSubject.length(); i++) {
                JSONObject subject = (JSONObject) jsonSubject.get(i);
                String name = subject.getString("name");
                int term = subject.getInt("term");
                int pillar = subject.getInt("pillar");

                Pillar pillarType = Pillar.ISTD;
                switch (pillar) {
                    case 0:
                        pillarType = Pillar.HASS;
                        break;
                    case 1:
                        pillarType = Pillar.ASD;
                        break;
                    case 2:
                        pillarType = Pillar.EPD;
                        break;
                    case 3:
                        pillarType = Pillar.ESD;
                        break;
                    case 4:
                        pillarType = Pillar.ISTD;
                        break;
                    case 5:
                        pillarType = Pillar.Freshmore;
                        break;
                }

                int courseId = subject.getInt("courseId");
                int type = subject.getInt("type");
                SubjectType subjectType;
                if (type == 0) {
                    subjectType = SubjectType.CORE;
                }else {
                    subjectType = SubjectType.ELECTIVE;
                }
                int sessionNum = subject.getInt("sessionNumber");
                int cohortNum = subject.getInt("cohortNumber");
                int totalEnrollNumber = subject.getInt("totalEnrollNumber");

                GenericClass[] subjectComp = new GenericClass[sessionNum];
                JSONArray component = subject.getJSONArray("component");
                for (int j = 0; j < component.length(); j++) {
                    JSONObject g = component.getJSONObject(j);
                    double duration = g.getDouble("duration");
                    JSONArray possibleRoomSet = g.getJSONArray("classroom");
                    Classroom[] rooms = new Classroom[possibleRoomSet.length()];
                    for (int d = 0; d < possibleRoomSet.length(); d++) {
                        rooms[d] = roomList.getRoomList()[possibleRoomSet.getInt(d)];
                    }
                    int sessionType = g.getInt("sessionType");
                    ClassType stype;
                    if (sessionType == 0) {
                        stype = ClassType.CBL;
                    }else if (sessionType == 1) {
                        stype = ClassType.LEC;
                    }else {
                        stype = ClassType.LAB;
                    }
                    JSONArray cohorts =  g.getJSONArray("cohorts");
                    if (cohorts.length() == 0) {
                        subjectComp[j] = new GenericClass(stype, duration, rooms);
                    }else {
                        ArrayList<Integer> s = new ArrayList<>();
                        for (int k = 0; k < cohorts.length(); k++) {
                            s.add((int)cohorts.get(k));
                        }
                        subjectComp[j] = new GenericClass(stype, duration, rooms, s);
                    }
                }

                int id = courseId;
//                System.out.println(id);
                Subject subject1 = new Subject(name, id, subjectType, term, totalEnrollNumber/cohortNum,
                        cohortNum, subjectComp);
                subjects.add(subject1);
            }

            return subjects;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<StudentGroup> readJsonStudentGroup(ArrayList<Subject> subjectList) {
        StringBuilder jsonStr = new StringBuilder();
        ArrayList<StudentGroup> studentGroups = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File("input.json"))); //或者使用Scanner
            String temp = "";
            while((temp = reader.readLine())!= null)
                jsonStr.append(temp);
            reader.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(jsonStr.toString());
            JSONArray jsonStudent = json.getJSONArray("studentGroup");
            for(int i=0;i<jsonStudent.length();i++){
                JSONObject studentGroup = jsonStudent.getJSONObject(i);
                int pillarNo = studentGroup.getInt("pillar");
                Pillar pillar = Pillar.ISTD;
                switch (pillarNo) {
                    case 0:
                        pillar = Pillar.HASS;
                        break;
                    case 1:
                        pillar = Pillar.ASD;
                        break;
                    case 2:
                        pillar = Pillar.EPD;
                        break;
                    case 3:
                        pillar = Pillar.ESD;
                        break;
                    case 4:
                        pillar = Pillar.ISTD;
                        break;
                    case 5:
                        pillar = Pillar.Freshmore;
                        break;
                }

                int size = studentGroup.getInt("size");
                String name = studentGroup.getString("name");
                int cohort = studentGroup.getInt("cohort");
                int term = studentGroup.getInt("term");
                int id = studentGroup.getInt("id");
                JSONArray subjects = studentGroup.getJSONArray("subjects");
                ArrayList<Subject> subjectSet = new ArrayList<>();
                for (int j = 0; j < subjects.length(); j++) {
                    int subjectId = subjects.getInt(j);
                    for (Subject s: subjectList) {
                        if (s.getId() == subjectId) {
                            subjectSet.add(s);
                            break;
                        }
                    }
                }
                StudentGroup studentGroup1 = new StudentGroup(id, name, pillar, term, cohort, subjectSet, size);
                studentGroups.add(studentGroup1);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return studentGroups;
    }

    public static ArrayList<Professor> readJsonProfessor(ArrayList<Subject> subjects, ArrayList<StudentGroup> studentGroups) {

        StringBuilder jsonStr = new StringBuilder();
        ArrayList<Professor> professors = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File("input.json"))); //或者使用Scanner
            String temp = "";
            while((temp = reader.readLine())!= null)
                jsonStr.append(temp);
            reader.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(jsonStr.toString());
            JSONArray jsonProf = json.getJSONArray("professor");
            for(int i=0;i<jsonProf.length();i++){
                JSONObject person = (JSONObject)jsonProf.get(i);
                String name = (String)person.get("name"); //获取JSON对象的键值对
                int id = (int)person.get("id");
                Professor p = new Professor(name, id);

                JSONObject coursetable = (JSONObject) person.get("coursetable");
                for (String courseId: coursetable.keySet()) {
                    JSONArray student = coursetable.getJSONArray(courseId);
                    for (int cohortNo = 0; cohortNo < student.length(); cohortNo++) {
                        for (StudentGroup sg: studentGroups) {
                            for (Subject sub: sg.getSubjects()) {
                                if (sg.getCohort() == student.getInt(cohortNo) && sub.getId() == Integer.valueOf(courseId)) {
                                    p.addSubject(sub, sg);
                                }
                            }
                        }
                    }
                }
                professors.add(p);
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return professors;
    }
}
