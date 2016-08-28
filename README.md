###Java中的代理模式和保护代理（The proxy pattern and protection proxy in Java）
=========

   >    假设我们有两个员工SunA和SunB。这里的员工有两个属性，分别为名称和等级。存在经理和员工的情况下,一个员工可以设置自己名字,但他不能设置另一个员工姓名,同样一个员工不能制定自己的年级,但他可以设置另一个员工的等级。
            
 1.首先创建一个雇员类：Employee.java

    package proxypattern;
    public interface Employee {

    public String getName();

    public int getGrade();

    public void setName(String aName);

    public void setGrade(int aGrade);}
    

2\. 实现Employee类里的方法
	
	package proxypattern;

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
    }}



3\. 接着创建两个InvocationHandler。Invocationhandler实现代理的行为。
	
	public class FirstInvocationHandler implements InvocationHandler {
    Employee gEmployee;
    public FirstInvocationHandler(Employee aEmployee) {
        gEmployee = aEmployee;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException  {
        try {
            if (method.getName().startsWith("get")) {
                return method.invoke(gEmployee, args);
            } else if (method.getName().equals("setGrade")) {
                throw new IllegalAccessException();
            } else if (method.getName().startsWith("set")) {
                return method.invoke(gEmployee, args);
            }
        } catch (InvocationTargetException aEx) {
            aEx.printStackTrace();
        }

        return null;
    }}
    -------------------------------------------------------------
    	/**
 			* Created by pingL on 16/8/28.
	 	*/
	public class SecondInvocationHandler implements InvocationHandler 	{
    Employee gEmployee;

    public SecondInvocationHandler(Employee aEmployee) {
        gEmployee = aEmployee;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException {
        try {
            if (method.getName().startsWith("get")){
                return method.invoke(gEmployee,args);
            }
            else if (method.getName().equals("setName")){
                throw new IllegalAccessException();
            }
            else if (method.getName().startsWith("set")) {
                return method.invoke(gEmployee, args);
            }
        } catch (InvocationTargetException aEx) {
            aEx.printStackTrace();
        }
        return null;
    }}

4\. 获取代理对象
	
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
 <		上面有两个返回代理对象的方法,用于创建代理对象我们调用代理的静态方法newProxyInstance(),这需要作为参数,分别为类对象、类加载器、interfaces和invocationhandler。>

5\. 创建一个员工数据库
	
	public class EmployeeDB {
    HashMap<String, Employee> theEmployeeDB = new HashMap<String, Employee>();

    public EmployeeDB() {
    }

    public void addEmployee(Employee aEmployee) {
        theEmployeeDB.put(aEmployee.getName(), aEmployee);
    }

    public Employee getEmployee(String aName) {
        return theEmployeeDB.get(aName);

    }}

6\. 测试类进行验证

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
    }}

7\. 当我们运行代码，我们将可以输出下面结果：

	New name of SunA is >>SunAChanged
	SunAChangedYou can't set your grade youself,. You grade is still :1
	SunAChangedYou grade is changed by SunB. You grade is now : 5
	No one can change anyone name So First employee name is still :SunAChanged


