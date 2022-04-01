package elevators;

import buildings.Building;
import buildings.Floor;

/**
 * A DispatchMode elevator is in the midst of a dispatch to a target floor in order to handle a request in a target
 * direction. The elevator will not stop on any floor that is not its destination, and will not respond to any other
 * request until it arrives at the destination.
 */
public class DispatchMode implements OperationMode {
	// The destination floor of the dispatch.
	private Floor mDestination;
	// The direction requested by the destination floor; NOT the direction the elevator must move to get to that floor.
	private Elevator.Direction mDesiredDirection;
	
	public DispatchMode(Floor destination, Elevator.Direction desiredDirection) {
		mDestination = destination;
		mDesiredDirection = desiredDirection;
	}
	
	// TODO: implement the other methods of the OperationMode interface.
	// Only Idle elevators can be dispatched.
	// A dispatching elevator ignores all other requests.
	// It does not check to see if it should stop of floors that are not the destination.
	// Its flow of ticks should go: IDLE_STATE -> ACCELERATING -> MOVING -> ... -> MOVING -> DECELERATING.
	//    When decelerating to the destination floor, change the elevator's direction to the desired direction,
	//    announce that it is decelerating, and then schedule an operation change in 3 seconds to
	//    ActiveOperation in the DOORS_OPENING state.
	// A DispatchOperation elevator should never be in the DOORS_OPENING, DOORS_OPEN, or DOORS_CLOSING states.


	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection){
		//elevator.setCurrentDirection(targetDirection);

	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {

	}

	@Override
	public void tick(Elevator elevator) {

		Elevator.Direction direction = elevator.getCurrentDirection();

		Building buildingElevator = elevator.getBuilding();


		switch (elevator.getmCurrentState()){

			case IDLE_STATE:

				elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 0);
				break;

			case ACCELERATING:

				elevator.getCurrentFloor().removeObserver(elevator);

				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 3);

				break;

			case MOVING:

				if (direction == Elevator.Direction.MOVING_DOWN){

					elevator.setCurrentFloor( buildingElevator.getFloor(elevator.getCurrentFloor().getNumber() - 1));

					if(elevator.getfloorsRequested().get(elevator.getNumber() - 1)){

						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);

						elevator.setCurrentDirection(mDesiredDirection);

					}

					else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
					}
				}

				else if( direction == Elevator.Direction.MOVING_UP){

					elevator.setCurrentFloor(buildingElevator.getFloor(elevator.getCurrentFloor().getNumber() + 1));

					if(elevator.getfloorsRequested().get(elevator.getNumber() - 1)){

						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);

						elevator.setCurrentDirection(mDesiredDirection);

					}

					else{
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);

					}

				}

				break;

			case DECELERATING:

				if(elevator.getCurrentFloor() == mDestination){
					elevator.setCurrentDirection(mDesiredDirection);

				}
				elevator.announceElevatorDecelerating();

				if(elevator.getCurrentFloor() == mDestination){
					elevator.scheduleModeChange(new ActiveMode(), Elevator.ElevatorState.DOORS_OPENING, 3);

				}

				else {
					elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 3);
				}

				break;

		}

	}

	@Override
	public String toString() {
		return "Dispatching to " + mDestination.getNumber() + " " + mDesiredDirection;
	}
}
