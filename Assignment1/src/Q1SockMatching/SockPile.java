package Q1SockMatching;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SockPile {
    private List<Sock> sockList;
    private final Object monitor;
    private final Random rand;

    SockPile() {
        sockList = new ArrayList<Sock>();
        monitor = new Object();
        rand = new Random();
    }

    public void addSock(Sock sock) {
        synchronized (monitor) {
            sockList.add(sock);
        }
    }

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
