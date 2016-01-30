package org.metrobots.botcv.cv;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Tasgo on 1/15/16.
 */
public class CvUtils {
    /**
     * Rotate a Mat by the specified degree
     * @param src The source Mat
     * @param angle The angle by which to rotate by
     * @return The rotated Mat
     */
    public static Mat rotate(Mat src, double angle)
    {
        int len = src.cols() > src.rows() ? src.cols() : src.rows();
        Point pt = new Point(len/2.0, len/2.0);
        Mat r = Imgproc.getRotationMatrix2D(pt, angle, 1.0);
        Mat dst = new Mat();

        Imgproc.warpAffine(src, dst, r, new Size(len, len));

        return dst;
    }
}
