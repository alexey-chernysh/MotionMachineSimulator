/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class UserInterface extends Thread {

    private boolean locked;
    private Thread UIThread;

    private static UserInterface ourInstance = new UserInterface();

    public static UserInterface getInstance() {
        return ourInstance;
    }

    private UserInterface() {
        locked = false;
        UIThread = new Thread(this);
        UIThread.start();
    }

    public void switch2LockedView(){
        locked = true;
    }

    public void switch2GeneralView(){
        locked = false;
    }

}
