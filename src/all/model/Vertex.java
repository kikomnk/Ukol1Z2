package all.model;

import all.transforms.Col;

import all.transforms.Point3D;


public class Vertex implements Vectorizable<Vertex>{
    private final Point3D position;
    private Col color;
    private final double one;

    private double x,y,z;



    // TODO: persp. korektní interpolace ... co s tím?

    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x, y, z);
        this.color = new Col(color);
        this.one = 1;
    }
    public Vertex(double x, double y, double z,Col color) {
        this.position = new Point3D(x, y, z);
        this.color = new Col(color);
        this.one = 1;
    }

    public Vertex(Point3D position,Col color,double one){
        this.position = new Point3D(position);
        this.color = color;
        this.one = one;
    }


    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }
    public Double getX(){
        return x;
    }
    public Double getY(){
        return y;
    }
    public Double getZ(){
        return z;
    }
    public Double getW() {
        return position.getW();
    }


    @Override
    public Vertex mul(double k) {
            return new Vertex(position.mul(k),color.mul(k).saturate(),one*k);
    }

    @Override
    public Vertex add(Vertex v) {

        return new Vertex(position.add(v.getPosition()),new Col(color.getR() + v.getColor().getR(), color.getG() + v.getColor().getG(), color.getB() + v.getColor().getB()),one+v.one);

    }
    public double getOne(){return one;}
    public Col getCol(){return color;}



}
