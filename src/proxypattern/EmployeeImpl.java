package proxypattern;

/**
 * Created by pingL on 16/8/28.
 */
public class EmployeeImpl implements Employee {

    private String gName;
    private int gGrade;

    public EmployeeImpl() {
    }

    @Override
    public String getName() {
        return gName;
    }

    @Override
    public int getGrade() {
        return gGrade;
    }

    @Override
    public void setName(String aName) {
            gName = aName;
    }

    @Override
    public void setGrade(int aGrade) {
            gGrade = aGrade;
    }
}
