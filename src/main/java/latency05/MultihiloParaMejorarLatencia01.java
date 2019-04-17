package latency05;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class MultihiloParaMejorarLatencia01 {
	
	private static File sourceImageFile, resultImageFile;

	static {
		String sourceImagePath = MultihiloParaMejorarLatencia01.class.getResource("/many-flowers.jpg").getFile();
		sourceImageFile = new File(sourceImagePath);
		resultImageFile = sourceImageFile.toPath().getParent().resolve("many-flowers-OUT.jpg").toFile();
		System.out.println("Fichero origen: " + sourceImagePath);
		System.out.println("Fichero destino: " + resultImageFile.getAbsolutePath());
	}

	public static void main(String[] args) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(sourceImageFile);
		BufferedImage resultImage = new BufferedImage( 	originalImage.getWidth(), 
														originalImage.getHeight(), 
														BufferedImage.TYPE_INT_RGB );
		
		long startTime = System.currentTimeMillis();
		
//		Cambia selectivamente colores de la imagen original
//		recolorSingleThreaded(originalImage, resultImage);
		recolorMultipleThreaded(originalImage, resultImage, 4);
		
		long endTime = System.currentTimeMillis(); 
		
		System.out.println("Recoloreado en buffer de memoria en " + (endTime - startTime) + " milisegundos.");
		
//		Guarda en fichero físico la imagen final (modificación de la original)
		ImageIO.write( 	resultImage, 
						"jpg", 
						resultImageFile );
	}
	
	
//	----------------------------
//	----------------------------
//	SOLUCIÓN CON MÚLTIPLES HILOS
//	----------------------------
//	----------------------------
	public static void recolorMultipleThreaded (BufferedImage imagenOriginal, BufferedImage imagenFinal, int numThreads) {
		List<Thread> threads = new ArrayList<>();
		int width = imagenOriginal.getWidth();
		int height = imagenOriginal.getHeight() / numThreads;
		
		for(int i = 0 ; i < numThreads ; i++) {
			final int threadMultiplier = i;
			
			Thread thread = new Thread( () -> { // Implementación de interfaz funcional Runnable
				int leftCorner =  0;
				int topCorner = height * threadMultiplier;
				
				recolorImage (	imagenOriginal, 
								imagenFinal, 
								leftCorner, 
								topCorner, 
								width, 
								height );
			});
			
			threads.add(thread);
		}
		
		for(Thread hilo : threads) { 
			hilo.start();
		}
		
		for(Thread hilo : threads) { 
			try {
				hilo.join();
			} catch (InterruptedException e) {
				System.out.println("El hilo " + hilo.getName() + " ha recibido una orden de interrupción");
			}
		}
		
	}
	
//	----------------------------
//	----------------------------
//	SOLUCIÓN CON UN SOLO HILO
//	----------------------------
//	----------------------------
	public static void recolorSingleThreaded (BufferedImage imagenOriginal, BufferedImage imagenFinal) {
		recolorImage( 	imagenOriginal, 
						imagenFinal, 
						0, 0, 
						imagenOriginal.getWidth(), 
						imagenOriginal.getHeight());
	}
	
	public static void recolorImage(BufferedImage imagenOriginal, BufferedImage imagenFinal, int puntoOrigenX, int puntoOrigenY, int width, int height) {
		for ( 	int x = puntoOrigenX ; 
				x < puntoOrigenX + width && x < imagenOriginal.getWidth() ; 
				x++ ) 
		{
			for (	int y = puntoOrigenY ; 
					y < puntoOrigenY + height && y < imagenOriginal.getHeight() ; 
					y++ ) 
			{
				recolorPixel(imagenOriginal, imagenFinal, x, y);
			}
		}
	}
	
	public static void recolorPixel(BufferedImage imagenOriginal, BufferedImage imagenFinal, int x, int y) {
		int newRed, newGreen, newBlue;
		
		int rgb = imagenOriginal.getRGB(x, y);
		
		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);
		
		if(isShadeOfGrey(red, green, blue)) {
			newRed = Math.min(255, red+10);
			newGreen = Math.max(0, green - 80);
			newBlue = Math.max(0,  blue - 20);
		} else {
			newRed = red;
			newGreen = green;
			newBlue = blue;
		}
		
		int newRGB = createRGFromColors(newRed, newGreen, newBlue);
		setRGB(imagenFinal, x, y, newRGB);
	}
	
    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

	
	public static boolean isShadeOfGrey(int red, int green, int blue) {
//		El criterio para definir colores de sombra es puramente empírico del autor del curso
		return 	Math.abs(red - green)  < 30 && 
				Math.abs(red - blue)   < 30 && 
				Math.abs(blue - green) < 30;
	}

	public static int createRGFromColors(int red, int green, int blue) {
		int rgb = 0;
		
//		Desplazamiento de bits (0, 8 ó 16)
		rgb |= blue;
		rgb |= green << 8; 
		rgb |= red << 16;
		rgb |= 0xFF000000;
		
		return rgb;
	}
	
	public static int getBlue(int rgb) {
		// Devuelve todos los bits a cero excepto los dos que corresponden a BLUE
		return rgb & 0x000000FF;
	}
	
	public static int getGreen(int rgb) {
		// Devuelve todos los bits a cero excepto los que corresponden a GREEN 
		// se hace un desplazamiento de 8 bits al resultado
		return rgb & 0x0000FF00 >> 8;
	}
	
	public static int getRed(int rgb) {
		return rgb & 0x00FF0000 >> 16;
	}
}
 