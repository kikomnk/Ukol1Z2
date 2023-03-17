package all.solids;

import all.model.Part;
import all.model.TopologyType;
import all.model.Vertex;
import all.transforms.Col;

import java.util.ArrayList;

public class Axis extends Solid {
    public Axis() {

        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();
        //vertex buffer
        getVB().add(new Vertex(0., 0., 0.,new Col(0xff0000)));//v1
        getVB().add(new Vertex(0., 0., 0.,new Col(0x00ff00)));//v1
        getVB().add(new Vertex(0., 0., 0.,new Col(0x0000ff)));//v1
        getVB().add(new Vertex(0.5, 0., 0.,new Col(0xff0000)));//v2
        getVB().add(new Vertex(0., 0.5, 0.,new Col(0x00ff00)));//v3
        getVB().add(new Vertex(0., 0., 0.5,new Col(0x0000ff)));//v4


        //index buffer
        getIB().add(0);
        getIB().add(3);
        getIB().add(1);
        getIB().add(4);
        getIB().add(2);
        getIB().add(5);
        //part buffer
        getPB().add(new Part(TopologyType.LINE,0,3));

    }
}