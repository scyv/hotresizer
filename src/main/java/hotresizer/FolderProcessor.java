package hotresizer;

import java.io.File;
import java.io.IOException;

/**
 * Watches a folder for recurring modifications.
 */
public abstract class FolderProcessor {

	// milliseconds * seconds * minutes
	private static final long PROCESS_DELAY = 1000 * 60 * 1;

	// milliseconds * seconds * minutes
	private int pollingIntervall = 1000 * 60 * 1;

	protected File inputFolder;
	protected File outputFolder;

	private boolean isInterrupted = false;

	/**
	 * Constructor.
	 * 
	 * @param inputFolder
	 *            the folder to scan.
	 * @param outputFolder
	 *            the folder where to put the result to.
	 */
	public FolderProcessor(File inputFolder, File outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		
		inputFolder.mkdirs();
		outputFolder.mkdirs();
	}

	/**
	 * Begin processing the folder.
	 * 
	 * This process never stops.
	 */
	public void processFolder() {
		processFolder(inputFolder);
	}

	private void processFolder(File currentFolder) {
		while (!this.isInterrupted) {
			File[] files = currentFolder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory())
						this.processFolder(file);
					else {
						if (file.lastModified() + PROCESS_DELAY < System.currentTimeMillis()) {
							File outputFile = new File(file.getAbsolutePath()
									.replace(this.inputFolder.getAbsolutePath(), this.outputFolder.getAbsolutePath()));

							if (outputFile.getParentFile().exists() || outputFile.getParentFile().mkdirs()) {
								try {
									processFile(file, outputFile);
								} catch (IOException ioe) {
									System.err.println("File " + file.getAbsolutePath() + " could not be process!");
									ioe.printStackTrace();
								}
							}
							file.delete();
						}
					}
				}
			}

			try {
				Thread.sleep(this.pollingIntervall);
			} catch (InterruptedException ie) {
				System.err.println("Main Thread interrupted!");
				ie.printStackTrace();
				isInterrupted = true;
			}
		}
	}

	/**
	 * Called by processFolder when a file shall be processed.
	 * 
	 * @param inputFile
	 *            the file to read from. (It is asured that the file does exist)
	 * @param outputFile
	 *            the target file. (This file does not exist yet. It is the
	 *            responsibility of the implementor to create the file)
	 * @throws IOException
	 *             when there was an error reading or writing the file.
	 */
	protected abstract void processFile(File inputFile, File outputFile) throws IOException;

}
