package Q1SockMatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for Storing The number of Sock pairs of different colors.
 */
public class Shelf {
    private List<Integer> numSockPairs;

    public Shelf() {
        numSockPairs = new ArrayList<Integer>(Collections.nCopies(5, 0));
    }

    /**
     * Adds a sock pair to the shelf
     * @param sockColor Color of sock
     */
    public void addSockPair(Integer sockColor) {
        numSockPairs.set(sockColor, numSockPairs.get(sockColor) + 1);
    }

    public List<Integer> getNumSockPairs() {
        return numSockPairs;
    }
}
