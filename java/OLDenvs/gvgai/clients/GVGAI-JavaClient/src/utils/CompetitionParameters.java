package utils;


public class CompetitionParameters
{
    /**
     * Indicates if the OS is Windows.
     */
    public static final boolean OS_WIN = System.getProperty("os.name").contains("Windows");

    /*
     * Milliseconds allowed for controller's constructor.
     */
    public static final int START_TIME = 1000;

    /**
     * Milliseconds allowed for controller initialization (per game played).
     */
    public static final int INITIALIZATION_TIME = 1000;


    /**
     * Milliseconds allowed per controller action.
     */
    public static final int ACTION_TIME = Integer.MAX_VALUE;

    /**
     * Milliseconds for controller disqualification, if it returns an action after this time.
     */
    public static int ACTION_TIME_DISQ = Integer.MAX_VALUE;


    private static final int MILLIS_IN_MIN = 60*1000;

    /**
     * Maximum time allowed for a learning track game, equivalent of 5 minutes in milliseconds.
     */

    public static final int TOTAL_LEARNING_TIME = Integer.MAX_VALUE; //10*MILLIS_IN_MIN

    /**
     * Extra second for learning time, used in case the last game finished after TOTAL_LEARNING_TIME.
     */
    public static final int EXTRA_LEARNING_TIME = Integer.MAX_VALUE; //1 second.

    /**
     * Use sockets for Learning track connection?
     */
    public static final boolean USE_SOCKETS = true;

    /**
     * Milliseconds allowed per controller action.
     */
    public static final int SOCKET_PORT = 8080;//3000;

    public static String SCREENSHOT_FILENAME = "gameStateByBytes.png";
}
