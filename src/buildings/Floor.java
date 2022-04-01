package buildings;

import elevators.ElevatorObserver;
import passengers.Passenger;
import elevators.Elevator;

import java.util.*;

public class Floor implements ElevatorObserver {
	private Building mBuilding;
	private List<Passenger> mPassengers = new ArrayList<>();
	private ArrayList<FloorObserver> mObservers = new ArrayList<>();
	private int mNumber;
	
	// You can assume that every floor has both up and down buttons, even the ground and top floors.
	private boolean upButton =  false;
	private boolean downButton =  false;


	public Floor(int number, Building building) {
		mNumber = number;
		mBuilding = building;
	}
	
	
	/**
	 * Sets a flag that the given direction has been requested by a passenger on this floor. If the direction
	 * had NOT already been requested, then all observers of the floor are notified that directionRequested.
	 * @param direction
	 */
	public void requestDirection(Elevator.Direction direction) {

		if (direction == Elevator.Direction.MOVING_UP && !upButton) {

			upButton = true;

			ArrayList<FloorObserver> pressedUp = new ArrayList<>(mObservers);

			for( FloorObserver floor: pressedUp ){
				floor.directionRequested(this, Elevator.Direction.MOVING_UP);
			}
		}
		else if (direction == Elevator.Direction.MOVING_DOWN && !downButton) {

			downButton = true;

			ArrayList<FloorObserver> pressedDown = new ArrayList<>(mObservers);

			for( FloorObserver floor: pressedDown ){
				floor.directionRequested(this, Elevator.Direction.MOVING_DOWN);
			}
		}
	}
	
	/**
	 * Returns true if the given direction button has been pressed.
	 */
	public boolean directionIsPressed(Elevator.Direction direction) {

		if ((upButton && Elevator.Direction.MOVING_UP == direction) || (downButton && Elevator.Direction.MOVING_DOWN == direction)){
			return true;
		}
		return false;
	}
	
	/**
	 * Clears the given direction button so it is no longer pressed.
	 */
	public void clearDirection(Elevator.Direction direction) {

		if (direction == Elevator.Direction.MOVING_DOWN) {
			downButton = false;
		}
		else if (direction == Elevator.Direction.MOVING_UP) {
			upButton = false;
		}
	}
	
	/**
	 * Adds a given Passenger as a waiting passenger on this floor, and presses the passenger's direction button.
	 */
	public void addWaitingPassenger(Passenger p) {
		mPassengers.add(p);
		addObserver(p);
		p.setState(Passenger.PassengerState.WAITING_ON_FLOOR);


		int destination = p.getDestination();

		if(destination < getNumber()){

			requestDirection(Elevator.Direction.MOVING_DOWN);

		}

		else{
			requestDirection(Elevator.Direction.MOVING_UP);
		}


	}
	
	/**
	 * Removes the given Passenger from the floor's waiting passengers.
	 */
	public void removeWaitingPassenger(Passenger p) {
		mPassengers.remove(p);
	}
	
	
	// Simple accessors.
	public int getNumber() {
		return mNumber;
	}

	public Building getmBuilding() {
		return mBuilding;
	}

	public List<Passenger> getWaitingPassengers() {
		return mPassengers;
	}



	@Override
	public String toString() {
		return "Floor " + mNumber;
	}
	
	// Observer methods.
	public void removeObserver(FloorObserver observer) {
		mObservers.remove(observer);
	}
	
	public void addObserver(FloorObserver observer) {
		mObservers.add(observer);
	}
	
	// Observer methods.
	@Override
	public void elevatorDecelerating(Elevator elevator) {
		if (elevator.getCurrentFloor() == this) {
			ArrayList<FloorObserver> floors = new ArrayList<>(mObservers);

			for (FloorObserver o: floors) {
				o.elevatorArriving(this, elevator);
			}

			clearDirection(elevator.getCurrentDirection());

		}

	}
	
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Not needed.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Not needed.
	}


	//create method to print and show active/passenger types

	public ArrayList<String> getNickName(){
		ArrayList<String> NickName = new ArrayList<>();
		for (Passenger i:mPassengers){
			NickName.add(i.getmShortName()+i.getId()+" ->"+i.getmTravelStragety().getDestination());
		}
		return NickName;
	}

}

