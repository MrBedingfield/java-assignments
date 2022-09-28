package nl.hva.ict.ads;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TimerTest {
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;
    protected Sorter<Archer> sorter = new ArcherSorter();
    protected ChampionSelector championSelector = new ChampionSelector(1L);

    @Test
    public void timerInsertionSort() {
        int i = 0;
        int archers = 50;
        long startTime;
        long endTime;
        long duration;
        LocalDateTime timeNow = LocalDateTime.now();
        do {
            i++;
            List<Archer> archerList1 = championSelector.enrollArchers(archers);
            archers *= 2;
            startTime = System.nanoTime();
            sorter.selInsSort(archerList1, scoringScheme);
            endTime = System.nanoTime();
            duration = (endTime - startTime);
            System.out.printf("Insertion sort Round: %d  Time: %dms Sorted archers: %d \n", i, (duration / 1000), archers);
            if (ChronoUnit.SECONDS.between(timeNow, LocalDateTime.now()) >= 20) break;
        } while (archers <= 5000000);
    }



    @Test
    public void timerQuickSort() {
            int i = 0;
            int archers = 50;
            long startTime;
            long endTime;
            long duration;
            LocalDateTime timeNow = LocalDateTime.now();
            do {
                i++;
                List<Archer> archerList1 = championSelector.enrollArchers(archers);
                archers *= 2;
                startTime = System.nanoTime();
                sorter.quickSort(archerList1, scoringScheme);
                endTime = System.nanoTime();
                duration = (endTime - startTime);
                System.out.printf("Quick Sort Round: %d Time: %dms Sorted archers: %d \n", i, (duration / 1000), archers);
                if (ChronoUnit.SECONDS.between(timeNow, LocalDateTime.now()) >= 20) break;
            } while (archers <= 5000000);
        }

    @Test
    public void timerCollectionSort() {
        int i = 0;
        int archers = 50;
        long startTime;
        long endTime;
        long duration;
        LocalDateTime timeNow = LocalDateTime.now();
        do {
            i++;
            List<Archer> archerList1 = championSelector.enrollArchers(archers);
            archers *= 2;
            startTime = System.nanoTime();
            archerList1.sort(scoringScheme);
            endTime = System.nanoTime();
            duration = (endTime - startTime);
            System.out.printf("Collection sort Sort Round: %d Time: %dms Sorted archers: %d \n", i, (duration / 1000), archers);
            if (ChronoUnit.SECONDS.between(timeNow, LocalDateTime.now()) >= 20) break;
        } while (archers <= 5000000);

    }
}
