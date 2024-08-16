public class Sphere {
    private double radius;
    private double mass;
    private double brightness;
    private double[] position;
    private double[] velocity;
    private double[] color; 

    public Sphere(double radius, double mass, double brightness, double[] color, double[] position, double[] velocity)
    {
        this.radius = radius;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.brightness = brightness;
        this.color = color;
    }
    public double getRadius()
    {
        return radius;
    }
    public void setVelocity(double[] velo)
    {
        velocity = velo;
    }
    public double[] getVelocity()
    {
        return velocity;
    }
    public double getBrightness()
    {
        return brightness;
    }
    public void setPosition(double[] pos)
    {
        position = pos;
    }
    public double[] getPosition()
    {
        return position;
    }
    public double getMass()
    {
        return mass;
    }

    public void stepForward(double t)
    {
        for(int i = 0; i<position.length; i++)
        {
            position[i] = position[i] + t*velocity[i];
        }
    }
    public double[] stepForwardTest(double t)
    {
        double[] positionTest = new double[position.length];
        for(int i = 0; i<position.length; i++)
        {
            positionTest[i] = position[i] + t*velocity[i];
        }
        return positionTest;
    }
    public double[] stepForwardTest(double[] v, double t)
    {
        double[] positionTest = new double[position.length];
        for(int i = 0; i<position.length; i++)
        {
            positionTest[i] = position[i] + t*v[i];
        }
        return positionTest;
    }

    public void calculateCollisionGround2D(Surface surface)
    {
        boolean touching = false;
        double[] pos= {0,0,0};
        for(double q = 0; q<100; q++)
        {
            for(double p = 0; p<100; p++)
            {
                pos[0] = radius*Math.sin(6.28*p/300)*Math.cos(6.28*q/300)+position[0];
                pos[1] = radius*Math.sin(6.28*p/300)*Math.sin(6.28*q/300)+position[1];
                pos[2] = radius*Math.cos(6.28*p/300)+position[2];
                if(surface.isOnSurface(pos))
                {
                    touching = true;
                    break;
                }
            }
            if(touching)
            {
                break;
            }
        }
        double[] normal = surface.normalVector(pos);
        if(touching && dotProduct(normal, velocity) < 0)
        {
            double fraction  = -2*dotProduct(velocity, normal)/dotProduct(normal, normal);
            double[] E = scalar(normal,fraction);
            E[0] = E[0] + velocity[0];
            E[1] = E[1] + velocity[1];
            E[2] = E[2] + velocity[2];
            E = scalar(E, (1-surface.getDamping()));
            this.velocity = E;
        }
    }
    public double[] calculateCollisionBall(Sphere ball2, double t)
    {
        double distance = distance(position, ball2.getPosition());
        if(distance<= radius+ball2.getRadius())
        {
            double[] unitForce = new double[velocity.length];
            for(int i = 0; i<unitForce.length; i++)
            {
                unitForce[i] = position[i]-ball2.getPosition()[i];
            }
            unitForce = scalar(unitForce, 1/Math.sqrt(dotProduct(unitForce, unitForce)));
            double f = 2*(dotProduct(velocity,unitForce)-dotProduct(ball2.getVelocity(), unitForce));
            f = f/(1/mass+1/ball2.getMass());
            f = Math.abs(f);
            this.velocity = vectorAddition(velocity, scalar(unitForce, f));
            double[] vector2 = vectorSubtraction(ball2.getVelocity(), scalar(unitForce, f));
            return vector2;
        }
        return ball2.getVelocity();
    }

    public double dotProduct(double[] v1, double[] v2)
    {
        double total = 0;
        for(int i = 0; i<v1.length; i++)
        {
            total+= v1[i]*v2[i];
        }
        return total;
    }

    public double[] vectorSubtraction(double[] v1, double[] v2)
    {
        double[] v = new double[v1.length];
        for(int i = 0; i<v.length; i++)
        {
            v[i] = v1[i]-v2[i];
        }
        return v;
    }

    public double[] vectorAddition(double[] v1, double[] v2)
    {
        double[] v = new double[v1.length];
        for(int i = 0; i<v.length; i++)
        {
            v[i] = v1[i]+v2[i];
        }
        return v;
    }

    public double[] scalar(double[] v, double scale)
    {
        double[] vCopy = new double[v.length];
        for(int i = 0; i<v.length; i++)
        {
            vCopy[i] = v[i]*scale;
        }
        return vCopy;
    }

    public double distance(double[] pos1, double[] pos2)
    {
        double distance = 0;
        for(int i = 0; i<pos1.length; i++)
        {
            distance+= Math.pow((pos1[i]-pos2[i]),2);
        }
        return Math.sqrt(distance);
    }

    public double[] calculateNormal(double[] point)
    {
        double[] normal = {2*(point[0]-position[0]), 2*(point[1]-position[1]),2*(point[2]-position[2])};
        return normal;
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
        if(brightness == 0)
        {
            return currentColor; 
        }
        double[] outgoing = new double[3];
        outgoing[0] = color[0]*brightness + currentColor[0];
        outgoing[1] = color[1]*brightness +currentColor[1];
        outgoing[2] = color[2]*brightness +currentColor[2];
        return outgoing;
    }
    public double[] getEmittedLight()
    {
        double[] emittedLight = {color[0]*brightness, color[1]*brightness, color[2]*brightness};
        return emittedLight;
    }

    public void print()
    {
        System.out.println("Velocity: "+velocity[0]+", "+velocity[1]+ "  Position: "+position[0]+", "+position[1]);
    }
}
