package passengers.EmbarkingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class ResponsibleEmbarking implements EmbarkingStrategy {

    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        elevator.ButtonPressed(passenger.getDestination());

        passenger.setState(Passenger.PassengerState.ON_ELEVATOR);

    }

    @Override
    public String toString() {
        return "Responsible Debarking";
    }
}


