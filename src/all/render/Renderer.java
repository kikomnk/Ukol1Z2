package all.render;

import all.model.Part;
import all.model.Vertex;
import all.raster.ZBuffer;
import all.rasterize.LineRasterizer;
import all.rasterize.Rasterizer;
import all.rasterize.TriangleRasterizer;
import all.shaders.Shader;
import all.solids.Solid;
import all.transforms.*;
import all.utils.Lerp;

import java.util.*;

public class Renderer {
    private final Rasterizer lineRasterizer, triangleRasterizer;
    private Mat4 view;
    private Mat4 proj;
    private final Lerp<Vertex> lerp;

    public Renderer(Shader shader, ZBuffer zBuffer) {


        this.lerp = new Lerp<>();
        lineRasterizer = new LineRasterizer(shader, zBuffer);
        triangleRasterizer = new TriangleRasterizer(zBuffer, shader);


    }

    public void renderSolid(Solid solid, boolean isFlat) {
        //mvp(všechny matice)

        Mat4 mvp = solid.getModel().mul(view).mul(proj);
        List<Vertex> mulVB = new ArrayList<>();
        for (Vertex v : solid.getVB()) {
            v = new Vertex(v.getPosition().mul(mvp), v.getColor(), v.getOne());
            mulVB.add(v);
        }

        for (Part p : solid.getPB()) {

            switch (p.getType()) {

                case LINE_STRIP:
                    int start = p.getIndex();
                    for (int i = 0; i < p.getCount(); i++) {


                        Vertex v1 = mulVB.get(solid.getIB().get(start));
                        Vertex v2 = mulVB.get(solid.getIB().get(start + 1));
                        start++;


                        if (!clipLine(v1, v2)) {
                            renderLine(v1, v2);
                        }

                    }
                    break;
                case LINE:
                    start = p.getIndex();

                    for (int i = 0; i < p.getCount(); i++) {
                        Vertex v1 = mulVB.get(solid.getIB().get(start));
                        Vertex v2 = mulVB.get(solid.getIB().get(start + 1));
                        start += 2;
                        if (!clipLine(v1, v2)) {
                            renderLine(v1, v2);
                        }
                    }

                    break;
                case TRIANGLE:
                    start = p.getIndex();

                    for (int i = 0; i < p.getCount(); ++i) {


                        Vertex v1 = mulVB.get(solid.getIB().get(start));
                        Vertex v2 = mulVB.get(solid.getIB().get(start + 1));
                        Vertex v3 = mulVB.get(solid.getIB().get(start + 2));

                        start += 3;

                        if (isFlat) {
                            renderTriangle(v1, v2, v3);

                        } else {
                            renderLine(v1, v2);
                            renderLine(v3, v2);
                            renderLine(v1, v3);
                        }
                    }

            }
        }
    }


    public void renderScene(List<Solid> solids, boolean isFlat) {
        for (Solid solid : solids) {
            renderSolid(solid, isFlat);
        }
    }

    public boolean clipLine(Vertex v1, Vertex v2) {
        double ax = v1.getX();
        double bx = v2.getX();
        double ay = v1.getY();
        double by = v2.getY();
        double az = v1.getZ();
        double bz = v2.getZ();
        double aw = v1.getPosition().getW();
        double bw = v2.getPosition().getW();
        return ((ax < -aw && bx < -bw) ||
                (ax > aw && bx > bw) ||
                (ay < -aw && by < -bw) ||
                (ay > aw && by > bw) ||
                (az < 0 && bz < 0) ||
                (az > aw && bz > bw));


    }

    private void renderLine(Vertex v1, Vertex v2) {
        if (!clipLine(v1, v2)) {

            rasterizeLine(v1, v2);
        }
    }

    private void renderTriangle(Vertex v1, Vertex v2, Vertex v3) {
        if (!clipTriangle(v1, v2, v3)) {

            rasterizeTriangle(v1, v2, v3);
        }
    }

    public boolean clipTriangle(Vertex v1, Vertex v2, Vertex v3) {

        double ax, ay, az, aw, bx, by, bz, bw, cx, cy, cz, cw;
        Point3D posA = v1.getPosition();
        Point3D posB = v2.getPosition();
        Point3D posC = v3.getPosition();
        ax = posA.getX();
        ay = posA.getY();
        az = posA.getZ();
        aw = posA.getW();
        bx = posB.getX();
        by = posB.getY();
        bz = posB.getZ();
        bw = posB.getW();
        cx = posC.getX();
        cy = posC.getY();
        cz = posC.getZ();
        cw = posC.getW();


        return ((ax < -aw && bx < -bw && cx < -cw) ||
                (ax > aw && bx > bw && cx > cw) ||
                (ay < -aw && by < -bw && cy < -cw) ||
                (ay > aw && by > bw && cy > cw) ||
                (az < 0 && bz < 0 && cz < 0) ||
                (az > aw && bz > bw && cz > cw));

    }

    private void rasterizeTriangle(Vertex v1, Vertex v2, Vertex v3) {

        List<Vertex> vec3DList = Arrays.asList(v1, v2, v3);
        Collections.sort(vec3DList, Comparator.comparingDouble(Vertex::getZ));
        v1 = vec3DList.get(2);
        v2 = vec3DList.get(1);
        v3 = vec3DList.get(0);


        if (v1.getPosition().getZ() < 0)
            return;

        if (v2.getPosition().getZ() < 0) {
            double t1 = 0 - v1.getPosition().getZ() / (v2.getPosition().getZ() - v1.getPosition().getZ());
            Vertex ab = lerp.lerp(v1,v2,t1);

            double t2 = 0 - v1.getPosition().getZ() / (v3.getPosition().getZ() - v1.getPosition().getZ());
            Vertex ac = lerp.lerp(v1,v3,t2);

            triangleRasterizer.rasterize(v1, ab, ac);
            return;
        }
        if (v3.getPosition().getZ() < 0) {
            double t1 = 0 - v1.getPosition().getZ() / (v3.getPosition().getZ() - v1.getPosition().getZ());
            Vertex ac = lerp.lerp(v1,v3,t1);
            double t2 = 0 - v2.getPosition().getZ() / (v3.getPosition().getZ() - v2.getPosition().getZ());
            Vertex bc = lerp.lerp(v2,v3,t2);

            triangleRasterizer.rasterize(v1, v2, bc);
            triangleRasterizer.rasterize(v1, ac, bc);
            return;
        }
        triangleRasterizer.rasterize(v1, v2, v3);

    }

    private void rasterizeLine(Vertex v1, Vertex v2) {
        if (v1.getPosition().getZ() < 0)
            return;

        if (v2.getPosition().getZ() < 0) {
            double t = v1.getPosition().getZ() / (v1.getPosition().getZ() - v2.getPosition().getZ());
            Vertex ab = lerp.lerp(v1,v2,t);
            lineRasterizer.rasterize(v1, ab);
            return;
        }

        lineRasterizer.rasterize(v1, v2);
    }

    public void setProjection(Mat4 proj) {
        this.proj = proj;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }
}