package throughput06;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * Uso de EXECUTOR 
 */
public class RecibirRequestHttpParaContarPalabras {

	private static class WordCountHandler implements HttpHandler {
		
		private static final int HTTP_OK = 200;
		
		private String textoAAnalizar;
		
		public WordCountHandler(String text) {
			this.textoAAnalizar = text;
		}

		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			
			URI requestURI = httpExchange.getRequestURI();
			String requestMethod = httpExchange.getRequestMethod();
			Headers requestHeaders = httpExchange.getRequestHeaders();
			InputStream requestBodyAsStream = httpExchange.getRequestBody();

			String query = requestURI.getQuery();
			String [] keyValue = query.split("=");
			String action = keyValue[0];
			String word = keyValue[1];
			if (!action.equals("word")) {
				httpExchange.sendResponseHeaders(400, 0);
				return;
			}
			long count = countWord(word);
						
			OutputStream responseBodyAsStream = httpExchange.getResponseBody();
			
			String respuesta = "Respuesta a la petición HTTP recibida." 
							+ " El número de ocurrencias de la palabra \"" + word 
							+ "\" es: " + Long.toString(count);
			byte[] respuestaAsBytes = respuesta.getBytes();
			
			int codigoStatusHttp = HTTP_OK;
			httpExchange.sendResponseHeaders(codigoStatusHttp, respuesta.length());
			
			responseBodyAsStream.write(respuestaAsBytes);
			responseBodyAsStream.close();
		}

		private long countWord(String palabraBuscada) {
			long count = 0;
			int ocurrencia = 0;
			
			while ( ocurrencia >= 0 ) {
				ocurrencia = textoAAnalizar.indexOf(palabraBuscada, ocurrencia);
				if ( ocurrencia >= 0 ) {
					count++;
					ocurrencia++;
				}
			}
			
			return count;
		} 	
	}

	private static final String INPUT_FILE = "resources/throughput/war_and_peace.txt";
	private static final int NUMERO_DE_HILOS = 1;

	public static void main(String[] args) throws IOException {
//		comprobarQueExisteFicheroDeRecursos(INPUT_FILE);

		String texto = volcarFicheroTextoEnUnString(INPUT_FILE);

		startServer(texto);

	}

	private static void startServer(String texto) throws IOException {
		InetSocketAddress direccionYPuertoIP = new InetSocketAddress(33334);
		int tamanoColaRequestsHttp = 0; // Será 0 para que las requests se queden en la cola del pool de hilos
		HttpServer servidorHttp = HttpServer.create(direccionYPuertoIP, tamanoColaRequestsHttp);
		
		WordCountHandler handler = new WordCountHandler(texto);
		servidorHttp.createContext("/search", handler);
		
		Executor ejecutor = Executors.newFixedThreadPool(NUMERO_DE_HILOS);
		servidorHttp.setExecutor(ejecutor);
		servidorHttp.start();
	}

//	---------------------------------------------------
//	Cómo volcar un fichero de texto a un String
//	---------------------------------------------------
	private static String volcarFicheroTextoEnUnString(String rutaFicheroAsString) throws IOException {
		Path rutaFicheroTxt = Paths.get(INPUT_FILE);
		byte[] todosLosBytesDelFicheroTxt = Files.readAllBytes(rutaFicheroTxt);
		String texto = new String(todosLosBytesDelFicheroTxt);
		return texto;
	}

	private static void comprobarQueExisteFicheroDeRecursos(String fichero) throws IOException {
		File auxFichero = new File(fichero);
		if (auxFichero == null) {
			throw new RuntimeException("No hay fichero");
		} else {
			System.out.println(auxFichero.getCanonicalPath());
		}
	}

}
