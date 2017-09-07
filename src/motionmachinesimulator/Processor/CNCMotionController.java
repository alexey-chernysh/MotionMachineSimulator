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
    private ExecutionDirection executionDirection;
    private ExecutionState executionState;

    private static CNCMotionController ourInstance = new CNCMotionController();

    public static CNCMotionController getInstance() {
        return ourInstance;
    }

    private CNCMotionController() {
        super();
        currentTask = new CNCTask();
        controllerThread = new Thread(this);
        executionDirection = ExecutionDirection.getInstance();
        executionState = ExecutionState.getInstance();
        controllerThread.start();
    }

    public CNCTask getCurrentTask() {
        return currentTask;
    }

    public void pauseExecution() {
        this.executionState.setStopped();
    }

    public void resumeForwardExecution() {
        if(this.executionState.isPaused()){
            executionDirection.setForward();
            checkThreadState();
            executionState.setRunning();
        }
    }

    public void resumeBackwardExecution() {
        if(this.executionState.isPaused()){
            executionDirection.setBackward();
            checkThreadState();
            executionState.setRunning();
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
                    if(executionState.isRunning()) {
                        if(ExecutionDirection.isForward())
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
                if(this.executionDirection.isForward())
                    currentMotionNum++;
                else
                    currentMotionNum--;
            }
            currentTask.reset();
        }while(true);
    }

}
