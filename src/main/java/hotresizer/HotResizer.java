package hotresizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Tool for automatic resizing of images, by scanning a folder for changes.
 */
public class HotResizer {

	private static final String VERSION = "1.0";

	private static final String PROP_INPUT_FOLDER = "inputFolder";
	private static final String PROP_OUTPUT_FOLDER = "outputFolder";

	private Properties configuration;

	/**
	 * Main Enty point
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("Hotresizer - Copyright Yves Schubert 2016 - Version " + VERSION);
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println();

		final HotResizer instance;
		
		if (args.length == 1) {
			String propertyFilePath = args[0];
			try {
				instance = new HotResizer(propertyFilePath);
			} catch (FileNotFoundException fnfe) {
				System.err.println("Cannot read the properties file: " + propertyFilePath
						+ " does not exist or can not be found.");
				fnfe.printStackTrace();
				return;
			}
		} else {
			instance = new HotResizer(new File("").getClass().getResourceAsStream("/config.properties"));
		}
		
		instance.start();
	}

	protected HotResizer(InputStream inputStream) {
		loadConfig(inputStream);
	}

	protected HotResizer(String propertyFilePath) throws FileNotFoundException {
		this(new FileInputStream(propertyFilePath));
	}

	private void start() {
		File inputFolder = new File(configuration.getProperty(PROP_INPUT_FOLDER));
		File outputFolder = new File(configuration.getProperty(PROP_OUTPUT_FOLDER));

		new ImageFileProcessor(inputFolder, outputFolder, configuration).processFolder();
	}

	private void loadConfig(InputStream inputStream) {
		try {
			// loading properties
			this.configuration = new Properties();
			configuration.load(inputStream);
		} catch (IOException ioe) {
			System.err.println("Cannot load configuration file.");
			ioe.printStackTrace();
		}
	}

}
