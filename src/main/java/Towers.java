/*
* COPYRIGHT: HAMMAD RAUF
* EMAIL: rauf.hammad@gmail.com
* LICENSE: MIT (USE THIS HOWEVER YOU SEE FIT.)
* DATE: 9/17/2021
* VERSION: 1.0
* 
*/

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Point;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hammad Rauf
 */
public class Towers {

    private static Logger LOGGER = null;
    private boolean doLogging = false;
    private boolean noBeeps = false;
    public static Turtle bob = null;
    private final int diskHeight = 5;
    private final int smallestDiskWidth = 15;
    public final int diskGap = 10;
    public final int tableTopOffset = -300;
    public final int max_screen_width = 1280;
    public final int max_screen_height = 720;
    private Color bgColor = null;
    private Color fgColor = null;
    private ArrayList<String> allColors = new ArrayList<String>();
    private int maxDisks = 20;
    private int poleTopEmptySpace = maxDisks * 10;
    private double validMoves = 0d;
    private double invalidMoves = 0d;
    private double totalMoves = 0d;
    private boolean displayOff = false;

    public enum poleNumber {
        A, B, C
    };

    public enum poleType {
        Source, Destination, Other
    };
    public ArrayList<Pole> poles = new ArrayList<Pole>();

    public Towers(int totalDisks, int animationSlowness, boolean isLoggingEnabled, boolean beepNotNeeded, boolean displayOff) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOGGER = Logger.getLogger(Towers.class.getName());

        this.doLogging = isLoggingEnabled;
        this.noBeeps = beepNotNeeded;
        this.maxDisks = totalDisks;
        this.poleTopEmptySpace = maxDisks * 10;
        this.displayOff = displayOff;
        if (displayOff) {
            this.doLogging = true;
        }

        if (!displayOff) {
            this.bob = new Turtle(0, 0);
            bob.hide();
            bob.speed(animationSlowness);
            bgColor = bob.colors.get("white");
            fgColor = bob.colors.get("black");
            Turtle.colors.keySet().forEach(n -> allColors.add(n));
        }
        poles.add(poleNumber.A.ordinal(), new Pole(poleNumber.A, false, poleType.Source));
        poles.add(poleNumber.B.ordinal(), new Pole(poleNumber.B, true, poleType.Other));
        poles.add(poleNumber.C.ordinal(), new Pole(poleNumber.C, true, poleType.Destination));

        this.doSetup();
    }

    private void doSetup() {
        if (!displayOff) {
            this.drawBoundingBox();
            Point ppA = this.getPoleStartingPoint(poleNumber.A);
            Point ppB = this.getPoleStartingPoint(poleNumber.B);
            Point ppC = this.getPoleStartingPoint(poleNumber.C);
            this.bob.showText("A", ppA.x, ppA.y - (this.diskGap + 14), 14);
            this.bob.showText("B", ppB.x, ppB.y - (this.diskGap + 14), 14);
            this.bob.showText("C", ppC.x, ppC.y - (this.diskGap + 14), 14);
        }
        for (Pole p : poles) {
            p.renderPole();
        }
    }

    public void logPoleData() {
        for (Pole p : poles) {
            LOGGER.info("Pole: " + p.pNumber.toString());
            for (Integer i : p.getPoleStack()) {
                LOGGER.info("Disk-" + i);
            }
        }
    }

    public class Pole {

        private poleNumber pNumber;
        private LinkedList<Integer> pStack = new LinkedList<Integer>();
        private poleType pType;
        private boolean empty;

        public Pole(poleNumber p, boolean isEmpty, poleType pT) {
            this.pNumber = p;
            this.pType = pT;
            this.empty = isEmpty;
            //this.renderPole();
        }

        public poleNumber getPoleNumber() {
            return this.pNumber;
        }

        public LinkedList<Integer> getPoleStack() {
            return this.pStack;
        }

        public poleType getPoleType() {
            return this.pType;
        }

        public boolean getIsEmpty() {
            return empty;
        }

        public void renderPole() {
            if (!this.empty) {
                for (int i = 1; i <= maxDisks; i++) {
                    if (!displayOff) {
                        initialDisksDraw(this.pNumber, i);
                    }
                    pStack.push(i);
                }
            }
        }
    }

    public double getTotalMoves() {
        return this.totalMoves;
    }

    public double getValidMoves() {
        return this.validMoves;
    }

    public double getInvalidMoves() {
        return this.invalidMoves;
    }

    /**
     * *
     * Move a Single Disk from fromPole to toPole. Throws exception if Sound
     * beep (speaker) issue or memory outage. Returns true if move is
     * successful. Returns false if invalid move, also plays a beep if beep is
     * enabled.
     *
     * @param fromPole
     * @param toPole
     * @return
     * @throws Exception
     */
    public boolean moveSingleDisk(Pole fromPole, Pole toPole) throws Exception {
        boolean retValue = false;
        Integer sourceDiskNo = fromPole.getPoleStack().peek();
        Integer destDiskNo = toPole.getPoleStack().peek();
        if (sourceDiskNo != null) {
            if (destDiskNo != null) {
                if (destDiskNo.intValue() >= sourceDiskNo.intValue()) {
                    retValue = false;
                    invalidMoves++;
                    totalMoves++;
                    if (this.doLogging) {
                        LOGGER.info("[Next Significant Disk: " + this.getNextSignificantDisk() + "] " + "[Invalid Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". Disk-" + sourceDiskNo.intValue() + " is not smaller then Disk-" + destDiskNo.intValue() + ".");
                    }
                    if (!noBeeps) {
                        SoundUtils.tone(1000, 100);
                    }
                } else {
                    retValue = true;
                    validMoves++;
                    totalMoves++;
                    this.anytimeDisksErase(fromPole, fromPole.getPoleStack().peek().intValue());
                    sourceDiskNo = fromPole.getPoleStack().pop();
                    toPole.getPoleStack().push(sourceDiskNo);
                    this.anytimeDisksDraw(toPole, toPole.getPoleStack().peek().intValue());
                    if (this.doLogging) {
                        LOGGER.info("[Next Significant Disk: " + this.getNextSignificantDisk() + "] " + "[Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". Disk-" + sourceDiskNo.intValue() + " moved.");
                    }
                }
            } else {
                retValue = true;
                validMoves++;
                totalMoves++;
                this.anytimeDisksErase(fromPole, fromPole.getPoleStack().peek().intValue());
                sourceDiskNo = fromPole.getPoleStack().pop();
                toPole.getPoleStack().push(sourceDiskNo);
                this.anytimeDisksDraw(toPole, toPole.getPoleStack().peek().intValue());
                if (this.doLogging) {
                    LOGGER.info("[Next Significant Disk: " + this.getNextSignificantDisk() + "] " + "[Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". Disk-" + sourceDiskNo.intValue() + " moved.");
                }
            }
        } else {
            retValue = false;
            invalidMoves++;
            totalMoves++;
            if (this.doLogging) {
                LOGGER.info("[Next Significant Disk: " + this.getNextSignificantDisk() + "] " + "[Invalid Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". No Disk available on Pole to move.");
            }
            if (!noBeeps) {
                SoundUtils.tone(1000, 100);
            }
        }
        return retValue;
    }

    /**
     * *
     * Used to verify that the "Towers Of Hanoi" problem has been solved.
     * Returns true is all disks are present on the given pole parameter "p".
     *
     * @param p
     * @param totalDisks
     * @return
     */
    public boolean isPoleAllDiskMoved(Pole p, int totalDisks) {
        boolean retValue = false;
        if (p.pStack.size() == totalDisks) {
            int dCounter = totalDisks;
            for (int pdisk : p.pStack) {
                if (pdisk == dCounter) {
                    dCounter--;
                    if (dCounter < 1) {
                        retValue = true;
                    }
                } else {
                    retValue = false;
                    break;
                }
            }
        } else {
            retValue = false;
        }
        return retValue;
    }

    /**
     * *
     * Gets the next significant disk number that is eligible for move on the
     * Source pole. Significant disk refers to a top level disk that has so far
     * not seen any movement itself. It returns 0 if there is no significant
     * disk. Source pole should be correctly identified when creating poles.
     *
     * @return
     */
    private int getNextSignificantDisk() {
        Pole sp = null;
        for (Pole x : this.poles) {
            if (x.getPoleType() == poleType.Source) {
                sp = x;
                break;
            }
        }
        int retValue = 0;
        if (sp.getPoleStack().size() < this.maxDisks) {
            int lowerDisk = 0;
            int upperDisk = 0;
            var it = sp.getPoleStack().descendingIterator();
            while (it.hasNext()) {
                upperDisk = it.next();
                if ((upperDisk - lowerDisk) != 1) {
                    retValue = lowerDisk;
                    break;
                }
                lowerDisk = upperDisk;
            }
            retValue = lowerDisk;
        } else {
            retValue = sp.getPoleStack().peek();
        }
        return retValue;
    }

    private void drawBoundingBox() {
        if (!displayOff) {
            int width = (3 * this.getDiskWidth(1)) + (4 * diskGap);
            int height = (diskHeight * maxDisks) + (diskGap * (diskGap - 1)) + (poleTopEmptySpace * 2) + diskGap;
            this.rectangle(0, tableTopOffset - diskGap, width, height, "black");
            int n_width = ((width + width / 8) > max_screen_width) ? max_screen_width : (width + width / 8);
            int n_height = ((height + height / 4) > max_screen_height) ? max_screen_height : (height + height / 4);
            //bob.resizeWindow(n_width, n_height);
            bob.resizeWindow(max_screen_width, max_screen_height);
            Turtle.zoomFit();
        }
    }

    private Point getPoleStartingPoint(poleNumber p) {
        int x = 0;
        int y = tableTopOffset;
        int maxDiskWidth = this.getDiskWidth(1);
        switch (p) {
            case A:
                x = 0 - (maxDiskWidth + diskGap);
                y = tableTopOffset;
                break;
            case B:
                x = 0;
                y = tableTopOffset;
                break;
            case C:
                x = 0 + (maxDiskWidth + diskGap);
                y = tableTopOffset;
                break;
            default:
                x = 0;
                y = tableTopOffset;
        }
        return (new Point(x, y));
    }

    private void initialDisksDraw(poleNumber p, int disk_no) {
        if (!displayOff) {
            Point pp = this.getPoleStartingPoint(p);
            int x = pp.x;
            int y = pp.y;
            if (!(disk_no < 1) && !(disk_no > maxDisks)) {
                int disk_width = this.getDiskWidth(disk_no);
                int disk_y_offset = this.getDiskYOffset(disk_no);

                this.rectangle(x, y + disk_y_offset, disk_width, diskHeight, "black");
            }
        }
    }

    /**
     * Disk should be Pushed first on pole, before calling this.
     *
     * @param p
     * @param disk_no
     */
    private void anytimeDisksDraw(Pole p, int disk_no) {
        Point pp = this.getPoleStartingPoint(p.getPoleNumber());
        int x = pp.x;
        int y = pp.y;
        if (!(disk_no < 1) && !(disk_no > maxDisks)) {
            int disk_width = this.getDiskWidth(disk_no);
            int disk_y_offset = this.getDiskYPositinalOffsetOnPole(disk_no, p);

            this.rectangle(x, y + disk_y_offset, disk_width, diskHeight, "black");
        }
    }

    /**
     * *
     * Disk should be Popped from pole, immediately after calling this.
     *
     * @param p
     * @param disk_no
     */
    private void anytimeDisksErase(Pole p, int disk_no) {
        Point pp = this.getPoleStartingPoint(p.getPoleNumber());
        int x = pp.x;
        int y = pp.y;
        if (!(disk_no < 1) && !(disk_no > maxDisks)) {
            int disk_width = this.getDiskWidth(disk_no);
            int disk_y_offset = this.getDiskYPositinalOffsetOnPole(disk_no, p);

            this.eraseRectangle(x, y + disk_y_offset, disk_width, diskHeight);
        }
    }

    private int getDiskWidth(int disk_no) {
        int dw = 0;
        if (!(disk_no < 1) && !(disk_no > maxDisks)) {
            dw = (smallestDiskWidth * (maxDisks - (disk_no - 1)));
        }
        return dw;
    }

    private int getDiskYOffset(int disk_no) {
        int dyoff = 0;
        if (!(disk_no < 1) && !(disk_no > maxDisks)) {
            dyoff = (disk_no - 1) * (diskHeight + diskGap);
        }
        return dyoff;
    }

    private int getDiskYPositinalOffsetOnPole(int disk_no, Pole p) {
        int retValue = 0;
        int position = p.getPoleStack().size();
        retValue = this.getDiskYOffset(position);
        return retValue;
    }

    private void rectangle(double xpos, double ypos, double width, double height, String c) {
        if (!displayOff) {
            bob.up();
            bob.penColor(c);
            bob.setPosition(xpos - width / 2, ypos, 0);
            bob.down();
            bob.setPosition(xpos + width / 2, ypos, 90);
            bob.setPosition(xpos + width / 2, ypos + height, 180);
            bob.setPosition(xpos - width / 2, ypos + height, 270);
            bob.setPosition(xpos - width / 2, ypos, 0);
        }
    }

    private void eraseRectangle(double xpos, double ypos, double width, double height) {
        if (!displayOff) {
            bob.up();
            bob.penColor(this.bgColor);
            bob.setPosition(xpos - width / 2, ypos, 0);
            bob.penWidth = 3;
            bob.down();
            bob.setPosition(xpos + width / 2, ypos, 90);
            bob.setPosition(xpos + width / 2, ypos + height, 180);
            bob.setPosition(xpos - width / 2, ypos + height, 270);
            bob.setPosition(xpos - width / 2, ypos, 0);
            bob.penWidth = 2;
        }
    }

}
