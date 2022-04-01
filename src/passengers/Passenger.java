package passengers;

import buildings.Floor;
import buildings.FloorObserver;
import elevators.Elevator;
import elevators.ElevatorObserver;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.TravelStragety.TravelStrategy;

/**
 * A passenger that is either waiting on a floor or riding an elevator.
 */
public class Passenger implements FloorObserver, ElevatorObserver {

	private String mName;
	private String mShortName;
	private BoardingStrategy mBoarding;
	private DebarkingStrategy mDebarking;
	private EmbarkingStrategy mEmbarking;
	private TravelStrategy mTravelStragety;


	public static int getmNextId() {
		return mNextId;
	}

	public int getmIdentifier() {
		return mIdentifier;
	}



	// An enum for determining whether a Passenger is on a floor, an elevator, or busy (visiting a room in the building).
	public enum PassengerState {
		WAITING_ON_FLOOR,
		ON_ELEVATOR,
		BUSY
	}


	// A cute trick for assigning unique IDs to each object that is created. (See the constructor.)
	private static int mNextId;
	protected static int nextPassengerId() {
		return ++mNextId;
	}
	
	private int mIdentifier;
	private PassengerState mCurrentState;


	public Passenger(String Name, String ShortName, DebarkingStrategy Debarking, BoardingStrategy Boarding, EmbarkingStrategy Embarking, TravelStrategy Travel) {

		mName = Name;
		mShortName = ShortName;
		mBoarding = Boarding;
		mDebarking = Debarking;
		mEmbarking = Embarking;
		mTravelStragety = Travel;
		mIdentifier = nextPassengerId();
		mCurrentState = PassengerState.WAITING_ON_FLOOR;
	}
	
	public void setState(PassengerState state) {
		mCurrentState = state;
	}
	
	/**
	 * Gets the passenger's unique identifier.
	 */


	public int getId() {
		return mIdentifier;
	}

	public int getDestination(){
		int a = mTravelStragety.getDestination();
		return a;
	}
	
	
	/**
	 * Handles an elevator arriving at the passenger's current floor.
	 */
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// This is a sanity check. A Passenger should never be observing a Floor they are not waiting on.
		if (floor.getWaitingPassengers().contains(this) && mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			Elevator.Direction elevatorDirection = elevator.getCurrentDirection();

			// TODO: check if the elevator is either NOT_MOVING, or is going in the direction that this passenger wants.// If so, this passenger becomes an observer of the elevator.

			floor.clearDirection(elevatorDirection);

			if(elevatorDirection!= null){

				switch (elevatorDirection) {

					case NOT_MOVING:
						elevator.addObserver(this);

						break;

					case MOVING_UP:
						if(this.getmTravelStragety().getDestination() > elevator.getCurrentFloor().getNumber()){

							elevator.addObserver(this);

						}

						break;

					case MOVING_DOWN:
						if(this.getmTravelStragety().getDestination() < elevator.getCurrentFloor().getNumber()){

							elevator.addObserver(this);

						}

						break;
				}

			}

		}
		// This else should not happen if your code is correct. Do not remove this branch; it reveals errors in your code.
		else {
			throw new RuntimeException("Passenger " + toString() + " is observing Floor " + floor.getNumber() + " but they are " +
			 "not waiting on that floor.");
		}
	}


	@Override
	public void elevatorDecelerating(Elevator sender) {

	}

	/**
	 * Handles an observed elevator opening its doors. Depart the elevator if we are on it; otherwise, enter the elevator.
	 */
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// The elevator is arriving at our destination. Remove ourselves from the elevator, and stop observing it.
		// Does NOT handle any "next" destination...
		if (mCurrentState == PassengerState.ON_ELEVATOR) {
			if(this.mDebarking.willLeaveElevator(this, elevator)==true){

				elevator.removeObserver(this);

				elevator.removePassenger(this);

				this.mDebarking.departedElevator(this, elevator);
			}
		}
		// The elevator has arrived on the floor we are waiting on. If the elevator has room for us, remove ourselves
		// from the floor, and enter the elevator.


		else if (mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			if (this.mBoarding.willBoardElevator(this, elevator) == true) {
				if (!elevator.getmPassengers().contains(this)) {

					elevator.addPassenger(this);

					mCurrentState = PassengerState.ON_ELEVATOR;
				}

				this.mEmbarking.enteredElevator(this, elevator);

				elevator.getCurrentFloor().removeWaitingPassenger(this);

				elevator.getCurrentFloor().removeObserver(this);

			}

			else {
				elevator.removeObserver(this);

				if (elevator.getCurrentFloor().getNumber() < this.getmTravelStragety().getDestination()) {

					elevator.getCurrentFloor().requestDirection(Elevator.Direction.MOVING_UP);
				}

				else if (elevator.getCurrentFloor().getNumber() > this.getmTravelStragety().getDestination()) {


					elevator.getCurrentFloor().requestDirection(Elevator.Direction.MOVING_DOWN);

				}

			}
		}
	}


	public boolean onElevator(){
		return this.getmCurrentState() == PassengerState.ON_ELEVATOR;
	}
	/**
	 * Returns the passenger's current destination (what floor they are travelling to).
	 */

	// This will be overridden by derived types.
	@Override
	public String toString() {
		return this.getmName()+" "+ this.getId()+" { ->"+ (getmTravelStragety().getDestination())+"} ";
	}
	
	@Override
	public void directionRequested(Floor sender, Elevator.Direction direction) {
		// Don't care.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Don't care about this.
	}
	
	// The next two methods allow Passengers to be used in data structures, using their id for equality. Don't change 'em.
	@Override
	public int hashCode() {
		return Integer.hashCode(mIdentifier);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Passenger passenger = (Passenger)o;
		return mIdentifier == passenger.mIdentifier;
	}

	public String getmName() {
		return mName;
	}

	public String getmShortName() {
		return mShortName;
	}

	public BoardingStrategy getmBoarding() {
		return mBoarding;
	}

	public DebarkingStrategy getmDebarking() {
		return mDebarking;
	}

	public EmbarkingStrategy getmEmbarking() {
		return mEmbarking;
	}

	public TravelStrategy getmTravelStragety() {
		return mTravelStragety;
	}

	public PassengerState getmCurrentState() {
		return mCurrentState;
	}


}