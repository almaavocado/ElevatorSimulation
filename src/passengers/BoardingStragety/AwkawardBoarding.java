package passengers.BoardingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class AwkawardBoarding implements BoardingStrategy {
    private int mThreshold;

    public AwkawardBoarding(int mThreshold) {
        this.mThreshold = mThreshold;
    }

    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        if(!(elevator.getPassengerCount() < elevator.getCapacity()) && elevator.getPassengerCount() <= mThreshold){
            mThreshold += 2;
            return false;
        }

        return true;
    }
}

