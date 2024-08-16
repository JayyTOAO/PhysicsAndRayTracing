public class Marching {
    public Marching()
    {
        
    }
    public static double[] potential_closest(double[] current_point)
    {
        double x = current_point[0];
        double y = current_point[1];
        double z = current_point[2];
        double r = 0;
        while(r<5)
        {
            double t = 6.28;
            while(t>0)
            {
                double xZ = r*Math.cos(t)+x;
                double yZ = r*Math.sin(t)+y;
                double zZ = evaluate(xZ,yZ);

                double aFx = -(x-xZ);
                double aFy = -(y-yZ);
                double aFz = -(z-zZ);
                double[] aF = {aFx, aFy, aFz};

                double[] grad = gradient(xZ, yZ, zZ);
                if(sameLine(grad, aF))
                {
                    double[] closest = {xZ, yZ, zZ};
                    return closest;
                }
                t-=.01;

            }
            r += .01;
        }
        double[]  none= {0,0,0};
        return none;
    }
    public static double evaluate(double x, double y)
    {
        //double z = Math.sin(x)*Math.cos(y);
        //double z = 2*x;
        double z = x*x*y*y;
        return z;
    }

    public static double[] gradient(double x, double y, double z)
    {
        //double[] grad = {Math.cos(y)*Math.cos(x), -Math.sin(x)*Math.sin(y),-1};
        //double[] grad = {2,0,-1};
        double[] grad = {2*y*y*x, 2*x*x*y, -1};
        return grad;
    }

    public static boolean sameLine(double[] v1, double[] v2)
    {
        double v1Mag = Math.sqrt(dotProduct(v1, v1));
        double v2Mag = Math.sqrt(dotProduct(v2, v2));
        double dot = dotProduct(v1, v2);
        double angle = Math.acos(dot/(v1Mag*v2Mag));
        if(angle < .01)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public static double dotProduct(double[] v1, double[] v2)
    {
        return v1[0]*v2[0]+ v1[1]*v2[1]+ v1[2]*v2[2];
    }
}
