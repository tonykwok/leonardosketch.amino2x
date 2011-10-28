package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.AminoColor;
import com.sun.opengl.util.BufferUtil;

import javax.media.opengl.GL2;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static javax.media.opengl.GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;

public class Shader {
    int id = 0;

    public static Shader load(GL2 gl, URL resource, URL resource1) {
        System.out.println("loading shader:" + resource1);
        int id = loadShaderProgram(gl, resource, resource1);
        Shader shader = new Shader();
        shader.id = id;
        return shader;
    }


    public void use(GL2 gl) {
        gl.glUseProgramObjectARB(id );
    }

    public void setFloatParameter(GL2 gl, String parameter, float value) {
        int loc = gl.glGetUniformLocationARB(id, parameter );
        gl.glUniform1fARB(loc, value);
    }

    public void setVec2Parameter(GL2 gl, String parameter, float value, float value2) {
        int loc = gl.glGetUniformLocationARB(id, parameter );
        gl.glUniform2fARB( loc, value, value2 );
    }

    public void setVec4Parameter(GL2 gl, String parameter, float r, float g, float b, float a) {
        int loc = gl.glGetUniformLocationARB(id, parameter );
        gl.glUniform4f(loc,r,g,b,a);
    }

    public void setVec2Parameter(GL2 gl, String parameter, Point pt) {
        int loc = gl.glGetUniformLocationARB(id, parameter );
        gl.glUniform2fARB(loc, (float) pt.x, (float) pt.y);
    }
    public void setVec4Parameter(GL2 gl, String parameter, AminoColor stop) {
        int loc = gl.glGetUniformLocationARB(id, parameter );
        gl.glUniform4f(loc, (float)stop.getRed()/255f, (float)stop.getGreen()/255f, (float)stop.getBlue()/255f, (float)stop.getAlpha()/255f);
    }

    public void setIntParameter(GL2 gl, String parameter, int value) {
        int loc = gl.glGetUniformLocationARB( id, parameter );
        gl.glUniform1iARB( loc, value );
    }
    public static int loadShaderProgram(GL2 gl, URL vert, URL frag) {
        if ( gl.isExtensionAvailable("GL_ARB_vertex_shader") ) {
            String vertexShaderSource = null;
            String fragmentShaderSource = null;

            try {
                BufferedReader shaderReader = new BufferedReader(
                    new InputStreamReader(
                        vert.openStream()
                    )
                );
                StringBuilder b = new StringBuilder();
                String line = shaderReader.readLine();
                while (line != null) {
                    b.append(line);
                    b.append("\n");
                    line = shaderReader.readLine();
                }
                vertexShaderSource = b.toString();

                shaderReader = new BufferedReader(
                    new InputStreamReader(
                            frag.openStream()
                    )
                );
                b = new StringBuilder();
                line = shaderReader.readLine();
                while (line != null) {
                    b.append(line);
                    b.append("\n");
                    line = shaderReader.readLine();
                }
                fragmentShaderSource = b.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if ( vertexShaderSource != null && fragmentShaderSource != null ) {
                int vertexShader = gl.glCreateShaderObjectARB( GL_VERTEX_SHADER );
                gl.glShaderSourceARB( vertexShader, 1, new String[]{vertexShaderSource}, (int[]) null, 0 );
                gl.glCompileShaderARB(vertexShader);
                checkLogInfo(gl, vertexShader);

                int fragmentShader = gl.glCreateShaderObjectARB( GL_FRAGMENT_SHADER );
                gl.glShaderSourceARB( fragmentShader, 1, new String[]{fragmentShaderSource}, (int[]) null, 0 );
                gl.glCompileShaderARB(fragmentShader);
                checkLogInfo(gl, fragmentShader);

                int programObject = gl.glCreateProgramObjectARB();
                gl.glAttachObjectARB(programObject, vertexShader);
                gl.glAttachObjectARB(programObject, fragmentShader);

                gl.glLinkProgramARB(programObject);
                gl.glValidateProgramARB(programObject);
                checkLogInfo(gl, programObject);

                return programObject;
            } else {
                return 0;
            }
        } else {
            throw new RuntimeException("GL_ARB_vertex_shader not supported!");
        }

    }
    private static void checkLogInfo( GL2 gl, int obj ) {
        IntBuffer iVal = BufferUtil.newIntBuffer(1);
        gl.glGetObjectParameterivARB(obj, GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();
        if (length <= 1) return;

        ByteBuffer infoLog = BufferUtil.newByteBuffer(length);

        iVal.flip();
        gl.glGetInfoLogARB(obj, length, iVal, infoLog);

        byte[] infoBytes = new byte[length];
        infoLog.get(infoBytes);
        System.out.println("GLSL Validation >> " + new String(infoBytes));

        throw new Error("shader validation failed!");
    }

}
