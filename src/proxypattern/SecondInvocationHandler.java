package proxypattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by pingL on 16/8/28.
 */
public class SecondInvocationHandler implements InvocationHandler {
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
    }
}
