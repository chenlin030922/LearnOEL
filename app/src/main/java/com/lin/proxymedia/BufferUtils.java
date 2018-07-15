package com.lin.proxymedia;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by linchen on 2018/7/15.
 * mail: linchen@sogou-inc.com
 */
public class BufferUtils {


    public static FloatBuffer getFloatBuffer(float[] coords) {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                coords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    public static ByteBuffer geBuffer(byte[] bytes) {
        ByteBuffer indicateBuffer = ByteBuffer.allocate(bytes.length);
        indicateBuffer.put(bytes);
        indicateBuffer.position(0);
        return indicateBuffer;
    }
}
