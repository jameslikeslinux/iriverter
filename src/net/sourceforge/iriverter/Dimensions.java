package net.sourceforge.iriverter;

public class Dimensions {
	private int width, height;
	
	public Dimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Dimensions(String dimensions) {
		width = Integer.parseInt(dimensions.substring(0, dimensions.indexOf('x')));
		height = Integer.parseInt(dimensions.substring(dimensions.indexOf('x') + 1));
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String toString() {
		return width + "x" + height;
	}
}
