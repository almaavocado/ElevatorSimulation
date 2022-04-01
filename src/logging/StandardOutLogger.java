package logging;

import elevators.Simulation;

public class StandardOutLogger extends Logger{

    public StandardOutLogger(Simulation simulation) {
        super(simulation);
    }

    @Override
    public void logString(String message) {
        System.out.println(message);
    }

}

