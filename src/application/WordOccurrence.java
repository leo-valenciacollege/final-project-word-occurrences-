package application;

import java.io.BufferedReader;
import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * WordOccurrence class to read the html file and return a list of the word occurrences 
 *
 */
public class WordOccurrence {

	/**
	 * 
	 * getList method
	 * 
	 * @return return the resultList result. This method is to create a List to read the htm file.
	 * The function will clean up the contents of the file and store the words into a List.
	 */
	public List<String> getList() {
		
		List<String> resultList = new ArrayList<String>();
		StringBuilder html = new StringBuilder();		
		String valString = "";
		
		//Path to read file to with data
		Path readFile = Paths.get("The Project Gutenberg eBook of The Raven, by Edgar Allan Poe.htm");

		//read the file and use the ReadFile method to build the string of the file's content
		valString = ReadFile(readFile, html);
		
		//clean the valString and replace remove all HTML tags
		String noHTMLString = valString.replaceAll("\\<.*?\\>", " ");
		//clean the noHTMLString and remove any extra white spaces
		String noExtraWhiteSpace = noHTMLString.replaceAll("\\s+"," ");
		//split all the words in the noExtraWhiteSpace by the white spaces
		String[] splitWords = noExtraWhiteSpace.split(" ");
		//create new List to store the words
		List<String> clearnSplitWords = new ArrayList<String>();

		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
		Map<String, Integer> mapOfLists = new HashMap<String, Integer>();
		int count = 1;
		
	    //sort array
	    Arrays.sort(splitWords);
	
    	for (int i = 0; i < splitWords.length; i++) {
			
    		if(splitWords[i] != null && !splitWords[i].isBlank()) {
    			
    			//remove non-alphanumeric characters
    			clearnSplitWords.add(splitWords[i].replaceAll("[^a-zA-Z0-9]", ""));
    		}
		}
    	
    	String currentWord = clearnSplitWords.get(0);
    	
    	for (int i = 0; i < clearnSplitWords.size(); i++) {
			
			if(splitWords[i].equals(currentWord)){
	            count++;
	        } else{
	        	mapOfLists.put(currentWord, count);
	            count = 1;
	            currentWord = clearnSplitWords.get(i);
	        }
		}
    	
    	for (Map.Entry<String, Integer> entry : mapOfLists.entrySet()) {
            list.add(entry.getValue());
        }
    	
    	//Sort from desc order
    	Collections.sort(list, Collections.reverseOrder()); 
    	String result;
    	
//    	System.out.println(list);
    	
    	//loop through numbered list and check to find match, then store mapOfLists value and num into sortedMap array
        for (int num : list) {
            for (Entry<String, Integer> entry : mapOfLists.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }
        
//        System.out.println(mapOfLists);
//        System.out.println(sortedMap);
        
        /****** Old block for List *****/
        
//        Iterator it = sortedMap.entrySet().iterator();
//        int counter = 0;
//        
//        while (it.hasNext() && counter < 20) {
//		
//	        Map.Entry pair = (Map.Entry)it.next();
//	        String tempString = pair.getKey().toString();
//			String tempInt = String.valueOf(pair.getValue());
//	        result = tempString + " x " + tempInt + " times";
//	        it.remove();
//	        resultList.add(result);
//            
//            counter++;
//        }
        
        String url = "jdbc:mysql://localhost:3306/word_occurrences";
		String uname = "root";
		String pass = "";
		String queryShowAll = "SELECT * FROM word";
		
		/****** New block for SQL statement ******/
		
		//Try catch to establish SQL connection
		try {
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Connection connection = DriverManager.getConnection(url, uname, pass);
			PreparedStatement statement;
			
			Iterator it = sortedMap.entrySet().iterator();
	        int counter = 0;
	        
	        while (it.hasNext() && counter < 20) {
			
		        Map.Entry pair = (Map.Entry)it.next();
		        String tempString = pair.getKey().toString();
				String tempInt = String.valueOf(pair.getValue());
				String insertQuery = "INSERT INTO word (word, occurrence) VALUES(?,?)";
				
				statement = connection.prepareStatement(insertQuery); //PreparedStatement
				statement.setString(1, tempString);
				statement.setString(2, tempInt);
				
				statement.executeUpdate();
	            
	            counter++;
//	            
	        }
			
	        //retrieve values from database
			statement = connection.prepareStatement(queryShowAll); //PreparedStatement
			ResultSet queryResultSet = statement.executeQuery();
			
			while(queryResultSet.next()) {
				resultList.add(queryResultSet.getString("word") + " x " + queryResultSet.getString("occurrence") + "times");
			}
			
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return resultList;
	}
	
	/**
	 * 
	 * ReadFile method
	 * 
	 * @param file for the file path
	 * @param html to read each line of the file to append to the html string builder 
	 * 
	 * @return return the result of the html string buffer. This method will read the htm file from a given
	 * path. The method will read each line and determine where to begin appending the file's content into the
	 * html string buffer using the "htmlPoemStart" to begin, and "htmlPoemEnd" to end the buffer. Once complete,
	 * store and return the results from the "results" variable.
	 */
	//function to read data from file
	public static String ReadFile(Path file, StringBuilder html) {
		
		String result = "";
		
		try(BufferedReader bufferedReader = Files.newBufferedReader(file)){
			
			String htmlPoemStart = "<h1>The Raven</h1>";
			String htmlPoemEnd = "</div><!--end chapter-->";
			boolean matchFound = false;
			String line = bufferedReader.readLine();
			
			while (line != null) {
				line = bufferedReader.readLine();
				
				//ensure the line buffer is not null
				if(line != null) {
					
					if (line.equals(htmlPoemStart)) {
						matchFound = true;
					}
					
					if (line.equals(htmlPoemEnd)) {
						matchFound = false;
					}
				}
				
				if (matchFound) {
					html.append(line);
				}
			}
			
			result = html.toString();
			
			//close file after completion
			bufferedReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}	
}
