package com.kokakiwi.utils.data;

public class DynamicDataBuffer extends DataBuffer
{
    @Override
    protected byte[] read(int start, int length)
    {
        if ((start + length) > readableBytes.length)
        {
            length = readableBytes.length - start;
        }
        final byte[] out = new byte[length];
        
        for (int i = 0; i < length; i++)
        {
            out[i] = readableBytes[start + i];
        }
        
        return out;
    }
    
    @Override
    protected void write(byte[] bytes, int start, int length)
    {
        final int diff = (start + length) - writeableBytes.length;
        if (diff > 0)
        {
            final byte[] newBytes = new byte[writeableBytes.length + diff];
            for (int i = 0; i < writeableBytes.length; i++)
            {
                newBytes[i] = writeableBytes[i];
            }
            writeableBytes = newBytes;
        }
        
        for (int i = 0; i < length; i++)
        {
            writeableBytes[start + i] = bytes[i];
        }
    }
    
    public static DynamicDataBuffer merge(DynamicDataBuffer... buffers)
    {
        final DynamicDataBuffer buffer = new DynamicDataBuffer();
        
        for (final DynamicDataBuffer buf : buffers)
        {
            buffer.writeBytes(buf.getWritedBytes());
        }
        
        return buffer;
    }
}
