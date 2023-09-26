package Graph;

import java.util.ArrayList;

public class ScaledDataSet extends ArrayList<Point> {
    //private ArrayList<Float> xvals, yvals; // why. just use an arraylist<point> but that requires a *lot* of refactoring so i won't do it
    private boolean yscaling = true;
    private boolean xscaling = true;
    private float inputymin, inputymax, scaledymin, scaledymax;
    private float inputxmin, inputxmax, scaledxmin, scaledxmax;
    private float xmin, xmax, ymin, ymax; // technically these are duplicated by the methods ArrayList.max() and ArrayList.min() but that would require implementing IEComparableComplex (IN A PREEXISTING USE ENVIRONMENT) and i'm not about to do that for this
    private float yshiftval, yscaleval, xshiftval, xscaleval;

    public ScaledDataSet() {
        super.clear();
        xmax = Float.MIN_NORMAL;
        ymax = Float.MIN_NORMAL;
        xmin = Float.MAX_VALUE;
        ymin = Float.MAX_VALUE;
    }

    // Calling this method will automatically scale
    // all points that get added with addPoint.
    // You tell it what your input min and max are
    // and what you want the scaled min and max to be.
    // ** addpoint will ignore data points not between
    // the input min and max! **
    public void setyScaling(float _inputymin, float _scaledymin,
            float _inputymax, float _scaledymax) {
        inputymin = _inputymin;
        inputymax = _inputymax;
        scaledymin = _scaledymin;
        scaledymax = _scaledymax;
        yscaling = true;
        yscaleval = (scaledymax - scaledymin)/(inputymax - inputymin);
        yshiftval = scaledymin - yscaleval*inputymin;
    }

    // todo:  check if re-scaling to existing scaling values!
    // nope. A conditional is slower than just doing the math. Maybe pare down the amount of re-scaling that needs to be called externally.
    public void setxScaling(float _inputxmin, float _scaledxmin,
            float _inputxmax, float _scaledxmax) {
        inputxmin = _inputxmin;
        inputxmax = _inputxmax;
        scaledxmin = _scaledxmin;
        scaledxmax = _scaledxmax;
        xscaling = true;
        xscaleval = (scaledxmax - scaledxmin)/(inputxmax - inputxmin);
        xshiftval = scaledxmin - xscaleval*inputxmin;
    }

    public void addPoint(float x, float y) {
        if (yscaling && xscaling) {
            // scale the input and then add!
            y = y * yscaleval + yshiftval;
            x = x * xscaleval + xshiftval;
            this.add(new Point(x, y));
        } else {
            System.out.println("Graph.ScaledDataSet couldn't add point " + x + ", "
                    + y + "; No scale values set!");
        }

        // update min and max values
        if (x < xmin) {
            xmin = x;
        } else if (x > xmax) {
            xmax = x;
        }

        if (y < ymin) {
            ymin = y;
        } else if (y > ymax) {
            ymax = y;
        }
    }

    public float getxmin() {
        return xmin;
    }

    public float getymin() {
        return ymin;
    }

    public float getxmax() {
        return xmax;
    }

    public float getymax() {
        return ymax;
    }

    // return number of data points in the set
    public int getSize() {
        return super.size();
    }

    public void clear() {
        super.clear();
        xmax = Float.MIN_NORMAL; // Any reason we're using MIN_NORMAL instead of 0 or MIN_VALUE?
        ymax = Float.MIN_NORMAL;
        xmin = Float.MAX_VALUE; // Any reason we're using MAX_VALUE instead of POSITIVE_INFINITY?
        ymin = Float.MAX_VALUE;
    }
}