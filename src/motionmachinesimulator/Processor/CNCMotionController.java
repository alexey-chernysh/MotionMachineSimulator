package motionmachinesimulator.Processor;

/**
 * Created by Sales on 16.02.2017.
 * Functions needed to be implemented:
 *  start/resume
 *  stop/pause
 *  direction change (forward/backward)
 *  velocity change (acceleration/deceleration)
 */

public class CNCMotionController extends Thread {

    private final CNCTask currentTask;
    private Thread controllerThread;

    private static CNCMotionController ourInstance = new CNCMotionController();

    public static CNCMotionController getInstance() {
        return ourInstance;
    }

    private CNCMotionController() {
        super();
        currentTask = new CNCTask();
        controllerThread = new Thread(this);
        controllerThread.start();
    }

    public CNCTask getCurrentTask() {
        return currentTask;
    }

    public void pauseExecution() {
        ExecutionState.setStopped();
    }

    public void resumeForwardExecution() {
        if(ExecutionState.isPaused()){
            checkThreadState();
            ExecutionState.setForward();
            ExecutionState.setResuming();
            ExecutionState.setRunning();
        }
    }

    public void resumeBackwardExecution() {
        if(ExecutionState.isPaused()){
            checkThreadState();
            ExecutionState.setBackward();
            ExecutionState.setResuming();
            ExecutionState.setRunning();
        }
    }

    private void checkThreadState(){
        if(!controllerThread.isAlive()) {
            controllerThread = new Thread(this);
            controllerThread.start();
        }
    }

    @Override
    public void run() {
        final int taskSize = currentTask.size();
        do{
            CNCMotion currentMotion;
            int currentMotionNum = 0;
            while((currentMotionNum >= 0)&&(currentMotionNum < taskSize)){
                System.out.println("Debug message: CNCMotion num =  " + currentMotionNum);
                currentMotion = currentTask.get(currentMotionNum);
                currentMotion.prepareData();
                boolean anotherStepNeeded = true;
                do{
                    if(ExecutionState.isRunning()) {
                        if(ExecutionState.isForward())
                            anotherStepNeeded = currentMotion.goByOneNanoStepForward(1.0);
                        else
                            anotherStepNeeded = currentMotion.goByOneNanoStepBackward(1.0);
                    }

                    try {
                        Thread.sleep(ControllerSettings.intervalInMillis);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                } while (anotherStepNeeded);
                if(ExecutionState.isForward())
                    currentMotionNum++;
                else
                    currentMotionNum--;
            }
            currentTask.reset();
        }while(true);
    }

}
