package all.shaders;

import all.model.Vertex;
import all.transforms.Col;


public class ShaderConstantColor implements Shader{

    @Override
    public Col getColor(Vertex v) {
        return new Col(0xff0000).mul(1/v.getOne());
    }
}
