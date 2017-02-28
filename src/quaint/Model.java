package quaint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 2/28/16 8:35 PM.
 */
public class Model {

    List<List<face_attr>> faces;
    List<v3> verts;
    List<v3> textures;
    List<v3> vnormals;
    String filename;
    float scale;

    public Model(String filename, float scale) {
        this.filename = filename;
        this.scale = scale;
    }


    public void loadData(boolean hasNormal, boolean hasTextures) {
        faces = new ArrayList<>();
        verts = new ArrayList<>();
        textures = new ArrayList<>();
        vnormals = new ArrayList<>();

        File file = new File(filename);
        try {
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                String line = scan.nextLine();
                v3 v;
                if (line.length() > 2) {
                    //TODO(Stephen): make parsing an obj file format more robust
                    // especially when there are multiple spaces between the value files
                    if (line.substring(0, 2).equals("v ")) {
                        float[] vals = parseLine(line);
                        v = new v3(scale * vals[0],
                                scale * vals[1], scale * vals[2]);
                        verts.add(v);
                    } else if (line.substring(0, 2).equals("f ")) {
                        List<face_attr> f = new ArrayList<>();
                        //TODO(stephen): regex for splitting the input model's faces
                        String[] vals = line.split(" +");

                        for (int i = 1; i < vals.length; i++) {
                            String[] tval = vals[i].split("/+");
                            int val = Integer.parseInt(tval[0]);
                            int txtrval = 0;
                            if(hasTextures) {
                                txtrval = Integer.parseInt(tval[1]);
                                txtrval -=1;
                            }
                            int normalval = 0;
                            if(hasNormal) {
                                //TODO(Stephen): Change 1 to 2 for models. it's 1 cause i'm testing the cube
                                normalval = Integer.parseInt(tval[2]);
                                normalval -=1;
                            }
                            val -= 1;
                            f.add(new face_attr(val, txtrval, normalval));
                        }
                        faces.add(f);
                    } else if(line.substring(0, 2).equals("vt")) {
                        float[] vals = parseLine(line);
                        v = new v3(vals[0], vals[1], vals[2]);
                        textures.add(v);
                    } else if(line.substring(0, 2).equals("vn")) {
                        float[] vals = parseLine(line);
                        v = new v3(vals[0],
                                 vals[1],  vals[2]);
                        vnormals.add(v);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File " + filename + " not found!");
        }
    }

    private float[] parseLine(String line) {
        char[] val = line.toCharArray();
        float[] fres = new float[3];
        StringBuilder res = new StringBuilder();
        int c = 0;
        for (int i = 0; i < val.length; i++) {
            if (Character.isDigit(val[i]) || val[i] == '-') {
                res.append(val[i]);
                int j = i+1;
                for (; j < val.length; j++) {
                    if(val[j] != ' ')
                        res.append(val[j]);
                    else {
                        break;
                    }
                }
                i=j;
                fres[c++] = Float.parseFloat(res.toString());
                res.delete(0, res.length());
            }
        }

        return fres;
    }

    public v3 getVertex(int idx) {
        return verts.get(idx);
    }

    public v3 getTextureVertext(int tdx) {
        return textures.get(tdx);
    }

    public List<face_attr> getFaces(int idx) {
        return faces.get(idx);
    }

    public v3 getVertexNormal(int vertn) {
        v3 v = vnormals.get(vertn);
        return v;
    }

    public int getNumberOfVertices() {
        return verts.size();
    }

    public int getNumberOfFaces() {
        return faces.size();
    }

    public static class face_attr {
        int vertexIndx;
        int textureIndx;
        int normalIndx;
        public face_attr(int vi, int ti, int ni) {
            // NOTE(stephen): The vertexIndx is the same as the
            // vertex normal's index;
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
}
