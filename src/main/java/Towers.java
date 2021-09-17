/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hammad Rauf
 */
public class Towers {

    private static Logger LOGGER = null;
    private boolean doLogging = false;
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

    public enum poleNumber {
        A, B, C
    };

    public enum poleType {
        Source, Destination, Other
    };
    public ArrayList<Pole> poles = new ArrayList<Pole>();

    public Towers(int totalDisks, int animationSlowness, boolean isLoggingEnabled) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOGGER = Logger.getLogger(Towers.class.getName());

        this.doLogging = isLoggingEnabled;
        this.maxDisks = totalDisks;
        this.poleTopEmptySpace = maxDisks * 10;

        this.bob = new Turtle(0, 0);
        bob.hide();
        bob.speed(animationSlowness);
        bgColor = bob.colors.get("white");
        fgColor = bob.colors.get("black");
        Turtle.colors.keySet().forEach(n -> allColors.add(n));
        poles.add(poleNumber.A.ordinal(), new Pole(poleNumber.A, false, poleType.Source));
        poles.add(poleNumber.B.ordinal(), new Pole(poleNumber.B, true, poleType.Other));
        poles.add(poleNumber.C.ordinal(), new Pole(poleNumber.C, true, poleType.Destination));

        this.doSetup();
    }

    private void doSetup() {
        this.drawBoundingBox();
        Point ppA = this.getPoleStartingPoint(poleNumber.A);
        Point ppB = this.getPoleStartingPoint(poleNumber.B);
        Point ppC = this.getPoleStartingPoint(poleNumber.C);
        this.bob.showText("A", ppA.x, ppA.y - (this.diskGap + 14), 14);
        this.bob.showText("B", ppB.x, ppB.y - (this.diskGap + 14), 14);
        this.bob.showText("C", ppC.x, ppC.y - (this.diskGap + 14), 14);

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
                    initialDisksDraw(this.pNumber, i);
                    pStack.push(i);
                }
            }
        }
    }

    public boolean moveSingleDisk(Pole fromPole, Pole toPole) throws Exception {
        boolean retValue = false;
        Integer sourceDiskNo = fromPole.getPoleStack().peek();
        Integer destDiskNo = toPole.getPoleStack().peek();
        if (sourceDiskNo != null) {
            if (destDiskNo != null) {
                if (destDiskNo.intValue() >= sourceDiskNo.intValue()) {
                    retValue = false;
                    if (this.doLogging) {
                        LOGGER.info("[Invalid Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". Disk-" + sourceDiskNo.intValue() + " is not smaller then Disk-" + destDiskNo.intValue() + ".");
                    }
                    SoundUtils.tone(1000, 100);
                } else {
                    retValue = true;
                    this.anytimeDisksErase(fromPole, fromPole.getPoleStack().peek().intValue());
                    sourceDiskNo = fromPole.getPoleStack().pop();
                    toPole.getPoleStack().push(sourceDiskNo);
                    this.anytimeDisksDraw(toPole, toPole.getPoleStack().peek().intValue());
                    if (this.doLogging) {
                        LOGGER.info("[Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". Disk-" + sourceDiskNo.intValue() + " moved.");
                    }
                }
            } else {
                retValue = true;
                this.anytimeDisksErase(fromPole, fromPole.getPoleStack().peek().intValue());
                sourceDiskNo = fromPole.getPoleStack().pop();
                toPole.getPoleStack().push(sourceDiskNo);
                this.anytimeDisksDraw(toPole, toPole.getPoleStack().peek().intValue());
                if (this.doLogging) {
                    LOGGER.info("[Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". Disk-" + sourceDiskNo.intValue() + " moved.");
                }
            }
        } else {
            retValue = false;
            if (this.doLogging) {
                LOGGER.info("[Invalid Move]: From Pole-" + fromPole.pNumber.toString() + " to Pole-" + toPole.pNumber.toString() + ". No Disk available on Pole to move.");
            }
            SoundUtils.tone(1000, 100);
        }
        return retValue;
    }

    private void drawBoundingBox() {
        int width = (3 * this.getDiskWidth(1)) + (4 * diskGap);
        int height = (diskHeight * maxDisks) + (diskGap * (diskGap - 1)) + (poleTopEmptySpace * 2) + diskGap;
        this.rectangle(0, tableTopOffset - diskGap, width, height, "black");
        int n_width = ((width + width / 8) > max_screen_width) ? max_screen_width : (width + width / 8);
        int n_height = ((height + height / 4) > max_screen_height) ? max_screen_height : (height + height / 4);
        //bob.resizeWindow(n_width, n_height);
        bob.resizeWindow(max_screen_width, max_screen_height);
        Turtle.zoomFit();
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
        Point pp = this.getPoleStartingPoint(p);
        int x = pp.x;
        int y = pp.y;
        if (!(disk_no < 1) && !(disk_no > maxDisks)) {
            int disk_width = this.getDiskWidth(disk_no);
            int disk_y_offset = this.getDiskYOffset(disk_no);

            this.rectangle(x, y + disk_y_offset, disk_width, diskHeight, "black");
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
        bob.up();
        bob.penColor(c);
        bob.setPosition(xpos - width / 2, ypos, 0);
        bob.down();
        bob.setPosition(xpos + width / 2, ypos, 90);
        bob.setPosition(xpos + width / 2, ypos + height, 180);
        bob.setPosition(xpos - width / 2, ypos + height, 270);
        bob.setPosition(xpos - width / 2, ypos, 0);
    }

    private void eraseRectangle(double xpos, double ypos, double width, double height) {
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
