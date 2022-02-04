package org.greygonz.physarumgen;

import org.greygonz.physarumgen.services.Simulation;
import org.greygonz.physarumgen.services.impl.SimulationImpl;
import org.opencv.core.Core;

public class main {

    static int WORLD_WIDTH = 300;
    static int WORLD_HEIGHT = 600;

    public static void main(String[] args) {

        float population = 0.1f;
        float decay_mult = 0.1f;
        float decay_sub = 0.5f;
        long diff_size = Integer.toUnsignedLong(3);

        /* Load OpenCV */
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        /* Preapare simulation */
        Simulation simulation = new SimulationImpl(WORLD_WIDTH, WORLD_HEIGHT);
        simulation.spawn_agents_uniform(population);
        simulation.set_world_decay(decay_mult, decay_sub);
        simulation.set_world_diffusion(diff_size);

        simulation.start_recording("rec/");

        int ticks = 300;
        for(int i = 0; i < ticks; i++) {
            simulation.tick();
            //System.out.println("\nTick: " + i + "\n");
        }

    }
}
