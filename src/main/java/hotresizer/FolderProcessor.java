package hotresizer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Watches a folder for recurring modifications.
 */
public abstract class FolderProcessor {

	// milliseconds * seconds * minutes
	private static final long PROCESS_DELAY = 1000 * 60 * 1;

	// milliseconds * seconds * minutes
	private static final int POLLING_INTERVAL = 1000 * 60 * 1;

	protected Properties configuration;
	
	private File inputFolder;
	private File outputFolder;

	/**
	 * Constructor.
	 * 
	 * @param inputFolder
	 *            the folder to scan.
	 * @param outputFolder
	 *            the folder where to put the result to.
	 */
	public FolderProcessor(File inputFolder, File outputFolder, Properties configuration) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		this.configuration = configuration;

		inputFolder.mkdirs();
		outputFolder.mkdirs();
	}

	/**
	 * Begin processing the folder.
	 * 
	 * This process never stops.
	 */
	public void processFolder() {
		while (true) {
			processFolder(inputFolder);
			try {
				Thread.sleep(POLLING_INTERVAL);
			} catch (InterruptedException ie) {
				System.err.println("Main Thread interrupted!");
				ie.printStackTrace();
				break;
			}
		}
	}

	private void processFolder(File currentFolder) {
		File[] files = currentFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory())
					this.processFolder(file);
				else {
					if (file.lastModified() + PROCESS_DELAY < System.currentTimeMillis()) {
						File outputFile = new File(file.getAbsolutePath().replace(this.inputFolder.getAbsolutePath(),
								this.outputFolder.getAbsolutePath()));

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
