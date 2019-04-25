package algorithm;

public class Classroom {
    private int id;
    private String name;
    private String address;
    private int capacity;
    private  ClassType type;

    Classroom(String name, String address, int capacity, ClassType type,
              int id) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.type = type;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class RoomList {
    private int lecRoomStart;
    private int LabRoomStart;
    private Classroom[] roomList;

    RoomList(Classroom[] roomList, int lecStart, int labStart) {
        this.lecRoomStart = lecStart;
        this.LabRoomStart = labStart;
        this.roomList = roomList;
    }

    public int getListNum() {
        return roomList.length;
    }

    public int getLabRoomStart() {
        return LabRoomStart;
    }

    public int getLecRoomStart() {
        return lecRoomStart;
    }

    public Classroom[] getRoomList() {
        return roomList;
    }

    public Classroom getFreshmoreRoom(int cohort) { // cohort = {0, 1, ..., 9}
        return roomList[cohort]; // assume 0-9 is the freshman classrooms
    }
}
