package all.raster;


import all.transforms.Col;

public class ZBuffer {
    private final ImageBuffer imageBuffer;
    private final DepthBuffer depthBuffer;
    public ZBuffer(ImageBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;


        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());


    }
    public void drawWithTest(int x, int y, double z, Col color){

        double actualZ = depthBuffer.getValue(x,y);

        if(z<actualZ){
            depthBuffer.setValue(x,y,z);
            imageBuffer.setValue(x,y,color);
        }

    }


    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }
    public void clear(){
        imageBuffer.clear();
        depthBuffer.clear();
    }

}