package nl.saxion.dna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * This TrieNode<T> class keeps track of all his children
 * and other variables such as his characters and if its a complete word
 * 
 * @author Idmon & Emre
 * @param <T> Data Structure
 *
 */
public class TrieNode<T> {
	
	private TrieNode<T> parent;						// The Parent node
	private ArrayList<TrieNode<T>> children;		// His child nodes
	private boolean isLeaf;							// Is it a leaf
	private boolean isWord;							// Is it a word
	private String startCharacter;					// The starting character
	private String characters;						// The remaining characters
	private T data;									// Data object to store extra information
	
	// Keep track of node numbers (For graph visualizing purposes)
	private static int NR;
	private int nr;
	
	/**
	 * Constructor for the root-node
	 */
	public TrieNode() {
		children = new ArrayList<TrieNode<T>>();
		isLeaf = true;
		isWord = false;
		characters = "";
		nr = NR++;
	}
	
	/**
	 * Constructor for the child-nodes
	 * @param startCharacter - The starting character
	 * @param characters 	 - The remaining characters
	 * @param data			 - The extra data-object
	 */
	public TrieNode(String startCharacter, String characters, T data) {
		this();
		this.startCharacter = startCharacter;
		this.characters = characters;
		this.data = data;
		nr = NR++;
	}
	
	/**
	 * Find a node with the same starting character
	 * @param c 		- Starting Character you're looking for
	 * @return child	- The child with this starting character
	 */
	public TrieNode<T> findNode(String s) {
		for(TrieNode<T> child : children) {
			if(s != null && child.startCharacter != null && s.equals(child.startCharacter)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Adds a new word to the TrieNode<T>. This method works through recursion
	 * by adding a new child if the character has not been found in its children.
	 * Also if there are still characters remaining, they will be all stored in the
	 * 'characters' variable to reduce the depth of this tree.
	 * @param word 	- The word that needs to be added
	 * @param newData 	- The data-object that needs to be stored within the word
	 */
    public void insert(String word, T newData) {
            isLeaf = false;
            String s = word.substring(0, 1);

            // Find the child with this specific character
            TrieNode<T> child = findNode(s);            
            if(child == null) {
            	// Character not found, so a new node will be created
            	// All remaining characters will be stored in this as well
                child = new TrieNode<T>(s, word.substring(1), null);
                child.parent = this;
                child.isWord = true;
                child.data = newData;
                children.add(child);                   
            } else {
            	
            	String childWord = child.startCharacter + child.characters;
            	
            	// If the word already exists, edit the stored Data-object and add new position
            	if(child.isWord && word.equals(childWord) && newData instanceof Data) {
                	Data chData = (Data) child.getData();
                	Data nwData = (Data) newData;
                	chData.addPosition(nwData.getPosition().get(0));
            	} else {
                	
                    // Split the characters in seperate nodes
                    if(child.characters.length() != 0) {
                        child.insert(child.characters, child.data);
                        child.isWord = false;
                        child.data = null;
                        child.characters = "";
                    }
                    
                    // If it an existing character within the trie
                    // Make it a word and store the data
                    if(word.length() == 1) {
                        child.isWord = true;
                        child.data = newData;
                    } else {
                    	child.insert(word.substring(1), newData);
                    }  
            	}
            }
    }

	
	/**
	 * This method deletes a word from the TrieNode<T>
	 * It uses recursion to and asks from its parent-node to be deleted
	 * After deletion, the trie is automatically gonna re-merge itself to
	 * reduce the depth
	 */
	public void delete(String s) {
		System.out.println("Traversing in: " + s);
		// Searching for the to be deleted character
		if(children.size() > 0){ 
			for(int i = 0; i < children.size(); i++) {
				TrieNode<T> child = children.get(i);
				if(child != null && s.equals((child.startCharacter + child.characters))) {
					System.out.println("Node '" + s + "' is deleted!");
					child = null;
					children.remove(i);
					isLeaf = children.size() == 0;
					break;
				}
			}
		}
		
		// If it is a leaf and not root-node, ask parent to delete yourself
		if(startCharacter != null && isLeaf) {
			parent.delete(startCharacter + characters);
		}
		
		// If it is not a leaf but within the trie, set isWord to false and delete the stored data
		if(s.equals(startCharacter) && !isLeaf) {
			System.out.println("Character stays in the tree, but word is being deleted");
			isWord = false;
			data = null;
		} else {
			// Search for its leaf to merge itself and
			// get rid of unnecessary depth
			if(!isLeaf) {
				TrieNode<T> leaf = getLeaf();
				if(leaf != null && leaf != this) {
					leaf.merge();
				}
			}
		}
	}
	
	/**
	 * Simple recursion method that gets the bottom leaf of a series nodes that have 1 child
	 * The retrieved leaf-node will be merged with its parent
	 * @return
	 */
	public TrieNode<T> getLeaf() {
		
		// It's a leaf when it has 0 children
		if(children.size() == 0) {
			return this;
		}
		
		// Traverse to the bottom leaf as long as each node has 1 child
		for(TrieNode<T> child : children) {
			if(child != null && children.size() == 1) {
				return child.getLeaf();
			} else {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * This method works on recursion by merging its remaining characters
	 * with his parent until the parent is either:
	 * - A word
	 * - Root node
	 * - Has more than 1 child
	 */
	public void merge() {
		if(parent.startCharacter == null || parent.isWord || parent.children.size() != 1) {
			return;
		} else {

			// Set the parent to a leaf, make it a word and store the data
			// Merge the remaining characters with its parent
			// Finally remove it from its children map
			parent.isWord = true;
			parent.isLeaf = true;
			parent.data = data;
			parent.characters += startCharacter + characters;
			parent.children.remove(0);	
			
			// Jump to his parent
			parent.merge();
		}
	}
	
	/**
	 * Builds a list with all the words under the current node
	 * Used as an autocomplete-feature
	 * @return ArrayList - List with words
	 */
	public ArrayList<String> autocomplete() {
		ArrayList<String> words = new ArrayList<String>();
		
		// If the current node is a word, add it to the list
		if(isWord) {
			words.add(toString());
		}
		
		// If there are children, find the words and add them as well (recursion)
		if(!isLeaf) {
			for(TrieNode<T> child : children) {
				if(child != null) {
					words.addAll(child.autocomplete());
				}
			}
		}
		return words;
	}
	
	/**
	 * Builds a list of words in Alfabetical order
	 * Also the depth of the node will be given with the map
	 * @return TreeMap<String, Integer> - The produced alfabetical table
	 */
	public TreeMap<String, Integer> getAlfabeticalOrder(int depth) {
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();

		// Add if it is indeed a word
		if(isWord) {
			map.put(toString(), depth);
		}
		
		// Increment the depth
		depth++;

		// If there are children, find them and add them as well (recursion)
		if(!isLeaf) {
			for(TrieNode<T> child : children) {
				if(child != null) {
					TreeMap<String, Integer> table = child.getAlfabeticalOrder(depth);
					map.putAll(table);
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 * Builds a list of words in frequency order
	 * Also the amount of frequency will be displayed
	 * @return HashMap<String, Integer> - The produced frequency list
	 */
	public HashMap<String, Integer> getFrequencyOrder() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		// Is it a word?
		if(isWord) {
			String word = toString();
			Data qData = (Data) getData();
			map.put(word, qData.getPosition().size());
		}
		
		// Look at his children
		if(!isLeaf) {
			for(TrieNode<T> child : children) {
				if(child != null) {
					// Merge the children map into its parent map
					HashMap<String, Integer> table = child.getFrequencyOrder();
					for(String key : table.keySet()) {
						map.put(key, table.get(key));
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * Print the Trie in the console in a nice way
	 * @return String - Structure for the console to output
	 */
	public String prettyPrint(int spaces) {
		// Calculate spaces
		String offset = "";
		for(int i = 0; i < spaces; i++) {
			offset += " ";
		}
		spaces+=2;
		
		// Add the rest
		StringBuilder builder = new StringBuilder("\n");
		for(TrieNode<T> child : children) {
			builder.append(offset);
			builder.append(child.startCharacter + child.characters);
			builder.append(child.prettyPrint(spaces));
		}
		return builder.toString();
	}

	/**
	 * Builds a String that TrieNode<T> represents
	 * Example: Current node = 't', parent = 'c', parent of 
	 * the parent = 'c' will output "cat"
	 */
	@Override
	public String toString() {
		if(parent == null) {
			return "";
		}else{
			return parent.toString() + startCharacter + characters;
		}
	}
	
	/**
	 * Prints the tree which should be copied into the following site: http://graphviz-dev.appspot.com/
	 */
	public void print(){
		System.out.println(toDotString());
	}

	/**
	 * Returns the tree in string format
	 * @return the tree in string format
	 */
	public String toDotString() {
		String output = "digraph{\n";
		// Print all nodes
		output += nodesToDot();
		// Print all edges
		output += edgesToDot();
		return output + "}";
	}

	/**
	 * Returns the nodes in string form
	 * @return the nodes
	 */
	private String nodesToDot() {
		String output = "n" + nr + "[label=\""+startCharacter + characters + "\"";
		if(isWord){
			output += ",fillcolor=\"palegreen\",style=\"filled,rounded\"];\n";
		}else{
			output += "];\n";
		}

		for (TrieNode<T> node : children) {
			output += node.nodesToDot();
		}
		return output;
	}

	/**
	 * Returns the edges in string form
	 * @return the edges
	 */
	private String edgesToDot() {
		String output = "";
		for (TrieNode<T> node : children) {
			output += "n" + nr + "->n" + node.nr + ";\n";
			output += node.edgesToDot();
		}
		return output;
	}
	
	/**
	 * Get all the remaining characters of the node
	 * @return
	 */
	public String getCharacters() {
		return characters;
	}
	
	/**
	 * Gives back its children
	 * @return ArrayList<TrieNode<T>> - An Arraylist of all its children
	 */
	public ArrayList<TrieNode<T>> getChildren() {
		return children;
	}
	
	/**
	 * Gives back the data-object that is stored
	 * @return Data
	 */
	public T getData() {
		return data;
	}
}
