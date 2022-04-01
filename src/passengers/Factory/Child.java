package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.AwkawardBoarding;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.DebarkingStragety.DistractedDebarking;
import passengers.EmbarkingStragety.ClumsyEmbarking;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.TravelStragety.SingleDestinationTravel;
import passengers.TravelStragety.TravelStrategy;

import java.util.Random;

public class Child implements PassengerFactory {

    private int weight;

    public Child(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Child";
    }

    @Override
    public String shortName() {
        return "C";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {

        return new AwkawardBoarding(4);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random rand = simulation.getRandom();
        System.out.println();
        return new SingleDestinationTravel(2 + rand.nextInt(simulation.getBuilding().getFloorCount()-1), (long)(7200 + rand.nextGaussian() * 1800));
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {

        return new ClumsyEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {

        return new DistractedDebarking();
    }
}

