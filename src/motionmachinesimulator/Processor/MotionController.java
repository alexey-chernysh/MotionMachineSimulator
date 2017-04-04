package motionmachinesimulator.Processor;

/**
 * Created by Sales on 16.02.2017.
 * Functions needed to be implemented:
 *  start/resume
 *  stop/pause
 *  direction change (forward/backward)
 *  velocity change (acceleration/deceleration)
 */

public class MotionController extends ControllerState {

    private Task currentTask;
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

    public void pauseExecution() {
        this.currentTask.setState(Task.TASK_STATE.PAUSED);
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public boolean isForwardDirection() {
        return this.forwardDirection;
    }

    public void velocityUp() {
        double tmpVelocity = ControllerSettings.getWorkingVelocity();
        ControllerSettings.setWorkingVelocity(tmpVelocity * 1.1);
    }
    public void velocityDown() {
        double tmpVelocity = ControllerSettings.getWorkingVelocity();
        ControllerSettings.setWorkingVelocity(tmpVelocity * 0.9);
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
                System.out.println(" Motion num =  " + currentMotionNum);
                currentMotion = currentTask.get(currentMotionNum);
                if(this.forwardDirection) startPos[currentMotionNum] = CurrentPosition.get();
                currentMotion.run(startPos[currentMotionNum]);
                if(this.forwardDirection) {
                    currentMotion.currentWayLength = currentMotion.wayLength;
                    currentMotionNum++;
                } else {
                    currentMotion.currentWayLength = 0.0;
                    currentMotionNum--;
                }
            }
            this.currentTask.setState(Task.TASK_STATE.READY_TO_START);
            currentTask.reset();
        }while(true);
    }

}
