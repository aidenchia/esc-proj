package algorithm;

import java.util.ArrayList;
import java.util.Random;

public class Scheduler {

    private static Classroom[] classrooms;
    private static RoomList roomList;
    public static ArrayList<Subject> subjects;
    private static ArrayList<StudentGroup> studentGroupSet;
    private static ArrayList<Professor> professorSet;

    public static void main(String[] args) throws Exception {

        JsonUtils.writeInput();

        importDatabase();

        Chromosome[] currentGen = new Chromosome[25];
        int[] scoreSet = new int[25];

        int[] score = {10, 10, 10};
        for (int i = 0; i < currentGen.length; i++) {
            currentGen[i] = new Chromosome(4, 10, 3);
            rate(currentGen[i]);
            scoreSet[i] = currentGen[i].getScore();
        }
        for(int i: scoreSet) {
            System.out.print(i + " ");
        }
        System.out.println();

        Chromosome answer = evolution(200000, currentGen);
        JsonUtils.writeToJson(answer);

        printChromosome(answer, 3, 0);
        System.out.println("---------------");
        printChromosome(answer, 3, 1);
        System.out.println("---------------");
        printChromosome(answer, 3, 2);
        System.out.println("---------------");

        for (SpecificClass s: answer.getLineChromosome()) {
            if (s != null && s.getSubject().getTerm() == 3) {
                s.setClassroom(roomList.getFreshmoreRoom(s.getCohortNo().get(0)));
            }
        }
        printChromosome(answer, 3, 0);
        System.out.println("---------------");
        printChromosome(answer, 3, 1);
        System.out.println("---------------");
        printChromosome(answer, 3, 2);
        System.out.println("---------------");

//        for (StudentGroup sg: studentGroupSet) {
//            System.out.println(sg.getCohort());
//            for (Subject sub: sg.getSubjects()) {
//                System.out.println(sub.getName());
//            }
//            System.out.println("------------");
//        }
//
//        for (Professor p: professorSet) {
//            System.out.println(p.getName());
//            for (Subject sub: p.getCourseTable().keySet()) {
//                for (StudentGroup sg: p.getCourseTable().get(sub)) {
//                    System.out.println(sub.getName() + " " + sg.getCohort());
//                }
//            }
//            System.out.println("-------------");
//        }
    }

    private static Chromosome evolution(int gen, Chromosome[] initGen) {
        Chromosome[] tempPool = new Chromosome[25];
        Chromosome[] currentGen = initGen;
        int[] scoreSet = new int[currentGen.length];
        for (int g = 0; g < gen; g++) {
            for (int i = 0; i < currentGen.length; i++) {
                scoreSet[i] = currentGen[i].getScore();
            }
            ArrayList<Integer> s = findThreeSmallestPos(scoreSet);
            Random r = new Random();
            tempPool[0] = Chromosome.merge(currentGen[s.get(0)],currentGen[s.get(1)]);
            rate(tempPool[0]);
            tempPool[1] = Chromosome.merge(currentGen[s.get(1)],currentGen[s.get(0)]);
            rate(tempPool[1]);
            tempPool[2] = Chromosome.merge(currentGen[s.get(1)],currentGen[s.get(2)]);
            rate(tempPool[2]);
            tempPool[3] = Chromosome.merge(currentGen[s.get(2)],currentGen[s.get(1)]);
            rate(tempPool[3]);
            tempPool[4] = Chromosome.merge(currentGen[s.get(0)],currentGen[s.get(2)]);
            rate(tempPool[4]);
            tempPool[5] = Chromosome.merge(currentGen[s.get(2)],currentGen[s.get(0)]);
            rate(tempPool[5]);
            tempPool[6] = Chromosome.merge(currentGen[s.get(0)],currentGen[s.get(1)]);
            rate(tempPool[6]);
            tempPool[7] = Chromosome.merge(currentGen[s.get(1)],currentGen[s.get(0)]);
            rate(tempPool[7]);
            tempPool[8] = Chromosome.merge(currentGen[s.get(0)],currentGen[s.get(2)]);
            rate(tempPool[8]);
            tempPool[9] = Chromosome.merge(currentGen[s.get(2)],currentGen[s.get(0)]);
            rate(tempPool[9]);
            tempPool[10] = Chromosome.merge(currentGen[s.get(2)],currentGen[s.get(1)]);
            rate(tempPool[10]);
            tempPool[11] = Chromosome.merge(currentGen[s.get(1)],currentGen[s.get(2)]);
            rate(tempPool[11]);
            tempPool[12] = Chromosome.merge(currentGen[s.get(1)],currentGen[s.get(2)]);
            rate(tempPool[12]);
            tempPool[13] = Chromosome.merge(currentGen[s.get(0)],currentGen[s.get(1)]);
            rate(tempPool[13]);
            tempPool[14] = Chromosome.merge(currentGen[s.get(0)],currentGen[s.get(1)]);
            rate(tempPool[14]);
            tempPool[15] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[15]);
            tempPool[16] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[16]);
            tempPool[17] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[17]);
            tempPool[18] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[18]);
            tempPool[19] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[19]);
            tempPool[20] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[20]);
            tempPool[21] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[21]);
            tempPool[22] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[22]);
            tempPool[23] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[23]);
            tempPool[24] = Chromosome.merge(currentGen[r.nextInt(25)],currentGen[r.nextInt(25)]);
            rate(tempPool[24]);

            ArrayList<Integer> usedNextGen = new ArrayList<>();
            for (int i = 0; i < currentGen.length; i++) {
                if (currentGen[i].getScore() == 0) {
                    return currentGen[i];
                }
                if (currentGen[i].getScore() > tempPool[i].getScore()) {
                    currentGen[i] = tempPool[i];
                }else {
//                    Random r1 = new Random();
                    if (r.nextDouble() > 0.6) {
//                        currentGen[i] = new Chromosome(3, 3, 3);
                        if (r.nextDouble() > 0.6) {
                            currentGen[i] = new Chromosome(4, 10, 3);
                        }else {
                            currentGen[i] = Chromosome.merge(currentGen[i], tempPool[i]);
                        }
                        rate(currentGen[i]);
                    }
                }
            }
//            for (int i = 0; i < currentGen.length; i++) {
//                for (int j = 0; j < tempPool.length; j++) {
//                    if (currentGen[i].getScore() > tempPool[j].getScore() && !usedNextGen.contains(j)) {
//                        currentGen[i] = tempPool[j];
//                        usedNextGen.add(j);
//                        break;
//                    }else {
//                        currentGen[i] = new Chromosome(3, 3,3);
//                        rate(currentGen[i]);
//                    }
//                }
//                scoreSet[i] = currentGen[i].getScore();
//            }

            System.out.print("Generation" + g + ": ");
            for (Chromosome c: currentGen) {
                System.out.print(c.getScore() + " ");
//                System.out.println(rate(c)[0] + " " + rate(c)[1] + " " + rate(c)[2]);
            }
            System.out.println();
            if (currentGen[0].getScore() == 0) {
                break;
            }
        }
        System.out.println(rate(currentGen[0])[0] + " " + rate(currentGen[0])[1] + " " +rate(currentGen[0])[2]);
        return currentGen[0];
    }

    private static ArrayList<Integer> findThreeSmallestPos(int[] l) {
        if (l.length < 3) {
            System.out.println("Too short array");
            return null;
        }else {
            ArrayList<Integer> result = new ArrayList<>();
            int idx = 0;
            for (int i = 0; i < 3; i++) {
                idx = 0;
                for (int j = 0; j < l.length; j++) {
                    if (l[j] < l[idx] && !result.contains(j)) {
                        idx = j;
                    }
                }
                result.add(idx);
            }
            return result;
        }
    }

    private static void importDatabase() {

//        ---------------------------------------\\
//         import classrooms info
//        Classroom cohort1 = new Classroom("cohort1", "2.501", 50, ClassType.CBL, 0);
//        Classroom cohort2 = new Classroom("cohort2", "2.501", 50, ClassType.CBL, 1);
//        Classroom cohort3 = new Classroom("cohort3", "2.501", 50, ClassType.CBL, 2);
//        Classroom lecture = new Classroom("Lec", "2.501", 150, ClassType.LEC, 1);
//        Classroom lab = new Classroom("lab", "2.501", 50, ClassType.LAB, 2);
//        classrooms = new Classroom[4];
//        classrooms[0] = cohort1;
//        classrooms[1] = cohort2;
//        classrooms[2] = lecture;
//        classrooms[3] = lab;
//        roomList = new RoomList(classrooms, 2, 3);


//        ---------------------------------------\\
//         import subjects
//        GenericClass coh1 = new GenericClass(ClassType.CBL, 1.5, null);
//        GenericClass coh2 = new GenericClass(ClassType.CBL, 2, null);
//
//        ArrayList<Integer> cohorts = new ArrayList<>();
//        cohorts.add(0);
//        cohorts.add(1);
//        cohorts.add(2);
//
//        GenericClass lec1 = new GenericClass(ClassType.LEC, 2, null, cohorts);
//        GenericClass lec2 = new GenericClass(ClassType.LEC, 1.5, null, cohorts);
//
//        GenericClass[] ESCarr = new GenericClass[3];
//        ESCarr[0] = coh1;
//        ESCarr[1] = coh1;
//        ESCarr[2] = coh2;
//
//        GenericClass[] CSEarr = new GenericClass[3];
//        CSEarr[0] = coh1;
//        CSEarr[1] = coh1;
//        CSEarr[2] = lec1;
//
//        GenericClass[] probArr = new GenericClass[3];
//        probArr[0] = lec2;
//        probArr[1] = lec2;
//        probArr[2] = coh2;
//
//        Subject cse = new Subject("cse", 50005, SubjectType.CORE,
//                5, 50, 3, CSEarr);
//        Subject esc = new Subject("esc", 50003, SubjectType.CORE,
//                5, 50, 3, ESCarr);
//        Subject prob = new Subject("prob", 50034, SubjectType.CORE,
//                5, 50, 3, probArr);
//
//        subjects = new ArrayList<>();
//        subjects.add(cse);
//        subjects.add(esc);
//        subjects.add(prob);
//        //---------------------------------------\\
//        // import studentG
//        studentGroupSet = new ArrayList<>();
//        StudentGroup t5c1 = new StudentGroup(0, "t5c1", Pillar.ISTD, 5,
//                0, subjects, 50);
//        StudentGroup t5c2 = new StudentGroup(0, "t5c2", Pillar.ISTD, 5,
//                1, subjects, 50);
//        StudentGroup t5c3 = new StudentGroup(0, "t5c3", Pillar.ISTD, 5,
//                2, subjects, 50);
//
//        studentGroupSet.add(t5c1);
//        studentGroupSet.add(t5c2);
//        studentGroupSet.add(t5c3);
//        //---------------------------------------\\
//        // import Prof
//        professorSet = new ArrayList<>();
//
//        Professor nat = new Professor("Nat", 0);
//        nat.addSubject(cse, t5c1);
//        nat.addSubject(cse, t5c2);
//        professorSet.add(nat);
//
//        Professor david = new Professor("David", 1);
//        david.addSubject(esc, t5c1);
//        david.addSubject(esc, t5c2);
//        david.addSubject(esc, t5c3);
//        professorSet.add(david);
//
//        Professor sj = new Professor("Sun Jun", 2);
//        sj.addSubject(esc, t5c1);
//        sj.addSubject(esc, t5c2);
//        sj.addSubject(esc, t5c3);
//        professorSet.add(sj);
//
//        Professor tq = new Professor("Tony", 3);
//        sj.addSubject(prob, t5c1);
//        sj.addSubject(prob, t5c2);
//        sj.addSubject(prob, t5c3);
//        professorSet.add(tq);

        roomList = JsonUtils.readJsonRoomList();
        subjects = JsonUtils.readJsonSubject(roomList);
        studentGroupSet = JsonUtils.readJsonStudentGroup(subjects);
        professorSet = JsonUtils.readJsonProfessor(subjects, studentGroupSet);

//        for (Subject s: subjects) {
//            System.out.println(s.getName());
//            for (GenericClass g: s.getClassComponent()) {
//                for (Classroom c: g.getPossibleRoomSet()) {
//                    System.out.print(c.getId() + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("-------");
//        }

//        for (Professor p: professorSet) {
//            System.out.println(p.getName());
//            for (Subject s: p.getCourseTable().keySet()) {
//                System.out.println(s.getName() + ": " + p.getCourseTable().get(s).size());
//            }
//        }

    }

    //TODO: a function that input is list of subject and output is 3-d mat of SpecificClass (x:subject; y:cohort; z:session)
    public static SpecificClass[][][] init(int MaxCohortNum, int MaxSessionNum) {
        if (subjects == null) {
            return null;
        }
        if (roomList == null) {
            return null;
        }
        GenericClass[] gClassSet;
        SpecificClass sClass;
        int cohortNum;
        int sessionNum;
        int subjectNum = subjects.size();
        int maxSessionNum = MaxSessionNum;
        int maxCohortNum = MaxCohortNum;

        SpecificClass[][][] result = new SpecificClass[subjectNum][maxCohortNum][maxSessionNum];
        for (int i = 0; i < subjectNum; i++) {
            gClassSet = subjects.get(i).getClassComponent();
            cohortNum = subjects.get(i).getNumOfCohort();
            sessionNum = gClassSet.length;
            int term = subjects.get(i).getTerm();
            for (int j = 0; j < cohortNum; j++) {
                for (int k = 0; k < sessionNum; k++) {
                    if (gClassSet[k].getClassType() == ClassType.LEC) {
                        if (j == 0) {
                            sClass = new SpecificClass(gClassSet[k], k, j, subjects.get(i), null);
                            result[i][j][k] = sClass;
                        }
                    }else {
                        Classroom room;
                        if (term <= 3) {
                            room = roomList.getFreshmoreRoom(j);
                        }else {
                            room = null;
                        }
                        sClass = new SpecificClass(gClassSet[k], k, j, subjects.get(i), room);
                        result[i][j][k] = sClass;
                    }
                }
            }
        }
        return result;
    }

    //TODO: function that input is sClass mat and output is randomly generated calendar
    public static Calendar randomGen(SpecificClass[][][] sClassSet) {
        Calendar calendar = null;
        int count = 5000;
        while (count > 0){
            calendar = new Calendar(roomList, sClassSet);
            boolean s = calendar.randomInit();
            if (s == true) {
                break;
            }
            count--;
        }
//        calendar.printOut();
        if (count==0) {
            System.out.println("null");
            return null;
        }else {
            return calendar;
        }
    }

    //TODO: function that input is calendar and print it out
    public static void printChromosome(Chromosome c) {
        SpecificClass[][][] chromosome = c.getChromosome();
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome[0].length; j++) {
                for (int k = 0; k < chromosome[0][0].length; k++) {
                    if (chromosome[i][j][k] != null) {
                        if (chromosome[i][j][k].getClassroom() == null) {
                            chromosome[i][j][k].printInfoWithoutRoom();
                        }else {
                            chromosome[i][j][k].printInfo();
                        }
                    }
                }
            }
        }
    }
    public static void printChromosome(Chromosome c, int term, int cohortNo) {
        SpecificClass[][][] chromosome = c.getChromosome();
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome[0].length; j++) {
                for (int k = 0; k < chromosome[0][0].length; k++) {
                    if (chromosome[i][j][k] != null) {
                        if (chromosome[i][j][k].getClassroom() == null) {
                            chromosome[i][j][k].printInfoWithoutRoom();
                        }else {
                            if (chromosome[i][j][k].getCohortNo().contains(cohortNo)
                                    && chromosome[i][j][k].getSubject().getTerm() == term) {
                                chromosome[i][j][k].printInfo();
                            }
                        }
                    }
                }
            }
        }
    }
    //TODO：rating automatically
    public static int[] rate(Chromosome chromosome) { // need StudentGroupSet and ProfSet
        int conflictNum = 0;
        int[] result = new int[3]; // 0: student conflict; 1: session conflict; 2: teacher conflict

        // check studentG conflict
        for (StudentGroup sg: studentGroupSet) {
            sg.setsClassSet(chromosome.getChromosome());
            conflictNum += sg.checkConflict();
        }
//        System.out.println("The studentG conflict number is: " + conflictNum);

        // check session error
        int sessionError = 0;
        SpecificClass[][][] temp = chromosome.getChromosome();
        for (int i = 0; i < chromosome.getXdim(); i++) {
            int cohortNumber = temp[i][0][0].getSubject().numOfCohort;
//            for (int j = 0; j < chromosome.getYdim(); j++) {
            for (int j = 0; j < cohortNumber; j++) {
                for (int k = 0; k < chromosome.getZdim()-1; k++) {
                    if (temp[i][j][k] == null || temp[i][j][k+1] == null) {
                        if (temp[i][j][k] == null && temp[i][j][k+1] == null) {// Lec + Lec + Coh

                            if (temp[i][0][1].getWeekday() >= temp[i][j][2].getWeekday()) {
                                sessionError++;
                            }
                            break;
                        }else if (temp[i][j][k] == null && temp[i][j][k+1] != null) { // Lec + Coh
                            if (temp[i][0][k].getWeekday() >= temp[i][j][k+1].getWeekday()) {
                                sessionError++;
                            }
                            break;
                        }else if (temp[i][j][k] != null && temp[i][j][k+1] == null) { // Coh + Lec
                            if (temp[i][j][k].getWeekday() >= temp[i][0][k+1].getWeekday()) {
                                sessionError++;
                            }
                            break;
                        }
                    }else {
                        if (temp[i][j][k].getWeekday() >= temp[i][j][k+1].getWeekday()) {
                            sessionError ++;
                        }
                    }
                }
            }
        }
//        System.out.println("The session error is: " + sessionError);

        // check prof conflict
        int profConflict = 0;
        for (Professor prof: professorSet) {
            prof.setsClassSet(chromosome.getChromosome());
            profConflict += prof.checkConflict();
        }
//        System.out.println("The prof conflict number is: " + profConflict);
//        System.out.println("score: " + conflictNum + " " + sessionError + " " + profConflict);
        chromosome.setScore(conflictNum + profConflict + sessionError);
        result[0] = conflictNum;
        result[1] = sessionError;
        result[2] = profConflict;
        return result;
    }
}

class Chromosome {
    private SpecificClass[][][] chromosome;
    private SpecificClass[] lineChromosome;
    private int subjectNum;
    private int cohortNum;
    private int sessionNum;
    private int score;

    Chromosome(int subjectNum, int cohortNum, int sessionNum) {
        this.chromosome = Scheduler.init(cohortNum, sessionNum); // y: cohort number; z: session number
        Scheduler.randomGen(this.chromosome);
        this.subjectNum = subjectNum;
        this.cohortNum = cohortNum;
        this.sessionNum = sessionNum;
        this.score = 0;
        lineChromosome = convertToArray();
    }

    Chromosome(SpecificClass[][][] sClassSet, int x, int y, int z) {
        this.chromosome = sClassSet;
        lineChromosome = convertToArray();
        this.subjectNum = x;
        this.cohortNum = y;
        this.sessionNum = z;
        this.score = 0;
    }

    Chromosome(SpecificClass[] lineC, int x, int y, int z) {
        this.subjectNum = x;
        this.cohortNum = y;
        this.sessionNum = z;
        this.score = 0;
        this.lineChromosome = lineC;
        this.chromosome = convertTo3d();

    }

    public void setScore(int score) {
        this.score = score;
    }

    public SpecificClass[][][] getChromosome() {
        return chromosome;
    }

    public SpecificClass[] getLineChromosome() {
        return lineChromosome;
    }

    public int getXdim(){
        return this.subjectNum;
    }

    public int getYdim() {
        return this.cohortNum;
    }

    public int getZdim() {
        return this.sessionNum;
    }

    public int getScore() {
        return score;
    }

    private SpecificClass[] convertToArray() {
        SpecificClass[] result = new SpecificClass[getXdim()*getYdim()*getZdim()];
        for(int i = 0; i < getXdim(); i++) {
            for (int j = 0; j < getYdim(); j++) {
                for (int k = 0; k < getZdim(); k++) {
                    result[i*getYdim()*getZdim() + j*getZdim() + k] = this.chromosome[i][j][k];
                }
            }
        }
        return result;
    }

    private SpecificClass[][][] convertTo3d() {
        SpecificClass[][][] result = new SpecificClass[getXdim()][getYdim()][getZdim()];
        for(int i = 0; i < getXdim(); i++) {
            for (int j = 0; j < getYdim(); j++) {
                for (int k = 0; k < getZdim(); k++) {
                    result[i][j][k] = this.lineChromosome[i*getYdim()*getZdim() + j*getZdim() + k];
                }
            }
        }
//        System.out.println(result.length + " " + result[0].length + " " + result[0][0].length);
        return result;
    }

    public static Chromosome merge(Chromosome c1, Chromosome c2) {
//        SpecificClass[][][] newChromosome = new SpecificClass[c1.getXdim()][c1.getYdim()][c1.getZdim()];
//        SpecificClass[][][] c1set = c1.getChromosome();
//        SpecificClass[][][] c2set = c2.getChromosome();
//        Random r = new Random();
//        for (int i = 0; i < c1.getXdim(); i++) {
//            for (int j = 0; j < c1.getYdim(); j++) {
//                for (int k = 0; k < c1.getZdim(); k++) {
//                    if (r.nextDouble() > 0.5) {
//                        newChromosome[i][j][k] = c1set[i][j][k];
//                    }else {
//                        newChromosome[i][j][k] = c2set[i][j][k];
//                    }
//                }
//            }
//        }
//        return new Chromosome(newChromosome, c1.getXdim(), c1.getYdim(), c1.getZdim());
        int chromosomeLength = c1.getXdim()*c1.getYdim()*c1.getZdim();
        SpecificClass[] lineC = new SpecificClass[chromosomeLength];

        Random r = new Random();
        int lowCut, highCut;

        lowCut = r.nextInt(chromosomeLength);
        highCut = r.nextInt(chromosomeLength);

        if (lowCut > highCut) {
            int temp = highCut;
            highCut = lowCut;
            lowCut = temp;
        }

        for (int i = 0; i < lowCut; i++) {
            lineC[i] = c1.getLineChromosome()[i];
        }
        for (int i = lowCut; i < highCut; i++) {
            lineC[i] = c2.getLineChromosome()[i];
        }
        for (int i = highCut; i < chromosomeLength; i++) {
            lineC[i] = c1.getLineChromosome()[i];
        }
        return new Chromosome(lineC, c1.getXdim(), c1.getYdim(), c1.getZdim());
    }

    public static Chromosome mutate(Chromosome c) {
        SpecificClass[][][] newChromosome = new SpecificClass[c.getXdim()][c.getYdim()][c.getZdim()];
        SpecificClass[][][] cSet = c.getChromosome();
        return null;
    }
}




