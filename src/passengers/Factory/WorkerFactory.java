package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.BoardingStragety.ThresholdBoarding;
import passengers.DebarkingStragety.AttentiveDebarking;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.EmbarkingStragety.ResponsibleEmbarking;
import passengers.TravelStragety.MultipleDestinationTravel;
import passengers.TravelStragety.TravelStrategy;

import java.util.Random;
import java.util.*;

public class WorkerFactory implements PassengerFactory {

   private int weight;

    public WorkerFactory(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Worker";
    }

    @Override
    public String shortName() {
        return "WRK";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new ThresholdBoarding(3);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {

        Random r = simulation.getRandom();

        List<Integer> floors = new ArrayList<>();

        List<Long> schedule = new ArrayList<>();

        int FloorDestination = r.nextInt(4) + 2 ;

        int lastFloor = 0;

        int Floor;

        for(int i = 0; i < FloorDestination; i++){

            Floor = r.nextInt(simulation.getBuilding().getFloorCount()- 1)+ 2;

            while(Floor == lastFloor){

                Floor = r.nextInt(simulation.getBuilding().getFloorCount()-1) + 2;

            }
            floors.add(Floor);

            lastFloor = Floor;
        }

        for(int i = 0; i < FloorDestination; i++){

            double temp = 180 * r.nextGaussian() + 600;
            schedule.add((long)(temp));
        }

        return new MultipleDestinationTravel(floors, schedule);

    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ResponsibleEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}

