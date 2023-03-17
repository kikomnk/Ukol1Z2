package all.solids;

import all.model.Part;
import all.model.TopologyType;
import all.model.Vertex;
import all.transforms.Bicubic;
import all.transforms.Col;
import all.transforms.Cubic;
import all.transforms.Point3D;

import java.util.ArrayList;

public class Grid extends Solid {
    public Grid() {

        double p = 0.05;
        //transformace
        double v = 1., tx = 0., ty = 0., tz = 0., rx = 0, ry = 0, rz = 0;
        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();
        transformations = new ArrayList<>();

        int row = 0;
        Bicubic bezierBicubic = new Bicubic(Cubic.BEZIER,
                new Point3D(-1.0, 2.0, 0), new Point3D(-1.0, 2.0, 0), new Point3D(-1.0, 2.0, 0), new Point3D(3.0, 2.0, 0.5),
                new Point3D(-1.0, 2.0, 3.0), new Point3D(1.0, 2.0, 4.0), new Point3D(1.0, 0.0, 3.0), new Point3D(1.0, 2.0, 1.0),
                new Point3D(-1.0, 6.0, 4.0), new Point3D(0.0, 5.0, 2.0), new Point3D(0.0, 6.0, 3.0), new Point3D(0.0, 1.0, 4.0),
                new Point3D(-1.0, 4.0, 6.0), new Point3D(3.0, 3.0, 6.0), new Point3D(3.0, 1.0, 3.0), new Point3D(3.0, 2.0, 2.0));


        for (double i = 0.0; i < 1.0; i += p) {
            row++;
            for (double j = 0.0; j < 1.0; j += p) {
                getVB().add(new Vertex(bezierBicubic.compute(i, j).getX(), bezierBicubic.compute(i, j).getY(), bezierBicubic.compute(i, j).getZ(), new Col(0xff00ff)));

            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (i < row - 1) {
                    getIB().add(i * row + j);
                    getIB().add((i + 1) * row + j);
                }
                if (j < row - 1) {
                    getIB().add(i * row + j);
                    getIB().add(i * row + j + 1);
                }
            }
        }
        getPB().add(new Part(TopologyType.LINE, 0, indexBuffer.size()/2));

        transformations.add(v);
        transformations.add(tx);
        transformations.add(ty);
        transformations.add(tz);
        transformations.add(rx);
        transformations.add(ry);
        transformations.add(rz);


    }
}