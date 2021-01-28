package uk.ac.nott.cs.g53dia.agent.ScoreAgent;

import uk.ac.nott.cs.g53dia.library.*;

public class TaskBin {
	Cell bin = null;
	
	int litterLevel = 0;
	
	public TaskBin(Cell bin) {
		this.bin = bin;
		litterLevel = ((LitterBin) bin).getTask().getAmount();
	}

	public Cell getBin() {
		return bin;
	}

	public void setLitterLevel(int litterLevel) {
		this.litterLevel = litterLevel;
	}
	
	public int getLitterLevel() {
		return litterLevel;
	}
	
	
	
}
