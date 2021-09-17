
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hammad Rauf
 */
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
        parser.addArgument("-l","--Logging")
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
        
        //m_i = new Main(8, 3, true);
    }

    public Main(int totalDisks, int animationSlowness, boolean doLogging ) {
        t = new Towers(totalDisks, animationSlowness, doLogging);
        Instant start = Instant.now();
        towersOfHanoi(totalDisks, t.poles.get(0), t.poles.get(2), t.poles.get(1));
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        if(doLogging)
            LOGGER.info("[TotalExecutionTime]: Hanoi Function - "+timeElapsed+" seconds.");
        //t.logPoleData();
    }

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
