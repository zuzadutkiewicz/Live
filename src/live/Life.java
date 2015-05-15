/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package live;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Life implements ActionListener {
    
    private JFrame frame;
    private MyCanvas mycanvas;
    private MyEvent myevent;
    private MyThread mythread;
    private JMenuBar mb;
    private JMenu m1, m2, m3;
    private JMenuItem mi1, mi2, mi3, mi4;
    private JCheckBoxMenuItem m31, m32, m33, m34;
    private int delaySec = 200;
    
    public Life() {
        frame = new JFrame("Life");
        mycanvas = new MyCanvas();
        mycanvas.setBackground(Color.WHITE);
        mb = new JMenuBar();
        m1 = new JMenu("Option");
        m2 = new JMenu("Help");
        m3 = new JMenu("Speed");
        m31 = new JCheckBoxMenuItem("25");
        m31.addActionListener(this);
        m32 = new JCheckBoxMenuItem("50");
        m32.addActionListener(this);
        m33 = new JCheckBoxMenuItem("100");
        m33.addActionListener(this);
        m34 = new JCheckBoxMenuItem("200");
        m34.addActionListener(this);
        m34.setState(true);
        
        mi1 = new JMenuItem("Start");
        mi1.setActionCommand("start");
        mi1.addActionListener(this);
        mi2 = new JMenuItem("Stop");
        mi2.setActionCommand("stop");
        mi2.setEnabled(false);
        mi2.addActionListener(this);
        mi3 = new JMenuItem("Quit");
        mi3.setActionCommand("quit");
        mi3.addActionListener(this);
        mi4 = new JMenuItem("About..");
        mi4.setActionCommand("about");
        mi4.addActionListener(this);
        
        m1.add(mi1);
        m1.add(mi2);
        m1.add(m3);
        m3.add(m31);
        m3.add(m32);
        m3.add(m33);
        m3.add(m34);
        m1.addSeparator();
        m1.add(mi3);
        m2.add(mi4);
        mb.add(m1);
        mb.add(m2);
        frame.setJMenuBar(mb);
    }
    
    public void launchFrame() {
        myevent = new MyEvent();
        frame.addWindowListener(myevent);
        frame.setSize(400, 400);
        mycanvas.setSize(400, 400);
        frame.add(mycanvas);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] argv) {
        Life life = new Life();
        life.launchFrame();
    }
    
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Button \"" + ae.getActionCommand() + "\" pressed.");
        
        if (ae.getActionCommand().equals("25")) {
            delaySec = 25;
            m31.setState(true);
            m32.setState(false);
            m33.setState(false);
            m34.setState(false);
        }
        if (ae.getActionCommand().equals("50")) {
            delaySec = 50;
            m31.setState(false);
            m32.setState(true);
            m33.setState(false);
            m34.setState(false);
        }
        if (ae.getActionCommand().equals("100")) {
            delaySec = 100;
            m31.setState(false);
            m32.setState(false);
            m33.setState(true);
            m34.setState(false);
        }
        if (ae.getActionCommand().equals("200")) {
            delaySec = 200;
            m31.setState(false);
            m32.setState(false);
            m33.setState(false);
            m34.setState(true);
        }
        
        if (ae.getActionCommand().equals("start")) {
            mi1.setEnabled(false);
            mi2.setEnabled(true);
            mythread = new MyThread();
            mythread.start();
        }
        if (ae.getActionCommand().equals("stop")) {
            mi1.setEnabled(true);
            mi2.setEnabled(false);
            if (mythread != null) {
                mythread.end();
            }
        }
        if (ae.getActionCommand().equals("quit")) {
            System.exit(0);
        }
        if (ae.getActionCommand().equals("about")) {
            JOptionPane.showMessageDialog(frame,
                    " Life - Z. Dutkiewicz - Enjoy this game \n            GNU GPL License - 2015      ",
                    "Life 2015",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    // ****** class MyEvent
    class MyEvent extends WindowAdapter {
        
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    // ****** class MyRunnable
    class MyThread extends Thread {
        
        private boolean running = true;
        
        public void end() {
            running = false;
        }
        
        public void run() {
            try {
                while (running) {
                    mycanvas.reDisplay();
                    Thread.sleep(delaySec);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    // ****** class MyCanvas
    class MyCanvas extends Canvas implements MouseListener {
        
        private MyCounter mycntr;
        
        public MyCanvas() {
            mycntr = new MyCounter();
            this.addMouseListener(this);
        }
        
        public void paint(Graphics g) {
            int iHeight = 0;
            int iWidth = 0;
            int iX = 0;
            int iY = 0;
            Rectangle rct = new Rectangle();
            rct = g.getClipBounds();
            Dimension dm = this.getSize();
            System.out.println(" dimX=" + dm.getWidth()
                    + " dimY=" + dm.getHeight());
            iWidth = (int) (dm.getWidth() / 20) * 20;
            iHeight = (int) (dm.getHeight() / 20) * 20;
            if (mycntr.getSizeX() != iWidth || mycntr.getSizeY() != iHeight) {
                mycanvas.setSize(iWidth, iHeight);
                frame.pack();
                iX = (int) (iWidth / 20);
                iY = (int) (iHeight / 20);
                mycntr.reSize(iX, iY);
            }
            g.setColor(Color.BLACK);
            reDrawAll(g);
        }
        
        private void reDrawAll(Graphics g) {
            int iHg = 0, iWd = 0;
            int iX = 0, iY = 0;
            int iHgAll = mycntr.getSizeY() * 20;
            int iWdAll = mycntr.getSizeX() * 20;
            
            for (iHg = 20; iHg < iHgAll; iHg += 20) {
                g.drawLine(0, iHg, iWdAll, iHg);
            }
            
            for (iWd = 20; iWd < iWdAll; iWd += 20) {
                g.drawLine(iWd, 0, iWd, iHgAll);
            }
            
            for (iX = 0; iX < mycntr.getSizeX(); iX++) {
                for (iY = 0; iY < mycntr.getSizeY(); iY++) {
                    if (mycntr.getCurSts(iX, iY)) {
                        int px = (int) (iX * 20) + 2;
                        int py = (int) (iY * 20) + 2;
                        g.fillOval(px, py, 17, 17);
                    }
                }
            }
        }
        
        public void setPoint(int ptx, int pty) {
            Graphics g;
            int px = (int) (ptx * 20) + 2;
            int py = (int) (pty * 20) + 2;
            
            g = this.getGraphics();
            if (mycntr.setSts(ptx, pty)) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillOval(px, py, 17, 17);
            g.setColor(Color.BLACK);
        }
        
        public void reDisplay() {
            int iX = 0;
            int iY = 0;
            Graphics g = this.getGraphics();
            
            mycntr.reCalc();
            for (iX = 0; iX < mycntr.getSizeX(); iX++) {
                for (iY = 0; iY < mycntr.getSizeY(); iY++) {
                    if (mycntr.getCurSts(iX, iY) != mycntr.getOldSts(iX, iY)) {
                        int px = (int) (iX * 20) + 2;
                        int py = (int) (iY * 20) + 2;
                        if (mycntr.getCurSts(iX, iY)) {
                            g.setColor(Color.BLACK);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillOval(px, py, 17, 17);
                    }
                }
            }
            g.setColor(Color.BLACK);
        }
        
        public void mouseClicked(MouseEvent e) {
            int ptx = (int) (e.getX() / 20);
            int pty = (int) (e.getY() / 20);
            
            mycanvas.setPoint(ptx, pty);
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
        }
        
    }

    // ****** class mycntr
    class MyCounter {
        
        private int curarr[][];
        private int oldarr[][];
        private int sizeX;
        private int sizeY;
        
        public MyCounter(int sizeX, int sizeY) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            curarr = new int[sizeX][sizeY];
        }
        
        public MyCounter() {
            this.sizeX = 0;
            this.sizeY = 0;
            curarr = null;
        }
        
        public int getSizeX() {
            return sizeX;
        }
        
        public int getSizeY() {
            return sizeY;
        }
        
        public boolean setSts(int setX, int setY) {
            if (curarr[setX][setY] == 1) {
                curarr[setX][setY] = 0;
            } else {
                curarr[setX][setY] = 1;
            }
            return (curarr[setX][setY] == 1);
        }
        
        public boolean getCurSts(int getX, int getY) {
            return (curarr[getX][getY] == 1);
        }
        
        public boolean getOldSts(int getX, int getY) {
            return (oldarr[getX][getY] == 1);
        }
        
        public void clrSize() {
            curarr = new int[sizeX][sizeY];
        }
        
        public void reSize(int sizeX, int sizeY) {
            int pomarr[][] = new int[sizeX][sizeY];
            int sizeXMin = Math.min(this.sizeX, sizeX);
            int sizeYMin = Math.min(this.sizeY, sizeY);
            
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            
            for (int sizeXp = 0; sizeXp < sizeXMin; sizeXp++) {
                for (int sizeYp = 0; sizeYp < sizeYMin; sizeYp++) {
                    pomarr[sizeXp][sizeYp] = curarr[sizeXp][sizeYp];
                }
            }
            curarr = pomarr;
        }
        
        public void reCalc() {
            int newarr[][] = new int[this.sizeX][this.sizeY];
            int lbPom;
            int x, y, nXp, nYp, suma;
            
            for (x = 0; x < sizeX; x++) {
                for (y = 0; y < sizeY; y++) {
                    if ((x - 1) > 0) {
                        nXp = (x - 1) % sizeX;
                    } else {
                        nXp = (sizeX + (x - 1)) % sizeX;
                    }
                    if ((y - 1) > 0) {
                        nYp = (y - 1) % sizeY;
                    } else {
                        nYp = (sizeY + (y - 1)) % sizeY;
                    }
                    
                    suma = curarr[x][(y + 1) % sizeY] + curarr[x][nYp]
                            + curarr[nXp][y] + curarr[(x + 1) % sizeX][y]
                            + curarr[nXp][(y + 1) % sizeY] + curarr[(x + 1) % sizeX][(y + 1) % sizeY]
                            + curarr[(x + 1) % sizeX][nYp] + curarr[nXp][nYp];
                    
                    newarr[x][y] = curarr[x][y];
                    if (curarr[x][y] == 1) {
                        if (suma < 2 || suma > 3) {
                            newarr[x][y] = 0;
                        }
                    } else {
                        if (suma == 3) {
                            newarr[x][y] = 1;
                        }
                    }
                }
            }
            oldarr = curarr;
            curarr = newarr;
        }
    }
}
