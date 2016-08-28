import proxypattern.*;

import java.lang.reflect.Proxy;

/**
 * Created by pingL on 16/8/28.
 */
public class TestProxyPattern {

    EmployeeDB employeeDB = new EmployeeDB();

    public TestProxyPattern() {
        Employee aEmp = new EmployeeImpl();
        aEmp.setName("SunA");
        aEmp.setGrade(1);

        Employee bEmp = new EmployeeImpl();
        bEmp.setName("SunB");
        bEmp.setGrade(2);

        employeeDB.addEmployee(aEmp);
        employeeDB.addEmployee(bEmp);
    }

    Employee getFirstProxy(Employee aEmployee) {
        return (Employee) Proxy.newProxyInstance(
                aEmployee.getClass().getClassLoader(),
                aEmployee.getClass().getInterfaces(),
                new FirstInvocationHandler(aEmployee));
    }

    Employee getSecondProxy(Employee bEmployee) {
        return (Employee) Proxy.newProxyInstance(
                bEmployee.getClass().getClassLoader(),
                bEmployee.getClass().getInterfaces(),
                new SecondInvocationHandler(bEmployee));
    }

    public void runExample() {
        Employee aFirstEmp = employeeDB.getEmployee("SunA");
        Employee aFirstEmpProxy = getFirstProxy(aFirstEmp);
        aFirstEmpProxy.setName("SunAChanged");
        System.out.println("New name of SunA is >>" + aFirstEmp.getName());

        try {
            aFirstEmpProxy.setGrade(10);
        } catch (Exception theEx) {
            System.out.println(aFirstEmp.getName() + "You can't set your grade youself," +
                    ". You grade is still :" + aFirstEmp.getGrade());
        }

        Employee aSecondEmp = employeeDB.getEmployee("SunB");
        Employee aSecondEmpProxy = getSecondProxy(aFirstEmp);
        aSecondEmpProxy.setGrade(5);
        System.out.println(aFirstEmp.getName() + "You grade is changed by " +
                aSecondEmp.getName() + ". You grade is now : " + aFirstEmp.getGrade());

        try {
            aSecondEmpProxy.setName("SunA_changed_by_SunB");
        } catch (Exception theEx) {
            System.out.println("No one can change anyone name " +
                    "So First employee name is still :" + aFirstEmp.getName());
        }
    }

    public static void main(String[] args) {
        TestProxyPattern testProxyPattern = new TestProxyPattern();
        testProxyPattern.runExample();
    }



}
