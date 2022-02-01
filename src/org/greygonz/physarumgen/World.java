package org.greygonz.physarumgen;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.List;

import static org.opencv.highgui.HighGui.*;

public class World {

    private Mat m_world_grid = new Mat();
    private Mat m_trail_grid;
    private Mat m_diff_kernel;
    private float m_decay_mult;
    private float m_decay_sub;
    private boolean m_do_diffusion = false;
    private boolean m_do_decay = false;
    private int m_width;
    private int m_height;

    public Mat getM_world_grid() {
        return m_world_grid;
    }

    public World(int width, int height) {

        this.m_width = width;
        this.m_height = height;
        this.m_world_grid = new Mat(height, width, CvType.CV_8UC1, new Scalar(0));
        this.m_trail_grid = new Mat(height, width, CvType.CV_16UC1, new Scalar(0));

        namedWindow("world");
        moveWindow("world", 500, 300);
        namedWindow("trail");
        moveWindow("trail", m_width + 500, 300);

    }

    public boolean place_agent(UIVec2 position) {

        boolean ret_val = false;

        if(check_inbounds(position) && check_cell_empty(position)) {

            m_world_grid.put(position.getY(), position.getX(), 255);

            ret_val = true;

        }

        return ret_val;

    }

    public boolean move_agent(FVec2 from, FVec2 to) {
        return move_agent(to_grid(from), to_grid(to));
    }

    public boolean move_agent(UIVec2 from, UIVec2 to) {

        boolean ret_val = false;

        if(validate_move(from, to)) {

            m_world_grid.put(from.getY(), from.getX(), 0);
            m_world_grid.put(to.getY(), to.getX(), 255);

            ret_val = true;

        }

        return ret_val;

    }

    public boolean validate_move(FVec2 from, FVec2 to) {
        return validate_move(to_grid(from), to_grid(to));
    }

    public boolean validate_move(UIVec2 from, UIVec2 to) {

        boolean ret_val = false;

        if(check_inbounds(from) && !check_cell_empty(from)) {

            if (check_inbounds(to) && check_cell_empty(to)) {
                ret_val = true;
            }

        }

        return ret_val;

    }

    public short sense_world(FVec2 position) {
        return sense_world(to_grid(position));
    }

    public short sense_world(UIVec2 position) {

        short ret_val = 0;

        if (check_inbounds(position)) {
            ret_val = (short) m_trail_grid.get(position.getY(), position.getX())[0];
        }

        return ret_val;

    }

    public void deposit_trail(FVec2 position, short value) {
        deposit_trail(to_grid(position), value);
    }

    public void deposit_trail(UIVec2 position, short value) {

        short trail_value = (short)m_trail_grid.get(position.getY(), position.getX())[0];

        if (check_inbounds(position) && (trail_value <= (65535 - value))) {

            short tvalue = (short) (trail_value + value);
            m_trail_grid.put(position.getY(), position.getX(), tvalue);

        }

    }

    public boolean same_cell(FVec2 from, FVec2 to) {
        return to_grid(from) == to_grid(to);
    }

    public void set_diffusion(int diff_size) {

        m_diff_kernel = Mat.ones(diff_size, diff_size, CvType.CV_32F);
        Core.divide(m_diff_kernel, new Scalar((float)(diff_size * diff_size)),  m_diff_kernel);

        m_do_diffusion = true;

    }

    public void set_decay(float decay_mult, float decay_sub) {

        m_decay_mult = decay_mult;
        m_decay_sub = decay_sub;
        m_do_decay = true;

    }

    public void diffuse() {

        if (m_do_diffusion) {
            Imgproc.filter2D(m_trail_grid, m_trail_grid, -1, m_diff_kernel, new Point(-1, -1));
        }

    }

    public void decay() {

        if (m_do_decay) {

            /* m_trail_grid = (1.0f-m_decay_mult) * m_trail_grid - m_decay_mult; */
            Mat tmp_mat = new Mat();
            Core.multiply(m_trail_grid, new Scalar((1.0f-m_decay_mult)), tmp_mat);
            Core.subtract(tmp_mat, new Scalar(m_decay_sub), m_trail_grid);

        }

    }

    public void display(int delay) {

        Mat normalized = new Mat();
        Core.normalize(m_trail_grid, normalized, 0, 65535, Core.NORM_MINMAX, CvType.CV_16UC1);

        normalized.convertTo(normalized, CvType.CV_8UC1, 0.0f, 255.0f);

        imshow("world", m_world_grid);
        imshow("trail", normalized);
        waitKey(delay);

    }

    public Mat get_world_snap() {

        Mat normalized_trail = new Mat();
        Mat snap_out = new Mat();

        Core.normalize(m_trail_grid, normalized_trail, 0, 65535, Core.NORM_MINMAX, CvType.CV_16UC1);

        normalized_trail.convertTo(normalized_trail, CvType.CV_8UC1, 1.0f/255.0f);

        List<Mat> matList = Arrays.asList(m_world_grid, normalized_trail);
        Core.hconcat(matList, snap_out);

        return snap_out;

    }

    private UIVec2 to_grid(FVec2 position) {

        UIVec2 ret_val = new UIVec2();
        ret_val.setX(Math.round(position.getX()));
        ret_val.setY(Math.round(position.getY()));

        return ret_val;

    }

    private boolean check_inbounds(UIVec2 position) {

        return((position.getX() >= 0) && (position.getX() < m_width) &&
                (position.getY() >= 0) && (position.getY() < m_height));

    }

    private boolean check_cell_empty(UIVec2 position) {
        return (m_world_grid.get(position.getY(), position.getX())[0] == 0);
    }

}
