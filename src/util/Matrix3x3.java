package util;

import java.util.Arrays;

import static java.lang.Math.*;

/**
 * 3/8/16 7:23 PM.
 */
public class Matrix3x3 {
    static float x, y;
    static float ix, iy;
    static float angle;
    static float cosAngle;
    static float sinAngle;
    static float negSinAngle;
    static float e = 5.5f;

    static float[][] mainMatrix;
    public static void setPoints(float[] a, float[] b, float[] c, float angle) {
        mainMatrix = new float[3][3];
        mainMatrix[0][0] = a[0];  mainMatrix[0][1] = b[0]; mainMatrix[0][2] = c[0];
        mainMatrix[1][0] = a[1];  mainMatrix[1][1] = b[1]; mainMatrix[1][2] = c[1];
        mainMatrix[2][0] = a[2];  mainMatrix[2][1] = b[2]; mainMatrix[2][2] = c[2];

        setAngle(angle);
        loadMatrices();
    }
    // represents a homogenous matrix
    static float[][] invTransMatrix;

    static float[][] rotationMatrix;
    static float[][] projectionMatrix;
    static float[][] transProjectionMatrix;


    public static void loadMatrices() {
//        invTransMatrix = new float[][]{{1, 0, x},
//                {0, 1, y},
//                {0, 0, 1}};

        rotationMatrix = new float[][]{
                {1.0f, 0, 0},
                {0,  cosAngle, negSinAngle},
                {0, sinAngle, cosAngle}
        };

        projectionMatrix =  new float[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, -1/e, 1}
        };
    }

    public static void loadProjectionMatrix() {
        projectionMatrix =  new float[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, -1/e, 1}
        };
    }

    public static void loadTransposeProjectionMatrix() {
        transProjectionMatrix =  new float[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 1/e},
                {0, 0, 0, 1}
        };
    }

    public static float[] multiply(float[][] a, float[] b){
        float[] result = new float[b.length];
        for (int i = 0; i < a.length; i++) {
//            System.out.println("orig: " + Arrays.toString(b));
            for (int j = 0; j < 1; j++) {
                float sum = 0;
                for(int k = 0; k < b.length; k++) {
                    sum += (a[i][k] * b[k]);
                }
                result[i] = sum;
            }
//            System.out.println("matt: " + Arrays.toString(result));
        }

        return result;
    }

    public static float[][] reOrderMatrix(float[][] matrix) {
       float[][] orderedMatrix = new float[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
               orderedMatrix[i][j] = matrix[j][i];
            }
        }

        return orderedMatrix;
    }
    public static float[][] doRotateUsingSetPoints() {
        return doRotateUsingSetPoints(rotationMatrix, mainMatrix);
    }

    private static float[][] doRotateUsingSetPoints(float[][] a, float[][] b) {
        assert(a.length == b[0].length);
        float[][] result = new float[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
//            System.out.println("orig: " + Arrays.toString(b));
            for (int j = 0; j < b[0].length; j++) {
                float sum = 0;
                for(int k = 0; k < b.length; k++) {
                    sum += (a[i][k] * b[k][j]);
                }
                result[i][j] = sum;
            }
//            System.out.println("matt: " + Arrays.toString(result));
        }
        return reOrderMatrix(result);
    }

    public static float[] doMultiply(float[] matrix, boolean isTranspose){
        if(projectionMatrix == null) {
            loadProjectionMatrix();
            loadTransposeProjectionMatrix();
        }
        float[] res;
        if(isTranspose) {
            res = multiply(transProjectionMatrix, matrix);
        } else {
            res = multiply(projectionMatrix, matrix);
        }
        return res;
    }

    public static void setAngle(float angle) {
        angle = (float)Math.toRadians(angle);
        cosAngle = (float)cos(angle);
        sinAngle = (float)sin(angle);
        negSinAngle = -1.0f*sinAngle;
    }

    public static void increaseProjectionDistance(float eChange) {
        e += eChange;
        System.out.println(e);
        loadProjectionMatrix();
        loadTransposeProjectionMatrix();
        if(e >= 10.0f) {
            e = 5.5f;
        }
    }

    public static void decreaseProjectionDistance(float eChange) {
        e -= eChange;
        System.out.println(e);
        loadProjectionMatrix();
        loadTransposeProjectionMatrix();
        if(e < 2.0f) {
            e = 5.5f;
        }
    }
}
