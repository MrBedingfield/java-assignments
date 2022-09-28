package nl.hva.ict.ads;

import java.util.Locale;

public class Archer {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;


    private final int id;         // TODO Once assigned a value is not allowed to change.
    private String firstName;
    private String lastName;
    private static int lastId = 135788;

    // TODO add instance variable(s) to track the scores per round per arrow
    private final int[][] score = new int[MAX_ROUNDS + 1][MAX_ARROWS];

    /**
     * Constructs a new instance of Archer and assigns a unique id to the instance.
     * Each new instance should be assigned a number that is 1 higher than the last one assigned.
     * The first instance created should have ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     */
    public Archer(String firstName, String lastName) {
        // TODO initialise the new archer
        //  generate and assign an new unique id
        //  initialise the scores of the archer
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = lastId++;
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        // TODO register the points into the archer's data structure for scores.
        for (int i = 0; i < points.length; i++) {
            score[round][i] = points[i];
        }
    }


    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return
     */
    public int getTotalScore() {
        // TODO calculate/get the total score that the archer has earned across all arrows of all registered rounds
        int totalScore = 0;
        for (int[] points : score) {
            for (int point : points) {
                totalScore += point;
            }
        }
        return totalScore;
    }

    public int getMisses() {
        int misses = 0;
        for (int[] points : score) {
            for (int point : points) {
                if (point == 0) {
                    misses++;
                }
            }
        }
        return misses;
    }

    /**
     * compares the scores/id of this archer with the scores/id of the other archer according to
     * the scoring scheme: highest total points -> least misses -> earliest registration
     * The archer with the lowest id has registered first
     *
     * @param other the other archer to compare against
     * @return negative number, zero or positive number according to Comparator convention
     */
    public int compareByHighestTotalScoreWithLeastMissesAndLowestId(Archer other) {
        // TODO compares the scores/id of this archer with the other archer
        //  and return the result according to Comparator conventions
        if (this.getTotalScore() != other.getTotalScore()) {
            return other.getTotalScore() - this.getTotalScore();
        } else if (this.getMisses() != other.getMisses()) {
            return this.getMisses() - other.getMisses();
        } else {
            return this.getId() - other.getId();
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // TODO provide a toSting implementation to format archers nicely

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%d (%d) %s %s", id, getTotalScore(), firstName, lastName);
    }
}
