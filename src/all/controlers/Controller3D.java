package all.controlers;


import all.raster.ImageBuffer;
import all.raster.Raster;
import all.raster.ZBuffer;

import all.render.Renderer;

import all.shaders.ShaderInterpolation;
import all.solids.*;
import all.transforms.*;

import all.view.Panel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class Controller3D implements Controller {
    private final Panel panel;

    private final ZBuffer zBuffer;
    List<Solid> scene = new ArrayList<>();


    Axis axis = new Axis();
    Arrow arrow = new Arrow();
    Diamond diamond = new Diamond();
    Triangle triangle = new Triangle();
    Grid grid = new Grid();

    private final Raster raster;

    // výběr tělesa
    private enum Teleso {ARROW, GRID, DIAMOND}

    // typ transformace
    private enum Transformace {SCALE, TRANSLATE, ROTATE}

    //osa transformace(po,podle)
    private enum Osy {X, Y, Z}

    //proměnné pro transformace objektů


    //pro animaci
    private Thread animation;
    private boolean a = false;
    private double ap = 0;
    // přepínání modelu
    private boolean isFlat = true;


    private final Renderer renderer;
    //camera
    private Camera camera;
    //pro posun kamery
    private int oldX = 0, oldY = 0;
    // projekce
    boolean isPer = true;


    public Controller3D(Panel panel) {

        this.panel = panel;
        this.raster = panel.getRaster();
        this.zBuffer = new ZBuffer(panel.getRaster());

        renderer = new Renderer(new ShaderInterpolation(), zBuffer);

        final Teleso[] vybraneTeleso = new Teleso[1];
        final Transformace[] vybranaTransformace = new Transformace[1];
        final Osy[] vybranaOsa = new Osy[1];
        // naplnění hodnotamy aby program nespadl při nezvolení 1(třeba osy)
        vybraneTeleso[0] = Teleso.ARROW;
        vybranaTransformace[0] = Transformace.SCALE;
        vybranaOsa[0] = Osy.X;


        scene.add(axis);
        scene.add(arrow);
        scene.add(triangle);
        scene.add(diamond);
        scene.add(grid);
        // camera
        camera = new Camera()
                .withPosition(new Vec3D(3, 0, 0))
                .withAzimuth(Math.PI)
                .withZenith(0)
                .withFirstPerson(true);


        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                oldX = e.getX();
                oldY = e.getY();

            }

        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int dX = oldX - e.getX();
                int dY = oldY - e.getY();


                double azimuth = (dX / (float) raster.getWidth()) * Math.PI;


                double zenith = (dY / (float) raster.getHeight()) * Math.PI;


                camera = camera.addAzimuth(azimuth);
                camera = camera.addZenith(zenith);

                oldX = e.getX();
                oldY = e.getY();
                redraw();


            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // kamera
                    case KeyEvent.VK_W:
                        camera = camera.forward(0.05);

                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(0.05);
                        break;
                    case KeyEvent.VK_A:
                        camera = camera.left(0.05);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(0.05);
                        break;
                    case KeyEvent.VK_F:
                        camera = camera.up(0.05);
                        break;
                    case KeyEvent.VK_G:
                        camera = camera.down(0.05);
                        break;
                    // přepínání pohledu
                    case KeyEvent.VK_P:
                        isPer = !isPer;
                        break;
                    case KeyEvent.VK_O:
                        isFlat = !isFlat;
                        break;
                    case KeyEvent.VK_NUMPAD1:
                        vybraneTeleso[0] = Teleso.ARROW;
                        break;

                    case KeyEvent.VK_NUMPAD2:
                        vybraneTeleso[0] = Teleso.DIAMOND;
                        break;
                    case KeyEvent.VK_NUMPAD3:
                        vybraneTeleso[0] = Teleso.GRID;
                        break;


                    case KeyEvent.VK_V:
                        vybranaTransformace[0] = Transformace.SCALE;
                        break;
                    case KeyEvent.VK_T:
                        vybranaTransformace[0] = Transformace.TRANSLATE;
                        break;
                    case KeyEvent.VK_R:
                        vybranaTransformace[0] = Transformace.ROTATE;
                        break;
                    case KeyEvent.VK_X:
                        vybranaOsa[0] = Osy.X;
                        break;
                    case KeyEvent.VK_Y:
                        vybranaOsa[0] = Osy.Y;
                        break;
                    case KeyEvent.VK_Z:
                        vybranaOsa[0] = Osy.Z;
                        break;

                    case KeyEvent.VK_SUBTRACT:
                        switch (vybraneTeleso[0]) {
                            case ARROW:
                                switch (vybranaTransformace[0]) {
                                    case SCALE:
                                        arrow.getT().set(0, arrow.getT().get(0) - 0.1);
                                        break;
                                    case TRANSLATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> arrow.getT().set(1, arrow.getT().get(1) - 0.1);
                                            case Y -> arrow.getT().set(2, arrow.getT().get(2) - 0.1);
                                            case Z -> arrow.getT().set(3, arrow.getT().get(3) - 0.1);
                                        }
                                        break;
                                    case ROTATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> arrow.getT().set(4, arrow.getT().get(4) - 0.1);
                                            case Y -> arrow.getT().set(5, arrow.getT().get(5) - 0.1);
                                            case Z -> arrow.getT().set(6, arrow.getT().get(6) - 0.1);
                                        }
                                        break;

                                }
                                break;
                            case GRID:
                                switch (vybranaTransformace[0]) {
                                    case SCALE:
                                        grid.getT().set(0, grid.getT().get(0) - 0.1);
                                        break;
                                    case TRANSLATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> grid.getT().set(1, grid.getT().get(1) - 0.1);
                                            case Y -> grid.getT().set(2, grid.getT().get(2) - 0.1);
                                            case Z -> grid.getT().set(3, grid.getT().get(3) - 0.1);
                                        }
                                        break;
                                    case ROTATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> grid.getT().set(4, grid.getT().get(4) - 0.1);
                                            case Y -> grid.getT().set(5, grid.getT().get(5) - 0.1);
                                            case Z -> grid.getT().set(6, grid.getT().get(6) - 0.1);
                                        }
                                        break;

                                }
                                break;
                            case DIAMOND:
                                switch (vybranaTransformace[0]) {
                                    case SCALE:
                                        diamond.getT().set(0, diamond.getT().get(0) - 0.1);
                                        break;
                                    case TRANSLATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> diamond.getT().set(1, diamond.getT().get(1) - 0.1);
                                            case Y -> diamond.getT().set(2, diamond.getT().get(2) - 0.1);
                                            case Z -> diamond.getT().set(3, diamond.getT().get(3) - 0.1);
                                        }
                                        break;

                                    case ROTATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> diamond.getT().set(4, diamond.getT().get(4) - 0.1);
                                            case Y -> diamond.getT().set(5, diamond.getT().get(5) - 0.1);
                                            case Z -> diamond.getT().set(6, diamond.getT().get(6) - 0.1);
                                        }
                                        break;

                                }
                                break;
                        }
                        break;

                    case KeyEvent.VK_ADD:
                        switch (vybraneTeleso[0]) {
                            case ARROW:

                                switch (vybranaTransformace[0]) {
                                    case SCALE:
                                        arrow.getT().set(0, arrow.getT().get(0) + 0.1);
                                        break;

                                    case TRANSLATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> arrow.getT().set(1, arrow.getT().get(1) + 0.1);
                                            case Y -> arrow.getT().set(2, arrow.getT().get(2) + 0.1);
                                            case Z -> arrow.getT().set(3, arrow.getT().get(3) + 0.1);
                                        }
                                        break;


                                    case ROTATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> arrow.getT().set(4, arrow.getT().get(4) + 0.1);
                                            case Y -> arrow.getT().set(5, arrow.getT().get(5) + 0.1);
                                            case Z -> arrow.getT().set(6, arrow.getT().get(6) + 0.1);
                                        }

                                        break;


                                }
                                break;
                            case GRID:
                                switch (vybranaTransformace[0]) {
                                    case SCALE:
                                        grid.getT().set(0, grid.getT().get(0) + 0.1);
                                        break;
                                    case TRANSLATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> grid.getT().set(1, grid.getT().get(1) + 0.1);
                                            case Y -> grid.getT().set(2, grid.getT().get(2) + 0.1);
                                            case Z -> grid.getT().set(3, grid.getT().get(3) + 0.1);
                                        }
                                        break;
                                    case ROTATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> grid.getT().set(4, grid.getT().get(4) + 0.1);
                                            case Y -> grid.getT().set(5, grid.getT().get(5) + 0.1);
                                            case Z -> grid.getT().set(6, grid.getT().get(6) + 0.1);
                                        }
                                        break;

                                }
                                break;
                            case DIAMOND:
                                switch (vybranaTransformace[0]) {
                                    case SCALE:
                                        diamond.getT().set(0, diamond.getT().get(0) + 0.1);
                                        break;
                                    case TRANSLATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> diamond.getT().set(1, diamond.getT().get(1) + 0.1);
                                            case Y -> diamond.getT().set(2, diamond.getT().get(2) + 0.1);
                                            case Z -> diamond.getT().set(3, diamond.getT().get(3) + 0.1);
                                        }
                                        break;

                                    case ROTATE:
                                        switch (vybranaOsa[0]) {
                                            case X -> diamond.getT().set(4, diamond.getT().get(4) + 0.1);
                                            case Y -> diamond.getT().set(5, diamond.getT().get(5) + 0.1);
                                            case Z -> diamond.getT().set(6, diamond.getT().get(6) + 0.1);
                                        }
                                        break;

                                }

                        }
                        break;

                    case KeyEvent.VK_M:

                        a = !a;
                        animation = new Thread(() -> {
                            while (a) {
                                ap += 0.1;
                                redraw();
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ex) {
                                    System.out.println("Chyba");
                                }
                            }
                        });
                        animation.start();
                        redraw();
                        break;

                }
                redraw();
            }

        });


        initObjects(panel.getRaster());

        initListeners();

        redraw();


    }


    public void initObjects(ImageBuffer raster) {
        raster.setClearValue(new Col(0x101010));
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void redraw() {
        zBuffer.clear();
        panel.clear();
        Mat4 proj;
        if (isPer) {
            proj = new Mat4PerspRH(Math.toRadians(60), raster.getHeight() / (float) raster.getWidth(), 0.1f, 200f);

        } else {
            proj = new Mat4OrthoRH(Math.toRadians(60), raster.getHeight() / (float) raster.getWidth(), 0.1f, 200f);
        }

        renderer.setProjection(proj);
        renderer.setView(camera.getViewMatrix());


        arrow.setModel(new Mat4Scale(arrow.getT().get(0)).mul(new Mat4Transl(arrow.getT().get(1), arrow.getT().get(2), arrow.getT().get(3)).mul(new Mat4RotXYZ(arrow.getT().get(4), arrow.getT().get(5), arrow.getT().get(6)))));
        diamond.setModel(new Mat4Scale(diamond.getT().get(0)).mul(new Mat4Transl(diamond.getT().get(1), diamond.getT().get(2), diamond.getT().get(3)).mul(new Mat4RotXYZ(diamond.getT().get(4), diamond.getT().get(5), (diamond.getT().get(6) + ap)))));
        grid.setModel(new Mat4Scale(grid.getT().get(0)).mul(new Mat4Transl(grid.getT().get(1), grid.getT().get(2), grid.getT().get(3)).mul(new Mat4RotXYZ(grid.getT().get(4), grid.getT().get(5), grid.getT().get(6)))));

        renderer.renderScene(scene, isFlat);
        panel.repaint();
    }
}
