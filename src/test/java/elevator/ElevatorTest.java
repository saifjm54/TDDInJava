package elevator;

import org.example.elevator.Elevator;
import org.example.elevator.ElevatorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorTest {

    private Elevator elevator;

    @BeforeEach
    public void setUp() {
        this.elevator = new Elevator();
    }

    @Test
    public void testUpElevator() {
        elevator.Up();
        assertEquals(ElevatorStatus.MOVING_UP, elevator.getStatus(), "Up should be moving up");
    }

    @Test
    public void testDownElevator() {
        elevator.Down();
        assertEquals(ElevatorStatus.MOVING_DOWN,elevator.getStatus(),"Down should be moving down");
    }

    @Test
    public void testElevatorInitialization() {
        assertNotNull (elevator);
        assertEquals(ElevatorStatus.STOPPED, elevator.getStatus(),"Elevator should start in STOPPED state");
        assertEquals(0,elevator.getCurrentFloor());
    }

    @Test
    public void testElevatorAfterOneUpShouldBeMovingUp() {
        elevator.Up();
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    public void testElevatorAfterOneDownShouldBeMovingDown() {
        elevator.setCurrentFloor(1);
        elevator.Down();
        assertEquals(0, elevator.getCurrentFloor());
    }

    @Test
    public void testElevatorAfterTwoUpShouldBeMovingUp() {
        elevator.Up();
        elevator.Up();
        assertEquals(2, elevator.getCurrentFloor());

    }

    @Test
    public void testElevatorShouldMoveUpWhenClickToUpperFloor() throws InterruptedException {

        elevator.setCurrentFloor(0); // Assume the elevator is currently on the ground floor

        // Create a separate thread to monitor the elevator's status change
        Thread statusChecker = new Thread(() -> {
            while (elevator.getStatus() != ElevatorStatus.MOVING_UP) {
                // Wait for the status to change
            }
            assertEquals(ElevatorStatus.MOVING_UP, elevator.getStatus(),"Elevator should be moving up when clicked to go to upper floor");
        });
        statusChecker.start();

        // Click to go to the upper floor
        elevator.click(4);

        // Wait for the status checker thread to finish
        statusChecker.join();

        assertEquals( 4, elevator.getCurrentFloor(),"Elevator's current floor should be updated after click");
    }

    @Test
    public void testElevatorShouldMoveDownWhenClickToDownFloor() throws InterruptedException {
        elevator.setCurrentFloor(2);

        Thread statusChecker = new Thread(() -> {
            while (elevator.getStatus() != ElevatorStatus.MOVING_DOWN){

            }
            assertEquals(ElevatorStatus.MOVING_DOWN,elevator.getStatus(),"Elevator should be moving down when clicked to go to down floor");
        });
        statusChecker.start();
        elevator.click(0);
        // Wait for the status checker thread to finish
        statusChecker.join();
        assertEquals(0, elevator.getCurrentFloor());
        assertEquals(ElevatorStatus.STOPPED, elevator.getStatus());
    }


    @Test
    public void testElevatorShouldStoppedInEveryClickedFloor() throws InterruptedException {
        Elevator elevator = new Elevator();
        elevator.setCurrentFloor(0);
        int[] clickedFloor = {2, 4, 1, 3};

        // Sort the clickedFloor array in ascending order
        Arrays.sort(clickedFloor);

        Thread elevatorIsStopped = new Thread(() -> {
            for (int floor : clickedFloor) {
                while (elevator.getCurrentFloor() != floor || elevator.getStatus() != ElevatorStatus.STOPPED) {
                    System.out.println("Floor: " + floor);
                }
                assertEquals(floor, elevator.getCurrentFloor());
                assertEquals(ElevatorStatus.STOPPED, elevator.getStatus());
            }
        });

        elevatorIsStopped.start(); // Start the thread

        // Click floors in arbitrary order
        for (int floor : clickedFloor) {
            elevator.click(floor);
        }

        elevatorIsStopped.join(); // Wait for the elevatorIsStopped thread to finish
    }



}
