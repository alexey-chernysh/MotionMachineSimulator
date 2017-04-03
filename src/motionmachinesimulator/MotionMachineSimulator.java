/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator;

import motionmachinesimulator.Processor.ControllerSettings;
import motionmachinesimulator.Processor.CurrentPosition;
import motionmachinesimulator.Processor.EjectFlag;
import motionmachinesimulator.Processor.MotionController;
import motionmachinesimulator.Views.TrajectoryView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author alexey
 */
public class MotionMachineSimulator extends JDialog implements ActionListener {
    private JPanel contentPane;
    private JPanel buttonsPane;
    private JButton buttonStart;
    private JButton buttonStop;
    private JButton buttonRewind;
    private JButton buttonVelocityUp;
    private JButton buttonVelocityDown;
    private JPanel viewPane;
    private JPanel motionPane;
    private JPanel positionPane;
    private JPanel InfoPanel;
    private JLabel velocityHeader;
    private JTextField textVelocity;
    private JTextField textPositionX;
    private JTextField textPositionY;
    private JLabel PositionX;
    private JLabel PositionY;
    private JButton buttonEject;

    Timer timer;

    public MotionMachineSimulator() {
        $$$setupUI$$$();

        timer = new Timer(100, this);
        timer.setInitialDelay(100);
        timer.start();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonStart);

        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MotionController.getInstance().resumeForwardExecution();
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MotionController.getInstance().pauseExecution();
            }
        });
        buttonRewind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MotionController.getInstance().resumeBackwardExecution();
            }
        });
        buttonVelocityUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MotionController.getInstance().velocityUp();
            }
        });
        buttonVelocityDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MotionController.getInstance().velocityDown();
            }
        });
        buttonEject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EjectFlag.set();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void createUIComponents() {
        positionPane = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                TrajectoryView.paint(g);
            }

            ;
        };
        motionPane = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                int width = getWidth();
                int height = getHeight();
                g.setColor(new Color(255, 0, 0));
                g.drawLine(0, 0, width, height);
            }
        };
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {

        MotionMachineSimulator dialog = new MotionMachineSimulator();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void displayVelocity(int value) {
        this.textVelocity.setText((new Integer(value)).toString());
    }

    public void displayPositionX(double value) {
        this.textPositionX.setText((new Double(value)).toString());
    }

    public void displayPositionY(double value) {
        this.textPositionY.setText((new Double(value)).toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int currentVelocity = (int) ControllerSettings.getWorkingVelocityMMinMin();
        this.displayVelocity(currentVelocity);
        double[] position = CurrentPosition.get();
        int x = (int) (position[0] * 1000000.0);
        int y = (int) (position[1] * 1000000.0);
        this.displayPositionX(x / 1000.0);
        this.displayPositionY(y / 1000.0);
        repaint();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        buttonsPane = new JPanel();
        buttonsPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(buttonsPane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        buttonsPane.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        buttonsPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonStart = new JButton();
        buttonStart.setBackground(new Color(-16711936));
        buttonStart.setForeground(new Color(-1));
        buttonStart.setText("Start");
        panel1.add(buttonStart, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRewind = new JButton();
        buttonRewind.setBackground(new Color(-16776961));
        buttonRewind.setForeground(new Color(-1));
        buttonRewind.setText("Rewind");
        panel1.add(buttonRewind, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonVelocityUp = new JButton();
        buttonVelocityUp.setBackground(new Color(-16777088));
        buttonVelocityUp.setForeground(new Color(-1));
        buttonVelocityUp.setText("Faster");
        panel1.add(buttonVelocityUp, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonVelocityDown = new JButton();
        buttonVelocityDown.setBackground(new Color(-16777088));
        buttonVelocityDown.setForeground(new Color(-1));
        buttonVelocityDown.setText("Slower");
        panel1.add(buttonVelocityDown, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonStop = new JButton();
        buttonStop.setBackground(new Color(-65536));
        buttonStop.setForeground(new Color(-1));
        buttonStop.setText("Stop");
        panel1.add(buttonStop, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEject = new JButton();
        buttonEject.setBackground(new Color(-65536));
        buttonEject.setForeground(new Color(-1));
        buttonEject.setText("Button");
        panel1.add(buttonEject, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewPane = new JPanel();
        viewPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(viewPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        motionPane.setBackground(new Color(-1));
        viewPane.add(motionPane, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(600, 200), null, null, 1, false));
        motionPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
        positionPane.setBackground(new Color(-1));
        viewPane.add(positionPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, 200), null, null, 1, false));
        positionPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
        InfoPanel = new JPanel();
        InfoPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        viewPane.add(InfoPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        velocityHeader = new JLabel();
        velocityHeader.setText("Vel");
        InfoPanel.add(velocityHeader, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, -1), null, 0, false));
        PositionX = new JLabel();
        PositionX.setText("X, mm");
        InfoPanel.add(PositionX, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        PositionY = new JLabel();
        PositionY.setText("Y, mm");
        InfoPanel.add(PositionY, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textPositionY = new JTextField();
        textPositionY.setEditable(false);
        textPositionY.setText("");
        InfoPanel.add(textPositionY, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0, false));
        textPositionX = new JTextField();
        textPositionX.setEditable(false);
        textPositionX.setText("");
        InfoPanel.add(textPositionX, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0, false));
        textVelocity = new JTextField();
        textVelocity.setEditable(false);
        textVelocity.setHorizontalAlignment(0);
        textVelocity.setText("");
        InfoPanel.add(textVelocity, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}

