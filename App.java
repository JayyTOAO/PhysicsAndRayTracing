import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class App extends Application{

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage stage){
        double[] pos = {2,5,-2}; 
        double[] pos1 = {-2,5,-2};
        double[] pos2 = {0,14};
        double[] velo = {0,0,0};
        double[] velo1 = {0,0,0};
        double[] velo2 = {0,-1};
        double[] color = {1,1,1};
        double[] color1 = {1,1,1};
        double[] color2 = {1,1,1};
        Surface surface = new Surface(0,0, color2);
        Sphere sphere = new Sphere(1,1,0,color,pos,velo);
        Sphere sphere1 = new Sphere(2,1,1,color1, pos1,velo1);
        Sphere sphere2 = new Sphere(1.3,1,0,color2, pos2,velo2);

        Sphere[] spheres = {sphere1, sphere};
        Physics physics = new Physics(.03, spheres, surface,10);
        double[] cameraPosition = {0,-10,0};
        double[] lengthWidth = {10,10};
        int threads = 1;
        RTX[] rtxs = new RTX[threads];
        
        for(double i = 0; i<threads; i++)
        {
            rtxs[(int)i] = new RTX(physics, 1,2,cameraPosition, lengthWidth,threads, i);
            rtxs[(int)i].start();
        }
         
        for (int i = 0; i<threads; i++)
        {
            try {
                rtxs[i].join();
            } catch (InterruptedException e) {
            }
        }
            
        
        Scene scene = rtxs[0].showScene(rtxs);
        Stage myStage = new Stage();
        myStage.setScene(scene);
        myStage.show();

        EventHandler<KeyEvent> eventHandler1 = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                RTX[] rtxs = new RTX[threads];
                physics.simulate();
                for(double i = 0; i<threads; i++)
                {
                    rtxs[(int)i] = new RTX(physics, 1,2,cameraPosition, lengthWidth,threads, i);
                    rtxs[(int)i].start();
                }
                for (int i = 0; i<threads; i++)
                {
                    try {
                        rtxs[i].join();
                    } catch (InterruptedException q) {
                }
                }
                Scene scene = rtxs[0].showScene(rtxs);
                myStage.setScene(scene);
                myStage.show();
            }
        };
        myStage.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler1);

    }

    }
