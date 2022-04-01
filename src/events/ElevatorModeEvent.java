package events;

import elevators.Elevator;
import elevators.OperationMode;
import elevators.Simulation;

public class ElevatorModeEvent extends SimulationEvent {

    private Elevator.ElevatorState mNewState;
    private OperationMode mNewMode;
    private Elevator mElevator;

    public ElevatorModeEvent(long scheduledTime, OperationMode newMode,Elevator.ElevatorState newState, Elevator elevator) {
        super(scheduledTime);

        mNewMode = newMode;

        mElevator = elevator;

        mNewState = newState;


    }


    @Override
    public void execute(Simulation sim) {
        mElevator.setMode(mNewMode);

        mElevator.setState(mNewState);

        mElevator.tick();
    }

    @Override
    public String toString() {
        return super.toString() + mElevator;

    }

}

