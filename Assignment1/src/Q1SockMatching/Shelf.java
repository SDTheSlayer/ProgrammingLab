package Q1SockMatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shelf {
    private List<Integer> numSockPairs;

    public Shelf() {
        numSockPairs = new ArrayList<Integer>(Collections.nCopies(5, 0));
    }

    public void addSockPair(Integer sock) {
        numSockPairs.set(sock, numSockPairs.get(sock) + 1);
    }

    public List<Integer> getNumSockPairs() {
        return numSockPairs;
    }
}
