package algorithm;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class JsonUtilsTest {
    @Test
    public void testcase1() {
        JsonUtils.writeInput();
        assert(true);
    }

    @Test
    public void testcase2() {
        boolean thrown = false;
        try {
            JsonUtils.writeToJson(null);
        }catch(Exception e) {
            thrown = true;
        }finally {
            assert thrown;
        }
    }

    @Test
    public void testcase3() {
        boolean thrown = false;
        try {
            Chromosome c = new Chromosome(3,3,3);
            if (c == null) {
                assert true;
                return;
            }
        }catch (Exception e) {
            thrown = true;
        }finally {
            assert thrown;
        }
    }

    @Test
    public void testcase4() {
        try {
            JsonUtils.writeInput();
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void testcase5() {
        boolean asrt = true;
        try {
            RoomList roomlist = JsonUtils.readJsonRoomList();
        }catch (Exception e) {
            asrt = false;
        }finally {
            assert asrt;
        }

    }

    @Test
    public void testcase6() {
        boolean asrt = true;
        try {
            RoomList roomlist = JsonUtils.readJsonRoomList();
            JsonUtils.readJsonSubject(roomlist);
        }catch (Exception e) {
            asrt = false;
        }finally {
            assert asrt;
        }
    }

    @Test
    public void testcase7() {
        boolean asrt = true;
        try {
            RoomList roomlist = JsonUtils.readJsonRoomList();
            ArrayList<Subject> subjects = JsonUtils.readJsonSubject(roomlist);
            JsonUtils.readJsonStudentGroup(subjects);
        }catch (Exception e) {
            asrt = false;
        }finally {
            assert asrt;
        }
    }

    @Test
    public void testcase8() {
        boolean asrt = true;
        try {
            RoomList roomlist = JsonUtils.readJsonRoomList();
            ArrayList<Subject> subjects = JsonUtils.readJsonSubject(roomlist);
            ArrayList<StudentGroup> studentGroups = JsonUtils.readJsonStudentGroup(subjects);
            JsonUtils.readJsonProfessor(subjects, studentGroups);
        }catch (Exception e) {
            asrt = false;
        }finally {
            assert asrt;
        }
    }
}