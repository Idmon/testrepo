package nl.saxion.dna;

import java.util.ArrayList;

public class Data {

	private int number;
	private ArrayList<Integer> positions;
	
	public Data(int pos) {
		this.positions = new ArrayList<Integer>();
		this.positions.add(pos);
	}
	
	public ArrayList<Integer> getPosition() {
		return positions;
	}
	
	public void addPosition(int pos) {
		positions.add(pos);
	}
}
