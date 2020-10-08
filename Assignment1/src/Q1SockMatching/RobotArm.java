package Q1SockMatching;

/**
 * Executable thread which picks socks from the sock pile and adds them to the Matching Machine buffer
 */
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

            // Pick a sock from the sock pile
            Sock sock = sockPile.pickSock();

            // If pile is empty, gracefully terminate the thread
            if (sock == null) {
                System.out.println("Robot Arm " + getName() + " couldn't find a sock and has stopped!");
                exit = true;
            } else {

                // If valid sock received try to add the sock to the matching machine buffer
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
