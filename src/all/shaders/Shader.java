package all.shaders;

import all.model.Vertex;
import all.transforms.Col;


 public interface Shader {
    Col getColor (Vertex v);
}
