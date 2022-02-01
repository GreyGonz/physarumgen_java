package org.greygonz.physarumgen.services.impl;

import org.greygonz.physarumgen.FVec2;
import org.greygonz.physarumgen.World;
import org.greygonz.physarumgen.services.Agent;

import java.util.Random;

public class AgentPhysarum implements Agent {

    private Long id;
    protected FVec2 position;
    protected float alpha;
    protected World m_world;

    private float sensor_angle = 45.0f;
    private float sensor_offset = 9.0f;
    private float rotation_angle = 45.0f;
    private float step_size = 1.0f;
    private short deposition_value = 5;

    public AgentPhysarum(Long id, FVec2 position, float alpha, World m_world) {
        this.id = id;
        this.position = position;
        this.alpha = alpha;
        this.m_world = m_world;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FVec2 getPosition() {
        return position;
    }

    public void setPosition(FVec2 position) {
        this.position = position;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public World getM_world() {
        return m_world;
    }

    public void setM_world(World m_world) {
        this.m_world = m_world;
    }

    @Override
    public void print_info() {
        // TODO
    }

    @Override
    public void movement_step() {

        FVec2 destination = calc_destination(position, alpha, step_size);

        if (m_world.same_cell(position, destination)) {
            position.setX(destination.getX());
            position.setY(destination.getY());
        } else if (m_world.move_agent(position, destination)) {
            position.setX(destination.getX());
            position.setY(destination.getY());
            m_world.deposit_trail(position, deposition_value);
        } else {
            float randomAngle = (float)Math.toRadians(Math.random() * 360); // TODO global function
            alpha = randomAngle;
        }

    }

    @Override
    public void sensing_step() {

        /* Calculate sensor positions */
        FVec2 sensor_center = calc_destination(position, alpha, sensor_offset);
        FVec2 sensor_left = calc_destination(position, alpha - sensor_angle, sensor_offset);
        FVec2 sensor_right = calc_destination(position, alpha + sensor_angle, sensor_offset);

        /* Sample sensor readings */
        short reading_center = m_world.sense_world(sensor_center);
        short reading_left = m_world.sense_world(sensor_left);
        short reading_right = m_world.sense_world(sensor_right);

        /* Adjust agent's direction */
        if (reading_center > reading_left && reading_center > reading_right) {
            /* Do nuthin */
        } else if (reading_center < reading_left && reading_center < reading_right) {

            Random random = new Random(5); // TODO function random_bool()?
            int direction = random.nextBoolean() ? 1 : -1;
            alpha += direction * rotation_angle;

        } else if (reading_left < reading_right) {
            alpha += rotation_angle;
        } else if (reading_left > reading_right) {
            alpha -= rotation_angle;
        } else {
            /* Do nuthin */
        }

    }

    public FVec2 calc_destination(FVec2 start_pt, float angle, float distance) {

        float pos_x = (float)(start_pt.getX() + distance * Math.cos((angle) * Math.PI / 180.0f));
        float pos_y = (float)(start_pt.getY() + distance * Math.sin((angle) * Math.PI / 180.0f));
        FVec2 ret_val = new FVec2(pos_x, pos_y);

        return ret_val;

    }

}
