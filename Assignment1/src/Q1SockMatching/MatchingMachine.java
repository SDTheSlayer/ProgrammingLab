package Q1SockMatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MatchingMachine extends Thread {
    private List<Sock> singleSocks;
    private final ShelfManager shelfManager;
    private LinkedBlockingQueue<Sock> queue;
    private Boolean exit;

    public MatchingMachine(ShelfManager shelfManager) {
        super();
        singleSocks = new ArrayList<Sock>(Collections.nCopies(5, null));
        this.shelfManager = shelfManager;
        this.queue = new LinkedBlockingQueue<Sock>(10);
        this.exit = false;
    }


    public void addSock(Sock sock) throws InterruptedException {
        this.queue.put(sock);
    }

    private void pairSock(Sock sock) {
        if (singleSocks.get(sock.getColor()) != null) {
            shelfManager.addSockPair(sock.getColor());
            System.out.println("Matched a pair of socks of color " + sock.getColor() + " with ids: " + sock.getId() + " & " + singleSocks.get(sock.getColor()).getId());
            singleSocks.set(sock.getColor(), null);
        } else {
            singleSocks.set(sock.getColor(), sock);
        }
    }


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
