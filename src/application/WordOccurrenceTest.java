package application;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 
 * JUnit test class to run the testGetList and testReadFile methods  
 *
 */
class WordOccurrenceTest {

	/**
	 * 
	 * wordOccurrence to instantiate new WordOccurrence()
	 * 
	 */
	WordOccurrence wordOccurrence = new WordOccurrence();

	/**
	 * 
	 * testGetList method to test if the WordOccurrence list is not null and 
	 * to only have a max of 20 iterations of the word occurrences
	 * 
	 */
    @Test
    public void testGetList() {
    	
        List<String> resultList = wordOccurrence.getList();

        //test list to ensure it is not null
        assertNotNull(resultList, "Result list should not be null");
        //test code to check that there are only 20 word occurrences
        assertTrue(resultList.size() <= 20, "Result list should have a maximum of 20 word occurrences");
    }

    /**
	 * 
	 * testReadFile method to test if the WordOccurrence list is not null and 
	 * to ensure the file is not empty
	 * 
	 */
    @Test
    public void testReadFile() {
    	
    	//path to read the html file
        Path readFile = Paths.get("The Project Gutenberg eBook of The Raven, by Edgar Allan Poe.htm");
        StringBuilder html = new StringBuilder();
        String content = WordOccurrence.ReadFile(readFile, html);

        //test the content to ensure it is not null
        assertNotNull(content, "Content should not be null");
        //test the content to ensure it is not empty
        assertTrue(content.length() > 0, "Content should not be empty");
    }

}
