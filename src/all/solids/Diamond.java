package all.solids;

import all.model.Part;
import all.model.TopologyType;
import all.model.Vertex;
import all.transforms.Col;


import java.util.ArrayList;

public class Diamond extends Solid {
    public Diamond() {
        double v=1.,tx=0.,ty=0.,tz=0.,rx=0,ry=0,rz=0;
        // Geometrie
        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();
        transformations = new ArrayList<>();

        getVB().add(new Vertex(-0.2, -0.4, -0.1,new Col(0xff0000)));
        getVB().add(new Vertex(0, -0.6, 0.3,new Col(0x00ff00)));
        getVB().add(new Vertex(-0.4, -0.6, 0.3,new Col(0x00ff00)));
        getVB().add(new Vertex(-0.4, -0.2, 0.3,new Col(0x00ff00)));
        getVB().add(new Vertex(0, -0.2, 0.3,new Col(0x00ff00)));
        getVB().add(new Vertex(-0.3, -0.5, 0.4,new Col(0x0000ff)));
        getVB().add(new Vertex(-0.3, -0.3, 0.4,new Col(0x0000ff)));
        getVB().add(new Vertex(-0.1, -0.5, 0.4,new Col(0x0000ff)));
        getVB().add(new Vertex(-0.1, -0.3, 0.4,new Col(0x0000ff)));





        // Topologie

        addIndicies(0,1,2);
        addIndicies(0,2,3);
        addIndicies(0,3,4);
        addIndicies(0,4,1);
        addIndicies(1,2,7);
        addIndicies(2,3,5);
        addIndicies(3,4,6);
        addIndicies(4,1,8);
        addIndicies(7,5,2);
        addIndicies(5,6,3);
        addIndicies(6,8,4);
        addIndicies(8,7,1);
        addIndicies(8,7,6);
        addIndicies(7,6,5);

        //partBuffer
        getPB().add(new Part(TopologyType.TRIANGLE,0,14));

        transformations.add(v);
        transformations.add(tx);
        transformations.add(ty);
        transformations.add(tz);
        transformations.add(rx);
        transformations.add(ry);
        transformations.add(rz);


    }

}