import java.io.*;
import java.nio.*;

public class ReadFile
{
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
	  Volume v = new Volume("C:/Users/BIKASH/Documents/SCC211/OS/ext2fs");
      v.superBlock();
      int p1 = v.inodeTablePointer(0);
      int p2 = v.inodeTablePointer(1);
      int p3 = v.inodeTablePointer(2);
      int p4 = v.inodeTablePointer(3);
       
      inode i2 = new inode(2,p1,v.getFile(),0);
      System.out.println("\n");
                        
      ByteBuffer bb2 = v.filedata(i2.getPointer(0));
      Directory d = new Directory(p1,v.getFile());
      d.getInfo(bb2,v);
      System.out.println("===========================================");  
      
      
      inode i = new inode(12,p1,v.getFile(),0);
      byte[] f1 = v.readfile(7681);
      Helper h = new Helper();
      h.dumpHexBytes(f1);
      System.out.println("\n");
      byte[] f2 = v.readfile(7682);
      h.dumpHexBytes(f2); 
      
      Ext2File f = new Ext2File(v,"/two-cities");
      byte[] buf = f.read(0L,f.size());
      System.out.println(new String(buf)); 

	}
}