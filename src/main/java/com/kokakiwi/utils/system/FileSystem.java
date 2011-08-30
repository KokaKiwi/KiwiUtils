package com.kokakiwi.utils.system;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileSystem
{
    private final Map<String, Path> paths = new HashMap<String, Path>();
    
    public FileSystem()
    {
        instance = this;
    }
    
    public void set(String name, String path)
    {
        paths.put(name, new Path(path));
        check(name);
    }
    
    public void set(String name, File file)
    {
        paths.put(name, new Path(file));
        check(name);
    }
    
    public Path get(String name)
    {
        return paths.get(name);
    }
    
    public String getPath(String name)
    {
        return paths.get(name).getPath();
    }
    
    public String getResolvedPath(String name)
    {
        return paths.get(name).getResolvedPath();
    }
    
    public String resolvePath(String path)
    {
        String finalPath = path;
        for (final String key : paths.keySet())
        {
            String replace = paths.get(key).getPath();
            if (!replace.equalsIgnoreCase(path))
            {
                if (replace.contains("$"))
                {
                    replace = resolvePath(replace);
                }
                
                finalPath = finalPath.replaceAll("\\$\\{" + key + "\\}",
                        replace);
            }
        }
        
        return finalPath;
    }
    
    public void checkPaths()
    {
        for (final String key : paths.keySet())
        {
            check(key);
        }
    }
    
    public void check(String name)
    {
        final Path path = paths.get(name);
        if (path != null)
        {
            if (!path.getFile().exists())
            {
                path.getFile().mkdirs();
            }
        }
    }
    
    public void checkPath(String path)
    {
        final String resolvedPath = resolvePath(path);
        final File file = new File(resolvedPath);
        if (!file.exists())
        {
            file.mkdirs();
        }
    }
    
    private static FileSystem instance = null;
    
    public static FileSystem getInstance()
    {
        if (instance == null)
        {
            instance = new FileSystem();
        }
        return instance;
    }
    
    public class Path
    {
        private String path = null;
        
        public Path(String path)
        {
            this.path = path;
        }
        
        public Path(File file)
        {
            path = file.getAbsolutePath();
        }
        
        public File getFile()
        {
            return new File(FileSystem.getInstance().resolvePath(path));
        }
        
        public String getPath()
        {
            return path;
        }
        
        public String getResolvedPath()
        {
            FileSystem.getInstance().checkPath(path);
            return FileSystem.getInstance().resolvePath(path);
        }
        
        public void setPath(String path)
        {
            this.path = path;
            FileSystem.getInstance().checkPath(path);
        }
    }
}
