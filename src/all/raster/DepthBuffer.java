package all.raster;



public class DepthBuffer implements Raster<Double> {
    private final int width, height;
    private final double[][] buffer;
    private double clearvalue =1;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new double[width][height]; for(int i=0;i<width;i++) {
            for (int j = 0;j < height; j++) {
                setValue(i, j, clearvalue);
            }
        }
    }

    @Override
    public void clear() {
        for(int i=0;i<width;i++) {
            for (int j = 0;j < height; j++) {
                setValue(i, j, clearvalue);
            }
        }
    }

    @Override
    public void setClearValue(Double value) {
        clearvalue = value;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Double getValue(int x, int y) {
        if(isInside(x,y)) {
            return buffer[x][y];
        }else{
            return 0.;
        }
    }

    @Override
    public void setValue(int x, int y, Double value) {
        if(isInside(x,y))
            buffer[x][y] = value;
    }


}