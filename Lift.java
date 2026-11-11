import java.util.TreeSet;

public class Lift {
    int id;
    int currentFloor;
    Direction direction;
    int lowestfloor;
    int highestfloor;
    TreeSet<Integer> nextFloors = new TreeSet<>();

    public Lift(int id, int currentFloor, Direction direction, int lowestfloor, int highestfloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.lowestfloor=lowestfloor;
        this.highestfloor=highestfloor;
    }

    public int getCurrentFloor() {

        return currentFloor;
    }

    public Direction getDirection() {

        return direction;
    }

}
