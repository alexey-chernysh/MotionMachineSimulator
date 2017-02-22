package motionmachinesimulator.Views;

/**
 * Created by Sales on 22.02.2017.
 */
public class ViewsController extends Thread {
    private static ViewsController ourInstance = new ViewsController();

    public static ViewsController getInstance() {
        return ourInstance;
    }

    private ViewsController() {
        this.start();
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
