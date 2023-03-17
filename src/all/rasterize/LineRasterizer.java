package all.rasterize;


import all.model.Vertex;
import all.raster.ZBuffer;
import all.shaders.Shader;

import all.transforms.Vec3D;
import all.utils.Lerp;

public class LineRasterizer extends Rasterizer {

    Lerp<Vertex> lerp;
    public LineRasterizer(Shader shader, ZBuffer zBuffer) {
        super(shader, zBuffer);
        lerp = new Lerp<>();
    }




    public void rasterize(Vertex v1, Vertex v2) {


        Vertex point1Dehomog = v1.mul(1 / v1.getW());
        Vertex point2Dehomog = v2.mul(1 / v2.getW());
        Vec3D vec1 = transformToWindow(point1Dehomog);
        Vec3D vec2 = transformToWindow(point2Dehomog);




        int x1 = (int)vec1.getX();
        int y1 = (int)vec1.getY();
        int z1 = (int)vec1.getZ();
        int x2 = (int)vec2.getX();
        int y2 = (int)vec2.getY();
        int z2 = (int)vec2.getZ();


        float k = (float)(y2 -y1)/(float)(x2-x1);
        float q= y1-k*x1;

        if(Math.abs(y2-y1)>(Math.abs(x2-x1))){
            if (x1 == x2) {
                for (int y = y1; y < y2; y++) {

                    float t = (y - y1) / (float) (y2 - y1);
                    Vertex v = lerp.lerp(v1,v2,t);
                    double z = (z1 * (1 - t) + z2 * t);
                    zBuffer.drawWithTest(x1, y, z, v.getColor());
                }
            }else {
                if (y1 > y2) {
                    int tmp = y1;
                    y1 = y2;
                    y2 = tmp;
                    Vertex tmpv = v1;
                    v1 = v2;
                    v2 = tmpv;

                }


                for (int y = y1; y < y2; y++) {
                    float x = ((float) y - q) / k;
                    float t = (y - y1) / (float) (y2 - y1);
                    Vertex v = lerp.lerp(v1,v2,t);
                    double z = (z1 * (1 - t) + z2 * t);
                    zBuffer.drawWithTest((int) x, y, z, v.getColor());
                }
            }


        }else if(Math.abs(y2-y1)<(Math.abs(x2-x1))) {
            if (x1 > x2) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
                Vertex tmpv = v1;
                v1 = v2;
                v2 = tmpv;
            }


                for (int x = x1; x < x2; x++) {
                    float y = ((float) x * k) + q;
                    float t = (x - x1) / (float) (x2 - x1);
                    Vertex v = lerp.lerp(v1,v2,t);
                    double z = (z1 * (1 - t) + z2 * t);
                    zBuffer.drawWithTest(x, (int) y, z, v.getColor());
                }



        }


    }


}
