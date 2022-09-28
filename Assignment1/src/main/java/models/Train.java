package models;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /* three helper methods that are useful in other methods */
    public boolean hasWagons() {
        return getFirstWagon() != null;
    }

    public boolean isPassengerTrain() {
        return getFirstWagon() instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return getFirstWagon() instanceof FreightWagon;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {

        if (wagon == null) {
            this.firstWagon = null;
        } else if (!wagon.hasPreviousWagon()) {
            this.firstWagon = wagon;
        }
    }

    // Extra method to get the amount of wagons
    public int getSize(Wagon wagon) {
        if (wagon == null) {
            return 0;
        }
        int size = 0;
        while (wagon != null) {
            size++;
            wagon = wagon.getNextWagon();
        }
        return size;
    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        if (hasWagons()) {
            return getFirstWagon().getTailLength() + 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        if (hasWagons()) {
            return getFirstWagon().getLastWagonAttached();
        } else {
            return null;
        }
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        if (isFreightTrain()) {
            return 0;
        } else {
            int numberOfSeats = 0;
            PassengerWagon wagon = (PassengerWagon) getFirstWagon();
            if (wagon == null) {
                return numberOfSeats;
            }
            numberOfSeats += wagon.getNumberOfSeats();
            while (wagon.hasNextWagon()) {
                wagon = (PassengerWagon) wagon.getNextWagon();
                numberOfSeats += wagon.getNumberOfSeats();
            }
            return numberOfSeats;
        }

    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        if (isPassengerTrain()) {
            return 0;
        } else {
            int maxWeight = 0;
            FreightWagon wagon = (FreightWagon) getFirstWagon();
            if (wagon == null) {
                return maxWeight;
            }
            maxWeight += wagon.getMaxWeight();
            while (wagon.hasNextWagon()) {
                wagon = (FreightWagon) wagon.getNextWagon();
                maxWeight += wagon.getMaxWeight();
            }
            return maxWeight;
        }

    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        Wagon wagon = getFirstWagon();
        if (position <= 0) {
            return null;
        }
        for (int i = 1; i < position; i++) {
            if (wagon == null) {
                return null;
            } else {
                wagon = wagon.getNextWagon();
            }
        }
        return wagon;
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        Wagon wagon = getFirstWagon();
        // Checks if first wagon is not null
        if (wagon != null) {
            //While wagon is not null you get all the id's of the wagon's and checks it with the input
            // if the next wagon is null return null
            while (true) {
                if (wagonId == wagon.getId()) {
                    return wagon;
                }
                wagon = wagon.getNextWagon();
                if (wagon == null) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return
     */
    public boolean canAttach(Wagon wagon) {
        if (wagon == null) {
            return false;
        }
        Locomotive engine = getEngine();
        Wagon firstWagon = getFirstWagon();
        int capacity = engine.getMaxWagons();
        if (firstWagon == null) {
            return capacity >= getSize(wagon);
        } else {
            return firstWagon.getClass().equals(wagon.getClass()) && capacity >= (getSize(firstWagon) + getSize(wagon));
        }
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {
        if (canAttach(wagon)) {

            if (getFirstWagon() == null) {
                setFirstWagon(wagon);
            } else {
                Wagon lastWagon = getFirstWagon().getLastWagonAttached();
                lastWagon.attachTail(wagon);
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        if (wagon != null && canAttach(wagon)) {
            if (getFirstWagon() != null && getFirstWagon().getLastWagonAttached() == wagon.getLastWagonAttached()) {
                return false;
            }
            Wagon headWagon = getFirstWagon();
            setFirstWagon(wagon);
            if (wagon == null) {
                return false;
            }
            if (headWagon != null) {
                wagon.getLastWagonAttached().attachTail(headWagon);
            }
            return true;

        }
        return false;
    }


    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        Wagon firstWagon = getFirstWagon();
        if (position <= 0) {
            return false;
        }
        if (position == 1) {
            setFirstWagon(wagon);
            return true;
        }
        if (firstWagon == null) {
            return false;
        } else {
            Wagon wagonAtPosition = findWagonAtPosition(position);
            if (wagonAtPosition == null) {
                return false;
            } else {
                Wagon wagonBeforePosition = wagonAtPosition.getPreviousWagon();
                wagonBeforePosition.attachTail(wagon);
                wagon.getLastWagonAttached().attachTail(wagonAtPosition);
                return true;
            }
        }
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        Wagon wagon = findWagonById(wagonId);
        if (wagon == null || !toTrain.canAttach(wagon)) {
            return false;
        }
        Wagon nextWagon = wagon.getNextWagon();
        wagon.removeFromSequence();
        if (wagon == getFirstWagon()) {
            setFirstWagon(nextWagon);
        }
        return toTrain.attachToRear(wagon);
    }


    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {

        Wagon wagon = findWagonAtPosition(position);
        if (wagon == null || !toTrain.canAttach(wagon)) {
            return false;
        }
        if (wagon == getFirstWagon()) {
            setFirstWagon(null);
        }
        wagon.detachFront();
        return toTrain.attachToRear(wagon);
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        if (hasWagons() && getFirstWagon().hasNextWagon()) {
            setFirstWagon(getFirstWagon().reverseSequence());
        }
    }

    @Override
    public String toString() {
        StringBuilder wagons = new StringBuilder();
        Wagon wagon = getFirstWagon();
        while (wagon != null) {
            wagons.append(wagon);
            wagon = wagon.getNextWagon();
        }
        return getEngine().toString() +
                wagons +
                " with " +
                getNumberOfWagons() +
                " wagons from " +
                getOrigin() +
                " to " +
                getDestination();
    }
}
