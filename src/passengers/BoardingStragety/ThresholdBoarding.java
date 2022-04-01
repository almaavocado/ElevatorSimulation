package passengers.BoardingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class ThresholdBoarding implements BoardingStrategy {

    private int mThreshold;

    public ThresholdBoarding(int mThreshold) {
        mThreshold = mThreshold;
    }

    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        return elevator.getPassengerCount() < elevator.getCapacity() && elevator.getPassengerCount()<= mThreshold;
    }
}

