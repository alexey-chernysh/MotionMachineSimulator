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
    private boolean forwardDirection = true;

    private static MotionController ourInstance = new MotionController();

    public static MotionController getInstance() {
        return ourInstance;
    }
    private MotionController() {
        super();
        currentTask = new Task();
        controllerThread = new Thread(this);
        controllerThread.start();
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void velocityUp() {
        ControllerSettings.setWorkingVelocity(ControllerSettings.getWorkingVelocity() * 1.05);
    }
    public void velocityDown() {
        ControllerSettings.setWorkingVelocity(ControllerSettings.getWorkingVelocity() * 0.95);
    }

    public void pauseExecution() {
        this.currentTask.setState(Task.TASK_STATE.PAUSED);
    }
    public void resumeForwardExecution() {
        forwardDirection = true;
        checkThreadState();
        this.currentTask.setState(Task.TASK_STATE.ON_THE_RUN);
    }
    public void resumeBackwardExecution() {
        forwardDirection = false;
        checkThreadState();
        this.currentTask.setState(Task.TASK_STATE.ON_THE_RUN);
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
        double[][] startPos = new double[taskSize][ControllerSettings.DIM];
        do{
            Motion currentMotion;
            int currentMotionNum = 0;
            while((currentMotionNum>=0)&&(currentMotionNum<taskSize)){
                System.out.println("Debug message: Motion num =  " + currentMotionNum);
                currentMotion = currentTask.get(currentMotionNum);
                if(this.forwardDirection) startPos[currentMotionNum] = CurrentPosition.get();
                currentMotion.run(startPos[currentMotionNum]);
                if(this.forwardDirection) currentMotionNum++;
                else currentMotionNum--;
            }
            currentTask.reset();
        }while(true);
    }

    public boolean isForwardDirection() {
        return this.forwardDirection;
    }

}
