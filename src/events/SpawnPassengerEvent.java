package events;

import buildings.Building;
import elevators.Simulation;
import passengers.Factory.PassengerFactory;
import passengers.Passenger;

import java.util.Random;
import java.util.*;


/**
 * A simulation event that adds a new random passenger on floor 1, and then schedules the next spawn event.
 */
public class SpawnPassengerEvent extends SimulationEvent {
	//private static long SPAWN_MEAN_DURATION = 10_800;
	//private static long SPAWN_STDEV_DURATION = 3_600;

	// After executing, will reference the Passenger object that was spawned.
	private Passenger mPassenger;
	private Building mBuilding;
	
	public SpawnPassengerEvent(long scheduledTime, Building building) {
		super(scheduledTime);
		mBuilding = building;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Adding " + mPassenger.getmName() + " " +
				mPassenger.getId() + " --> " + " [ " + mPassenger.getDestination() +" ]" + " to floor 1.";
	}
	
	@Override
	public void execute(Simulation sim) {

		Random r = mBuilding.getSimulation().getRandom();

		List<PassengerFactory> PassengerFactories = sim.getPassengerFactories();

		int temp = 0;

		for (PassengerFactory o : PassengerFactories) {

			temp += o.factoryWeight();

		}

		int weight = 0;

		int rand = r.nextInt(temp);

		for (PassengerFactory p : PassengerFactories) {

			weight += p.factoryWeight();

			if (weight > rand) {
				mPassenger = new Passenger(p.factoryName(), p.shortName(), p.createDebarkingStrategy(sim), p.createBoardingStrategy(sim),
						p.createEmbarkingStrategy(sim), p.createTravelStrategy(sim));

				break;

			}
		}

		mBuilding.getFloor(1).addWaitingPassenger(mPassenger);


		int min = 1 ;
		int max = 30;

		long scheduleTime = min + sim.getRandom().nextInt(max) + sim.currentTime();

		SpawnPassengerEvent ev = new SpawnPassengerEvent(scheduleTime, mBuilding);

		sim.scheduleEvent(ev);

	}

}
