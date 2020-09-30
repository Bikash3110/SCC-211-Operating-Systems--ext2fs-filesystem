import java.io.*;
import java.nio.*;
import java.util.*;

/*
*This class reads the inode structure of the required inode and
* give access to the contents of inodes.
*
*/

public class inode
{
    private final int IFSCK = 0xC000;      // Socket
    private final int IFLNK = 0xA000;      // Symbolic Link
    private final int IFREG = 0x8000;      // Regular fileMode
    private final int IFBLK = 0x6000;      // Block Device
    private final int IFDIR = 0x4000;      // Directory
    private final int IFCHR = 0x2000;      // Character Device
    private final int IFIFO = 0x1000;      // FIFO

    private final int ISUID = 0x0800;      // Set process User ID
    private final int ISGID = 0x0400;      // Set process Group ID
    private final int ISVTX = 0x0200;      // Sticky bit


    private final int IRUSR = 0x0100;      // User read
    private final int IWUSR = 0x0080;      // User write
    private final int IXUSR = 0x0040;      // User execute

    private final int IRGRP = 0x0020;      // Group read
    private final int IWGRP = 0x0010;      // Group write
    private final int IXGRP = 0x0008;      // Group execute

    private final int IROTH = 0x0004;      // Others read
    private final int IWOTH = 0x0002;      // Others wite
    private final int IXOTH = 0x0001;      // Others execute

    private short fileMode;
    private short UserID;
    private int fileSizeLower;
    private int lastAccessTime;
    private int creationTime;
    private int lastModifiedTime;
    private int deletedTime;
    private short groupID;
    private short numHardLinks;
    private int[] pointers;          //for the first 12 blocks
    private int indirectPointer;
    private int doubleIndirectPointer;
    private int tripleIndirectPointer;
    private int fileSizeUpper;
    private final int blockSize = 1024;
    private Volume v;

    private int[] others;
    private int[] group;
    private int[] user;

 /*
 *This constructor meathod gives access to the contents of inode 
 */
	public inode(int inodeNo, int inodeTablePointer, RandomAccessFile f, int blockGroup) throws FileNotFoundException, IOException
	{  	
   	  inodeNo = inodeNo - 1712*blockGroup;
   	  ByteBuffer buffer = ByteBuffer.allocate(1024);
   	  buffer.order(ByteOrder.LITTLE_ENDIAN);
   	  byte[] inodeBlock = new byte[1024];
   	  f.seek((blockSize*inodeTablePointer)+ 128*(inodeNo-1));
      f.read(inodeBlock);
      buffer.put(inodeBlock);
   
	    fileMode = buffer.getShort(0);
      permission();
      UserID = buffer.getShort(2);
      fileSizeLower = buffer.getInt(4);
      lastAccessTime = buffer.getInt(8);
      creationTime = buffer.getInt(12);
      lastModifiedTime = buffer.getInt(16);
      deletedTime = buffer.getInt(20);
      groupID = buffer.getShort(24);
      numHardLinks = buffer.getShort(26);
      pointers = new int[12];
      for(int i=0; i<12; i++)
      {
       pointers[i] = buffer.getInt(40 + (i*4));
      }
      indirectPointer = buffer.getInt(88);
      doubleIndirectPointer = buffer.getInt(92);
      tripleIndirectPointer = buffer.getInt(96);
      fileSizeUpper = buffer.getInt(108);
    }

	public short getUserID()
	{
		return UserID;
	} 

	public short getGroupID()
	{
		return groupID;
	} 

	public int getHardLinks()
	{
		return numHardLinks;
	}

	public int getPointer(int num)
	{
		return pointers[num];
	}

  public int[] getPointer()
  {
    return pointers;
  }

/*
	public int getModifiedDate()
	{
		return lastModifiedTime;
	}
*/

 // returns the file size  
	public int getFileSize()
	{
		String upper = Integer.toBinaryString(fileSizeUpper);
		String lower = Integer.toBinaryString(fileSizeLower);
		int Size = Integer.parseInt(lower.concat(upper),2);
		return Size;
	}

 // checks the access permission for user, group and others from the inode fileMode. 
	public void permission()
	{
	  others = new int[3];
      others[0] = IROTH;
      others[1] = IWOTH;
      others[2] = IXOTH;

      group = new int[3];
      group[0] = IRGRP;
      group[1] = IWGRP;
      group[2] = IXGRP;
        
      user = new int[3];
      user[0] = IRUSR;
      user[1] = IWUSR;
      user[2] = IXUSR;

      char permission_dir = 'd';
      char permission_execute = 'x';
      char permission_write = 'w';
      char permission_read = 'r';
      char none = '-';

      int file = IFREG;
      int dir = IFDIR;

      if((dir & fileMode) == dir)
      {
      	System.out.print(permission_dir);
      }
      else
      {
      	System.out.print(none);
      }

    // USER PERMISSION SET
      if((fileMode & user[0])== user[0])
      {
      	System.out.print(permission_read);
      }
      else
      {
      	System.out.print(none);
      }   

      if((fileMode & user[1])== user[1])
      {
      	System.out.print(permission_write);
      }
      else
      {
      	System.out.print(none);
      }

      if((fileMode & user[2])== user[2])
      {
      	System.out.print(permission_execute);
      }
      else
      {
      	System.out.print(none);
      }

   // GROUP PERMISSION SET
      if((fileMode & group[0])== group[0])
      {
      	System.out.print(permission_read);
      }
      else
      {
      	System.out.print(none);
      }

      if((fileMode & group[1])== group[1])
      {
      	System.out.print(permission_write);
      }
      else
      {
      	System.out.print(none);
      }

      if((fileMode & group[2])==group[2])
      {
      	System.out.print(permission_execute);
      }
      else
      {
      	System.out.print(none);
      }

    // OTHERS PERMISSION SET
      if((fileMode & others[0])== others[0])
      {
      	System.out.print(permission_read);
      }
      else
      {
      	System.out.print(none);
      }

      if((fileMode & others[1])== others[1])
      {
      	System.out.print(permission_write);
      }
      else
      {
      	System.out.print(none);
      }

      if((fileMode & others[2])== others[2])
      {
      	System.out.print(permission_execute);
      }
      else
      {
      	System.out.print(none);
      }  
 	}
}