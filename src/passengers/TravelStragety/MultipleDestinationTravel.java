package passengers.TravelStragety;

import buildings.Floor;
import elevators.Simulation;
import events.PassengerNextDestinationEvent;
import passengers.Passenger;
import java.util.*;

public class MultipleDestinationTravel implements TravelStrategy {

    private List <Integer> mDestination;
    private List <Long> mSchedule;

    public MultipleDestinationTravel(List<Integer> destination , List<Long> schedule){
        mDestination = destination;
        mSchedule = schedule;


    }
    @Override
    public int getDestination() {
        if(mDestination.isEmpty()){
            return 1;
        }
        return mDestination.get(0);
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
        this.mDestination.remove(0);

        Simulation sim = currentFloor.getmBuilding().getSimulation();
        PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(sim.currentTime() + this.mSchedule.get(0), passenger,currentFloor);

        this.mSchedule.remove(0);
        sim.scheduleEvent(ev);
    }
}

