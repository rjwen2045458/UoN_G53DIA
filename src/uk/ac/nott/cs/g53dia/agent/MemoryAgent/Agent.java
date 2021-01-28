package uk.ac.nott.cs.g53dia.agent.MemoryAgent;

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
public class Agent extends LitterAgent {
	
	private Cell currentCell = null;
	
	private Database database = new Database();
	
	ReactiveLayer reactiveLayer = new ReactiveLayer(this);
	
	DeliberativeLayer deliberativeLayer = new DeliberativeLayer(this);
	
	private int explore = 0;
	
	public Agent() {
		this(new Random());
		explore = r.nextInt(8);
	}

	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * @param r
	 *            The random number generator.
	 */
	public Agent(Random r) {
		this.r = r;
	}

	/**
	 * The control subsystem of the agent
	 */
	public Action senseAndAct(Cell[][] view, long timestep) {
		
		// store current position information in the currentcell
		currentCell = getCurrentCell(view);
		
		// update agent database
		database.updateCurrentCell(currentCell);
		database.updateRechargePointList(view);
		database.updateRecyclingStationList(view);
		database.updateWasteStationList(view);
		database.updateTaskBinList(view);
		
		// To reactive layer
		// The agent on non-empty cell
		if(!(currentCell instanceof EmptyCell)) {
			Action react = reactiveLayer.execute();
			if(react != null) return react;
			else {
				return deliberativeLayer.execute(); 
			}
		}
		
		// To deliberative layer
		// The agent on a empty cell
		else {
			return deliberativeLayer.execute();
		}		
	}

	/**
	 * @return The current cell
	 */
	public Cell getCurrentCell() {
		return currentCell;
	}
	
	/**
	 * @return The agent database 
	 */
	public Database getDatabase() {
		return database;
	}
	
	/**
	 * @return the explore direction
	 */
	public int exploreDirection() {
		if(r.nextDouble()> 0.9)
			explore = r.nextInt(8); 
		return explore;		
	}
	
}

