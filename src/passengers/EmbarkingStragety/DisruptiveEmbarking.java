package passengers.EmbarkingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class DisruptiveEmbarking implements EmbarkingStrategy {
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {

        if (elevator.getCurrentFloor().getNumber() - passenger.getDestination() >= 1) {
            for (int i = passenger.getDestination(); i >= 1; i--) {
                elevator.ButtonPressed(i);
            }
        } else {
            for (int i = passenger.getDestination(); i <= elevator.getBuilding().getFloorCount(); i++) {
                elevator.ButtonPressed(i);
            }
        }
        passenger.setState(Passenger.PassengerState.ON_ELEVATOR);

    }

    @Override
    public String toString() {
        return "Disruptive Embarking";
    }
}

