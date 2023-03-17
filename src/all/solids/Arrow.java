package all.solids;

import all.model.Part;
import all.model.TopologyType;
import all.model.Vertex;
import all.transforms.Col;

import java.util.ArrayList;

public class Arrow extends Solid {
    public Arrow(){
        //transformace
        double v=1.,tx=0.,ty=0.,tz=0.,rx=0,ry=0,rz=0;
        //vertex buffer
        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();
        transformations = new ArrayList<>();

        getVB().add(new Vertex(0.1,0.,0.,new Col(0xff0000)));//v1
        getVB().add(new Vertex(0.2,0,0,new Col(0xff0000)));//v2
        getVB().add(new Vertex(0.2,0.8,0.5,new Col(0xff0000)));//v3
        getVB().add(new Vertex(0.5,0.8,0.5,new Col(0xff0000)));//v4
        getVB().add(new Vertex(0.15,1,0.6,new Col(0xff0000)));//v5
        getVB().add(new Vertex(-0.3,0.8,0.5,new Col(0xff0000)));//v6
        getVB().add(new Vertex(0.1,0.8,0.5,new Col(0xff0000)));//v7

        //index buffer
        addIndicies(0,1,2);
        addIndicies(3,4,5);
        addIndicies(6,0);

        /*
        addIndicies(0,1,3);
        addIndicies(0,1,3);
        addIndicies(0,1,3);*/


        //part buffer
        getPB().add(new Part(TopologyType.LINE_STRIP,0,2));
        getPB().add(new Part(TopologyType.TRIANGLE,3,1));
        getPB().add(new Part(TopologyType.LINE_STRIP,6,1));
        // transformace
        transformations.add(v);
        transformations.add(tx);
        transformations.add(ty);
        transformations.add(tz);
        transformations.add(rx);
        transformations.add(ry);
        transformations.add(rz);


    }
}