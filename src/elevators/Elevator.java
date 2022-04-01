package elevators;

import buildings.Building;
import buildings.Floor;
import buildings.FloorObserver;
import events.ElevatorModeEvent;
import events.ElevatorStateEvent;
import logging.Logger;
import logging.StandardOutLogger;
import passengers.Passenger;

import java.util.*;
import java.util.stream.Collectors;

public class Elevator implements FloorObserver {

	public enum ElevatorState {
		IDLE_STATE,
		DOORS_OPENING,
		DOORS_CLOSING,
		DOORS_OPEN,
		ACCELERATING,
		DECELERATING,
		MOVING
	}
	
	public enum Direction {
		NOT_MOVING,
		MOVING_UP,
		MOVING_DOWN
	}

	private OperationMode mOperation = new IdleMode();

	private int mNumber;
	private Building mBuilding;

	private ElevatorState mCurrentState = ElevatorState.IDLE_STATE;
	private Direction mCurrentDirection = Direction.NOT_MOVING;
	private Floor mCurrentFloor;
	private List<Passenger> mPassengers = new ArrayList<>();
	
	private List<ElevatorObserver> mObservers = new ArrayList<>();

	private BitSet mfloorsRequested;



	public int getNumber() {
		return mNumber;
	}

	public Building getmBuilding() {
		return mBuilding;
	}

	public ElevatorState getmCurrentState() {
		return mCurrentState;
	}

	public Direction getmCurrentDirection() {
		return mCurrentDirection;
	}

	public Floor getmCurrentFloor() {
		return mCurrentFloor;
	}

	public List<Passenger> getmPassengers() {
		return mPassengers;
	}

	public List<ElevatorObserver> getmObservers() {
		return mObservers;
	}

	public BitSet getfloorsRequested() {
		return mfloorsRequested;
	}



	public Elevator(int number, Building bld) {
		mNumber = number;
		mBuilding = bld;
		mCurrentFloor = bld.getFloor(1);
		mfloorsRequested = new BitSet(mBuilding.getFloorCount());
		mOperation = new IdleMode();
		this.setCurrentDirection(Direction.NOT_MOVING);
		this.scheduleStateChange(ElevatorState.IDLE_STATE, 0);
		mCurrentFloor.addObserver(this);
	}


	/**
	 * Helper method to schedule a state change in a given number of seconds from now.
	 */
	public void scheduleStateChange(ElevatorState state, long timeFromNow) {
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorStateEvent(sim.currentTime() + timeFromNow, state, this));
	}

	public void scheduleModeChange(OperationMode mode, ElevatorState state, long mtimeFromNow ){
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(sim.currentTime() + mtimeFromNow , mode , state , this));

	}

	/**
	 * Adds the given passenger to the elevator's list of passengers, and requests the passenger's destination floor.
	 */
	public void addPassenger(Passenger passenger) {
		mPassengers.add(passenger);

	}
	
	public void removePassenger(Passenger passenger) {
		mPassengers.remove(passenger);
	}
	
	public OperationMode getOperationMode(){
		return mOperation;
	}


	/**
	 * Schedules the elevator's next state change based on its current state.
	 */
	public void tick() {
		mOperation.tick(this);
	}


	public void announceElevatorIdle() {
		List<ElevatorObserver> temp = new ArrayList<>(mObservers);
		for (ElevatorObserver o: temp) {
			o.elevatorWentIdle(this);
		}
	}

	public void announceElevatorDecelerating() {
		List<ElevatorObserver> temp = new ArrayList<>(mObservers);
		for (ElevatorObserver o: temp) {
			o.elevatorDecelerating(this);
		}
	}



	/**
	 * Sends an idle elevator to the given floor.
	 */
	public void dispatchTo(Floor floor, Elevator.Direction direction) {
		mOperation.dispatchToFloor(this, floor, direction);


	}
	
	// Simple accessors
	public Floor getCurrentFloor() {
		return mCurrentFloor;
	}
	
	public Direction getCurrentDirection() {
		return mCurrentDirection;
	}
	
	public Building getBuilding() {
		return mBuilding;
	}
	
	/**
	 * Returns true if this elevator is in the idle state.
	 * @return
	 */
	public boolean isIdle() {
		if(mCurrentState == ElevatorState.IDLE_STATE){
			return true;
		}
		return false;
	}
	
	// All elevators have a capacity of 10, for now.
	public int getCapacity() {
		return 10;
	}
	
	public int getPassengerCount() {
		return mPassengers.size();
	}
	
	// Simple mutators
	public void setState(ElevatorState newState) {
		mCurrentState = newState;
	}
	
	public void setCurrentDirection(Direction direction) {
		mCurrentDirection = direction;
	}

	public void setCurrentFloor(Floor mCurrentFloor) {
		this.mCurrentFloor = mCurrentFloor;
	}

	public OperationMode getmOperation() {
		return mOperation;
	}


	public void setMode(OperationMode mode) {
		mOperation = mode;
	}


	// Observers
	public void addObserver(ElevatorObserver observer) {
		mObservers.add(observer);
	}
	
	public void removeObserver(ElevatorObserver observer) {
		mObservers.remove(observer);
	}




	// FloorObserver methods
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// Not used.
	}


	public void ButtonPressed(int destination) {
		if (destination != mCurrentFloor.getNumber()){
			mfloorsRequested.set(destination - 1);
		}
	}


	public Direction findOppisiteDirection (Direction direction) {

		if (direction == Direction.MOVING_UP) {
			return Direction.MOVING_DOWN;
		}

		else if (direction == Direction.MOVING_DOWN) {
			return Direction.MOVING_UP;
		}
		else {
			return Direction.NOT_MOVING;
		}
	}
	/**
	 * Triggered when our current floor receives a direction request.
	 */
	@Override
	public void directionRequested(Floor sender, Direction direction) {
		mOperation.directionRequested(this, sender, direction);
	}


	public boolean TopFloor() {

		boolean top = (mCurrentFloor == mBuilding.getFloor(mBuilding.getFloorCount()));

		return top;
	}

	public boolean BottomFloor() {

		boolean bottom = (mCurrentFloor == mBuilding.getFloor(1));

		return bottom;
	}


	public boolean RequestUp(Direction way){

		boolean temp;

		if(way == Direction.MOVING_DOWN){
			temp = mfloorsRequested.get(0, mCurrentFloor.getNumber()).cardinality() > 0;
			return temp;
		}

		else if(way == Direction.MOVING_UP){
			temp = mfloorsRequested.get(mCurrentFloor.getNumber(), mBuilding.getFloorCount()).cardinality() > 0;

			return temp;
		}

		else {
			return false;
		}

	}


	// Voodoo magic.
	@Override
	public String toString() {
		return "Elevator " + mNumber + " - ["+this.getOperationMode()+"]" + " - " + mCurrentFloor + " - " + mCurrentState + " - " + mCurrentDirection + " - "
		 + "[" + mPassengers.stream().map(p -> p.getmShortName()+p.getId()).collect(Collectors.joining(", "))
		 + "]" + this.getfloorsRequested();
	}
	
}
