# ElevatorSimulation

This Elevator Sumulation project uses a next-event time simulation. Events will be scheduled for a certain point in the future, 
and the simulation will "skip ahead" to the next event using a priority queue. 

We will model passengers and floors of the building as Java classes, allowing us to associate
behaviors with those entities instead of simply representing them as integers. Floors will represent the "call
elevator" up/down buttons explicitly, and elevators will only stop on a floor if the correct direction button
has been pressed. The building will keep track of these button presses and will send idle elevators to floors
that have been waiting a long time for an elevator.

There will be different types of passengers: some that enter the building on floor 1, visit a random floor for
a random period of time, then return to floor 1; others will travel between many floors before leaving. We
will use different random number distributions for generating different random attributes of the simulation,
including uniform distributions and the Gaussian (normal) distribution.

Entities (buildings, floors, elevators, passengers) will communicate with each other using the Observer
design pattern. Entities will announce when certain events have occurred, which will allow other entities to
respond - for example, a floor will announce when an elevator has arrived, which will allow waiting passengers
to board the elevator. Elevators will not manipulate passengers directly, instead using the observer pattern
to alert passengers when it is time to embark or disembark, and leaving it up to the passengers to execute
those operations.

*Project from class*
