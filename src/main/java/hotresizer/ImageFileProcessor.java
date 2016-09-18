package hotresizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

/**
 * Processes image files in a way that all images it supports are resized and
 * stored in the target folder.
 */
public class ImageFileProcessor extends FolderProcessor {

	private static final String PROP_TARGET_SIZE = "targetSize";

	/**
	 * Constructor.
	 * 
	 * @param inputFolder
	 *            the folder to scan.
	 * @param outputFolder
	 *            the folder where to put the result to.
	 * @param configuration
	 *            the configuration
	 */
	public ImageFileProcessor(File inputFolder, File outputFolder, Properties configuration) {
		super(inputFolder, outputFolder, configuration);
	}

	@Override
	protected void processFile(File inputFile, File outputFile) throws IOException {
		System.out.println("Processing: " + inputFile.getAbsolutePath());
		BufferedImage source = ImageIO.read(inputFile);
		BufferedImage scaledImage = Scalr.resize(source,
				Integer.parseInt(String.valueOf(configuration.get(PROP_TARGET_SIZE))));
		ImageIO.write(scaledImage, inputFile.getName().substring(inputFile.getName().lastIndexOf(".") + 1), outputFile);
	}
}
