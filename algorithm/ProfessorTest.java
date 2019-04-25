package algorithm;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ProfessorTest {
    @Test(expected = java.lang.NullPointerException.class)
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
    @Test(expected = java.lang.NullPointerException.class)
    public void testProfAddsubjec1() {
        StudentGroup sg1 = new StudentGroup();
        StudentGroup sg2 = new StudentGroup();
        Subject s = new Subject();

        Professor p = new Professor("Tony", 12);
        p.addSubject(s, sg1);

        System.out.println(p.getCourseTable().get("sss").size());
        p.addSubject(s, sg2);
        System.out.println(p.getCourseTable().get("sss").size());
    }
    @Test(expected = java.lang.NullPointerException.class)
    public void testProfAddsubject2() {
        StudentGroup sg1 = new StudentGroup();
        StudentGroup sg2 = new StudentGroup();
        Subject s = new Subject();

        Professor p = new Professor("David", 12);
        p.addSubject(s, sg1);

        System.out.println(p.getCourseTable().get("sss").size());
        p.addSubject(s, sg2);
        System.out.println(p.getCourseTable().get("sss").size());
    }
    @Test(expected = java.lang.NullPointerException.class)
    public void testProfAddsubject3() {
        StudentGroup sg1 = new StudentGroup();
        StudentGroup sg2 = new StudentGroup();
        Subject s = new Subject();

        Professor p = new Professor("Sun Jun", 13);
        p.addSubject(s, sg1);

        System.out.println(p.getCourseTable().get("sss").size());
        p.addSubject(s, sg2);
        p.addSubject(s, sg2);
        System.out.println(p.getCourseTable().get("sss").size());
    }
}