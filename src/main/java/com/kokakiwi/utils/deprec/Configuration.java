package com.kokakiwi.utils.deprec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kokakiwi.utils.java.ClassWrapper;

public class Configuration
{
    private final Map<String, Object> config        = new HashMap<String, Object>();
    private static boolean            jDomSupported = false;
    private static boolean            yamlSupported = false;
    
    static
    {
        try
        {
            Class.forName("org.jdom.Document");
            jDomSupported = true;
        }
        catch (final Throwable throwable)
        {
            
        }
        try
        {
            Class.forName("org.yaml.snakeyaml.Yaml");
            yamlSupported = true;
        }
        catch (final Throwable throwable)
        {
            
        }
    }
    
    public Configuration()
    {
        instance = this;
    }
    
    public boolean load(File file)
    {
        final String ext = file.getName().substring(
                file.getName().lastIndexOf(".") + 1);
        String type;
        if (ext.equals("yml"))
        {
            type = "yaml";
        }
        else if (ext.equals("xml"))
        {
            type = "xml";
        }
        else
        {
            type = "";
        }
        try
        {
            return load(new FileInputStream(file), type);
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean load(InputStream inputStream)
    {
        try
        {
            return load(inputStream, "");
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public boolean load(InputStream inputFile, String type) throws Exception
    {
        if (type.equalsIgnoreCase("yaml"))
        {
            // Yaml yamlParser = new Yaml();
            final ClassWrapper clazz = new ClassWrapper(
                    "org.yaml.snakeyaml.Yaml");
            final Map<String, Object> data = (Map<String, Object>) clazz
                    .invokeMethod("load", inputFile);
            if (data != null)
            {
                merge(data, config);
            }
        }
        else if (type.equalsIgnoreCase("xml"))
        {
            try
            {
                final ClassWrapper sxb = new ClassWrapper(
                        "org.jdom.input.SAXBuilder");
                final ClassWrapper document = new ClassWrapper(true,
                        sxb.invokeMethod("build",
                                new Class<?>[] { InputStream.class },
                                new Object[] { inputFile }));
                final ClassWrapper root = new ClassWrapper(true,
                        document.invokeMethod("getRootElement"));
                merge(mapXML(new IElement(root)), config);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            final Properties props = new Properties();
            
            try
            {
                props.load(inputFile);
            }
            catch (final IOException e)
            {
                e.printStackTrace();
                return false;
            }
            
            for (final Object key : props.stringPropertyNames())
            {
                final String name = key.toString();
                final String value = props.getProperty(name);
                
                config.put(name, value);
            }
        }
        
        return true;
    }
    
    private Map<String, Object> mapXML(IElement element) throws Exception
    {
        final Map<String, Object> parsed = new HashMap<String, Object>();
        
        for (final Object children : (List<?>) element.getChildren())
        {
            final ClassWrapper clazz = (ClassWrapper) children;
            final IElement child = new IElement(clazz);
            final String name = child.getName();
            Object value;
            if (child.getChildren().size() > 0)
            {
                if (child.getAttributeValue("type") != null)
                {
                    if (child.getAttributeValue("type")
                            .equalsIgnoreCase("list"))
                    {
                        value = listXML(child);
                    }
                    else
                    {
                        value = mapXML(child);
                    }
                }
                else
                {
                    value = mapXML(child);
                }
            }
            else
            {
                value = format(child.getText());
            }
            parsed.put(name, value);
        }
        
        return parsed;
    }
    
    private List<Object> listXML(IElement element) throws Exception
    {
        final List<Object> parsed = new ArrayList<Object>();
        
        for (final Object children : element.getChildren())
        {
            final IElement child = (IElement) children;
            parsed.add(format(child.getText()));
        }
        
        return parsed;
    }
    
    private Object format(String o)
    {
        Object r;
        
        if (o.equalsIgnoreCase("true"))
        {
            r = true;
        }
        else if (o.equalsIgnoreCase("false"))
        {
            r = false;
        }
        else
        {
            r = o;
        }
        
        return r;
    }
    
    @SuppressWarnings("unchecked")
    public void merge(Map<String, Object> from, Map<String, Object> to)
    {
        for (final String key : from.keySet())
        {
            if (to.get(key) == null)
            {
                to.put(key, from.get(key));
            }
            else
            {
                if (to.get(key) instanceof Map)
                {
                    merge((Map<String, Object>) from.get(key),
                            (Map<String, Object>) to.get(key));
                }
                else
                {
                    to.put(key, from.get(key));
                }
            }
        }
    }
    
    public void set(String name, Object value)
    {
        config.put(name.toLowerCase(), value);
    }
    
    public String getString(String name)
    {
        return (String) get(name);
    }
    
    @SuppressWarnings("unchecked")
    public List<Object> getList(String name)
    {
        return (List<Object>) get(name);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getStringList(String name)
    {
        return (List<String>) get(name);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getNode(String name)
    {
        return (Map<String, Object>) get(name);
    }
    
    public boolean getBoolean(String name)
    {
        return (Boolean) (get(name) == null ? false : get(name));
    }
    
    public int getInteger(String name)
    {
        return Integer.parseInt((String) get("port"));
    }
    
    @SuppressWarnings("unchecked")
    public Object get(String nodeName)
    {
        if (nodeName.contains("."))
        {
            final String[] nodes = nodeName.split("\\.");
            Object currentNode = null;
            
            for (final String node : nodes)
            {
                if (currentNode == null)
                {
                    currentNode = config.get(node);
                }
                else
                {
                    if (currentNode instanceof Map)
                    {
                        currentNode = ((Map<String, Object>) currentNode)
                                .get(node);
                    }
                    else
                    {
                        break;
                    }
                }
            }
            
            return currentNode;
        }
        else
        {
            return config.get(nodeName.toLowerCase());
        }
    }
    
    private static Configuration instance;
    
    public static Configuration getInstance()
    {
        if (instance == null)
        {
            instance = new Configuration();
        }
        return instance;
    }
}
