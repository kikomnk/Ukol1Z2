package all.rasterize;

import all.model.Vertex;
import all.raster.ZBuffer;
import all.shaders.Shader;

import all.transforms.Vec3D;
import all.utils.Lerp;


public class TriangleRasterizer extends Rasterizer{

    private final Lerp<Vertex> lerp;


    //shader
    public TriangleRasterizer(ZBuffer zBuffer,Shader shader) {
        super(shader,zBuffer);

        this.lerp = new Lerp<>();
    }

    public void rasterize(Vertex v1, Vertex v2, Vertex v3) {

        v1 = v1.mul(1 / v1.getW());
        v2 = v2.mul(1 / v2.getW());
        v3 = v3.mul(1 / v3.getW());

        Vec3D a = transformToWindow(v1);
        Vec3D b = transformToWindow(v2);
        Vec3D c = transformToWindow(v3);

        Vec3D temp;
        Vertex tempv;

        if (b.getY() < a.getY()) {
            temp = a;
            a = b;
            b = temp;

            tempv = v1;
            v1 = v2;
            v2 = tempv;
        }
        if (c.getY() < b.getY()) {
            temp = b;
            b = c;
            c = temp;

            tempv = v2;
            v2 = v3;
            v3 = tempv;
        }
        if (b.getY() < a.getY()) {
            temp = a;
            a = b;
            b = temp;

            tempv = v1;
            v1 = v2;
            v2 = tempv;
        }
            double ax = a.getX();
            double ay = a.getY();
            double az = a.getZ();
            double bx = b.getX();
            double by = b.getY();
            double bz = b.getZ();
            double cx = c.getX();
            double cy = c.getY();
            double cz = c.getZ();


        for (int y = (int) Math.max(ay + 1, 0); y <= Math.min(by, height - 1); y++) {
            double t1 = (y - ay) / (by - ay);
            double x1 = (ax * (1 - t1) + bx * t1);

            Vertex vab =lerp.lerp(v1,v2,t1);


            double t2 = (y - ay) / (cy - ay);
            double x2 = (ax * (1 - t2) + cx * t2);


            Vertex vac = lerp.lerp(v1,v3,t2);


            if (x1 > x2) {
                double tmp = x2;
                x2 = x1;
                x1 = tmp;

                tempv = vab;
                vab = vac;
                vac = tempv;
            }


            for (int x = (int) Math.max(x1 + 1, 0); x <= Math.min(x2, width - 1); x++) {

                double t = (x - x1) / (x2 - x1);
                double z = (bz * (1 - t) + cz * t);
                Vertex v = lerp.lerp(vab,vac,t);
                zBuffer.drawWithTest(x, y, z, shader.getColor(v));
            }
        }
        for (int y = (int) Math.max(by + 1, 0); y <= Math.min(cy, height - 1); y++) {

            double t2 = (y - ay) / (cy - ay);
            double x2 = (ax * (1 - t2) + cx * t2);


            Vertex vac = lerp.lerp(v1,v3,t2);

            double t1 = (y - by) / (cy - by);
            double x1 = (bx * (1 - t1) + cx * t1);



            Vertex vbc = lerp.lerp(v2,v3,t1);

            if (x1 > x2) {
                double tmp = x2;
                x2 = x1;
                x1 = tmp;

                tempv = vac;
                vac = vbc;
                vbc = tempv;
            }

            for (int x = (int) Math.max(x1 + 1, 0); x <= Math.min(x2, width - 1); x++) {
                double t = (x - x1) / (x2 - x1);


                Vertex v = lerp.lerp(vbc,vac,t);
                double z = (bz * (1 - t) + cz * t);

                zBuffer.drawWithTest(x, y, z ,shader.getColor(v));
            }
        }

    }


}