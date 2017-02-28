package screens;

import core.Screen;
import quaint.Model;
import quaint.v3;
import util.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class ModelScreen extends Screen {

    boolean[] keys;
    Model model;
    Texture textureData;
    boolean escape;
    boolean change_light_up;
    boolean change_light_down;
    boolean scene_changed;

    v3 light_dir;
    float angle;

    @Override
    public void init() {
        light_dir = new v3(1.0f, -1.0f, 1.0f);
//        light_dir = new v3(0, 0.0f, -6.0f);
//        light_dir = new v3(0, 1.0f, 1.0f);
//        light_dir = new v3(0,0,1.0f);
        angle = 15;
        scene_changed = true;
        // TODO(stephen): documentation
        model = new Model("./src/obj/african_head.obj", 1.0f);

        model.loadData(true, true);

        textureData = TGALoader.getImage("./src/obj/african_head_diffuse.tga");
        keys = new boolean[256];
    }

    public void drawModel(v3 light_dir) {
        light_dir = light_dir.normalv3();
        float[] zBuffer = new float[bitmap.width * bitmap.height];
        //NOTE(Stephen): We're using Integer.MIN_VALUE here because apparently
        // java's Float.MIN_VALUE is a positive number so all our negative value would
        // false if we said (float.min_value < -1.4)( required for z-interpolation)
        Arrays.fill(zBuffer, Integer.MIN_VALUE);
        for (int i = 0; i < model.getNumberOfFaces(); i++) {
            List<Model.face_attr> faceList = model.getFaces(i);
            v3[] screen_coords = new v3[3];
            v3[] texture_coords = new v3[3];
            v3[] world_coords = new v3[3];

            float[] vn_intensity = new float[3];

            for (int j = 0; j < 3; j++) {
                int vIndex = faceList.get(j).getVertexIndx();
                v3 v0 = model.getVertex(vIndex);
                v3 t0 = model.getTextureVertext(faceList.get(j).getTextureIndx());

                float x = ((v0.x + 1.0f) * bitmap.width / 2.0f);
                float y = ((v0.y + 1.0f) * bitmap.height / 2.0f);
                float z = v0.z;
                screen_coords[j] = new v3(x, y, z);
                world_coords[j] = v0;

                // store the texture coordinates
                texture_coords[j] = t0;

                v3 vn = model.getVertexNormal(faceList.get(j).getNormalIndx());
                // transform normal
                vn = vn.getPos(Matrix3x3.doMultiply(vn.homogenousRep(), true));

                vn.normalize();

                vn_intensity[j] = v3.dot(vn, light_dir);
            }

            v3 k = v3.vectorBetweenPoints(world_coords[2], world_coords[0]);
            v3 j = v3.vectorBetweenPoints(world_coords[1], world_coords[0]);
            v3 world_normal = v3.cross(k, j);
            world_normal.normalize();
            float intensity = v3.dot(world_normal, light_dir);

            // Perspective projection
            screen_coords[0].setPos(Matrix3x3.doMultiply(screen_coords[0].homogenousRep(), false));
            screen_coords[1].setPos(Matrix3x3.doMultiply(screen_coords[1].homogenousRep(), false));
            screen_coords[2].setPos(Matrix3x3.doMultiply(screen_coords[2].homogenousRep(), false));


            // draw the rendering as a solid object
//            if (intensity > 0)
//                Plotter.baryTriangle(bitmap, screen_coords, texture_coords, vn_intensity,
//                        textureData, intensity, zBuffer);


            // wireframe rendering
//            if (intensity > 0) {
                for (int q = 0; q < screen_coords.length; q++) {
                    int x1 = (int) screen_coords[q].x;
                    int y1 = (int) screen_coords[q].y;
                    int x2 = (int) screen_coords[(q + 1) % 3].x;
                    int y2 = (int) screen_coords[(q + 1) % 3].y;

                    Plotter.drawLine(bitmap, x1, y1, x2, y2, 0xffffff);
                }
//            }
        }
    }

    @Override
    public void updateAndRender() {
        updateKeys();
//        bitmap.clear();
//        drawModel(light_dir);
//        angle += 1;
//        if(angle > 90) {
//            angle = 0;
//        }
//        bitmap.clear();
        if (scene_changed) {
            bitmap.clear();
            drawModel(light_dir);
            System.out.println("scene changed");
            scene_changed = false;
        }

        if (change_light_up) {
            System.out.println("up pressed!");
            Matrix3x3.increaseProjectionDistance(0.4f);
            bitmap.clear();
            drawModel(light_dir);
            System.out.println(scene_changed);
            scene_changed = true;
        } else if (change_light_down) {
            System.out.println("down pressed!");
            Matrix3x3.decreaseProjectionDistance(0.4f);
            bitmap.clear();
            drawModel(light_dir);
            System.out.println(scene_changed);
            scene_changed = true;
        }
    }

    public void updateKeys() {
        for (int i = 0; i < keys.length; i++) {
            escape = keys[KeyEvent.VK_ESCAPE];
            change_light_up = keys[KeyEvent.VK_UP];
            change_light_down = keys[KeyEvent.VK_DOWN];
        }

        Arrays.fill(keys, false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
}