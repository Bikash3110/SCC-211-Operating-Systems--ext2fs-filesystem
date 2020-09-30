import java.io.*;
import java.util.*;
import java.nio.*;

public class Directory
{
 private int InodeTablepointer;
 private RandomAccessFile f;
 FileInfo[] file;

 public Directory(int inodeTablepointer, RandomAccessFile file)
 {
 	f=file;
 	InodeTablepointer = inodeTablepointer;
 }
/*
*Returns Directory contents in unix like format output  
*/

 public FileInfo[] getInfo(ByteBuffer buffer, Volume v) throws IOException
 {
 	List <FileInfo> info = new ArrayList<FileInfo>();
 	long capacity = 0;
 	FileInfo prev = new FileInfo(0, f, buffer, v);
 	for(int i = 0 ; capacity<1024; i++)
 	{
 		if(i==0)
 		{
 			info.add(prev);
 		}
 		else
 		{
 			info.add(new FileInfo((int)capacity, f,buffer,v));
 		}
     capacity+= info.get(i).getLength();
     prev = info.get(i);
 	}
 	return info.toArray(new FileInfo[info.size()]);
 }
}