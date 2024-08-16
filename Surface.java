public class Surface {
    private double damping;
    private double brightness;
    private double[] color;

    public Surface(double damping, double brightness, double[] color)
    {
        this.damping = damping;
        this.brightness = brightness;
        this.color = color;
    }
    public double getDamping()
    {
        return damping;
    }
    public double getBrightness()
    {
        return brightness;
    }

    public double evaluate(double[] point)
    {
        return point[0]*point[0]+point[2]*point[2]-25;
    }
    public boolean isOnSurface(double[] point)
    {
        double eval = evaluate(point);
        if(Math.abs(eval)<0.1)
        {
            return true;
        }
        return false;
    }
    public double[] normalVector(double[] point)
    {
        double[] normal = {-2*point[0], 0, -2*point[2]};
        return normal;
    }
    public double closestDistance(double[] point)
    {
        double minDistance = 5-Math.sqrt(point[0]*point[0]+point[2]*point[2]);
        return minDistance;
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

    public double[] newColor(double[] currentColor)
    {
        double[] outgoing = new double[3];
        outgoing[0] = color[0]*currentColor[0];
        outgoing[1] = color[1]*currentColor[1];
        outgoing[2] = color[2]*currentColor[2];
        return outgoing;
    }
    public double[] addEmission(double[] currentColor)
    {
        double[] outgoing = new double[3];
        outgoing[0] = color[0]*brightness + currentColor[0];
        outgoing[1] = color[1]*brightness +currentColor[1];
        outgoing[2] = color[2]*brightness +currentColor[2];
        return outgoing;
    }
}
