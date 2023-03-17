package all.shaders;

import all.model.Vertex;
import all.transforms.Col;

public class ShaderInterpolation implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return v.getColor();
    }
}
