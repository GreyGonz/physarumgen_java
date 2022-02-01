package org.greygonz.physarumgen.services.impl;

import org.greygonz.physarumgen.*;
import org.greygonz.physarumgen.services.Simulation;

import java.util.*;

public class SimulationImpl  implements Simulation {

    private List<AgentPhysarum> m_agents = new ArrayList<>();
    private World m_world;
    private WorldSampler m_world_sampler;
    private Recorder m_recorder = new Recorder();
    private int m_ticks = 0;
    private int m_width = 0;
    private int m_height = 0;

    public SimulationImpl(int width, int height) {
        this.m_world = new World(width, height);
        this.m_world_sampler = new WorldSampler(width, height);
        this.m_width = width;
        this.m_height = height;
    }

    @Override
    public void spawn_world(int width, int height) {

        m_recorder.stop_recording();

        m_agents.clear();

        m_world = new World(width, height);
        m_width = width;
        m_height = height;
        m_ticks = 0;

        m_world_sampler.reset_range(width, height);

    }

    @Override
    public void set_world_diffusion(int diff_size) {
        m_world.set_diffusion(diff_size);
    }

    @Override
    public void set_world_decay(float decay_mult, float decay_sub) {
        m_world.set_decay(decay_mult, decay_sub);
    }

    @Override
    public boolean spawn_agents_uniform(float world_precentage) {

        boolean ret_val = true;

        /* Calculate how many agents are requested */
        int world_area = m_width * m_height;
        int num_agents = (int)(world_precentage * world_area);

        if (num_agents >= world_area) {

            System.out.println("Could not spawn agents.");
            return false;

        }

        /* (Re)spawn agents */
        m_agents.clear();

        for (int i = 0; i < num_agents; i++) {

            UIVec2 tmp_pos = m_world_sampler.get_next_position();

            if(m_world.place_agent(tmp_pos)) {

                Long id = (long)i;
                float randomAngle = (float)Math.toRadians(Math.random() * 360); // TODO global function
                FVec2 tmp_vec = new FVec2((float)tmp_pos.getX(), (float)tmp_pos.getY());
                AgentPhysarum agentPhysarum = new AgentPhysarum(id, tmp_vec, randomAngle, m_world);

                m_agents.add(agentPhysarum);

            } else {

                System.out.println("Invalid spawn position");
                ret_val = false;

            }

        }

        System.out.println("Spawned " + m_agents.size() + " agents");
        return ret_val;

    }

    @Override
    public boolean spawn_agents_square(int side) {

        if (side > m_width || side > m_height) {
            System.out.println("Could not spawn agents.");
            return false;
        }

        boolean ret_val = true;
        UIVec2 spawn_corner = new UIVec2((m_width-side)/2, (m_height-side)/2);
        int num_agents = side * side;

        /* (Re)spawn agents */
        m_agents.clear();

        for (int i = 0; i < num_agents; i++) {

            UIVec2 tmp_pos = new UIVec2((spawn_corner.getX() + i%side), (spawn_corner.getY() + i/side));

            if (m_world.place_agent(tmp_pos)) {

                Long id = (long)i;
                float randomAngle = (float) Math.toRadians(Math.random() * 360); // TODO global function
                FVec2 tmp_vec = new FVec2((float) tmp_pos.getX(), (float) tmp_pos.getY());
                AgentPhysarum agentPhysarum = new AgentPhysarum(id, tmp_vec, randomAngle, m_world);

                m_agents.add(agentPhysarum);
            } else {
                System.out.println("Invalid spawn position.");
                ret_val = false;
            }

        }

        System.out.println("Spawned " + m_agents.size() + " agents.");

        return ret_val;
    }

    @Override
    public void tick() {

        Collections.shuffle(m_agents);

        for (AgentPhysarum agent : m_agents) {
            agent.movement_step();
        }

        for (AgentPhysarum agent : m_agents) {
            agent.sensing_step();
        }

        /* Update world */
        m_world.diffuse();
        m_world.decay();

        m_world.display(1);

        if (m_recorder.m_recording) {

            m_recorder.video_add_frame(m_world.get_world_snap());

            if (m_ticks%100 == 0) {
                m_recorder.save_image(m_world.get_world_snap());
            }

        }

        m_ticks++;

    }

    @Override
    public void start_recording(String path) {
        m_recorder.new_recording(2*m_width, m_height, path);
    }

}
