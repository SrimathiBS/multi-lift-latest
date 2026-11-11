import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // reads UP / DOWN / UP,DOWN for the request and keeps only the valid directions
    static void readDirections(Scanner scanner, Request request, int lowestFloor, int highestFloor) {

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LiftAllocationService allocationService = new LiftAllocationService();

        int noOfLifts = 0;
        while (true) {
            try {
                System.out.print("Enter number of lifts: ");
                noOfLifts = scanner.nextInt();
                if (noOfLifts > 0) {
                    break;
                }
                System.out.println("Number of lifts should be greater than 0");
            } catch (InputMismatchException e) {
                System.out.println("Enter a whole number");
                scanner.nextLine();
            }
        }

        int buildingLowestFloor = Integer.MAX_VALUE, buildingHighestFloor = Integer.MIN_VALUE;
        for (int liftNumber = 1; liftNumber <= noOfLifts; liftNumber++) {
            System.out.println("\nLift " + liftNumber);
            int lowestFloor = 0, highestFloor = 0, currentFloor = 0;
            while (true) {
                try {
                    System.out.print("Enter lowest floor: ");
                    lowestFloor = scanner.nextInt();
                    System.out.print("Enter highest floor: ");
                    highestFloor = scanner.nextInt();
                    if (highestFloor <= lowestFloor) {
                        System.out.println("highest floor should not be less than or equal to lowest floor");
                        continue;
                    }
                    System.out.print("Enter current floor: ");
                    currentFloor = scanner.nextInt();
                    if (currentFloor < lowestFloor || currentFloor > highestFloor) {
                        System.out.println("Enter current floor from " + lowestFloor + " to " + highestFloor);
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Enter valid floor number");
                    scanner.nextLine();
                }
            }
            Lift lift = new Lift(liftNumber, currentFloor, Direction.IDLE, lowestFloor, highestFloor);
            while (true) {
                try {
                    System.out.print("Enter direction of the lift (UP/DOWN/IDLE): ");
                    lift.direction = Direction.valueOf(scanner.next().trim().toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid direction. Use UP, DOWN or IDLE.");
                }
            }

            // idle lift has no next floors. If no next floor is entered, addLift saves the lift as IDLE
            if (lift.getDirection() != Direction.IDLE) {
                while (true) {
                    System.out.print("Enter next floor (type done to finish): ");
                    try {
                        String input = scanner.next();
                        if (input.equalsIgnoreCase("done")) {
                            break;
                        }
                        int nextFloor = Integer.parseInt(input);
                        if (nextFloor >= lowestFloor && nextFloor <= highestFloor && nextFloor != currentFloor) {
                            lift.nextFloors.add(nextFloor);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } catch (NumberFormatException e) {
                        scanner.nextLine();
                        System.out.println("Enter a whole number for the floor, or type done");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Enter valid floor from " + lowestFloor + " to " + highestFloor + " and it should not be equal to current floor");
                    }
                }
            }

            allocationService.addLift(lift);
            buildingLowestFloor = Math.min(buildingLowestFloor, lowestFloor);
            buildingHighestFloor = Math.max(buildingHighestFloor, highestFloor);
        }

        while (true) {
            Request request = null;
            System.out.print("\nEnter pickup floor (type exit to end the program): ");
            try {
                String input = scanner.next();
                if (input.equalsIgnoreCase("exit")) {
                    return;
                }
                int pickupFloor = Integer.parseInt(input);
                if (pickupFloor >= buildingLowestFloor && pickupFloor <= buildingHighestFloor) {
                    request = new Request(pickupFloor);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (NumberFormatException e) {
                scanner.nextLine();   // throw away the wrong input (letters, decimals...)
                System.out.println("Enter a whole number for the floor, or type exit");
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println("Enter valid floor from " + buildingLowestFloor + " to " + buildingHighestFloor);
                continue;
            }

            scanner.nextLine();
            System.out.print("Enter direction (UP/DOWN, comma-separated if both): ");
            String directions = scanner.nextLine();
            String[] directionValues = directions.split(",");
            for (String value : directionValues) {
                try {
                    if(value.trim().toUpperCase().equals("IDLE")){
                        throw new IllegalArgumentException();
                    }else {
                        Direction direction = Direction.valueOf(value.trim().toUpperCase());
                        if ((request.getPickupFloor() == buildingHighestFloor && direction == Direction.UP) ||
                                (request.getPickupFloor() == buildingLowestFloor && direction == Direction.DOWN)) {
                            System.out.println("Invalid direction: " + direction + " is not possible for floor " + request.getPickupFloor());
                        } else {
                            request.addDirection(direction);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid direction: " + value + ". Use UP, DOWN, or UP,DOWN.");
                }
            }

            if (request.getDirection().isEmpty()) {
                System.out.println("No valid direction entered, request not added");
                continue;
            }
            allocationService.allocateLift(request);
        }
    }
}

