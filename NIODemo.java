package com.b.nio.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class NIODemo {
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * 
	 * example from http://tutorials.jenkov.com/java-nio
<Channel>
FileChannel
DatagramChannel
SocketChannel
ServerSocketChannel

<Buffer>
ByteBuffer
MappedByteBuffer
CharBuffer
DoubleBuffer
FloatBuffer
IntBuffer
LongBuffer
ShortBuffer

<Buffer Fields>
capacity
position
limit

	 * 
	 */
	public static void main(String args[]) throws IOException{
//		FileChannelByteBufferDemo();
//		System.out.println("================================");
//		FileChannelByteBufferScatteringReadAndGatheringWriteDemo();
//		System.out.println("================================");
		channelTransfer();
	}
	
	public static void FileChannelByteBufferDemo() throws IOException{
		RandomAccessFile aFile = new RandomAccessFile("C:/UBS/Dev/b/eclipse-jee-mars-1-win32-x86_64/workspace/jbehavetest/src/test/java/com/b/jbehavetest/demo_story.story", "rw");
	    FileChannel inChannel = aFile.getChannel();
	    ByteBuffer buf = ByteBuffer.allocate(1024);//create buffer with capacity of 1024 bytes
	    int bytesRead = inChannel.read(buf);//read into buffer.
	    while (bytesRead != -1) {
	      //System.out.println("Read " + bytesRead);
	      buf.flip();//make buffer ready for read, rewind() sets the position back to 0, so you can reread all the data in the buffer
	      while(buf.hasRemaining()){
	          System.out.print((char) buf.get());// read 1 byte at a time
	      }
	      buf.clear();//make buffer ready for writing
	      bytesRead = inChannel.read(buf);
	    }
	    aFile.close();
	}
	
	public static void FileChannelByteBufferScatteringReadAndGatheringWriteDemo() throws IOException{

		RandomAccessFile aFile = new RandomAccessFile("C:/UBS/Dev/b/eclipse-jee-mars-1-win32-x86_64/workspace/jbehavetest/src/test/java/com/b/jbehavetest/demo_story.story", "rw");
	    FileChannel inChannel = aFile.getChannel();
	    ByteBuffer buf1 = ByteBuffer.allocate(128);
	    ByteBuffer buf2 = ByteBuffer.allocate(256);
	    ByteBuffer[] bufferArray1 = { buf1, buf2 };
	    inChannel.read(bufferArray1);
	 
	    ByteBuffer buf3 = null;
	    ByteBuffer buf4 = null;
	    
	    buf1.flip();//make buffer ready for read, rewind() sets the position back to 0, so you can reread all the data in the buffer
	    buf3 = ByteBuffer.wrap(buf1.array());
	    buf1.clear();//make buffer ready for writing

	    buf2.flip();//make buffer ready for read, rewind() sets the position back to 0, so you can reread all the data in the buffer
	    buf4 = ByteBuffer.wrap(buf2.array());
	    buf2.clear();//make buffer ready for writing

	    ByteBuffer[] bufferArray2 = { buf3, buf4 };
	    
	    FileOutputStream fileOutputStream = new FileOutputStream(new File("C:/UBS/Dev/b/eclipse-jee-mars-1-win32-x86_64/workspace/jbehavetest/src/main/java/com/b/nio/example/ttt.txt"));
	    FileChannel outChannel = fileOutputStream.getChannel();
	    outChannel.write(bufferArray2);
	    fileOutputStream.close();
	    
	    aFile.close();
	}
	
	public static void channelTransfer() throws IOException{
		String filePath = "C:/UBS/Dev/b/eclipse-jee-mars-1-win32-x86_64/workspace/jbehavetest/src/main/java/com/b/nio/example/ttt1.txt";
//		File file = new File(filePath);
//		if(file.exists()){
//			file.delete();
//		}
		
		RandomAccessFile fromFile = new RandomAccessFile("C:/UBS/Dev/b/eclipse-jee-mars-1-win32-x86_64/workspace/jbehavetest/src/test/java/com/b/jbehavetest/demo_story.story", "rw");
		FileChannel      fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile(filePath, "rw");
		FileChannel      toChannel = toFile.getChannel();

		long position = 0;
		long count    = fromChannel.size();

		toChannel.transferFrom(fromChannel, position, count);
		//fromChannel.transferTo(position, count, toChannel);
		
		fromChannel.close();
		toChannel.close();
	}
	
	public static void channelSelector() throws IOException{
		
		Selector selector = Selector.open();//create a Selector by calling the Selector.open() method

		
	}
	
	
}
