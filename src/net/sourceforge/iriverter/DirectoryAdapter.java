package org.thestaticvoid.iriverter;

public class DirectoryAdapter implements DirectoryInfo {
	private String inputDirectory, outputDirectory;
	private boolean convertSubdirectories;
	
	public DirectoryAdapter(String inputDirectory, String outputDirectory, boolean convertSubdirectories) {
		this.inputDirectory = inputDirectory;
		this.outputDirectory = outputDirectory;
		this.convertSubdirectories = convertSubdirectories;
	}
	
	public synchronized String getInputDirectory() {
		return inputDirectory;
	}
	
	public synchronized String getOutputDirectory() {
		return outputDirectory;
	}
	
	public synchronized boolean getConvertSubdirectories() {
		return convertSubdirectories;
	}
}
