import java.io.*;
import java.nio.*;
import java.util.*;
import javax.xml.bind.DatatypeConverter;

public class Helper
{
 public Helper()
 {

 }

 /*
 * This meathod outputs the array of bytes holding filedata @param bytes
 * into a readable Hexadecimal format, with printable ASCII code by the side.
 */

 public void dumpHexBytes(byte[] bytes)
 {

   for(int i=0; i<bytes.length; i++)
   {
    int x = bytes[i] & 0xFF;              //  initialize each byte to x 
    if(x == 0)                            
    {
       System.out.print("00 ");           
    }
    else
    {
    System.out.print(Integer.toHexString(x)+" ");   // converts and print interger to hexstring
    }

    if(i%8==0)                         
    {
     System.out.print(" | ");                // Seperates every 8th byte with '|' 
    }
     
     if((i%16)==0 && i>=16)                  
     {
     	int j = i-16;
     	int z = 0;                              
        while(z<16)
        {
           
          int Ascii =bytes[j] & 0xFF;	
          char c = (char) Ascii;	           // converts the each byte to Ascii
           
          if(z%8==0 && z>0)
          {
           System.out.print(" | ");
          } 

          if(Ascii == 0)
          {
           System.out.print(".");
          } 
          else
          {
           System.out.print(c);                // prints Ascii value of int byte   
          }
          j++; 
          z++;
        }

     	System.out.print("\n");               // Jumps to new line after 16bytes of hexString   
     }

   }
 

 }
}