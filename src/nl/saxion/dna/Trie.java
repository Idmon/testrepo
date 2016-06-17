package nl.saxion.dna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A reduced (Trie) represents words with the help of a tree structure
 * Each node represents a series of character (or a single character)
 * Compared to a normal trie, this one reduces the depth which makes
 * traversing a little bit easier. especially for long words.
 * 
 * @author Idmon & Emre
 * @param <T> Data structure
 */
public class Trie<T> {

	private TrieNode<T> root;					// The root-node
	
	public Trie() {
		root = new TrieNode<T>();
	}
	
	/**
	 * Adds a new word to the Trie
	 * Voegt een woord aan de Trie toe.
	 * @param Word 	- The word that needs to be added to the trie
	 * @param Data 	- The Data-object that needs to be stored
	 */
	public void insert(String word, T data) {
		assert word != null	: "Word is null";
		assert word != ""	: "Word is empty";
		assert data != null	: "Data object is null";
		
		if(word != null && !word.isEmpty() && word.matches("[a-zA-Z]+")) {
			root.insert(word.toLowerCase(), data);
		}
		
		assert search(word) != null	: "Word has not been added";
	}
	
	/**
	 * Search the Trie for a specific prefix
	 * @param prefix
	 * @return Data 	- The Data-object stored in the word
	 */
	public T search(String prefix) {
		assert prefix != null	: "Prefix is null";
		assert prefix != ""	: "prefix is empty";
		
		TrieNode<T> lastNode = root;
		
		// Trying to find a node of the last letter of the prefix
		for(int i = 0; i < prefix.length(); i++) {
			lastNode = lastNode.findNode(prefix.substring(i, i+1));
			
			// If there hasn't been found one, return 'null'
			if(lastNode == null) {
				return null;
			}
			
			String remaingstr = prefix.substring(i+1);
			if(lastNode.getCharacters().equals(remaingstr)) {
				break;
			}
		}
		return lastNode.getData();
	}
	
	/**
	 * Deletes all characters from the prefix from the Trie
	 * @param prefix
	 */
	public void delete(String prefix) {
		assert prefix != null	: "Prefix is null";
		assert prefix != ""	: "prefix is empty";
		
		TrieNode<T> lastNode = root;
		
		// Trying to find a node of the last letter of the prefix
		int i = 0;
		for(; i < prefix.length(); i++) {
			lastNode = lastNode.findNode(prefix.substring(i, i+1));
			
			// If there hasn't been found one, return 'null'
			if(lastNode == null) {
				return;
			}
			
			String remaingstr = prefix.substring(i+1);
			if(lastNode.getCharacters().equals(remaingstr)) {
				break;
			}
		}
		lastNode.delete(prefix.substring(i));
	}
	
	/**
	 * Prints a list of all the words in the Trie in
	 * Alfabetical order
	 */
	public void getAlfabeticalOrder() {
		TreeMap<String, Integer> map = root.getAlfabeticalOrder(0);
		for(String word : map.keySet()) {
			System.out.println(map.get(word) + ": " + word);
		}
	}
	
	/**
	 * Prints a list of all the words in the Trie 
	 * and sorts them based on their frequency
	 */
	public void getFrequencyOrder() { 
		Map<String, Integer> map = root.getFrequencyOrder();
		map = MapUtil.sortByValue( map );
		for(String word : map.keySet()) {
			System.out.println(word + ": " + map.get(word) + "x");
		}
	}
	
	/**
	 * Builds a list of words with the given Prefix
	 * @param prefix
	 * @return List  - A list of all the options
	 */
	public ArrayList<String> autocomplete(String prefix) {
		TrieNode<T> lastNode = root;
		
		// Trying to find a node of the last letter of the prefix
		for(int i = 0; i < prefix.length(); i++) {
			lastNode = lastNode.findNode(prefix.substring(i, i+1));
			
			// If there hasn't been found one, return 'null'
			if(lastNode == null) {
				return new ArrayList<String>();
			}
			
			String remaingstr = prefix.substring(i+1);
			if(lastNode.getCharacters().equals(remaingstr)) {
				break;
			}
		}
		return lastNode.autocomplete();
	}
	
	/**
	 * Prints the Trie in a nice way
	 */
	public void prettyPrint() {
		System.out.println(root.prettyPrint(0));
	}
	
	/**
	 * Prints the tree which should be copied into the following site: http://graphviz-dev.appspot.com/
	 */
	public void print(){
		root.print();
	}
	
	/**
	 * Basic get method to get the root node
	 * @return
	 */
	public TrieNode<T> getRoot() {
		return root;
	}
	
	@Override
	public String toString() {
		return root.toString();
	}
}

