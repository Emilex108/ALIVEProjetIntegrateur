package utillities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Test {
	public static void main(String[] args) {
		try (InputStream in = new URL("http://192.168.0.100:81/stream").openStream()) {
		    Files.copy(in, Paths.get("/resources/test.jpg"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {}
	}
}
