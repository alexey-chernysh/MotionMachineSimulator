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
        this.executionState.setState(ExecutionState.EXECUTION_STATE.PAUSED);
    }

    public void resumeForwardExecution() {
        executionDirection.setForward();
        checkThreadState();
        executionState.setState(ExecutionState.EXECUTION_STATE.ON_THE_RUN);
    }

    public void resumeBackwardExecution() {
        executionDirection.setBackward();
        checkThreadState();
        executionState.setState(ExecutionState.EXECUTION_STATE.ON_THE_RUN);
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
                currentMotion.run();
                if(this.executionDirection.isForward())
                    currentMotionNum++;
                else
                    currentMotionNum--;
            }
            currentTask.reset();
        }while(true);
    }

}
