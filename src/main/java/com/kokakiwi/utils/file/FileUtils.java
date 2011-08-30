package com.kokakiwi.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kokakiwi.utils.data.DynamicDataBuffer;

public class FileUtils
{
    public static void save(File file, byte[] data) throws IOException
    {
        if(!file.exists())
        {
            file.createNewFile();
        }
        
        OutputStream out = new FileOutputStream(file);
        save(out, data);
        out.close();
    }
    
    public static void save(OutputStream out, byte[] data) throws IOException
    {
        out.write(data);
    }
    
    public static byte[] load(File file) throws IOException
    {
        if(file.exists())
        {
            InputStream in = new FileInputStream(file);
            byte[] data = load(in);
            in.close();
            
            return data;
        }
        return null;
    }
    
    public static byte[] load(InputStream in) throws IOException
    {
        DynamicDataBuffer buf = new DynamicDataBuffer();
        
        final byte[] buffer = new byte[65536];
        int bufferSize;
        while((bufferSize = in.read(buffer, 0, buffer.length)) != -1)
        {
            buf.writeBytes(buffer, bufferSize);
        }
        
        return buf.getWritedBytes();
    }
}
