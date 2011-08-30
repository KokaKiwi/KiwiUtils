package com.kokakiwi.utils.system;

import java.io.File;

public class SystemUtils
{
    public static OS getSystemOS()
    {
        return OS.getOSFromString(System.getProperty("os.name"));
    }
    
    public static Arch getSystemArch()
    {
        return Arch.getArchFromString(System.getProperty("os.arch"));
    }
    
    public static String getSystemVersion()
    {
        return System.getProperty("os.version");
    }
    
    public static File getExecDirectory()
    {
        return new File(getExecDirectoryPath());
    }
    
    public static String getExecDirectoryPath()
    {
        String name = SystemUtils.class.getName().replace('.', '/');
        name = SystemUtils.class.getResource("/" + name + ".class").toString();
        if (name.contains(".jar"))
        {
            name = name.substring(0, name.indexOf(".jar"));
            name = name.substring(name.lastIndexOf(':') + 1,
                    name.lastIndexOf('/') + 1).replace('%', ' ');
            String s = "";
            for (int i = 0; i < name.length(); i++)
            {
                s += name.charAt(i);
                if (name.charAt(i) == ' ')
                {
                    i += 2;
                }
            }
            
            return s.replace('/', File.separatorChar);
        }
        else
        {
            name = SystemUtils.class.getProtectionDomain().getCodeSource()
                    .getLocation().getPath();
            name = name.replace('%', ' ');
            String s = "";
            for (int i = 0; i < name.length(); i++)
            {
                s += name.charAt(i);
                if (name.charAt(i) == ' ')
                {
                    i += 2;
                }
            }
            
            return s.replace('/', File.separatorChar);
        }
    }
    
    public enum OS
    {
        windows("Windows", "win"), linux("Linux", "linux"), macosx("Mac OS",
                "mac"), solaris("Solaris", "sunos", "solaris"), unknown;
        
        private final String   name;
        private final String[] labels;
        
        OS()
        {
            this("Unknown");
        }
        
        OS(String name, String... strings)
        {
            this.name = name;
            labels = strings;
        }
        
        public String getName()
        {
            return name;
        }
        
        public static OS getOSFromString(String name)
        {
            for (final OS os : values())
            {
                for (final String label : os.labels)
                {
                    if (name.toLowerCase().contains(label.toLowerCase()))
                    {
                        return os;
                    }
                }
            }
            
            return unknown;
        }
    }
    
    public enum Arch
    {
        x86("x86"), amd64("amd64"), unknown;
        
        private final String[] labels;
        
        Arch(String... strings)
        {
            labels = strings;
        }
        
        public String getName()
        {
            return name();
        }
        
        public static Arch getArchFromString(String name)
        {
            for (final Arch os : values())
            {
                for (final String label : os.labels)
                {
                    if (name.toLowerCase().contains(label.toLowerCase()))
                    {
                        return os;
                    }
                }
            }
            
            return unknown;
        }
    }
}
