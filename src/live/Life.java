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
    private MyKeyListener mykeylistener;
    private MyRunnable myrunnable;
    private JMenuBar mb;
    private JMenu m1, m2, m3;
    private JMenuItem mi1, mi2, mi3, mi4;
    private JCheckBoxMenuItem m31, m32, m33, m34;

    public Life() {
        frame = new JFrame("Life");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mycanvas = new MyCanvas();
        mycanvas.setSize(400, 400);
        mycanvas.setBackground(Color.WHITE);
        myrunnable = new MyRunnable(200, (Canvas) mycanvas);
        mykeylistener = new MyKeyListener();
        frame.addKeyListener(mykeylistener);
        mycanvas.addKeyListener(mykeylistener);
        frame.add(mycanvas);
       
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

    public static void main(String[] argv) {
        Life life = (new Life());
        life.launchFrame();
    }

    public void launchFrame() {
         frame.pack();
         frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        System.out.println("Button \"" + ae.getActionCommand() + "\" pressed.");
        
        // ustawienie szybkosci poruszania sie
        if (       ae.getActionCommand().equals("25")
                || ae.getActionCommand().equals("50")
                || ae.getActionCommand().equals("100")
                || ae.getActionCommand().equals("200")) {
            
            int dS = Integer.parseInt(ae.getActionCommand());
            myrunnable.setDelaySec(dS);
            if(dS == 25)  m31.setState(true); else m31.setState(false);
            if(dS == 50)  m32.setState(true); else m32.setState(false);
            if(dS == 100) m33.setState(true); else m33.setState(false);
            if(dS == 200) m34.setState(true); else m34.setState(false);
        }

        if (ae.getActionCommand().equals("start")) {
            mi1.setEnabled(false);
            mi2.setEnabled(true);
            (new Thread(myrunnable)).start();
        }

        if (ae.getActionCommand().equals("stop")) {
            mi1.setEnabled(true);
            mi2.setEnabled(false);
            myrunnable.end();
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

    class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (' ' == e.getKeyChar()) {
                (new Thread(new MyRunnable(0, mycanvas))).start();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    // ****** class MyRunnable
    class MyRunnable implements Runnable {

        private volatile boolean running;
        private volatile int delaySec;
        private volatile Canvas canvas;

        public MyRunnable(int delaySec, Canvas canvas) {
            this.running = true;
            this.delaySec = delaySec;
            this.canvas = canvas;
        }

        public void end() {
            this.running = false;
        }

        public void setDelaySec(int delaySec) {
            this.delaySec = delaySec;
        }

        @Override
        public void run() {
            try {
                running = true;
                if (delaySec > 0) {
                    while (running) {
                        mycanvas.reDisplay();
                        Thread.sleep(delaySec);
                    }
                } else {
                    mycanvas.reDisplay();
                }
            } catch (InterruptedException e) {
                System.out.println("MyThread.run.InterruptedException:" + e.getMessage());
            }
        }
    }

    // ****** class MyCanvas
    class MyCanvas extends Canvas {

        private MyCounter mycntr;

        public MyCanvas() {
            mycntr = new MyCounter();
            this.addMouseListener(new MyMouseListener());
        }

        @Override
        public void paint(Graphics g) {
            int iHeight;
            int iWidth;
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
                mycntr.reSize((int) (iWidth / 20), (int) (iHeight / 20));
            }
            g.setColor(Color.BLACK);
            reDrawAll(g);
        }

        private void reDrawAll(Graphics g) {
            int iHgAll = mycntr.getSizeY() * 20;
            int iWdAll = mycntr.getSizeX() * 20;

            for (int iHg = 20; iHg < iHgAll; iHg += 20) {
                g.drawLine(0, iHg, iWdAll, iHg);
            }

            for (int iWd = 20; iWd < iWdAll; iWd += 20) {
                g.drawLine(iWd, 0, iWd, iHgAll);
            }

            for (int iX = 0; iX < mycntr.getSizeX(); iX++) {
                for (int iY = 0; iY < mycntr.getSizeY(); iY++) {
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

        public synchronized void reDisplay() {

            Graphics g = this.getGraphics();

            mycntr.reCalc();
            for (int iX = 0; iX < mycntr.getSizeX(); iX++) {
                for (int iY = 0; iY < mycntr.getSizeY(); iY++) {
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

        class MyMouseListener implements MouseListener {

            @Override
            public void mouseClicked(MouseEvent e) {
                int ptx = (int) (e.getX() / 20);
                int pty = (int) (e.getY() / 20);
                mycanvas.setPoint(ptx, pty);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
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
            int pomcur[][] = new int[sizeX][sizeY];
            int pomold[][] = new int[sizeX][sizeY];
            int sizeXMin = Math.min(this.sizeX, sizeX);
            int sizeYMin = Math.min(this.sizeY, sizeY);

            this.sizeX = sizeX;
            this.sizeY = sizeY;

            for (int sizeXp = 0; sizeXp < sizeXMin; sizeXp++) {
                System.arraycopy(curarr[sizeXp], 0, pomcur[sizeXp], 0, sizeYMin);
                System.arraycopy(oldarr[sizeXp], 0, pomold[sizeXp], 0, sizeYMin);
            }
            
            curarr = pomcur;
            oldarr = pomold;
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
