package algorithm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuzzingInput {
    public static void main(String[] args) {
        writeInput(5, 3, 0.2);
    }
    public static void writeInput(int roomNum, int subjNum, double roomRatio) {
        JSONObject input = new JSONObject();
        JSONArray classroomSet = generateRandomRoom(roomNum, roomRatio);
        JSONArray subjectSet = generateRandomSubject(subjNum, classroomSet, (int)(roomNum*roomRatio));
        JSONArray studentGroupSet = generateRandomSG(subjectSet);
        JSONArray professorSet = generateRandomProf(studentGroupSet);
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
    private static JSONArray generateRandomRoom(int totalNum, double ratio) {
        JSONArray classroomSet = new JSONArray();
        JSONObject room;
        int id = 0;
        String cohort = "CC";
        String lecture = "LT";

        int splitPoint = (int)(totalNum * ratio);
        int forIdx = 0;

        for (forIdx = 0; forIdx < splitPoint; forIdx++) {
            room = JsonUtils.writeARoom(id, cohort + String.valueOf(id), "UNKNOWN", 50, 0);
            classroomSet.put(room);
            id += 1;
        }
        for (forIdx = id; forIdx < totalNum; forIdx++) {
            room = JsonUtils.writeARoom(id, lecture + String.valueOf(id), "UNKNOWN", 50, 1);
            classroomSet.put(room);
            id += 1;
        }

        return classroomSet;
    }

    private static JSONArray generateRandomSubject(int totalNum, JSONArray roomList, int split) {
        JSONArray subjects = new JSONArray();
        JSONObject subj;
        JSONArray compoent = new JSONArray();
        JSONObject c;
        ArrayList<Integer> zeroCohort = new ArrayList<>();

        int[] cohorts = new int[split];
        for (int i = 0; i < split; i++) {
            cohorts[i] = i;
        }
        int[] lectures = new int[roomList.length() - split];
        for (int i = split; i < roomList.length(); i++) {
            lectures[i-split] = i;
        }
        c = JsonUtils.writeComponents(0, 1.5, cohorts, zeroCohort);
        compoent.put(c);
        compoent.put(c);
        c = JsonUtils.writeComponents(1, 2.0, lectures, zeroCohort);
        compoent.put(c);
        for (int i = 0; i < totalNum; i++) {
            subj = JsonUtils.writeASubejct("subject" + String.valueOf(i), i, 0, 3, 4, 3,
                    150, 3, compoent);
            subjects.put(subj);
        }
        return subjects;
    }

    private static JSONArray generateRandomSG(JSONArray subjects) {
        JSONArray sgs = new JSONArray();
        JSONObject sg;
        for (int  i = 0; i < subjects.length(); i++) {
            int term = subjects.getJSONObject(i).getInt("term");
            int courseId = subjects.getJSONObject(i).getInt("courseId");
            sg = JsonUtils.writeAStudentGroup(term, 0, "null", 50, List.of(courseId), 4, i);
            sgs.put(sg);
        }
        return sgs;
    }

    private static JSONArray generateRandomProf(JSONArray sgs) {
        JSONArray profs = new JSONArray();
        JSONObject prof;
        for (int i = 0; i < sgs.length(); i++) {
            int course = sgs.getJSONObject(i).getJSONArray("subjects").getInt(0);
            int sgId = sgs.getJSONObject(i).getInt("id");
            HashMap<Integer, List> map = new HashMap<>();
            map.put(course, List.of(sgId));
            prof = JsonUtils.writeAProf(i, "ANYONE", map);
            profs.put(prof);
        }
        return profs;
    }
}
