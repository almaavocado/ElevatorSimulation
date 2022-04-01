package buildings;

import elevators.Simulation;
import elevators.Elevator;
import elevators.ElevatorObserver;

import java.util.*;

public class Building implements ElevatorObserver, FloorObserver {
	private List<Elevator> mElevators = new ArrayList<>();
	private List<Floor> mFloors = new ArrayList<>();
	private Simulation mSimulation;
	private Queue<FloorRequest> mWaitingFloors = new ArrayDeque<>();

	public Building(int floors, int elevatorCount, Simulation sim) {
		mSimulation = sim;

		// Construct the floors, and observe each one.
		for (int i = 0; i < floors; i++) {
			Floor f = new Floor(i + 1, this);
			f.addObserver(this);
			mFloors.add(f);
		}

		// Construct the elevators, and observe each one.
		for (int i = 0; i < elevatorCount; i++) {
			Elevator elevator = new Elevator(i + 1, this);
			elevator.addObserver(this);
			for (Floor f : mFloors) {
				elevator.addObserver(f);
			}
			mElevators.add(elevator);
		}
	}


	public String toString() {
		ArrayList<String> elevatorArt = new ArrayList<>();

		ArrayList<String> strings = new ArrayList<>();


		for(int i = 0; i < mElevators.size(); i++){

			strings.add("|  |");
		}

		for(int j = mFloors.size()-1;j >= 0;j--){

			for(int k = 0; k < mElevators.size(); k++){

				if (mElevators.get(k).getCurrentFloor().getNumber()-1 == j){

					strings.set(k, "| x |");

				}
				else{
					strings.set(k,"|   |");
				}
			}
			if(j + 1 >= 10) {
				elevatorArt.add((j+1)+ ": "+String.join("",strings) +mFloors.get(j).getNickName()+"\n");
			}
			else {
				elevatorArt.add(" "+(j+1)+": "+String.join("",strings) +mFloors.get(j).getNickName()+"\n");
			}

		}


		for(Elevator z: mElevators){
			elevatorArt.add(z.toString()+"\n");
		}


		String result = String.join("", elevatorArt);

		return result;

	}

	public int getFloorCount() {
		return mFloors.size();
	}

	public Floor getFloor(int floor) {
		return mFloors.get(floor - 1);
	}

	public Simulation getSimulation() {
		return mSimulation;
	}



	@Override
	public void elevatorDecelerating(Elevator elevator) {
		// Have to implement all interface methods even if we don't use them.
	}

	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Don't care.
	}

	@Override
	public void elevatorWentIdle(Elevator elevator) {

		if (!mWaitingFloors.isEmpty()){
			FloorRequest floorRequest = mWaitingFloors.remove();

			elevator.dispatchTo(floorRequest.mDestination, floorRequest.mDirection);
		}

	}

	@Override
	public void elevatorArriving(Floor sender, Elevator elevator) {

		mWaitingFloors.removeIf(f -> f.mDestination.getNumber() == sender.getNumber() &&

				(elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING ||

						elevator.getCurrentDirection() == f.mDirection));

	}



	@Override
	public void directionRequested(Floor floor, Elevator.Direction direction) {
		for (Elevator ele: mElevators) {
			if (ele.getOperationMode().canBeDispatchedToFloor(ele, floor)){

				ele.getOperationMode().directionRequested(ele, floor, direction);

				break;
			}

			else {
				mWaitingFloors.add(new FloorRequest(floor, direction));
			}
		}

	}

	public class FloorRequest{

		private Floor mDestination;
		private Elevator.Direction mDirection;

		private FloorRequest(Floor Destination, Elevator.Direction Direction) {
			mDestination = Destination;
			mDirection = Direction;
		}


		public Floor getDestination() {
			return mDestination;
		}

		public Elevator.Direction getDirection() {
			return mDirection;
		}


		public boolean equals(FloorRequest request){
			if(this.mDestination.equals(request.getDestination()) && this.mDirection.equals(request.getDirection())){
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return "FloorRequest{" +
					"mDestination=" + mDestination +
					", mDirection=" + mDirection +
					'}';
		}
	}


}
