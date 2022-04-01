package passengers.DebarkingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class ConfusedDebarking implements DebarkingStrategy {

    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if(elevator.getCurrentFloor().getNumber()== 1){
            return true;
        }
        return false;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        passenger.getmTravelStragety().scheduleNextDestination(passenger, elevator.getCurrentFloor());
    }
}

