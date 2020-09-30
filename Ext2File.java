import java.io.*;
import java.nio.*;
import java.util.*;

public class Ext2File
{
  private long position;
  private long size;
  private long start;
  private String fileName;
  private RandomAccessFile f;
  private Volume v;
  private inode inode;
  private ByteBuffer buffer;
  private FileInfo curFile;
  private List<String> files;
  private List<ByteBuffer> buf;
  private Directory d;

  public Ext2File(Volume vol, String FilePath) throws FileNotFoundException, IOException
  {
    this.v = vol;
    FilePath = FilePath.substring(1,FilePath.length());
    files = Arrays.asList(FilePath.split("/"));
    d = new Directory(v.inodeTablePointer(0),v.getFile());
    FileInfo[] dir = d.getInfo(v.filedata(298),v);
    traverse(dir);
  }
  
  /*
  *Reads at most length bytes starting at byte offset startByte from start of file. Byte 0 is the first byte in the file.
  *startByte must be such that, 0 ≤ startByte < file.size or an exception should be raised.
  *If there are fewer than length bytes remaining these will be read and a smaller number of bytes than requested will be returned.
  */
  public byte[] read(long startByte, long length) throws IOException
  {
    int parts;

    if(length>1024)
    {
      parts = (int)Math.ceil((double)length/1024.0);
    }
    else
    {
      parts = 1;
    }
    
    int newLength = (int) length/parts;
    byte[] bytes = new byte[newLength];

    buffer.position((int) startByte);
    buffer.get(bytes);
    return bytes;
  }
/* 
*Reads at most length bytes starting at current position in the file.
*If the current position is set beyond the end of the file, and exception should be raised.
*If there are fewer than length bytes remaining these will be read and a smaller number of bytes than requested will be returned.
*/
  public byte[] read(long length) throws IOException
  {
    byte[] bytes = new byte[(int) length];
    buffer.get(bytes);
    return bytes;
  }
  /*
  *Move to byte position in file.
  */
  public void seek(long pos) throws IOException
  {
    f.seek((int) pos);
  }
  /*
  *Returns current position in file, i.e. the byte offset from the start of the file. The file position will be zero when the file 
  * is first opened and will advance by the number of bytes read with every call to one of the read( ) routines.
  */
  public long position()
  {
    return position;
  }
  
  /*
  *Returns size of file as specified in filesystem.
  */
  public long size()
  {
   return curFile.getFileSize();
  }
  
  /*
  * This meathod traverse the Filesystem to locate file and its content 
  * @param die is of type FileInfo[].
  */
  public void traverse(FileInfo[] dir) throws IOException
  {
     for(int i=0; i<files.size(); i++)
     {
      for(int j =0; j<dir.length; j++)
      {
         if(files.get(0).equalsIgnoreCase(dir[j].getFileName()))
         {
          curFile = dir[j];
          int inodeTablePointer;
          int block = 0;
          if(dir[j].getInodeNum()>v.getInodesPerGroup())
          {
            block = dir[j].getInodeNum()/v.getInodesPerGroup();
            inodeTablePointer = v.inodeTablePointer(block);
          }
          else
          {
            inodeTablePointer = v.inodeTablePointer(block);
          }

          inode x = new inode(dir[j].getInodeNum(),inodeTablePointer,v.getFile(),block);
          inode = x;
          buf = new ArrayList<ByteBuffer>();
          for(int k=0; k < x.getPointer().length;k++)
          {
            if(x.getPointer(k)!=0)
            {
              inode=x;
              buf.add(v.filedata(x.getPointer(k)));
            }
          }

          buffer = buf.get(0);
          buf = buf.subList(1,files.size());
          break;

         } 

      }
      
      if(curFile.getFileType()==1)
      {
        break;
      }
      else
      {
        files =files.subList(1,files.size());
        traverse(d.getInfo(buffer,v)); 
      }

     }
  }

}