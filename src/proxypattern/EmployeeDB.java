package proxypattern;

import java.util.HashMap;

/**
 * Created by pingL on 16/8/28.
 */
public class EmployeeDB {
    HashMap<String, Employee> theEmployeeDB = new HashMap<String, Employee>();

    public EmployeeDB() {
    }

    public void addEmployee(Employee aEmployee) {
        theEmployeeDB.put(aEmployee.getName(), aEmployee);
    }

    public Employee getEmployee(String aName) {
        return theEmployeeDB.get(aName);

    }
}
