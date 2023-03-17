package all.solids;

import all.model.Part;
import all.model.TopologyType;
import all.model.Vertex;
import all.transforms.Col;

import java.util.ArrayList;

public class Triangle extends Solid{
    public Triangle(){
        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();
        //vertex buffer
        getVB().add(new Vertex(0., 0., 0.,new Col(0xff0000)));//v1
        getVB().add(new Vertex(-0.2, 0, 0.25,new Col(0x00ff00)));//v2
        getVB().add(new Vertex(0, -0.1, 0.3,new Col(0x0000ff)));//v3



        //index buffer
        getIB().add(0);
        getIB().add(1);
        getIB().add(2);



        //part buffer
        getPB().add(new Part(TopologyType.TRIANGLE,0,1));
    }
}
