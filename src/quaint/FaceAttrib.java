package quaint;

public class FaceAttrib {
    int vertexIndx;
    int textureIndx;
    int normalIndx;
    public FaceAttrib(int vi, int ti, int ni) {
        vertexIndx = vi;
        textureIndx = ti;
        normalIndx = ni;
    }

    public int getTextureIndx() {
        return textureIndx;
    }

    public int getVertexIndx() {
        return vertexIndx;
    }

    public int getNormalIndx() {
        return normalIndx;
    }
}
