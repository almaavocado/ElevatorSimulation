package passengers.Factory;

import elevators.Simulation;
import passengers.BoardingStragety.BoardingStrategy;
import passengers.BoardingStragety.CapacityBoarding;
import passengers.DebarkingStragety.AttentiveDebarking;
import passengers.DebarkingStragety.DebarkingStrategy;
import passengers.EmbarkingStragety.EmbarkingStrategy;
import passengers.EmbarkingStragety.JoyrideEmbarking;
import passengers.TravelStragety.TravelStrategy;

//I wasn't able to finish in time to implement both Joyrider and Prankster


public class Joyrider implements PassengerFactory {


    private int weight;

    public Joyrider(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Joyrider";
    }

    @Override
    public String shortName() {
        return "JRYD";
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
        return null;
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new JoyrideEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}

