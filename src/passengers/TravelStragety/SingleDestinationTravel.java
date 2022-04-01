package passengers.TravelStragety;

import buildings.Floor;
import elevators.Simulation;
import passengers.Passenger;
import events.PassengerNextDestinationEvent;

public class SingleDestinationTravel implements TravelStrategy {

    private int mDestination;
    private long mTime;

    public SingleDestinationTravel(int Destination, long Time ) {
        mDestination = Destination;
        mTime = Time;

    }

    @Override
    public int getDestination() {
        return mDestination;
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {

        this.mDestination = 1;

        Simulation sim = currentFloor.getmBuilding().getSimulation();

        PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(sim.currentTime() + mTime, passenger,currentFloor);

        sim.scheduleEvent(ev);

    }
}

