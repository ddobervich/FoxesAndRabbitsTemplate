package Graph;

import java.util.HashMap;
import java.util.Set;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * The Graph.Graph class represents and draws a 2d line graph
 * @author David
 *
 */

public class Graph {
	private PApplet graphicsWindow;
	private float xmin, xmax, ymin, ymax;
	private float dataxmin, dataxmax, dataymin, dataymax;
	private float dataxrange, datayrange;
	private float yshiftval, yscaleval, xshiftval, xscaleval;
	private boolean CONNECTED = true;
	private boolean wrap = true;
	
	// used by the plotdata method to tell itself
	// when it needs to clear data during a wrap
	private boolean cleardata = false;
	
	// DataSets for all past numbers of animals at any timestep
	private HashMap<Object, ScaledDataSet> dataSets; // ignore java complaining about this it's wrong
	private HashMap<Object, Integer> colorMap;
	private static int[] defaultColors;
	private int nextColor = 0;
	public boolean drawLines = false;
	public boolean drawPoints = true;
	public int pointSize = 3;
	
	// labels etc.
	public String xLabel; // camel case dammit
	public String yLabel;
	public String title;
	public int titlePointSize = 20;
	public int labelPointSize = 16;
	public int numXIncrements = 5;
	public int numYIncrements = 5;
	public int HSPACING = 20;
	PFont font;

	public Graph(PApplet p, float _xmin, float _ymin, float _xmax, float _ymax,
			float dataxmin, float dataymin, float dataxmax, float dataymax) {
		xmin = _xmin; // this is how at least one style guide recommends you make constructors
		ymin = _ymin;
		xmax = _xmax;
		ymax = _ymax;
		setDataRanges(dataxmin, dataxmax, dataymin, dataymax);
		this.graphicsWindow = p;
		this.dataSets = new HashMap<>(); // useless explicit type argument
		this.colorMap = new HashMap<>();
		Graph.defaultColors = new int[3]; // use class name for static variable reference
		Graph.defaultColors[0] = p.color(255, 100, 100);
		Graph.defaultColors[1] = p.color(100, 100, 255);
		Graph.defaultColors[2] = p.color(100, 255, 100);
		font = p.createFont("SansSerif", 20);
		p.textFont(font);
	}

	public void setDataRanges(float _xmin, float _xmax, float _ymin, float _ymax) {
		dataxmin = _xmin; // completely useless use of (this) in the first place
		dataxmax = _xmax;
		dataymin = _ymin;
		dataymax = _ymax;

		dataxrange = _xmax - _xmin;
		datayrange = _ymax - _ymin;

		xscaleval = (_xmax - _xmin) / (dataxmax - dataxmin); // redundant casting
		xshiftval = _xmin - xscaleval * dataxmin;
		yscaleval = (_ymax - _ymin) / (dataymax - dataymin);
		yshiftval = _ymin - yscaleval * dataymin;

		// don't comment code for vcs just delete it
	}

	public void setBounds(float _xmin, float _xmax, float _ymin, float _ymax) { // "position" should represent 1 (x,y) pair, not a pair of pairs
		xmin = _xmin;
		xmax = _xmax;
		ymin = _ymin;
		ymax = _ymax;
	}

	public void draw() {
		int c;
		ScaledDataSet dataSet;

		this.drawAxes();
		drawTitle();
		drawXAxisLabel();
		drawYAxisLabel();
		drawXIncrements();
		drawYIncrements();
		drawXIncrementLabels();
		drawYIncrementLabels();

		for (Object f : dataSets.keySet()) {
			dataSet = dataSets.get(f);
			c = this.colorMap.get(f);
			graphicsWindow.fill(c); // change the color
			graphicsWindow.stroke(c); // change the color
			for (int i = 1; i < dataSet.getSize(); i++) {
				if (drawLines) {
					graphicsWindow.line(dataSet.get(i - 1).x, dataSet.get(i - 1).y, dataSet.get(i).x, dataSet.get(i).y);
				}
				if (drawPoints) {
					graphicsWindow.ellipse(dataSet.get(i).x, dataSet.get(i).y, pointSize, pointSize);
				}
			}
		}
		graphicsWindow.stroke(0);
	}

	private void drawAxes() {
		graphicsWindow.stroke(graphicsWindow.color(0));
		graphicsWindow.line(xmin, ymin, xmin, ymax);
		graphicsWindow.line(xmin, ymax, xmax, ymax);
	}

	private void drawTitle() {
		if ((title != null) && (!title.equals(""))) {
			graphicsWindow.fill(0);
			graphicsWindow.textSize(titlePointSize);
			graphicsWindow.textAlign(graphicsWindow.LEFT);
			graphicsWindow.text(title, xmin + 10, ymin);
		}
	}

	private void drawYAxisLabel() {
		if ((yLabel != null) && (!yLabel.equals(""))) {
			graphicsWindow.fill(0);
			graphicsWindow.textSize(labelPointSize);
			graphicsWindow.textAlign(graphicsWindow.RIGHT);
			graphicsWindow.text(yLabel, xmin - HSPACING, (ymin + ymax) / 2);
		}
	}

	private void drawXAxisLabel() {
		if ((xLabel != null) && (!xLabel.equals(""))) {
			graphicsWindow.fill(0);
			graphicsWindow.textSize(labelPointSize);
			graphicsWindow.textAlign(graphicsWindow.CENTER);
			graphicsWindow.text(xLabel, (xmin + xmax) / 2, ymax + 22);
		}
	}

	private void drawXIncrements() {
		float dx = (xmax - xmin) / numXIncrements;

		for (float i = xmin; i < xmax; i += dx) {
			graphicsWindow.stroke(100);
			graphicsWindow.line(i, ymax, i, ymax + 4);
		}
	}

	private void drawXIncrementLabels() {
		float dx = (xmax - xmin) / numXIncrements;
		float datadx = (dataxmax - dataxmin) / numXIncrements;
		String text;

		float c = dataxmin;
		for (float i = xmin; i < xmax; i += dx) {
			graphicsWindow.fill(0);
			graphicsWindow.textSize(10);
			graphicsWindow.textAlign(graphicsWindow.CENTER);
			text = Float.toString(c);
			graphicsWindow.text(text, i, ymax + 10);
			c += datadx;
		}
	}

	private void drawYIncrementLabels() {
		float dy = (ymin - ymax) / numYIncrements;
		float datady = (dataymax - dataymin) / numYIncrements;
		String text;

		float c = dataymax;
		for (float i = ymax; i < ymin; i += dy) {
			graphicsWindow.fill(0);
			graphicsWindow.textSize(10);
			graphicsWindow.textAlign(graphicsWindow.CENTER);
			text = Float.toString(c);
			graphicsWindow.text(text, xmin - HSPACING, i);
			c -= datady;
		}
	}

	private void drawYIncrements() {
		float dy = (ymin - ymax) / numYIncrements;

		for (float i = ymax; i < ymin; i += dy) {
			graphicsWindow.stroke(100);
			graphicsWindow.line(xmin, i, xmin - HSPACING / 2.0f, i);
		}
	}

	public void plotPoint(float x, float y, Object key) {
		if ((x > dataxmax) && (!wrap)) {
			return;
		}

		if (x < dataxmin) {
			return;
		}

		if (y < dataymin) {
			return;
		}

		if (y > dataymax) {
			return;
		}

		if ((x > dataxmax) && (wrap)) {
			// if we're wrapping, re-adjust data range
			cleardata = true;
			float datarange = dataxmax - dataxmin;
			while (x > dataxmax) {
				dataxmin += datarange;
				dataxmax += datarange;
			}
		}

		if (cleardata) {
			clearData();
		}

		if (dataSets.containsKey(key)) {
			ScaledDataSet d = dataSets.get(key);
			d.addPoint(x, y);
		} else {
			ScaledDataSet d = new ScaledDataSet();
			d.setxScaling(dataxmin, xmin, dataxmax, xmax);
			d.setyScaling(dataymin, ymax, dataymax, ymin);
			d.addPoint(x, y);
			dataSets.put(key, d);
			if (!colorMap.containsKey(key)) {
				colorMap.put(key, this.getNextColor());
			}
		}
	}

	// Clear all data sets. Re-create them empty
	// with the current scaling factors.
	private void clearData() {
		ScaledDataSet d;
		Set<Object> s = dataSets.keySet();

		dataSets.clear();

		for (Object f : s) {
			d = new ScaledDataSet();
			d.setxScaling(dataxmin, xmin, dataxmax, xmax);
			d.setyScaling(dataymin, ymin, dataymax, ymax);
			dataSets.put(f, d);
		}
		cleardata = false;
	}

	private int getNextColor() {
		int i = defaultColors[nextColor++];
		nextColor %= defaultColors.length;
		return i;
	}

	// redundant "x" ("x data to x coord") (just say "x" once lol)
	public float dataToCoordX(float xdata) {
		return xdata * xscaleval + xshiftval;
	}

	public float coordToDataX(float xcoord) {
		return (xcoord - xshiftval) / xscaleval;
	}

	public float dataToCoordY(float ydata) {
		return ydata * yscaleval + yshiftval;
	}

	public float coordToDataY(float ycoord) {
		return (ycoord - yshiftval) / yscaleval;
	}

	/**
	 * Define a color to be used for a given class of animal.
	 * 
	 * @param animalClass
	 *          The animal's Class object.
	 * @param color
	 *          The color to be used for the given class.
	 */
	public void setColor(Class animalClass, Integer color) {
		colorMap.put(animalClass, color);
	}

	public void clear() {
		clearData();
	}
}