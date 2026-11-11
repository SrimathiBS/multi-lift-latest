import java.util.ArrayList;

public class Request implements Comparable<Request> {
    int pickupFloor;
    ArrayList<Direction> direction;

    public Request(int pickupFloor) {
        this.pickupFloor = pickupFloor;
        this.direction = new ArrayList<>();
    }


    public void addDirection(Direction dir) {
        if (!direction.contains(dir)) {
            direction.add(dir);
        }
    }

    public int getPickupFloor() {
        return pickupFloor;
    }


    public ArrayList<Direction> getDirection() {
        return direction;
    }

    @Override
    public int compareTo(Request other) {
        return Integer.compare(pickupFloor, other.pickupFloor);
    }
}
