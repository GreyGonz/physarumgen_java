package org.greygonz.physarumgen.services;

public interface Simulation {

    public void spawn_world(int width, int height);

    public void set_world_diffusion(int diff_size);

    public void set_world_decay(float decay_mult, float decay_sub);

    public boolean spawn_agents_uniform(float world_precentage);

    public boolean spawn_agents_square(int side);

    public void tick();

    public void start_recording(String path);

}
