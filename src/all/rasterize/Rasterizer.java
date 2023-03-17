package all.rasterize;

import all.model.Vertex;
import all.raster.ZBuffer;
import all.shaders.Shader;
import all.transforms.Vec3D;

public abstract class Rasterizer {
    protected int height,width;
    protected Shader shader;
    ZBuffer zBuffer;

    public Rasterizer(Shader shader, ZBuffer zBuffer) {

        this.shader = shader;
        this.zBuffer = zBuffer;
        height = zBuffer.getHeight();
        width = zBuffer.getWidth();
    }

    public void rasterize(Vertex v1,Vertex v2){

    }
    public void rasterize(Vertex v1,Vertex v2,Vertex v3){

    }


    protected Vec3D transformToWindow(Vertex v1){
        return v1.getPosition().ignoreW()
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((zBuffer.getWidth() - 1) / 2., (zBuffer.getHeight() - 1) / 2., 1));

    }
}
