package org.greygonz.physarumgen.services;

import org.greygonz.physarumgen.FVec2;

public interface Agent {

    public void print_info();

    public void movement_step();

    public void sensing_step();

}
