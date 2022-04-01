package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.BoardingStragety.ThresholdBoarding;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.DebarkingStragety.DistractedDebarking;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.EmbarkingStragety.ResponsibleEmbarking;
import passengers.TravelStragety.MultipleDestinationTravel;
import passengers.TravelStragety.TravelStrategy;

import java.util.Random;
import java.util.*;

public class DeliveryPerson implements PassengerFactory {

    private int weight;

    public DeliveryPerson(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Delivery Person";
    }

    @Override
    public String shortName() {
        return "DP";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new ThresholdBoarding(5);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {

        Random r = simulation.getRandom();

        List<Integer> floor = new ArrayList<>();

        List<Long> schedule = new ArrayList<>();

        int FloorDestination = r.nextInt(simulation.getBuilding().getFloorCount() * 2 / 3) + 1 ;

        int Floor;

        for(int i = 0; i < FloorDestination; i++){

            Floor = r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2;

            while(floor.contains(Floor)){

                Floor = r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2;

            }
            floor.add(Floor);
        }

        for(int i = 0; i < FloorDestination; i++){

            schedule.add((long)(10 * r.nextGaussian() + 60));

        }

        return new MultipleDestinationTravel(floor, schedule);


    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ResponsibleEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new DistractedDebarking();
    }
}

