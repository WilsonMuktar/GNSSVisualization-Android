package com.yenwie.world.gnss.satelliteView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLUtils;

public class Quad {
    private float vertices[] =
            {
                    -1.0f, -1.0f, 0.0f, // Bottom left[1]
                    -1.5f, 1.5f, 0.0f,  // Top left[3]
                    1.0f, -1.0f, 0.0f,  // Bottom right[2]
                    1.5f, 1.5f, 0.0f,   // Top right[4]
            };

    private float texture[] =
            {
                    0.0f, 1.0f, // Top left[3]
                    0.0f, 0.0f, // Bottom left[1]
                    1.0f, 1.0f, // Top right[4]
                    1.0f, 0.0f  // Bottom right[2]
            };

    private FloatBuffer vertexBuffer = null;
    private FloatBuffer textureBuffer = null;
    private final int[] textures = new int[1];

    public boolean visibility = false;

    public Quad() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    public void Draw(GL10 gl) {
        if (visibility == true) {
            //fix white square
            gl.glEnable(GL10.GL_TEXTURE_2D);

            // bind the previously generated texture
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

            // Point to our buffers
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Set the face rotation
            gl.glFrontFace(GL10.GL_CW);

            // Point to our vertex buffer
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

            // Draw the vertices as triangle strip
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.vertices.length / 3);

            // Disable the client state before leaving
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
    }

    public void loadGLTexture(GL10 gl, Context con, int texture, String name) {
        // loading texture
//		Bitmap bitmap = BitmapFactory.decodeResource(con.getResources(),texture);

        //loading texture with words
        final Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_4444);
        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);
        // get a background image from resources
        Drawable background = con.getResources().getDrawable(texture);
        background.setBounds(0, 0, 100, 100);
        background.draw(canvas); // draw the background to our bitmap
        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
        // draw the text centered
        canvas.drawText(name, 0, 30, textPaint);

        // Generate one texture pointer, and bind it to the texture array.
        gl.glGenTextures(1, this.textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[0]);

        // Create nearest filtered texture.
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap.
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Tidy up.
        bitmap.recycle();
    }

    public void setvisibility(boolean tf) {
        visibility = tf;
    }
}

