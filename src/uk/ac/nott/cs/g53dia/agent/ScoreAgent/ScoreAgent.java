package uk.ac.nott.cs.g53dia.agent.ScoreAgent;

import uk.ac.nott.cs.g53dia.library.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple example LitterAgent
 * 
 * @author Julian Zappala
 */
/*
 * Copyright (c) 2011 Julian Zappala
 * 
 * See the file "license.terms" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class ScoreAgent extends LitterAgent {
	
	private ScoreMemory AgentMemory = new ScoreMemory();
	private Cell currentCell = null;
	private ReactiveLayer reactiveLayer = new ReactiveLayer(this);
	private DeliberativeLayer deliberativeLayer= new DeliberativeLayer(this);
	
	public int distance = 0;
	
	public ScoreAgent() {
		this(new Random());
	
	}

	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * 
	 * @param r
	 *            The random number generator.
	 */
	public ScoreAgent(Random r) {
		this.r = r;
	}

	/**
	 * The operation control center of agent
	 */
	public Action senseAndAct(Cell[][] view, long timestep) {
		// store current position information in the currentcell
		currentCell = getCurrentCell(view);
		
		AgentMemory.updateCurrentCell(currentCell);
		AgentMemory.updateRechargePointList(view);
		AgentMemory.updateRecyclingStationList(view);
		AgentMemory.updateWasteStationList(view);
		AgentMemory.updateTaskBinList(view);
		AgentMemory.updateBinList(view);
		
//		System.out.println(getLitterLevel());
//		System.out.println(currentCell instanceof EmptyCell);
//		System.out.println(currentCell.getClass());
//		if(currentCell instanceof LitterBin)
//		System.out.println(((LitterBin) currentCell).getTask() != null);
		//System.out.println(distance);
		// The agent on a non-empty cell
		if(!(currentCell instanceof EmptyCell)) {
			Action react = reactiveLayer.Execute();
			if(react != null) return react;
			else {
				distance++;
				return deliberativeLayer.Execute(); 
			}
		}
		// The agent on a empty cell
		
		else {
			distance++;
			return deliberativeLayer.Execute();
		}
		
	}
	
	
	
	
	public Cell getCurrentCell() {
		return currentCell;
	}
	
	public ScoreMemory getAgentMemory() {
		return AgentMemory;
	}
	
}

