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
import java.util.Random;

public class RTX extends Thread{
    private Physics physics;
    private double[] cameraPosition;
    private double[] lengthWidth;
    private double photonsPerPixel;
    private double bounces;
    private int totalThreads;
    private double currentThread;
    private double[][][] exists;
    private double[][] color;

    public RTX(Physics physics, double photonsPerPixel, double bounces,double[] cameraPosition, double[] lengthWidth,int totalThreads, double currentThread)
    {
        this.physics = physics;
        this.totalThreads = totalThreads;
        this.currentThread = currentThread;
        this.cameraPosition= cameraPosition;
        this.photonsPerPixel = photonsPerPixel;
        this.bounces = bounces;
        this.exists = new double[500/totalThreads][500][3];
        this.lengthWidth = lengthWidth;
    }
    public double[][][] getExists()
    {
        return exists;
    }

    @Override
    public void run()
    {
        generatePixels();
    }
    public void generatePixels()
    {
        double width = 500/totalThreads;
        for(int i = 0; i<photonsPerPixel; i++)
        {
        for(double z = 0; z<width; z++)
        {
            
            for(double x = 0; x<500; x++)
            {
                double[] screenPos = {(lengthWidth[0]/500.0)*x-lengthWidth[0]/2,0,(lengthWidth[1]/500.0)*z-lengthWidth[1]/2+(lengthWidth[1]/totalThreads)*currentThread};
                double[] vector = {screenPos[0]-cameraPosition[0], screenPos[1]-cameraPosition[1], screenPos[2]-cameraPosition[2]};
                double[] rayOrigin = {0,0,0};
                rayOrigin[0] = cameraPosition[0];
                rayOrigin[1] = cameraPosition[1];
                rayOrigin[2] = cameraPosition[2];
                vector = normalize(vector);
                double brightness = 0;
                double[] currentColor = {1,1,1};
                //double[] incomingLight = {0,0,0};
                for(int bounce = 0; bounce<bounces; bounce++)
                {
                    double[] intercept = getIntercept(rayOrigin, vector);
                    if(intercept[3] == 0)
                    {
                        break;
                    }
                    else if(intercept[3] == 1)
                    {
                        double[] specificIntercept = {intercept[0], intercept[1], intercept[2]};
                        double[] normal = physics.getSpheres()[(int)intercept[4]].calculateNormal(specificIntercept);
                        vector = generateRandomVector(normal);
                        brightness+= physics.getSpheres()[(int)intercept[4]].getBrightness();
                        currentColor = physics.getSpheres()[(int)intercept[4]].newColor(currentColor);
                        rayOrigin = intercept;
                    }
                    else 
                    {
                        double[] specificIntercept = {intercept[0], intercept[1], intercept[2]};
                        double[] normal = physics.getSurface().normalVector(specificIntercept);
                        vector = generateRandomVector(normal);
                        brightness+=physics.getSurface().getBrightness();
                        currentColor = physics.getSurface().newColor(currentColor);
                        rayOrigin = intercept;
                    }
                    if(brightness >1)
                    {
                        brightness = 1;
                    }
                }
                //brightness = brightness/photonsPerPixel;
                //exists[(int)z][(int)x] += brightness;}
                exists[(int)z][(int)x][0] += brightness*currentColor[0]/photonsPerPixel;
                exists[(int)z][(int)x][1] += brightness*currentColor[1]/photonsPerPixel;
                exists[(int)z][(int)x][2] += brightness*currentColor[2]/photonsPerPixel;
            }
        }
    }
    }
    public double[] getIntercept(double[] startPos, double[] vector)
    {
        double[] hitInfo = {0,0,0,1,0};
        double[] starting = startPos;
        starting[0] +=0.2*vector[0];
        starting[1] +=0.2*vector[1];
        starting[2] +=0.2*vector[2];
        double minDistance = 1000000;
        double step = 0;
        while(minDistance>0.05 && step<50)
        {
            step++;
            for(int i = 0; i<physics.getSpheres().length; i++)
            {
                if((distance(starting, physics.getSpheres()[i].getPosition())-physics.getSpheres()[i].getRadius())<minDistance)
                {
                    minDistance = distance(starting, physics.getSpheres()[i].getPosition())-physics.getSpheres()[i].getRadius();
                    if(minDistance<0)
                    {
                        minDistance = 0;
                    }
                    hitInfo[3] = 1;
                    hitInfo[4] = i;
                }
            }
            double surfaceDistance = physics.getSurface().closestDistance(starting);
            if(surfaceDistance < minDistance)
            {
                minDistance = surfaceDistance;
                hitInfo[3] = 2;
            }
            starting[0] +=minDistance*vector[0];
            starting[1] +=minDistance*vector[1];
            starting[2] +=minDistance*vector[2];
        }
        if(minDistance>0.1)
        {
            hitInfo[3] = 0;
        }
        hitInfo[0] = starting[0];
        hitInfo[1] = starting[1];
        hitInfo[2] = starting[2];
        return hitInfo;

    }
    public double[] generateRandomVector(double[] normal)
    {
        Random generator = new Random();
        double[] valid_vector = {generator.nextGaussian(), generator.nextGaussian(), generator.nextGaussian()};
        double dot_product = dotProduct(normal, valid_vector);
        if(dot_product < 0)
        {
            valid_vector[0] = -valid_vector[0];
            valid_vector[1] = -valid_vector[1];
            valid_vector[2] = -valid_vector[2];
        }
        valid_vector = normalize(valid_vector);
        return valid_vector;
    }

    public double dotProduct(double[] v1, double[] v2)
    {
        double total = 0;
        for(int i = 0; i<3; i++)
        {
            total+= v1[i]*v2[i];
        }
        return total;
    }

    public double distance(double[] pos1, double[] pos2)
    {
        double distance = 0;
        for(int i = 0; i<3; i++)
        {
            distance+= Math.pow((pos1[i]-pos2[i]),2);
        }
        return Math.sqrt(distance);
    }

    public double[] normalize(double[] vector)
    {
        double length = Math.sqrt(dotProduct(vector, vector));
        double[] normalized = {1/length*vector[0], 1/length*vector[1], 1/length*vector[2]};
        return normalized;
    }

    public Scene showScene(RTX[] threads)
    {
        Group root = new Group();
        Rectangle[][] rectanges = new Rectangle[500][500];
        for(int i = 0; i<threads.length; i++)
        {
            double[][][] pixelData = threads[i].getExists();
            int rowOffset = i*(500/totalThreads);
            for(int row = 0; row<pixelData.length; row++)
            {
                for(int column = 0; column<500; column++)
                {
                    if(pixelData[row][column][0] !=0 || pixelData[row][column][1] !=0 || pixelData[row][column][2] !=0)
                    {
                        rectanges[row+rowOffset][column] = new Rectangle(column, row+rowOffset, 1, 1);
                        rectanges[row+rowOffset][column].setFill(Color.rgb((int)(255*pixelData[row][column][0]), (int)(255*pixelData[row][column][1]), (int)(255*pixelData[row][column][2])));
                        root.getChildren().add(rectanges[row+rowOffset][column]);
                    }
                }
            }
            
        }
        Scene scene = new Scene(root, 500, 500);
        scene.setFill(Color.BLACK);
        return scene;

    }

    public double[] rescale(double[] incoming)
    {
        if(incoming[0]<1 && incoming[1]<1 && incoming[2]<1)
        {
            return incoming;
        }
        double max = 0; 
        for(int i = 0; i<3; i++)
        {
            if(incoming[i]> max)
            {
                max = incoming[1];
            }
        }
        double[] outgoing = {incoming[0]/max, incoming[1]/max, incoming[2]/max};
        return outgoing;
    }

}
