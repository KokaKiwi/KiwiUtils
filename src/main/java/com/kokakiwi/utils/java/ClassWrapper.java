package com.kokakiwi.utils.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassWrapper
{
    private Class<?> clazz;
    private Object   object;
    
    public ClassWrapper(String className, Object... args)
    {
        try
        {
            clazz = Class.forName(className);
            final Class<?>[] argsClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++)
            {
                argsClasses[i] = args[i].getClass();
            }
            final Constructor<?> constructor = clazz
                    .getDeclaredConstructor(argsClasses);
            object = constructor.newInstance(args);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public ClassWrapper(boolean a, Object obj)
    {
        object = obj;
        clazz = obj.getClass();
    }
    
    public Method getMethod(String name, Object... args) throws Exception
    {
        final Class<?>[] argsClasses = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++)
        {
            argsClasses[i] = args[i].getClass();
        }
        final Method method = clazz.getDeclaredMethod(name, argsClasses);
        
        return method;
    }
    
    public Method getMethod(String name, Class<?>[] argsClasses, Object[] args)
            throws Exception
    {
        final Method method = clazz.getDeclaredMethod(name, argsClasses);
        
        return method;
    }
    
    public Object invokeMethod(String name, Object... args) throws Exception
    {
        final Method method = getMethod(name, args);
        
        if (method != null)
        {
            return method.invoke(object, args);
        }
        
        return null;
    }
    
    public Object invokeMethod(String name, Class<?>[] argsClasses,
            Object[] args) throws Exception
    {
        final Method method = getMethod(name, argsClasses, args);
        
        if (method != null)
        {
            return method.invoke(object, args);
        }
        
        return null;
    }
    
    public Class<?> getClazz()
    {
        return clazz;
    }
    
    public Object getObject()
    {
        return object;
    }
}
