package Q1SockMatching;

public class ShelfManager {
    private final Shelf shelf;

    public ShelfManager() {
        this.shelf = new Shelf();
    }

    public void addSockPair(Integer sock) {
        synchronized (shelf) {
            shelf.addSockPair(sock);
        }
    }

    public void displayShelf() {
        synchronized (shelf) {
            System.out.println("The num of Sock Pairs Currently in the shelf are: ");
            System.out.println(String.format("White: %d", shelf.getNumSockPairs().get(Colors.WHITE)));
            System.out.println(String.format("Black: %d", shelf.getNumSockPairs().get(Colors.BLACK)));
            System.out.println(String.format("Blue: %d", shelf.getNumSockPairs().get(Colors.BLUE)));
            System.out.println(String.format("Grey: %d", shelf.getNumSockPairs().get(Colors.GREY)));
        }
    }

}
