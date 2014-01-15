package test;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import parser.ParserConfig;

/**
 * Used for test
 * @author Julien
 *
 */
public final class TestConfigParser {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logger logger = Logger.getLogger("test");
		ParserConfig pc = new ParserConfig();
		pc.loadFile("C:\\Users\\Alain\\workspace\\Batmeca2012\\Ressources\\MAT4_TEST\\test_insert\\config\\X63_ST_L01.par");
		try {
			pc.processLinebyLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE,"file not found",e);
		}

	}

}
