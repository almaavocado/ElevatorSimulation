package passengers.DebarkingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class AttentiveDebarking implements DebarkingStrategy {

    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        return passenger.getDestination() == elevator.getCurrentFloor().getNumber();
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
       passenger.getmTravelStragety().scheduleNextDestination(passenger, elevator.getCurrentFloor());

    }

}


