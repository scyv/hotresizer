package hotresizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

/**
 * Processes image files in a way that all images it supports are resized and stored in the target folder.
 */
public class ImageFileProcessor extends FolderProcessor {

	
	/**
	 * Constructor.
	 * 
	 * @param inputFolder the folder to scan.
	 * @param outputFolder the folder where to put the result to.
	 */
	public ImageFileProcessor(File inputFolder, File outputFolder) {
		super(inputFolder, outputFolder);
	}

	@Override
	protected void processFile(File inputFile, File outputFile) throws IOException {
		System.out.println("Processing: " + inputFile.getAbsolutePath());
		BufferedImage source = ImageIO.read(inputFile);
		BufferedImage scaledImage = Scalr.resize(source, 1500);
		ImageIO.write(scaledImage, inputFile.getName().substring(inputFile.getName().lastIndexOf(".")+ 1), outputFile);
	}
}
