package Q1SockMatching;

public class RobotArm extends Thread {
    private final SockPile sockPile;
    private final MatchingMachine matchingMachine;
    private boolean exit;

    public RobotArm(SockPile sockPile, MatchingMachine matchingMachine, String robotName) {
        super();
        setName(robotName);
        this.sockPile = sockPile;
        exit = false;
        this.matchingMachine = matchingMachine;
    }

    @Override
    public void run() {
        while (!exit) {
            Sock sock = sockPile.pickSock();
            if (sock == null) {
                System.out.println("Robot Arm " + getName() + " couldn't find a sock and has stopped!");
                exit = true;
            } else {
                System.out.println("Robot Arm " + getName() + " has received " + sock);
                try {
                    matchingMachine.addSock(sock);
                } catch (InterruptedException e) {
                    System.out.println("Robot Arm " + getName() + " has been interrupted " + e.toString());
                }
            }
        }
    }

}
