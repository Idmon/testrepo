package nl.saxion.dna;

import java.io.File;
import java.util.Scanner;

/**
 * Assignment #3: Reduced Trie
 * @author Idmon & Emre
 */
public class Apl {

	/**
	 * This is the basic program that creates a Trie
	 * Adds a couple of basic words to it
	 * More actions are available
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Trie<Data> trie = new Trie<Data>();

		// Read the txt file and insert all the words into the Trie
		Scanner scanner = new Scanner(new File("words.txt"));
		int number = 0;
		while(scanner.hasNext()) {
			String word = scanner.next();
			Data data = new Data(++number);
			trie.insert(word, data);
		}
		scanner.close();
		
		// This print generates a code for: http://graphviz-dev.appspot.com/
		//trie.print();
		
		// Shows the trie in alfabetical order along with its depth
		System.out.println("\nPrinting Alfabetical order:");
		trie.getAlfabeticalOrder();
		
		// Shows the trie in frequency order
		System.out.println("\nPrinting Frequency order:");
		trie.getFrequencyOrder();
		
		// Delete a couple of words
		trie.prettyPrint();
		trie.delete("do");
		trie.delete("doll");
		trie.delete("dorm");
		trie.prettyPrint();
		
		// Search
		String query = "ball";
		Data data = trie.search(query);
		System.out.println("Searching for '" + query + "'");
		System.out.println("Data (Line nr): " + data.getPosition().toString());
		
		// Autocomplete
		query = "se";
		System.out.println("\nAutocomplete '" + query + "':");
		for(String str : trie.autocomplete(query)) {
			System.out.println(str);
		}
		
	}
	
}
