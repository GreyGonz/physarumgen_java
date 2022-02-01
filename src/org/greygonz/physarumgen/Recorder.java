package org.greygonz.physarumgen;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoWriter;

public class Recorder {

    public boolean m_recording = false;

    private VideoWriter m_video_writer = new VideoWriter();
    private String m_path;
    private int m_frame_number = 0;

    public Recorder() {

    }

    public Recorder(int width, int height, String path) {
        new_recording(width, height, path);
    }

    public boolean new_recording(int width, int height, String path) {

        m_path = path;
        m_frame_number = 0;
        m_recording = m_video_writer.open((path+"physarum.mp4"), VideoWriter.fourcc('a', 'v', 'c', '1'), 30, new Size(height, width), false);

        return m_recording;

    }

    public void stop_recording() {

        m_video_writer.release();
        m_recording = false;

    }

    public void video_add_frame(Mat frame) {

        if (m_recording) {
            m_video_writer.write(frame);
        }

    }

    public void save_image(Mat frame) {

        if (m_recording) {

            String image_path = m_path + "physarum_" + Integer.toString(m_frame_number) + ".jpg";
            Imgcodecs.imwrite(image_path, frame);
            m_frame_number++;

        }

    }

}
