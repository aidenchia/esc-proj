

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonUtils {
    public static void writeToJson(Chromosome c) {
        JSONObject json = new JSONObject();
        JSONArray sClassSet = new JSONArray();
        try {
            SpecificClass[] lineC = c.getLineChromosome();
            for(SpecificClass s: lineC) {
                if (s != null) {
                    JSONObject sClass = new JSONObject();
                    sClass.put("subject", s.getSubject().getName());
                    sClass.put("cohort", s.getCohortNo());
                    sClass.put("session", s.getSession());
                    sClass.put("weekday", s.getWeekday());
                    sClass.put("startTime", s.getStartTime());
                    sClass.put("classroom", s.getClassroom().getName());
                    sClassSet.put(sClass);
                }
            }
            json.put("specific class", sClassSet);
        }catch(JSONException ex){
            ex.printStackTrace();
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

    private static JSONArray writeSubjects(){
        JSONArray subjectSet = new JSONArray();
        try {
            ArrayList<Integer> cohorts = new ArrayList<>();
            cohorts.add(0); cohorts.add(1); cohorts.add(2);
            ArrayList<Integer> zeroCohort = new ArrayList<>();

            JSONObject subjectCSE = new JSONObject();
            subjectCSE.put("name", "Computer System Engineering");
            subjectCSE.put("courseId", "50.005");
            subjectCSE.put("type", 0); // 0: CORE; 1: ELECTIVE
            subjectCSE.put("term", 5);
            subjectCSE.put("pillar", 4); // 0: HASS; 1: ASD; 2: EPD; 3: ESD; 4: ISTD
            subjectCSE.put("cohortNumber", 3);
            subjectCSE.put("totalEnrollNumber", 150);
            subjectCSE.put("sessionNumber", 3);

            JSONArray CSEcompoent = new JSONArray();
            JSONObject component1 = new JSONObject();
            component1.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component1.put("duration", 1.5); // value type is double
            component1.put("classroom", 0); // every classroom has an integer id; 0 means null
            component1.put("cohorts", zeroCohort);
            JSONObject component2 = new JSONObject();
            component2.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component2.put("duration", 1.5); // value type is double
            component2.put("classroom", 0); // every classroom has an integer id; 0 means null
            component2.put("cohorts", zeroCohort);
            JSONObject component3 = new JSONObject();
            component3.put("sessionType", 1); // 0: cohort; 1: lecture; 2: lab
            component3.put("duration", 2.0); // value type is double
            component3.put("classroom", 0); // every classroom has an integer id; 0 means null
            component3.put("cohorts", cohorts);
            CSEcompoent.put(component1);
            CSEcompoent.put(component2);
            CSEcompoent.put(component3);
            subjectCSE.put("component", CSEcompoent);
            subjectSet.put(subjectCSE);

            JSONObject subjectESC = new JSONObject();
            subjectESC.put("name", "Elements of Software Construction");
            subjectESC.put("courseId", "50.003");
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
            component4.put("classroom", 0); // every classroom has an integer id; 0 means null
            component4.put("cohorts", zeroCohort);
            ESCcompoent.put(component1);
            ESCcompoent.put(component2);
            ESCcompoent.put(component4);
            subjectESC.put("component", ESCcompoent);
            // every subject
            subjectSet.put(subjectESC);

            JSONObject subjectPROB = new JSONObject();
            subjectPROB.put("name", "Probability and Statistics");
            subjectPROB.put("courseId", "50.034");
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
            component5.put("classroom", 0); // every classroom has an integer id; 0 means null
            component5.put("cohorts", cohorts);
            JSONObject component6 = new JSONObject();
            component6.put("sessionType", 1); // 0: cohort; 1: lecture; 2: lab
            component6.put("duration", 1.5); // value type is double
            component6.put("classroom", 0); // every classroom has an integer id; 0 means null
            component6.put("cohorts", cohorts);
            JSONObject component7 = new JSONObject();
            component7.put("sessionType", 0); // 0: cohort; 1: lecture; 2: lab
            component7.put("duration", 2.0); // value type is double
            component7.put("classroom", 0); // every classroom has an integer id; 0 means null\
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

    private static JSONArray writeClassroom() {
        JSONArray classroomSet = new JSONArray();
        JSONObject classroom1 = new JSONObject();
        classroom1.put("id", 1); // 0 means empty
        classroom1.put("name", "CC13");
        classroom1.put("location", "2.503");
        classroom1.put("capacity", 50);
        classroom1.put("roomType", 0); //0: cohort; 1: lecture; 2: lab
        classroomSet.put(classroom1);
        JSONObject classroom2 = new JSONObject();
        classroom2.put("id", 2); // 0 means empty
        classroom2.put("name", "CC14");
        classroom2.put("location", "2.504");
        classroom2.put("capacity", 50);
        classroom2.put("roomType", 0); //0: cohort; 1: lecture; 2: lab
        classroomSet.put(classroom2);
        JSONObject classroom3 = new JSONObject();
        classroom3.put("id", 3); // 0 means empty
        classroom3.put("name", "LT5");
        classroom3.put("location", "2.501");
        classroom3.put("capacity", 50);
        classroom3.put("roomType", 1); //0: cohort; 1: lecture; 2: lab
        classroomSet.put(classroom3);
        return classroomSet;
    }

    private static JSONArray writeStudentGroup() {
        ArrayList<String> subjects = new ArrayList<>();
        subjects.add("Computer System Engineering");
        subjects.add("Elements of Software Construction");
        subjects.add("Probability and Statistics");

        JSONArray studentGroupSet = new JSONArray();

        JSONObject studentgroup1 = new JSONObject();
        studentgroup1.put("term", 5);
        studentgroup1.put("cohort", 0);
        studentgroup1.put("name", "t5c1");
        studentgroup1.put("size", 50);
        studentgroup1.put("subjects", subjects);
        studentgroup1.put("pillar", 4); //0: HASS; 1: ASD; 4: ISTD;
        studentGroupSet.put(studentgroup1);

        JSONObject studentgroup2 = new JSONObject();
        studentgroup2.put("term", 5);
        studentgroup2.put("cohort", 1);
        studentgroup2.put("name", "t5c2");
        studentgroup2.put("size", 50);
        studentgroup2.put("subjects", subjects);
        studentgroup2.put("pillar", 4); //0: HASS; 1: ASD; 4: ISTD;
        studentGroupSet.put(studentgroup2);

        JSONObject studentgroup3 = new JSONObject();
        studentgroup3.put("term", 5);
        studentgroup3.put("cohort", 2);
        studentgroup3.put("name", "t5c3");
        studentgroup3.put("size", 50);
        studentgroup3.put("subjects", subjects);
        studentgroup3.put("pillar", 4); //0: HASS; 1: ASD; 4: ISTD;
        studentGroupSet.put(studentgroup3);

        return studentGroupSet;
    }

    private static JSONArray writeProfessor() {
        ArrayList<Integer> cohortLs1 = new ArrayList<>();
        cohortLs1.add(0);cohortLs1.add(1);cohortLs1.add(2);
        ArrayList<Integer> cohortLs2 = new ArrayList<>();
        cohortLs2.add(0); cohortLs2.add(1);
        ArrayList<Integer> cohortLs3 = new ArrayList<>();
        cohortLs3.add(2);

        JSONArray professorSet = new JSONArray();

        JSONObject prof1 = new JSONObject();
        prof1.put("id", 0);
        prof1.put("name", "Natalie");
        HashMap<String, ArrayList> courseTable1 = new HashMap<>();
        courseTable1.put("Computer System Engineering", cohortLs2);
        prof1.put("coursetable",courseTable1);
        professorSet.put(prof1);

        JSONObject prof2 = new JSONObject();
        prof2.put("id", 1);
        prof2.put("name", "Sun Jun");
        HashMap<String, ArrayList> courseTable2 = new HashMap<>();
        courseTable2.put("Elements of Software Construction", cohortLs1);
        prof2.put("coursetable",courseTable2);
        professorSet.put(prof2);

        JSONObject prof3 = new JSONObject();
        prof3.put("id", 2);
        prof3.put("name", "Tony");
        HashMap<String, ArrayList> courseTable3 = new HashMap<>();
        courseTable3.put("Probability and Statistics", cohortLs1);
        prof3.put("coursetable",courseTable3);
        professorSet.put(prof3);

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

                String courseId = subject.getString("courseId");
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
                    int classroomNo = g.getInt("classroom");
                    Classroom room;
                    if (classroomNo == -1) {
                        room = null;
                    }else {
                        room = roomList.getRoomList()[classroomNo];
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
                        subjectComp[j] = new GenericClass(stype, duration, room);
                    }else {
                        ArrayList<Integer> s = new ArrayList<>();
                        for (int k = 0; k < cohorts.length(); k++) {
                            s.add((int)cohorts.get(k));
                        }
                        subjectComp[j] = new GenericClass(stype, duration, room, s);
                    }
                }
                String s = String.valueOf(courseId.charAt(0)) + String.valueOf(courseId.charAt(1)) +
                        String.valueOf(courseId.charAt(3)) + String.valueOf(courseId.charAt(4)) +
                        String.valueOf(courseId.charAt(5));
                int id = Integer.valueOf(s);
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
                StudentGroup studentGroup1 = new StudentGroup(0, name, pillar, term, cohort, subjectSet, size);
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
                                if (sg.getCohort() == cohortNo && sub.getId() == Integer.valueOf(courseId)) {
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
