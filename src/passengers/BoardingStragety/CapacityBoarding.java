package passengers.BoardingStragety;


import elevators.Elevator;
import passengers.Passenger;

/**
 * A CapacityBoarding is a boarding strategy for a Passenger that will get on any elevator that has not reached its
 * capacity.
 */
public class CapacityBoarding implements BoardingStrategy {
    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getPassengerCount() < elevator.getCapacity()){
            return true;
        }

        else{
            return false;
        }
    }
}
