package motionmachinesimulator.Processor;

/**
 * Created by Sales on 16.02.2017.
 * Functions needed to be implemented:
 *  start/resume
 *  stop/pause
 *  direction change (forward/backward)
 *  velocity change (acceleration/deceleration)
 */

public class MotionController extends Thread {

    private final Task currentTask;
    private Thread controllerThread;
    private ExecutionDirection executionDirection;
    private ExecutionState executionState;

    private static MotionController ourInstance = new MotionController();

    public static MotionController getInstance() {
        return ourInstance;
    }
    private MotionController() {
        super();
        currentTask = new Task();
        controllerThread = new Thread(this);
        executionDirection = ExecutionDirection.getInstance();
        executionState = ExecutionState.getInstance();
        controllerThread.start();
    }

    public Task getCurrentTask() {
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
        CNCPoint2D[] startPos = new CNCPoint2D[taskSize];
        do{
            CNCMotion currentMotion;
            int currentMotionNum = 0;
            while((currentMotionNum>=0)&&(currentMotionNum<taskSize)){
                System.out.println("Debug message: CNCMotion num =  " + currentMotionNum);
                currentMotion = currentTask.get(currentMotionNum);
                if(this.executionDirection.isForward()) startPos[currentMotionNum] = CurrentPosition.getInstance().get();
                currentMotion.run(startPos[currentMotionNum]);
                if(this.executionDirection.isForward()) currentMotionNum++;
                else currentMotionNum--;
            }
            currentTask.reset();
        }while(true);
    }

}
