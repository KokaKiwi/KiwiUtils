package com.kokakiwi.utils.deprec;

import java.util.ArrayList;
import java.util.List;

import com.kokakiwi.utils.java.ClassWrapper;

public class IElement
{
    private final ClassWrapper clazz;
    
    public IElement(ClassWrapper clazz)
    {
        this.clazz = clazz;
    }
    
    public List<ClassWrapper> getChildren() throws Exception
    {
        final List<ClassWrapper> list = new ArrayList<ClassWrapper>();
        final List<?> elList = (List<?>) clazz.invokeMethod("getChildren");
        for (final Object o : elList)
        {
            list.add(new ClassWrapper(true, o));
        }
        
        return list;
    }
    
    public String getName() throws Exception
    {
        return (String) clazz.invokeMethod("getName");
    }
    
    public String getAttributeValue(String name) throws Exception
    {
        return (String) clazz.invokeMethod("getAttributeValue", name);
    }
    
    public String getText() throws Exception
    {
        return (String) clazz.invokeMethod("getText");
    }
    
}
