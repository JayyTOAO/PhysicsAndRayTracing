import java.util.ArrayList;
import java.util.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Display {
    private Physics physics;
    private double scale;
    double width = 500;
    double height = 500;
    Circle[] surfaceCircles = new Circle[200];
    Circle[] objects;

    public Display(Physics physics, double scale)
    {
        this.physics = physics;
        this.scale = scale;
        this.objects = new Circle[physics.getSpheres().length];
    }

    public Scene showFrame2D()
    {
        Group root = new Group();
        for(double t = 0; t<100; t++)
        {
            surfaceCircles[(int)t] = new Circle(1);
            double positiony = physics.getSurface().evaluate(t/5);
            double[] position = {t/5, positiony};
            position = adjustCoords(position);
            surfaceCircles[(int)t].setCenterX(position[0]);
            surfaceCircles[(int)t].setCenterY(position[1]);
            root.getChildren().add(surfaceCircles[(int)t]);
        }
        for(double t = 100; t<200; t++)
        {
            surfaceCircles[(int)t] = new Circle(1);
            double positiony = physics.getSurface().evaluate((t-200)/5);
            double[] position = {(t-200)/5, positiony};
            position = adjustCoords(position);
            surfaceCircles[(int)t].setCenterX(position[0]);
            surfaceCircles[(int)t].setCenterY(position[1]);
            root.getChildren().add(surfaceCircles[(int)t]);
        }
        for(int i = 0; i<objects.length; i++)
        {
            objects[i] = new Circle(scale*physics.getSpheres()[i].getRadius());
            root.getChildren().add(objects[i]);
        }
        physics.simulate();
        for(int i = 0; i<physics.getSpheres().length; i++)
        {
            double[] position = physics.getSpheres()[i].getPosition();
            position = adjustCoords(position);
            objects[i].setCenterX(position[0]);
            objects[i].setCenterY(position[1]);
        }
        Scene scene = new Scene(root, 500, 500);
        return scene;

    }

    public double[] adjustCoords(double[] point)
    {
        double[] adjust = {(width/2+scale*point[0]), (0.9*height-scale*point[1])};
        return adjust;
    }


}
