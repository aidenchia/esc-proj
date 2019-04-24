package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class Calendar {
    private SpecificClass[][][] timetable; // x: room; y: weekday; z: time slot
    private SpecificClass[][][] input3D; // x: subject; y: cohort; z: session
    private int roomNum;
    private RoomList roomList;

    //roomList is a list with the order: cohort, lecture, lab
    int lecRoomStart;
    int labRoomStart;

    Calendar(RoomList roomList, SpecificClass[][][] sClass) {
        this.roomNum = roomList.getListNum();
        this.roomList = roomList;
        this.lecRoomStart = roomList.getLecRoomStart();
        this.labRoomStart = roomList.getLabRoomStart();
        timetable = new SpecificClass[roomNum][5][21]; //  room, weekday, half-hour
        this.input3D = sClass;
        initTimeTable();
//        initTerm3TimeTable();
    }

    private void initTimeTable() {
        SpecificClass fifthRow = new SpecificClass("fifthRow");
        SpecificClass hass = new SpecificClass("HASS");
//        SpecificClass hass = null;

        int[] fifthRowWeekday = {2, 4}; // Wednesday and Friday
        for (int i = 0; i < roomNum; i++) {
            for (int j : fifthRowWeekday) {
                for (int k = 9; k < 20; k++) { // start from 13:00
                    timetable[i][j][k] = fifthRow;
                }
            }
        }
        for (int i = 0; i < roomNum; i++) { // Monday HASS
            for (int j = 13; j < 20; j++) {
                timetable[i][0][j] = hass;
            }
        }

        for (int i = 0; i < roomNum; i++) { // Tuesday HASS
            for (int j = 0; j < 3; j++) {
                timetable[i][1][j] = hass;
            }
        }

        for (int i = 0; i < roomNum; i++) { // Thursday HASS
            for (int j = 13; j < 20; j++) {
                timetable[i][3][j] = hass;
            }
        }

//        for (int i = 0; i < roomNum; i++) { // Friday HASS
//            for (int j = 6; j < 10; j++) {
//                timetable[i][4][j] = hass;
//            }
//        }
    }

    private void initTerm3TimeTable() {
        SpecificClass fifthRow = new SpecificClass("fifthRow");

        int[] fifthRowWeekday = {2, 4}; // Wednesday and Friday
        for (int i = 0; i < roomNum; i++) {
            for (int j : fifthRowWeekday) {
                for (int k = 9; k < 20; k++) { // start from 13:00
                    timetable[i][j][k] = fifthRow;
                }
            }
        }
    }

    public boolean randomInit() {
        // assume the instance has been created and classrooms are assigned already
        Random rand = new Random();
        int subjectNum = input3D.length;// length of sClass column
        int sessionNum;
        int cohortNum;
        double classDuration;
        ClassType roomType;
        int roomID;
        int preWeekDay = -1; // previous session weekday
        int currentWeekday = 0;
        int numOfSlot; // if duration == 1.5, numOfSlot = 3

        int[] s = new int[subjectNum];
        for (int d = 0; d < subjectNum; d++) {
            s[d] = d;
        }
        s = randomSort(s);

        for (int i = 0; i < subjectNum; i++) {
            sessionNum = input3D[s[i]][0][0].getSubject().getClassComponent().length;
            cohortNum = input3D[s[i]][0][0].getSubject().getNumOfCohort();
            int[] randomCoh = new int[cohortNum];
            for (int f = 0; f < cohortNum; f++) {
                randomCoh[f] = f;
            }
            boolean isTerm3 = input3D[i][0][0].getSubject().getTerm() == 3;

            randomCoh = randomSort(randomCoh);
            for (int j = 0; j < cohortNum; j++) {
                preWeekDay = -1;
                for (int k = 0; k < sessionNum; k++) {
                    if (input3D[s[i]][randomCoh[j]][k] == null) {
                        preWeekDay = 2;
                        continue;
                    }else{
                        // ------------------------------------------------ \\
                        // Randomly choose a weekday
                        // Policy: Assume that possible session number is {2, 3}
                        if (sessionNum == 3) {
                            if (k == 0) {
                                currentWeekday = (int)Math.floor(rand.nextDouble()*2);
                                preWeekDay = currentWeekday;
                            }else if (k == 1) {
                                currentWeekday = (int)Math.floor(rand.nextDouble()*(2-preWeekDay)+preWeekDay+1);
                                preWeekDay = currentWeekday;
                            }else { // k = 3
                                currentWeekday = (int)Math.floor(rand.nextDouble()*2 + 3);
                            }
                        }else { // sessionNum = 2
                            if (k == 0) {
                                currentWeekday = (int)Math.floor(rand.nextDouble()*2);
                            }else { // k = 1
                                currentWeekday = (int)Math.floor(rand.nextDouble()*2+2);
                            }
                        }
                        // ------------------------------------------------ \\
                        // Randomly choose a classroom
                        // Policy: if the specific class has been assigned a classroom (case 1)
                        //         if has not been assigned, then get type and randomly choose (case 2)
                        int[] possibleRoomSelect = new int[input3D[s[i]][randomCoh[j]][k].getPossibleRoomSet().length];
                        for (int d = 0; d < input3D[s[i]][randomCoh[j]][k].getPossibleRoomSet().length; d++) {
                            possibleRoomSelect[d] = input3D[s[i]][randomCoh[j]][k].getPossibleRoomSet()[d].getId();
                        }
                        possibleRoomSelect = randomSort(possibleRoomSelect);
//                        if (input3D[s[i]][j][k].getClassroom() != null) {
//                            possibleRoomSelect = new int[]{input3D[s[i]][j][k].getClassroom().getId()};
//                        }else {
//                            roomType = input3D[s[i]][j][k].getType();
//                            switch (roomType) {
//                                case CBL:
//                                    possibleRoomSelect = new int[lecRoomStart];
//                                    for (int idx = 0; idx < lecRoomStart; idx++) {
//                                        possibleRoomSelect[idx] = idx;
//                                    }
//                                    possibleRoomSelect = randomSort(possibleRoomSelect);
//                                    break;
//                                case LEC:
//                                    possibleRoomSelect = new int[labRoomStart - lecRoomStart];
//                                    for (int idx = 0; idx < labRoomStart - lecRoomStart; idx++) {
//                                        possibleRoomSelect[idx] = idx+lecRoomStart;
//                                    }
//                                    possibleRoomSelect = randomSort(possibleRoomSelect);
//                                    break;
//                                case LAB:
//                                    possibleRoomSelect = new int[roomNum - labRoomStart];
//                                    for (int idx = 0; idx < roomNum - labRoomStart; idx++) {
//                                        possibleRoomSelect[idx] = idx + lecRoomStart;
//                                    }
//                                    possibleRoomSelect = randomSort(possibleRoomSelect);
//                                    break;
//                                    default: possibleRoomSelect = new int[0];
//                                    break;
//                            }
//                        }
                        //select a room id
                        int possibleRoomPointer = 0; // cannot larger than the specific room number
                        roomID = possibleRoomSelect[possibleRoomPointer];

                        // below random choose a begin time slot
                        int timeSlotPointer = 0;
                        int putIn = 0;
                        classDuration = input3D[s[i]][randomCoh[j]][k].getDuration();
                        numOfSlot = (int) (classDuration / 0.5);

                        while(timeSlotPointer < 18) {
                            for (int sln = 0; sln < numOfSlot; sln++) {
                                if(timetable[roomID][currentWeekday][timeSlotPointer+sln] != null) {
                                    break;
                                }else {
                                    putIn++;
                                }
                            }
                            if (putIn == numOfSlot) {
                                input3D[s[i]][randomCoh[j]][k].setTimeAndPos(currentWeekday, timeSlotPointer, roomList.getRoomList()[roomID]);
                                for (int sln = 0; sln < numOfSlot; sln++) {
                                    timetable[roomID][currentWeekday][timeSlotPointer+sln] =
                                            input3D[s[i]][randomCoh[j]][k];
                                }
                                break;
                            }else {
//                                timeSlotPointer += numOfSlot - 1;
                                timeSlotPointer++;
                            }
                            if (timeSlotPointer >= 18) {
                                if (possibleRoomPointer == possibleRoomSelect.length-1) {
                                    if(k==2 && currentWeekday != 3) {
//                                        System.out.println("session2 change to Thursday");
                                        currentWeekday = 3;
                                        possibleRoomPointer = 0;
                                        roomID = possibleRoomSelect[possibleRoomPointer];
                                        timeSlotPointer = 0;
                                    }else if(k < 2 && currentWeekday != 1) {
//                                        System.out.println("session0,1 change to Tuesday");
                                        currentWeekday = 1;
                                        possibleRoomPointer = 0;
                                        roomID = possibleRoomSelect[possibleRoomPointer];
                                        timeSlotPointer = 0;
                                    }
                                    else {
//                                        input3D[i][j][k].printInfoWithoutRoom();
////                                        System.out.println("Fail to find suitable slot"); // 考虑换天的情况
//                                        System.out.println();
                                        return false;
                                    }
                                }else {
                                    roomID = possibleRoomSelect[++possibleRoomPointer];
                                    timeSlotPointer = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void printOut() {
        for(int k = 0; k < roomNum; k++) {
            for(int i = 0; i < 5; i++) {
                for (int j = 0; j < 20; j++) {
                    if (timetable[k][i][j] == null) {
                        System.out.print(" ");
                    }else {
                        timetable[k][i][j].printInfo();
                    }
                }
                System.out.println();
            }
        }
    }

    public static int[] randomSort(int[] ls) { // given a ls, randomly sort all the elements
        Random rr = new Random();
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();

        int randomInt;
        for (int i = 0; i < ls.length; i++) {
            randomInt = rr.nextInt(500);
            map.put(randomInt, ls[i]);
            arrayList.add(randomInt);
        }
        arrayList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        int[] result = new int[ls.length];
        for (int i = 0; i < ls.length; i++) {
            result[i] = map.get(arrayList.get(i));
        }
        return result;
    }
}
