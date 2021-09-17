/*
* COPYRIGHT: HAMMAD RAUF
* EMAIL: rauf.hammad@gmail.com
* LICENSE: MIT (USE THIS HOWEVER YOU SEE FIT.)
* DATE: 9/17/2021
* VERSION: 1.0
* 
* THIS SOFTWARE HAS NO WARRANTY.  IF IT WORKS, SUPER.  IF IT DOESN'T, LET ME
* KNOW AND I MIGHT OR MIGHT NOT DO SOMETHING ABOUT IT.
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

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOGGER = Logger.getLogger(Main.class.getName());
                      
        ArgumentParser parser = ArgumentParsers.newFor("towersofhanoi").build()
                .description("Perform \"Towers of Hanoi\" simulation for given inputs, recursivley.");
        parser.addArgument("count_disks")
                .metavar("N")
                .type(Integer.class)
                .required(true)
                .help("number of disks to move from tower. 1 or Higher.");
        parser.addArgument("-s","--slowness")
                .dest("slowness")
                .type(Integer.class)
                .setDefault(0)
                .help("slowness of animation. 0 or Higher. Default is 0");
        parser.addArgument("-l","--logging")
                .dest("logging")
                .type(Boolean.class)
                .setDefault(false)
                .help("set if text ouput log messages are needed. Boolean value. Default is false.");
        try {
            Namespace res = parser.parseArgs(args);
            m_i = new Main(res.get("count_disks"), res.get("slowness"), res.get("logging"));
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            //System.exit(0);
        }        
    }

    public Main(int totalDisks, int animationSlowness, boolean doLogging ) {
        t = new Towers(totalDisks, animationSlowness, doLogging);
        Instant start = Instant.now();
        towersOfHanoi(totalDisks, t.poles.get(0), t.poles.get(2), t.poles.get(1));
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        if(doLogging)
            LOGGER.info("[TotalExecutionTime]: Hanoi Function - "+timeElapsed+" seconds.");
    }

    /**
     * Recursive towersOfHanoi method. Learning tip: Hide/delete this method if you want someone to learn to solve the classic problem.
     * @param diskNo
     * @param sourcePole
     * @param destinationPole
     * @param otherPole 
     */
    public void towersOfHanoi(int diskNo, Towers.Pole sourcePole, Towers.Pole destinationPole, Towers.Pole otherPole) {
        boolean b = false;
        int storedDisk;
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
                storedDisk = diskNo;
                diskNo--;
                this.towersOfHanoi(diskNo, sourcePole, otherPole, destinationPole);
                b = t.moveSingleDisk(sourcePole, destinationPole);
                this.towersOfHanoi(diskNo, otherPole, destinationPole, sourcePole);
                return;
            }
            return;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,"Sound Beep Exception. Check your computer speaker.", ex);
        }
        return;
    }
}
