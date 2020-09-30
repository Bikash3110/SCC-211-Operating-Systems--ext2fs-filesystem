import java.io.*;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Volume
{
	private int blockSize = 1024;
	private int inodeTablePointer;
	private short magicNum;
	private int totalInodes;
	private int totalBlocks;
	private int blocksPerGroup;
	private int inodesPerGroup;
	private int sizeOfInode;
	private String volumeLabel;
  private RandomAccessFile f;

   public Volume(String filePath) throws FileNotFoundException, IOException
   {
    f = new RandomAccessFile(filePath, "r");
   }

   /**
   * This meathod get access to superlock.
   * And get the contents of superblock.
   */
   public void superBlock() throws FileNotFoundException, IOException
   {
   	ByteBuffer buf = ByteBuffer.allocate(1024);
   	buf.order(ByteOrder.LITTLE_ENDIAN);
   	byte[] bytes = new byte[1024];
    f.seek(1024);
   	f.read(bytes);
   	buf.put(bytes);

   	magicNum = buf.getShort(56);
   	totalInodes = buf.getInt(0);
   	totalBlocks = buf.getInt(4);
   	blocksPerGroup = buf.getInt(32);
   	inodesPerGroup = buf.getInt(40);
   	sizeOfInode = buf.getInt(88);

   	byte[] stringLabel = new byte[16];
   	buf.position(120);
    buf.get(stringLabel);
   	volumeLabel = new String(stringLabel);

    System.out.println("Magic Number : "+String.format("0x%04X",magicNum));
    System.out.println("Total Inodes: "+totalInodes);
    System.out.println("Total Blocks: "+totalBlocks);
    System.out.println("Blocks per Group: "+blocksPerGroup);
    System.out.println("Inodes per Group: "+inodesPerGroup);
    System.out.println("Size Of each Inode in bytes: "+sizeOfInode);
    System.out.println("Volume Label: "+ volumeLabel+ "\n");
   }
    
    /*
    *This meathod get access to the Group Descriptor blocks 
    *And retrns the inode Table pointer from the group descriptor.
    *@param groupDescriptorNo is the Group Descriptor number.
    */
   public int inodeTablePointer(int groupDescriptorNo) throws IOException
   {
   	ByteBuffer buf_groupDescriptor = ByteBuffer.allocate(1024);
   	buf_groupDescriptor.order(ByteOrder.LITTLE_ENDIAN);
   	byte[] bytes = new byte[1024];
    f.seek(2048);
   	f.read(bytes);
    
   	buf_groupDescriptor.put(bytes);
    inodeTablePointer = buf_groupDescriptor.getInt((groupDescriptorNo*32)+8);
    //System.out.println("inodeTablePointer "+groupDescriptorNo +" : "+ inodeTablePointer);
    return inodeTablePointer;
   }
    
    /*
    * This Meathod read the filedata block present and returns the Bytebuffer consisting of Filedata
    * pos @param states the start position of filedata. 
    */

    public ByteBuffer filedata(int pos) throws IOException
    {
     ByteBuffer buf = ByteBuffer.allocate(1024);
   	 buf.order(ByteOrder.LITTLE_ENDIAN);
   	 byte[] bytes = new byte[1024];
     f.seek(1024*pos);
   	 f.read(bytes);
   	 buf.put(bytes);
   	 return buf;
    }

    /*
    *This meathod returns an array of byte consisting of file data  
    *starting from position @param "pos" 
    * Note: called in helper class to read filedata
    */

    public byte[] readfile(int pos) throws IOException
    {
     byte[] bytes = new byte[1024];
     f.seek(1024*pos);
     f.read(bytes);
     return bytes;
    }
    
    // returns the Inode pointer per groups
    public int getInodesPerGroup()
    {
      return inodesPerGroup;
    }
    
    // returns the Random Accessfile 
    public RandomAccessFile getFile()
    {
    	return f;
    }
}