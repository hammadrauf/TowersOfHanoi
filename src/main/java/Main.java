/*
* COPYRIGHT: HAMMAD RAUF
* EMAIL: rauf.hammad@gmail.com
* LICENSE: MIT (USE THIS HOWEVER YOU SEE FIT.)
* DATE: 9/17/2021
* VERSION: 1.0
* 
*/

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {

    public static Main m_i;
    public Towers t;
    private static Logger LOGGER = null;
    private int slowness = 0;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOGGER = Logger.getLogger(Main.class.getName());

        ArgumentParser parser = ArgumentParsers.newFor("towersofhanoi").build()
                .description("Perform \"Towers of Hanoi\" simulation for given inputs, recursivley (default) or iteratively.");
        parser.addArgument("count_disks")
                .metavar("N")
                .type(Integer.class)
                .required(true)
                .help("number of disks to move from tower. 1 or Higher.");
        parser.addArgument("-s", "--slowness")
                .dest("slowness")
                .type(Integer.class)
                .setDefault(0)
                .help("slowness of animation. 0 or Higher. Default is 0");
        parser.addArgument("-l", "--logging")
                .dest("logging")
                .type(Boolean.class)
                .setDefault(false)
                .help("set if text ouput log messages are needed. Boolean value. Default is false.");
        parser.addArgument("-i", "--iterative")
                .dest("iterate")
                .type(Boolean.class)
                .setDefault(false)
                .help("set if iterative method is needed. Boolean value. Default is false.");
        parser.addArgument("-n", "--nobeep")
                .dest("noBeep")
                .type(Boolean.class)
                .setDefault(false)
                .help("set if silent mode (no beep) is needed. Boolean value. Default is false (There will be beeps).");
        parser.addArgument("-d", "--displayoff")
                .dest("displayOff")
                .type(Boolean.class)
                .setDefault(false)
                .help("set if no GUI display is needed. Boolean value. Default is false (There will be display). Logging is enabled if this is set.");
        try {
            Namespace res = parser.parseArgs(args);
            m_i = new Main(res.get("count_disks"), res.get("slowness"), res.get("logging"), res.get("iterate"), res.get("noBeep"), res.get("displayOff"));
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            //System.exit(0);
        }
    }

    public Main(int totalDisks, int animationSlowness, boolean doLogging, boolean iterative, boolean nobeeps, boolean displayOff) {
        this.slowness = animationSlowness;
        if(displayOff)
            doLogging = true;
        t = new Towers(totalDisks, 0, doLogging, nobeeps, displayOff);
        Instant start = Instant.now();
        if (!iterative) {
            if (doLogging) {
                LOGGER.info("[StartExecution]: Hanoi Function - Recursive.");
                LOGGER.info("[Next Significant Disk - Definition]: Top most disk that has not seen any movement.");
            }
            recursiveTowersOfHanoi(totalDisks, t.poles.get(0), t.poles.get(2), t.poles.get(1));
        } else {
            if (doLogging) {
                LOGGER.info("[StartExecution]: Hanoi Function - Iterative.");
                LOGGER.info("[Next Significant Disk - Definition]: Top most disk that has not seen any movement.");
            }
            iterativeTowersOfHanoi(totalDisks, t.poles.get(0), t.poles.get(2), t.poles.get(1));
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        if (doLogging) {
            LOGGER.info("[TotalExecutionTime]: Hanoi Function - " + timeElapsed + " seconds.");
            LOGGER.info("[TaskCompletedCheck]: Pole C: " + t.isPoleAllDiskMoved(t.poles.get(2), totalDisks));
            LOGGER.info("[TotalMoves]: Total Moves: " + t.getTotalMoves());
            LOGGER.info("[ValidMoves]: Valid Moves: " + t.getValidMoves());
            LOGGER.info("[InvalidMoves]: Invalid Moves: " + t.getInvalidMoves());
        }
    }

    /**
     * *
     * Recursive towersOfHanoi method. Learning tip: Hide/delete this method if
     * you want someone to learn to solve the classic problem.
     *
     * @param diskNo
     * @param sourcePole
     * @param destinationPole
     * @param otherPole
     */
    public void recursiveTowersOfHanoi(int diskNo, Towers.Pole sourcePole, Towers.Pole destinationPole, Towers.Pole otherPole) {
        boolean b = false;
        //int storedDisk;
        try {
            if (diskNo < 1) {
                return;
            }
            if (diskNo == 1) {
                b = t.moveSingleDisk(sourcePole, destinationPole);
                return;
            }
            if (diskNo == 2) {
                b = t.moveSingleDisk(sourcePole, otherPole);
                b = t.moveSingleDisk(sourcePole, destinationPole);
                b = t.moveSingleDisk(otherPole, destinationPole);
                return;
            }
            if (diskNo > 2) {
                //storedDisk = diskNo;
                diskNo--;
                Thread.sleep(this.slowness);
                this.recursiveTowersOfHanoi(diskNo, sourcePole, otherPole, destinationPole);
                b = t.moveSingleDisk(sourcePole, destinationPole);
                Thread.sleep(this.slowness);
                this.recursiveTowersOfHanoi(diskNo, otherPole, destinationPole, sourcePole);
                return;
            }
            return;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception occured. Check your computer speaker. Maybe number of disks/moves is too many for your hardware.", ex);
        }
        return;
    }

    /**
     * *
     * Iterative towersOfHanoi method. Learning tip: Hide/delete this method if
     * you want someone to learn to solve the classic problem.
     *
     * @param diskNo
     * @param sourcePole
     * @param destinationPole
     * @param otherPole
     */
    public void iterativeTowersOfHanoi(int diskNo, Towers.Pole sourcePole, Towers.Pole destinationPole, Towers.Pole otherPole) {
        boolean isEven = (diskNo % 2) == 0 ? true : false;
        boolean b = false;
        try {
            if (isEven) {
                do {
                    //make the legal move between pegs A and B (in either direction),
                    b = false;
                    b = t.moveSingleDisk(sourcePole, otherPole);
                    if (!b) {
                        b = t.moveSingleDisk(otherPole, sourcePole);
                    }

                    //make the legal move between pegs A and C (in either direction)
                    b = false;
                    b = t.moveSingleDisk(sourcePole, destinationPole);
                    if (t.isPoleAllDiskMoved(destinationPole, diskNo)) {
                        break;
                    }
                    if (!b) {
                        b = t.moveSingleDisk(destinationPole, sourcePole);
                    }

                    //make the legal move between pegs B and C (in either direction)
                    b = false;
                    b = t.moveSingleDisk(otherPole, destinationPole);
                    if (t.isPoleAllDiskMoved(destinationPole, diskNo)) {
                        break;
                    }
                    if (!b) {
                        b = t.moveSingleDisk(destinationPole, otherPole);
                    }
                    Thread.sleep(this.slowness);
                } while (!t.isPoleAllDiskMoved(destinationPole, diskNo));
            } else {
                do {
                    //make the legal move between pegs A and C (in either direction)
                    b = false;
                    b = t.moveSingleDisk(sourcePole, destinationPole);
                    if (t.isPoleAllDiskMoved(destinationPole, diskNo)) {
                        break;
                    }
                    if (!b) {
                        b = t.moveSingleDisk(destinationPole, sourcePole);
                    }

                    //make the legal move between pegs A and B (in either direction)
                    b = false;
                    b = t.moveSingleDisk(sourcePole, otherPole);
                    if (!b) {
                        b = t.moveSingleDisk(otherPole, sourcePole);
                    }

                    //make the legal move between pegs B and C (in either direction)
                    b = false;
                    b = t.moveSingleDisk(otherPole, destinationPole);
                    if (t.isPoleAllDiskMoved(destinationPole, diskNo)) {
                        break;
                    }
                    if (!b) {
                        b = t.moveSingleDisk(destinationPole, otherPole);
                    }
                    Thread.sleep(this.slowness);
                } while (!t.isPoleAllDiskMoved(destinationPole, diskNo));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception occured. Check your computer speaker. Maybe number of disks/moves is too many for your hardware.", ex);
        }
        return;
    }
}
