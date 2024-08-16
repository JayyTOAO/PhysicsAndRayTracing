public class Physics {
    private double deltaT;
    private Sphere[] spheres;
    private Surface surface;
    private double gravity;

    public Physics(double deltaT, Sphere[] spheres, Surface surface, double gravity)
    {
        this.deltaT = deltaT;
        this.spheres = spheres;
        this.surface = surface;
        this.gravity = gravity;
    }

    public Sphere[] getSpheres()
    {
        return spheres;
    }
    public Surface getSurface()
    {
        return surface;
    }

    public void simulate()
    {
        for(int j = 0; j<spheres.length; j++)
        {
            spheres[j].calculateCollisionGround2D(surface);
            for(int i = j+1; i<spheres.length; i++)
            {
                double[] newVelo = spheres[j].calculateCollisionBall(spheres[i], deltaT);
                spheres[i].setVelocity(newVelo);

            }
            double[] velocity = spheres[j].getVelocity();
            velocity[2] += gravity*deltaT;
            spheres[j].setVelocity(velocity);
            spheres[j].stepForward(deltaT);
        }
    }
}
