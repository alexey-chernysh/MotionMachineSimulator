package motionmachinesimulator.Processor;

public class EjectFlag {

    private static boolean flag = false;

    public static void set(){ flag = true;}

    static boolean taskShouldBeEjected(){
        boolean result = flag;
        flag = false;
        return result;
    }

}
