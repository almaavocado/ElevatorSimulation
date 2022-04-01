package elevators;

import buildings.Building;
import events.SimulationEvent;
import events.SpawnPassengerEvent;
import logging.Logger;
import logging.StandardOutLogger;
import passengers.Factory.*;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class Simulation {
	private Random mRandom;
	private PriorityQueue<SimulationEvent> mEvents = new PriorityQueue<>();
	private long mCurrentTime;
	private List<PassengerFactory> passengers=new ArrayList<>();
	private Building mBuilding;

	/**
	 * Seeds the Simulation with a given random number generator.
	 * @param random
	 */
	public Simulation(Random random) {
		mRandom = random;
	}

	public long getTime(){
		return mCurrentTime;
	}

	public Building getBuilding(){
		return mBuilding;
	}

	public List<PassengerFactory> getPassengerFactories(){
		return passengers;
	}

	public void setPassengerFactory(List<PassengerFactory> passengerFactoryList){
		passengerFactoryList = passengers;
	}

	public void addPassenger(PassengerFactory pas){
		passengers.add(pas);
	}
	/**
	 * Gets the current time of the simulation.
	 * @return
	 */
	public long currentTime() {
		return mCurrentTime;
	}

	/**
	 * Access the Random object for the simulation.
	 * @return
	 */
	public Random getRandom() {
		return mRandom;
	}

	/**
	 * Adds the given event to a priority queue sorted on the scheduled time of execution.
	 * @param ev
	 */
	public void scheduleEvent(SimulationEvent ev) {
		mEvents.add(ev);
	}

	public void startSimulation(Scanner input) {

		StandardOutLogger standardOutLogger = new StandardOutLogger(this);

		Logger.setInstance(standardOutLogger);

		standardOutLogger.logString("\nEnter # of Floors: ");

		int floors = input.nextInt();
		standardOutLogger.logString("\nEnter # of Elevators: ");

		int elevators = input.nextInt();

		mBuilding = new Building(floors, elevators, this);

		for(PassengerFactory i: passengers){

			standardOutLogger.logString(i.factoryName()+": weight  = "+i.factoryWeight());
		}

		SpawnPassengerEvent ev = new SpawnPassengerEvent(0, mBuilding);

		scheduleEvent(ev);

		long nextSimLength = -1;

		// Set this boolean to true to make the simulation run at "real time".
		boolean simulateRealTime = false;
		// Change the scale below to less than 1 to speed up the "real time".
		double realTimeScale = 1.0;
		standardOutLogger.logString(" ");
		// TODO: the simulation currently stops at 200s. Instead, ask the user how long they want to simulate.

		standardOutLogger.logString("\nEnter Simulation Time: ");

		nextSimLength = input.nextInt();

		while(nextSimLength!=-1){
			long nextStopTime = mCurrentTime + nextSimLength;
			// If the next event in the queue occurs after the requested sim time, then just fast forward to the requested sim time.
			if (mEvents.peek().getScheduledTime() >= nextStopTime) {
				mCurrentTime = nextStopTime;
			}

			// As long as there are events that happen between "now" and the requested sim time, process those events and-1
			// advance the current time along the way.
			while (!mEvents.isEmpty() && mEvents.peek().getScheduledTime() <= nextStopTime) {
				SimulationEvent nextEvent = mEvents.poll();

				long diffTime = nextEvent.getScheduledTime() - mCurrentTime;
				if (simulateRealTime && diffTime > 0) {
					try {
						Thread.sleep((long)(realTimeScale * diffTime * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				mCurrentTime += diffTime;
				nextEvent.execute(this);
				standardOutLogger.logEvent(nextEvent);


			}

			// TODO: print the Building after simulating the requested time.
			standardOutLogger.logString(mBuilding.toString());

		/*
		 TODO: the simulation stops after one round of simulation. Write a loop that continues to ask the user
		 how many seconds to simulate, simulates that many seconds, and stops only if they choose -1 seconds.
		*/
			standardOutLogger.logString("Please Enter your simulation time. Enter -1 to exit");
			nextSimLength = input.nextInt();

		}

	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.println("Please enter a seed value number: ");
		int seedValue = s.nextInt();

		VisitorFactory visitorFactory = new VisitorFactory(3);

		WorkerFactory workerFactory = new WorkerFactory(10);

		Child child = new Child(2);

		DeliveryPerson deliveryPerson = new DeliveryPerson(2);

		Stoner stoner = new Stoner(1);

		Jerk jerk = new Jerk(2);


		Simulation sim = new Simulation(new Random(seedValue));
		sim.addPassenger(visitorFactory);
		sim.addPassenger(workerFactory);
		sim.addPassenger(child);
		sim.addPassenger(deliveryPerson);
		sim.addPassenger(stoner);
		sim.addPassenger(jerk);
		sim.startSimulation(s);

	}
}

