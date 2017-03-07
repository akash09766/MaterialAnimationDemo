package avantari.com.materilaanimationtask.model;

/**
 * Created by Akash Wangalwar on 26-01-2017.
 */
public class ChartData {

    public ChartData(String xVal, String yVal) {
        this.xVal = xVal;
        this.yVal = yVal;
    }

    public String getxVal() {
        return xVal;
    }

    public String getyVal() {
        return yVal;
    }

    private String xVal;
    private String yVal;
}
