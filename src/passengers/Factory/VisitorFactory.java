package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.BoardingStragety.CapacityBoarding;
import passengers.DebarkingStragety.AttentiveDebarking;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.EmbarkingStragety.ResponsibleEmbarking;
import passengers.TravelStragety.SingleDestinationTravel;
import passengers.TravelStragety.TravelStrategy;

import java.util.Random;

public class VisitorFactory implements PassengerFactory {

    private int weight;

    public VisitorFactory(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Visitor";
    }

    @Override
    public String shortName() {
        return "V";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new CapacityBoarding();
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random rand = simulation.getRandom();

        int destination = 2 + rand.nextInt(simulation.getBuilding().getFloorCount() - 1);
        long duration = (long)(3600 + rand.nextGaussian() * 1200);

        return new SingleDestinationTravel(destination, duration);
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

