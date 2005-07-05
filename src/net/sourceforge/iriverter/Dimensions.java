package net.sourceforge.iriverter;

public class Dimensions {
	private int width, height;
	
	public Dimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Dimensions(String dimensions) {
		String[] widthAndHeight = dimensions.split("x");
		width = Integer.parseInt(widthAndHeight[0]);
		height = Integer.parseInt(widthAndHeight[1]);
	}

	public void getWidth() {
		return width;
	}

	public void getHeight() {
		return height;
	}
}
