package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.BoardingStragety.CapacityBoarding;
import passengers.DebarkingStragety.AttentiveDebarking;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.EmbarkingStragety.DisruptiveEmbarking;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.TravelStragety.SingleDestinationTravel;
import passengers.TravelStragety.TravelStrategy;

import java.util.Random;

public class Jerk implements PassengerFactory {

    private int weight;

    public Jerk(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Jerk";
    }

    @Override
    public String shortName() {
        return "JRK";
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
        int mDestination = 2 + rand.nextInt(simulation.getBuilding().getFloorCount()-1);
        long mSchedule = (long)(3600 + rand.nextGaussian() * 120);
        return new SingleDestinationTravel(mDestination , mSchedule);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new DisruptiveEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}

