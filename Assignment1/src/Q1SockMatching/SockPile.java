package Q1SockMatching;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class symbolising a pile of socks, has methods for adds and randomly removing socks
 */
public class SockPile {
    /**
     * List of all socks in the pile
     */
    private List<Sock> sockList;

    /**
     * Monitor for ensuring synchronisation
     */
    private final Object monitor;
    private final Random rand;

    SockPile() {
        sockList = new ArrayList<Sock>();
        monitor = new Object();
        rand = new Random();
    }

    /**
     * Adding Socks to the pile in synchronised manner
     */
    public void addSock(Sock sock) {
        synchronized (monitor) {
            sockList.add(sock);
        }
    }

    /**
     * Pick a random sock from the List and delete it.
     * A complete lock on the Sock Pile is needed since we are deleting elements from the List.
     */
    public Sock pickSock() {
        synchronized (monitor) {
            if (sockList.isEmpty()) {
                return null;
            }
            int num = rand.nextInt(sockList.size());
            Sock sock = sockList.get(num);
            sockList.remove(num);
            return sock;
        }
    }

    public boolean isEmpty() {
        return sockList.isEmpty();
    }

    public void displaySocks() {
        System.out.print("Socks in the Sock Pile: ");
        synchronized (monitor) {
            for (Sock i : sockList) {
                System.out.print(i + " ");
            }
            System.out.println("");
        }
    }
}
