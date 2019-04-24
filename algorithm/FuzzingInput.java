package algorithm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class FuzzingInput {
    public static void main(String[] args) {

    }
    public static void writeInput() {
        JSONObject input = new JSONObject();
        JSONArray subjectSet = writeSubjects();
        JSONArray classroomSet = generateRandomRoom(5, 0.2);
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
}
