package nl.saxion.dna;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestTrie {
	
	private Trie<Data> trie;
	
	/**
	 * Constructs a basic Trie-structure
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		trie = new Trie<Data>();
	}

	@Test
	public void testTrie() {
		assertNotNull(trie);										// Make sure the object is created
		assertTrue(trie.getRoot().getChildren().size() == 0);		// Make sure it has no children
	}	

	/**
	 * Tests finding words/positions into the Trie
	 */
	@Test
	public void testInsertAndSearch() {		
		trie.insert("do", new Data(1));			// Add the word 'do' on position 1
		trie.insert("dorm", new Data(4));		// Add the word 'dorm' on position 4
		trie.insert("doll", new Data(7));		// Add the word 'doll' on position 7
		
		// Find the words in the trie
		int pos1 = trie.search("do").getPosition().get(0);
		int pos2 = trie.search("dorm").getPosition().get(0);
		int pos3 = trie.search("doll").getPosition().get(0);
		
		// Check the positions
		assertEquals(1, pos1);		// CHECK: position = 1
		assertEquals(4, pos2);		// CHECK: position = 2
		assertEquals(7, pos3);		// CHECK: position = 3
		
		// FAIL check (bad weather)
		assertNotEquals(99, pos1);	// Fail: Expected = 1
		assertNotEquals(99, pos2);	// Fail: Expected = 2
		assertNotEquals(99, pos3);	// Fail: Expected = 3
	}
	
	/**
	 * Testing the delete function
	 */
	@Test
	public void testDelete() {
		trie.insert("do", new Data(1));			// Add the word 'do' on position 1
		trie.insert("dorm", new Data(4));		// Add the word 'dorm' on position 4
		trie.insert("doll", new Data(7));		// Add the word 'doll' on position 7
		
		assertNotNull(trie.search("do"));		// Check: Word will be found
		trie.delete("do");						// Delete the word from the trie
		assertNull(trie.search("do"));			// Check: Word can't be found anymore
	}
	
	/**
	 * Test the autocomplete feature
	 */
	@Test
	public void testAutocomplete() {
		trie.insert("do", new Data(1));			// Add the word 'do' on position 1
		trie.insert("dorm", new Data(4));		// Add the word 'dorm' on position 4
		trie.insert("doll", new Data(7));		// Add the word 'doll' on position 7
		trie.insert("send", new Data(8));		// Add the word 'send' on position 8
		trie.insert("sense", new Data(5));		// Add the word 'doll' on position 5
		
		// Execute autocomplete on 'do' and store the results in ArrayList
		ArrayList<String> autocomplete = trie.autocomplete("do");
		assertTrue(autocomplete.contains("do"));		// CHECK: do is in the list
		assertTrue(autocomplete.contains("dorm"));		// CHECK: dorm is in the list
		assertTrue(autocomplete.contains("doll"));		// CHECK: doll is in the list
		
		assertFalse(autocomplete.contains("send"));		// FAIL: Send was not expected
		assertFalse(autocomplete.contains("sense"));	// FAIL: Sense was not expected
	}
	
	/**
	 * Tests inserting null or empty objects
	 * FAIL expected
	 */
	@Test(expected=AssertionError.class)
	public void insertNullAndEmpty() {
		trie.insert(null, new Data(8));		// Null word
		trie.insert("", new Data(8));		// Empty string word
		trie.insert("dorm", null);			// Null data-object
	}
	
	/**
	 * Tests deleting null or empty objects
	 * FAIL expected
	 */
	@Test(expected=AssertionError.class)
	public void deleteNullAndEmpty() {
		trie.delete(null);					// Null word
		trie.delete("");					// Empty string word
	}
	
	/**
	 * Tests finding null or empty objects
	 * FAIL expected
	 */
	@Test(expected=AssertionError.class)
	public void findNullAndEmpty() {
		trie.search(null);					// Null word
		trie.search("");					// Empty string word
	}
	
}
