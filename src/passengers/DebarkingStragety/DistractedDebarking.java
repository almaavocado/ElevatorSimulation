package passengers.DebarkingStragety;

import elevators.Elevator;
import elevators.Simulation;
import events.PassengerNextDestinationEvent;
import passengers.Passenger;

public class DistractedDebarking implements DebarkingStrategy {

    private boolean missed = false;
    private boolean boarded = false;



    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if(passenger.getDestination() == elevator.getCurrentFloor().getNumber() && missed == false && passenger.onElevator()){
            missed = true;
            return false;

        }

        else if(missed == true && boarded == false){
            boarded = true;
            return true;

        }

        else if(passenger.getDestination() == elevator.getCurrentFloor().getNumber() && passenger.onElevator()){
            return true;

        }
        return false;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {

        if(passenger.getDestination() == elevator.getCurrentFloor().getNumber() ){
            passenger.getmTravelStragety().scheduleNextDestination(passenger, elevator.getCurrentFloor());
        }

        else{

            Simulation sim = elevator.getBuilding().getSimulation();
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent((long)(sim.currentTime() + 5), passenger,elevator.getCurrentFloor());
            sim.scheduleEvent(ev);


        }
    }
}

