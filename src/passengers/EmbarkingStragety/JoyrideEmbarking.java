package passengers.EmbarkingStragety;

import elevators.Elevator;
import passengers.Passenger;

public class JoyrideEmbarking implements EmbarkingStrategy {

    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        passenger.setState(Passenger.PassengerState.ON_ELEVATOR);
    }
}

