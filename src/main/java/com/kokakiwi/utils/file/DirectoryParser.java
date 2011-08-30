package com.kokakiwi.utils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryParser
{
    public static List<File> parseFiles(File dir)
    {
        return parse(dir, DirectoryParser.Filter.FILE);
    }
    
    public static List<File> parse(File dir, DirectoryParser.Filter filter)
    {
        List<File> list = new ArrayList<File>();
        
        if(dir.isDirectory())
        {
            for(File sub : dir.listFiles())
            {
                if(sub.isDirectory())
                {
                    list.addAll(parse(sub, filter));
                }
                else
                {
                    list.add(sub);
                }
            }
        }
        
        return list;
    }
    
    public static enum Filter
    {
        FILE, DIRECTORY;
    }
}
