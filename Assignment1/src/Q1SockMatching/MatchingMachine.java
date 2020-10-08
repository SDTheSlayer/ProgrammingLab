package Q1SockMatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  Executable thread for matching socks given in its buffer and sending them to shelf manager
 */
public class MatchingMachine extends Thread {

    /**
     * Maintains a list of all the unmatched socks
     */
    private List<Sock> singleSocks;
    private final ShelfManager shelfManager;

    /**
     * Has a blocking queue of fixed size to simulate having a limited buffer for the robot arms to put socks in
     */
    private LinkedBlockingQueue<Sock> queue;
    private Boolean exit;

    public MatchingMachine(ShelfManager shelfManager) {
        super();
        singleSocks = new ArrayList<Sock>(Collections.nCopies(5, null));
        this.shelfManager = shelfManager;
        this.queue = new LinkedBlockingQueue<Sock>(10);
        this.exit = false;
    }

    /**
     * Called by Robot arm for attempting to hand a sock over to the matching machine.
      * @param sock to be matched
     * @throws InterruptedException if the robot is interupted while waiting for space to free up in the buffer
     */
    public void addSock(Sock sock) throws InterruptedException {
        this.queue.put(sock);
    }

    /**
     * Searches for an unmatched sock of the provided color. If matched displays the ids and sends to Shelf manager.
     * Else puts it in unmatched socks list.
     */
    private void pairSock(Sock sock) {
        if (singleSocks.get(sock.getColor()) != null) {
            shelfManager.addSockPair(sock.getColor());
            System.out.println("Matched a pair of socks of color " + sock.getColor() + " with ids: " + sock.getId() + " & " + singleSocks.get(sock.getColor()).getId());
            singleSocks.set(sock.getColor(), null);
        } else {
            singleSocks.set(sock.getColor(), sock);
        }
    }


    /**
     * The thread repeatedly polls if there are any socks in the buffered queue, exits after processing all the socks
     * and receiving shutdown signal.
     */
    @Override
    public void run() {
        while (!exit || !queue.isEmpty()) {
            Sock curSock;
            curSock = queue.poll();
            if (curSock != null) {
                this.pairSock(curSock);
            }
        }
    }

    public void shutdown() {
        this.exit = true;
    }
}
