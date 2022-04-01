package passengers.EmbarkingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class ClumsyEmbarking implements EmbarkingStrategy {

    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {

        if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN ||
                elevator.getCurrentFloor().getNumber() > passenger.getDestination()) {

            elevator.ButtonPressed(passenger.getDestination() + 1);

        }
        else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP || elevator.getCurrentFloor().getNumber() < passenger.getDestination()) {
            elevator.ButtonPressed(passenger.getDestination() - 1);
        }
    }



    @Override
    public String toString() {
        return "Clumsy Embarking";
    }
}

