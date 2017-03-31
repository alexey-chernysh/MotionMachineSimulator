package motionmachinesimulator.Processor;

/**
 * Created by Sales on 31.03.2017.
 */
public class EjectFlag {

    boolean flag = false;

    EjectFlag(){}

    void set(){ flag = true;}

    boolean taskShouldBeEjected(){
        boolean result = flag;
        flag = false;
        return result;
    }

}
