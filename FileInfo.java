import java.io.*;
import java.util.*;
import java.nio.*;

 /*
 * This class Access the information present about the directory and file  
 * and return the content present of them.
 * @param pos is the pointer to the directory
 * @param f is the Random Access File
 * @param buffer is the content of the file Directory
 * @param v is the object of Volume class 
 */

public class FileInfo
{
 private int inodeNum;
 private short length ;
 private byte nameLength;
 private byte fileType;
 private String fileName;
 private inode inode;
 private int buf_pos = 0;

 public FileInfo(int pos, RandomAccessFile f, ByteBuffer buffer, Volume v) throws IOException
 {
 	inodeNum = buffer.getInt(pos);
 	length = buffer.getShort(pos+4);
 	nameLength = buffer.get(pos+6);
 	fileType = buffer.get(pos+7);

 	byte[] String = new byte[nameLength];  // (int) nameLength
 	buffer.position(pos+8);
 	buffer.get(String);
 	fileName = new String(String);
    
  int blockGroup = inodeNum/1712;
  int inodeTablePointer = v.inodeTablePointer(blockGroup);
  inode = new inode(inodeNum, inodeTablePointer, f, blockGroup);
 	System.out.println("(INODE NUM : "+inodeNum+")  "+getHardLinks()+"  "+getUserID()+"  "+getGroupID()+"  "+getFileSize()+"  "+fileName);
  buf_pos = (8+nameLength);
 }

 // returns the position of buffer
 public int getpos()
 {
 	return buf_pos;
 }
// returns the length of File name
 public int getLength()
 {
 	return length;
 }
// returns file Name
 public String getFileName()
 {
 	return fileName;
 }
//returns File type
  public byte getFileType()
 {
  return fileType;
 }
//returns inode Number 
 public int getInodeNum()
 {
 	return inodeNum;
 }
// returns User ID
 public short getUserID()
 {
   return inode.getUserID();
 }

 public short getGroupID()
 {
   return inode.getGroupID();
 }
 public int getHardLinks()
 {
    return inode.getHardLinks();
 }

  public int getFileSize()
 {
   return inode.getFileSize();
 }

/*
  public int getDate()
 {

 }
*/
}