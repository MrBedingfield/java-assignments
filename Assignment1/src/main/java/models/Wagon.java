package models;

public class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;

    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    protected Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return getNextWagon() != null;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return getPreviousWagon() != null;
    }

    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     *
     * @return the wagon
     */
    public Wagon getLastWagonAttached() {

        Wagon lastWagon = this;
        while (lastWagon.hasNextWagon()) {
            lastWagon = lastWagon.getNextWagon();
        }

        return lastWagon;
    }

    /**
     * @return the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {

        Wagon wagon = this;
        int length = 0;

        while (wagon.hasNextWagon()) {
            wagon = wagon.getNextWagon();
            length++;
        }

        return length;
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     *
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        if (tail == null) {
            throw new IllegalStateException("ERROR: Nothing to attach");
        } else if (this.hasNextWagon()) {
            throw new IllegalStateException("ERROR: Wagon " + this + " has next wagon " + getNextWagon() +
                    " and cannot be attached");

        } else if (tail.hasPreviousWagon()) {
            throw new IllegalStateException("ERROR: Wagon " + tail + " has previous wagon " + tail.getPreviousWagon() +
                    " and cannot be attached");
        } else {
            setNextWagon(tail);
            tail.setPreviousWagon(this);
        }
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        if (hasNextWagon()) {
            Wagon tail = getNextWagon();
            setNextWagon(null);
            tail.setPreviousWagon(null);
            return tail;
        } else {
            return null;
        }
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        if (hasPreviousWagon()) {
            Wagon front = getPreviousWagon();
            setPreviousWagon(null);
            front.setNextWagon(null);
            return front;
        } else {
            return null;
        }
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     *
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        detachFront();
        front.detachTail();
        front.attachTail(this);
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        Wagon tail = detachTail();
        Wagon front = detachFront();
        if (front != null && tail != null) {
            front.attachTail(tail);
        }
    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        if (hasNextWagon()) {
            Wagon wagon = this;
            Wagon newHead = null;
            Wagon front = getPreviousWagon();
            while (wagon != null) {
                Wagon next = wagon.getNextWagon();
                wagon.removeFromSequence();
                if (newHead != null) {
                    wagon.attachTail(newHead);
                }
                newHead = wagon;
                wagon = next;
            }
            if (front != null) {
                front.attachTail(newHead);
            }
            return newHead;
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return "[Wagon-" + getId() + ']';
    }
}
