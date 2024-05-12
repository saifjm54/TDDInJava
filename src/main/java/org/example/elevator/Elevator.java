package org.example.elevator;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Elevator {

    private ElevatorStatus status;
    private  Integer currentFloor = 0;
    private List<Integer> clickedFloorsForMoveUp = new ArrayList<>();
    private List<Integer> clickedFloorsForMoveDown = new ArrayList<>();
    private Boolean isElevatorDoorOpen = false;
    private int delayBetweenEachFloorInSeconds = 1;
    private int stoppedDelayInSeconds = 1;

    public Elevator() {
        this.status = ElevatorStatus.STOPPED;
    }

    public void setStatus(ElevatorStatus status) {
        this.status = status;
    }
    public ElevatorStatus getStatus() {
        return status;
    }

    public void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }
    public Integer getCurrentFloor() {
        return currentFloor;
    }

    public void Up(){
        try {
            this.status = ElevatorStatus.MOVING_UP;
            this.currentFloor++;
            System.out.println("Up");
            Thread.sleep(1000*delayBetweenEachFloorInSeconds); // 1 second delay between each floor movement
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            System.err.println("Floor movement interrupted");
        }

    }

    public void Down(){
        try {
            this.status = ElevatorStatus.MOVING_DOWN;
            this.currentFloor--;
            System.out.println("Down");
            Thread.sleep(1000*delayBetweenEachFloorInSeconds); // 1 second delay between each floor movement
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            System.err.println("Floor movement interrupted");
        }

    }
    private void addClickedFloorForMoveUp(Integer floor) {
        int index = 0;
        while (index < clickedFloorsForMoveUp.size() && clickedFloorsForMoveUp.get(index) < floor) {
            index++;
        }
        clickedFloorsForMoveUp.add(index, floor);
    }

    private void addClickedFloorForMoveDown(Integer floor) {
        int index = 0;
        while (index < this.clickedFloorsForMoveDown.size() && this.clickedFloorsForMoveDown.get(index) > floor) {
            index++;
        }
        clickedFloorsForMoveDown.add(index, floor);
    }

    public void click(Integer floor){
        if(floor > this.currentFloor){
            addClickedFloorForMoveUp(floor);
        }
        else {
            addClickedFloorForMoveDown(floor);
        }
        moveElevator();
    }
    private void moveToTheNextFloor(Integer nextFloor){
        while (currentFloor < nextFloor){
            Up();
        }
       stopElevator();
    }
    private void moveToThePreviousFloor(Integer previousFloor){
        while (currentFloor > previousFloor){
            Down();
        }
        stopElevator();
    }
    private void stopElevator() {
        try {

            this.status = ElevatorStatus.STOPPED;
            this.isElevatorDoorOpen = true;
            Thread.sleep(1000*delayBetweenEachFloorInSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            // Log or handle the interruption gracefully
            System.err.println("Elevator stop interrupted");
        } finally {
            this.isElevatorDoorOpen = false;
        }
    }
    private void moveElevator()  {
        while (!clickedFloorsForMoveUp.isEmpty()){
            Integer nexFloor = clickedFloorsForMoveUp.get(0);
            clickedFloorsForMoveUp.remove(0);
            moveToTheNextFloor(nexFloor);
        }
        while (!clickedFloorsForMoveDown.isEmpty()){
            Integer nexFloor = clickedFloorsForMoveDown.get(0);
            clickedFloorsForMoveDown.remove(0);
            moveToThePreviousFloor(nexFloor);
        }

    }

}
