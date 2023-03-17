package all.solids;

import all.model.Part;
import all.model.Vertex;
import all.transforms.Mat4;
import all.transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Solid {

    protected ArrayList<Integer> indexBuffer;
    protected ArrayList<Double> transformations;
    protected ArrayList<Vertex> vertexBuffer;



    protected ArrayList<Part> partBuffer;

    public ArrayList<Vertex> getVB() {
        return vertexBuffer;
    }
    public ArrayList<Integer> getIB() {
        return indexBuffer;
    }

    public ArrayList<Part> getPB() {
        return partBuffer;
    }
    private Mat4 model = new Mat4Identity();
    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void setT(ArrayList<Double> transformations) {
        this.transformations = transformations;
    }

    public ArrayList<Double> getT() {
        return transformations;
    }
    protected void addIndicies(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }

}
