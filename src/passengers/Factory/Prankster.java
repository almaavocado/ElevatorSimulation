package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.DebarkingStragety.AttentiveDebarking;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.TravelStragety.SingleDestinationTravel;
import passengers.TravelStragety.TravelStrategy;

import java.util.Random;

public class Prankster implements PassengerFactory{

    private int weight;

    public Prankster(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Prankster";
    }

    @Override
    public String shortName() {
        return "PRK";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return null;
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        return new SingleDestinationTravel(1, 0);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return null;
    }


    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}

