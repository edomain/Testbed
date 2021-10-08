package util;

import core.Bitmap;
import quaint.Point;
import quaint.v2;
import quaint.v3;

import java.util.Arrays;

/**
 * 2/28/16 9:07 PM.
 */
public class Plotter {
    public static void drawLine(Bitmap bitmap, v2 p0, v2 p1, int color) {
        drawLine(bitmap, p0.x, p0.y, p1.x, p1.y, color);
    }

    public static void drawLine(Bitmap bitmap, int x1, int y1, int x2, int y2, int color) {
        boolean steep = false;
        if (Math.abs(x2 - x1) < Math.abs(y2 - y1)) {
            int temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
            steep = true;
        }
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;
        float derror = Math.abs(2 * dy);
        float error = 0;
        int y = y1;
        for (int x = x1; x <= x2; x++) {
            if (steep)
                bitmap.setPixel(y, x, color);
            else
                bitmap.setPixel(x, y, color);

            error += derror;
            if (error > dx) {
                y += (y2 > y1 ? 1 : -1);
                error -= dx * 2;
            }
        }

    }


    // rasterizing triangles using segment heights and interpolating along
    // those heights for each triangle subsegment.
    // TODO(Stephen): For learning purposes, rewrite it the naive way.
    public static void drawTriangle(Bitmap bitmap, v3[] t, float intensity, float ity0, float ity1, float ity2, float[] zBuffer) {
        // sort the triangles using their y's
        Arrays.sort(t);
        float totalHeight = t[2].y - t[0].y;
        float halfSeg = t[1].y - t[0].y;
        float midSeg = t[2].y - t[1].y;
        // drawing half of the triangle
        for (int i = 0; i <= totalHeight; i++) {
            boolean otherHalf = false;
            if (i > halfSeg) {
                otherHalf = true;
            }
            float segmentHeight = otherHalf ? midSeg : halfSeg;
            float alpha = i / totalHeight;
            float val = (otherHalf) ? halfSeg : 0;
            float beta = (i - val) / segmentHeight;

            v3 A = t[0].add(v3.vectorBetweenPoints(t[0], t[2]).mull(alpha));
            v3 B;
            float ityA = ity0 + (ity2 - ity0) * alpha;

            float ityB;
            if (otherHalf) {
                B = t[1].add(v3.vectorBetweenPoints(t[1], t[2]).mull(beta));
                ityB = ity1 + (ity2 - ity1) * beta;
            } else {
                B = t[0].add(v3.vectorBetweenPoints(t[0], t[1]).mull(beta));
                ityB = ity0 + (ity1 - ity0) * beta;
            }
            if (A.x > B.x) {
                v3 temp = A;
                A = B;
                B = temp;
            }

            for (int j = (int) A.x; j <= (int) B.x; j++) {
                float phi = ((int) (A.x) == (int) B.x) ? 1.0f : (j - A.x) / (B.x - A.x);

                v3 P = A.add(v3.vectorBetweenPoints(A, B).mull(phi));
                int idx = (int) P.x + (int) P.y * bitmap.width;

                float ityP = ityA + (ityB - ityA) * phi;
                if (P.x > bitmap.width || P.y > bitmap.height || P.x < 0 || P.y < 0) continue;

                if (zBuffer[idx] < P.z) {
                    zBuffer[idx] = P.z;
                    if (ityP < 0) {
                        ityP = 0;
                    }
                    int color = Color.color(ityP, ityP, ityP);
                    bitmap.setPixel((int) P.x, (int) P.y, color);
                }
            }

        }
    }

    public static void baryTriangle(Bitmap bitmap, v3[] triangle, v3[] texture_coords, float[] vn, Texture txtData,
                                    float intensity, float[] zBuffer) {
        int[] res = sortTrianglesX(triangle, texture_coords, vn, true, true);
        int xmin = (int) triangle[0].x;
        int xmax = (int) triangle[2].x;
        int ymin = res[0];
        int ymax = res[1];

        v3 v0 = v3.vectorBetweenPoints(triangle[0], triangle[2]);
        v3 v1 = v3.vectorBetweenPoints(triangle[0], triangle[1]);
        float dot00 = dot(v0, v0);
        float dot01 = dot(v0, v1);
        float dot11 = dot(v1, v1);

        float u, v;
        float invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);

        for (int y = ymin; y <= ymax; y++) {
            for (int x = xmin; x <= xmax; x++) {
                // pixel to compute
                v3 p = new v3(x, y, 0.0f);

                v3 v2i = v3.vectorBetweenPoints(triangle[0], p);

                float dot02 = dot(v0, v2i);
                float dot12 = dot(v1, v2i);
                u = ((dot02 * dot11 - dot12 * dot01)) * invDenom;
                v = ((dot00 * dot12 - dot01 * dot02)) * invDenom;
                float sum = u + v;

                float q = 1 - u - v;
                if ((u >= 0) && (v >= 0) && (sum < 1)) {
                    // why q v u
                    float[] bc = {q, v, u};

                    // interpolate z
                    p.z = 0;
                    for (int i = 0; i < 3; i++) {
                        p.z += (triangle[i].z * bc[i]);
                    }

                    if (x + y * bitmap.width >= bitmap.width * bitmap.height) continue;
                    if (x + y * bitmap.width < 0) continue;
                    if (zBuffer[x + y * bitmap.width] < p.z) {
                        zBuffer[x + y * bitmap.width] = p.z;

                        float _intensity = q * vn[0] + v * vn[1] + u * vn[2];

                        /**
                         * Texture mapping
                         */
                        v3 t1 = texture_coords[0].mull(q);
                        v3 t2 = texture_coords[1].mull(v);
                        v3 t3 = texture_coords[2].mull(u);
                        v3 result = t1.add(t2);
                        result = result.add(t3);

                        int xx = (int) (result.x * txtData.width);
                        int yy = (int) (result.y * txtData.height);

                        int ind = txtData.data[xx + yy * txtData.width];
//                        int color_result = Color.color(intensity, intensity,intensity);

                        if (_intensity < 0)
                            _intensity = 0;
                        // texture mapping color
//                        int color_result = Color.color(_intensity, _intensity, _intensity);
                        int color_result = Color.decomposeAndInterpolate(ind, _intensity);
                        bitmap.setPixel(x, y, color_result);
                    }
                }
            }
        }
    }

    public static int[] sortTrianglesX(v3[] triangle, v3[] texture_coords, float[] vertex_normals, boolean hasTextures,
                                       boolean hasNormals) {
        int ymax = (int) (triangle[0].y);
        int ymin = (int) (triangle[0].y);
        for (int i = 0; i < triangle.length; i++) {
            int minx = (int) triangle[i].x;
            int ind = i;
            for (int j = i + 1; j < triangle.length; j++) {
                if (minx > triangle[j].x) {
                    minx = (int) triangle[j].x;
                    ind = j;
                }
                if (ymax < triangle[j].y) {
                    ymax = (int) triangle[j].y;
                }
                if (ymin > triangle[j].y) {
                    ymin = (int) triangle[j].y;
                }
            }

            v3 temp = triangle[i];
            triangle[i] = triangle[ind];
            triangle[ind] = temp;

            if (hasTextures) {
                temp = texture_coords[i];
                texture_coords[i] = texture_coords[ind];
                texture_coords[ind] = temp;
            }

            if (hasNormals) {
                float tem = vertex_normals[i];
                vertex_normals[i] = vertex_normals[ind];
                vertex_normals[ind] = tem;
            }
        }

        return new int[]{ymin, ymax};
    }

    // algebraic dot
    public static float dot(v3 a, v3 b) {
        float result = a.x * b.x + a.y * b.y + a.z * b.z;
        return result;
    }

    public static float dot(v2 a, v2 b) {
        return (a.x * b.x + a.y * b.y);
    }
}

