package elevators;

import buildings.Floor;

import java.util.ArrayList;

/**
 * An ActiveMode elevator is handling at least one floor request.
 */
public class ActiveMode implements OperationMode {

	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {

		//ignore ----> canBeDispatchedToFloor returns false

	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {

		//ignore
	}

	@Override
	public void tick(Elevator elevator) {

		switch (elevator.getmCurrentState()) {

			case DOORS_OPENING:

				elevator.getfloorsRequested().set(elevator.getCurrentFloor().getNumber() - 1, false);
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPEN, 2);

				break;

			case DOORS_OPEN:

				ArrayList<ElevatorObserver> ele = new ArrayList<>();

				for (ElevatorObserver o : ele) {
					o.elevatorDoorsOpened(elevator);
				}

				int count = (elevator.getPassengerCount() / 2) + 1;
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_CLOSING, count);


				break;

			case DOORS_CLOSING:

				if (elevator.RequestUp(elevator.getCurrentDirection())) {

					elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 2);

				} else if (elevator.RequestUp(elevator.findOppisiteDirection(elevator.getCurrentDirection()))) {

					elevator.setCurrentDirection(elevator.findOppisiteDirection(elevator.getCurrentDirection()));
					elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 2);

				} else {
					elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
					elevator.scheduleModeChange(new IdleMode(), Elevator.ElevatorState.IDLE_STATE, 2);
				}

				break;

			case ACCELERATING:

				elevator.getCurrentFloor().removeObserver(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 3);

				break;

			case MOVING:

				if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {

					elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() - 1));

					if (elevator.getfloorsRequested().get(elevator.getCurrentFloor().getNumber() - 2) || elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() - 1).directionIsPressed(Elevator.Direction.MOVING_DOWN)
							|| elevator.getCurrentFloor().getNumber() == 1) {

						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);

					} else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
					}

				} else {

					elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() + 1));

					if (elevator.getfloorsRequested().get(elevator.getCurrentFloor().getNumber()) || elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() + 1).directionIsPressed(Elevator.Direction.MOVING_UP)
							|| elevator.getCurrentFloor().getNumber() == elevator.getBuilding().getFloorCount()) {

						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);

					} else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);

					}
				}


				break;


			case DECELERATING:

				if (elevator.TopFloor() || elevator.BottomFloor()) {

					if (elevator.getCurrentFloor().directionIsPressed(elevator.findOppisiteDirection(elevator.getCurrentDirection()))) {

						elevator.setCurrentDirection(elevator.findOppisiteDirection(elevator.getCurrentDirection()));

					} else {
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
					}

				} else {

					if (elevator.RequestUp(elevator.getCurrentDirection()) || elevator.getCurrentFloor().directionIsPressed(elevator.getCurrentDirection())) {

					} else if (elevator.getCurrentFloor().directionIsPressed(elevator.findOppisiteDirection(elevator.getCurrentDirection()))) {
						elevator.setCurrentDirection(elevator.findOppisiteDirection(elevator.getCurrentDirection()));
					} else {
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
					}

				}

				elevator.announceElevatorDecelerating();

				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 3);

				break;
		}

	}



	@Override
	public String toString() {
		return "Active";
	}


}

