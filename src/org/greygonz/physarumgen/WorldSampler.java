package org.greygonz.physarumgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldSampler {

    private List<Integer> m_positions = new ArrayList<>();
    private int m_width;
    private int m_height;
    private int m_current_idx;

    public WorldSampler(int width, int height) {

        this.m_width = width;
        this.m_height = height;

        reset_range(m_width, m_height);

    }

    public List<Integer> getM_positions() {
        return m_positions;
    }

    public void setM_positions(List<Integer> m_positions) {
        this.m_positions = m_positions;
    }

    public int getM_width() {
        return m_width;
    }

    public void setM_width(int m_width) {
        this.m_width = m_width;
    }

    public int getM_height() {
        return m_height;
    }

    public void setM_height(int m_height) {
        this.m_height = m_height;
    }

    public int getM_current_idx() {
        return m_current_idx;
    }

    public void setM_current_idx(int m_current_idx) {
        this.m_current_idx = m_current_idx;
    }

    public void reset_range(int width, int height) {

        int num_elements = width * height;

        m_positions.clear();
        for (int i = 0; i < num_elements; i++) {
            m_positions.add(i);
        }

        Collections.shuffle(m_positions);

        m_current_idx = 0;

    }

    public UIVec2 get_next_position() {

        UIVec2 ret_val = new UIVec2(0, 0);

        if (m_current_idx >= m_positions.size()) {

            System.out.println("ERROR: Already sampled all");

        } else {

            int position = m_positions.get(m_current_idx);
            ret_val.setX(position%m_width);
            ret_val.setY(position/m_width);
            m_current_idx++;

        }

        return ret_val;
    }

    @Override
    public String toString() {
        return "WorldSampler{" +
                "m_positions=" + m_positions +
                ", m_width=" + m_width +
                ", m_height=" + m_height +
                ", m_current_idx=" + m_current_idx +
                '}';
    }

}
