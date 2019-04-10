

import org.junit.Test;

import static org.junit.Assert.*;

public class ProfessorTest {
    @Test
    public void testProfAddsubject() {
        StudentGroup sg1 = new StudentGroup();
        StudentGroup sg2 = new StudentGroup();
        Subject s = new Subject();

        Professor p = new Professor("Nat", 12);
        p.addSubject(s, sg1);

        System.out.println(p.getCourseTable().get("sss").size());
        p.addSubject(s, sg2);
        System.out.println(p.getCourseTable().get("sss").size());
    }
}