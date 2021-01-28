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
public class GreedyMemoryAgent extends LitterAgent {
	
	Database AgentMemory = new Database();
	
	public GreedyMemoryAgent() {
		this(new Random());
	}

	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * 
	 * @param r
	 *            The random number generator.
	 */
	public GreedyMemoryAgent(Random r) {
		this.r = r;
	}

	/**
	 * The operation control center of agent
	 */
	public Action senseAndAct(Cell[][] view, long timestep) {
		// store current position information in the currentcell
		Cell currentCell = getCurrentCell(view);
		
		AgentMemory.updateCurrentCell(currentCell);
		AgentMemory.updateRechargePointList(view);
		AgentMemory.updateRecyclingStationList(view);
		AgentMemory.updateWasteStationList(view);
		AgentMemory.updateTaskBinList(view);
		
		// The agent on a non-empty cell
		
		if(getChargeLevel() < MAX_CHARGE && currentCell instanceof RechargePoint)
			return new RechargeAction();
		if((getRecyclingLevel() > 0 && currentCell instanceof RecyclingStation) 
			|| (getWasteLevel() > 0 && currentCell instanceof WasteStation)){
			
			return new DisposeAction();
		}
		if(((getLitterLevel() <= 0 && (currentCell instanceof RecyclingBin || currentCell instanceof WasteBin))
			|| (0 < getRecyclingLevel() && getRecyclingLevel() < MAX_LITTER && currentCell instanceof RecyclingBin) 
			|| (0 < getWasteLevel() && getWasteLevel() < MAX_LITTER && currentCell instanceof WasteBin))
			&& ((LitterBin) currentCell).getTask() != null) {
			AgentMemory.taskFinished(currentCell);
			return new LoadAction(((LitterBin) currentCell).getTask());
		}
		
		// The agent on a empty cell
		
		// Check battery level and recharge if the remaining battery is not enough to return recharge point
		if(!AgentMemory.checkBatteryLevel(getChargeLevel())) {
			if(AgentMemory.isRechargePointListEmpty())
				return new MoveAction(exploreMove());
			else
				return new MoveTowardsAction(AgentMemory.findNearestRechargePoint().getPoint());
		}
		
		// Find a task bin when there are no litter carried
		if(getLitterLevel() <= 0) {
			if(AgentMemory.isTaskBinListEmpty())
				return new MoveAction(exploreMove());
			else
				return new MoveTowardsAction(AgentMemory.findNearestTaskBin().getPoint());
		}
		
		// The agent carrys recycling 
		if(getRecyclingLevel() > 0) {
			if(getRecyclingLevel() >= 200) {
				if(AgentMemory.isRecyclingStationListEmpty())
					return new MoveAction(exploreMove());
				else
					return new MoveTowardsAction(AgentMemory.findNearestRecyclingStation().getPoint());
			}
			else {
		 		if(!AgentMemory.isTaskBinListContainRecycling() && AgentMemory.isRecyclingStationListEmpty())
			 		return new MoveAction(exploreMove());
				else if(!AgentMemory.isTaskBinListContainRecycling())
					return new MoveTowardsAction(AgentMemory.findNearestRecyclingStation().getPoint());
				else if(AgentMemory.isRecyclingStationListEmpty())
					return new MoveTowardsAction(AgentMemory.findNearestRecyclingTaskBin().getPoint());
				else {
					return new MoveTowardsAction(AgentMemory.findNearestRecyclingStation().getPoint());
				}
			}
		}
		
		// The agent carrys waste
		else { 
			if(getWasteLevel() >= 200) {
				if(AgentMemory.isWasteStationListEmpty())
					return new MoveAction(exploreMove());
				else
					return new MoveTowardsAction(AgentMemory.findNearestWasteStation().getPoint());
			}
			else {
		 		if(!AgentMemory.isTaskBinListContainWaste() && AgentMemory.isWasteStationListEmpty())
			 		return new MoveAction(exploreMove());
				else if(!AgentMemory.isTaskBinListContainWaste())
					return new MoveTowardsAction(AgentMemory.findNearestWasteStation().getPoint());
				else if(AgentMemory.isWasteStationListEmpty())
					return new MoveTowardsAction(AgentMemory.findNearestWasteTaskBin().getPoint());
				else {
					return new MoveTowardsAction(AgentMemory.findNearestWasteStation().getPoint());
				}
			}
		}
	}
	
	
	private int exploreMove() {
		int direction = r.nextInt(8);
		return direction;
	}
	
	

	
	
	/**
	 * Check if given bin is closer to station 
	 * @param litterbin Given litter bin
	 * @param station Given litter station
	 * @return
	 */
	private boolean isBinCloser(Cell bin, Cell station) {
		return DistanceTo(bin) < DistanceTo(station);
	}
	
	/**
	 * Check if given bin and station is in the same direction with respect to the position of agent
	 * @param bin
	 * @param station
	 * @return
	 */
	private boolean isSameDirection(Cell bin, Cell station) {
		// positive means same direction
		boolean xDirection = !(bin.getPoint().getX() > getPosition().getX() ^ station.getPoint().getX() > getPosition().getX());
		boolean yDirection = !(bin.getPoint().getY() > getPosition().getY() ^ station.getPoint().getY() > getPosition().getY());
		return xDirection && yDirection;
	}
	
	// input the target cell and return the distance from agent to it
	private int DistanceTo(Cell target) {
		return getPosition().distanceTo(target.getPoint());
	}
	
	
	
}

