package tuling.nio;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class BufferTest {
    public static void main(String[] args) throws IOException {
        ByteBufferTest();
    }
    private static void  ByteBufferTest(){
        //分配新的byte缓冲区，参数为缓冲区容量
        //新缓冲区的当前位置将为零，其界限(限制位置)将为其容量,它将具有一个底层实现数组，其数组偏移量将为零。
        ByteBuffer byteBuffer= ByteBuffer.allocate(10);
        output("初始化缓冲区：",byteBuffer);
        for(int i=0;i<byteBuffer.capacity()-1;i++){
            byteBuffer.put(Byte.parseByte(new SecureRandom().nextInt(20)+""));
        }
        output("写入缓冲区9个byte：",byteBuffer);
        byteBuffer.flip();
        output("使用flip重置元素位置：",byteBuffer);
        while (byteBuffer.hasRemaining()){
            System.out.print(byteBuffer.get()+"|");
        }
        System.out.print("\n");
        output("使用get读取元素：",byteBuffer);
        byteBuffer.clear();
        output("恢复初始化态clear：",byteBuffer);

    }
    private static void output(String step, Buffer buffer) {
        System.out.println(step + " : ");
        System.out.print("capacity: " + buffer.capacity() + ", ");
        System.out.print("position: " + buffer.position() + ", ");
        System.out.println("limit: " + buffer.limit());
        System.out.println("mark: " + buffer.mark());
        System.out.println();
    }
}