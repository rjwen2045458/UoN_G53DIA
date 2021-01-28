package uk.ac.nott.cs.g53dia.agent;

import uk.ac.nott.cs.g53dia.library.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.PrimitiveIterator.OfDouble;

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
public class RememberLitterAgent extends LitterAgent {
	/**
	 * Store the recharge point the agent have been
	 */
	private ArrayList<Point> RechargePointList = new ArrayList<Point>();
	
	public RememberLitterAgent() {
		this(new Random());
	}

	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * 
	 * @param r
	 *            The random number generator.
	 */
	public RememberLitterAgent(Random r) {
		this.r = r;
		
		RechargePointList.add(RECHARGE_POINT_LOCATION);
	}

	/**
	 * The operation control center of agent
	 */
	public Action senseAndAct(Cell[][] view, long timestep) {
		// store current position information in the currentcell
		Cell currentCell = getCurrentCell(view);
		// check whether there are new recharge point detected and add to list  
		addNewRechargePoint(view);
		
		if(!checkBatteryLevel()) {
			// battery level is not enough
			if(currentCell instanceof RechargePoint) {
				// on a recharge point
				return new RechargeAction();
			}
			else {
				// find a recharge point
				Point target = findNearestRechargePointInList();
				return new MoveTowardsAction(target);
			}
		}
		else if (getLitterLevel() <= 0) {
			// carrying nothing
			if(currentCell instanceof LitterBin) {
				// on a litter bin
				if(((LitterBin) currentCell).getTask() != null) 
					return new LoadAction(((LitterBin) currentCell).getTask());
				else
					return new MoveAction(4);
			}
			else {
				// find a bin
				Cell target = findNearestNonEmptyLitterBin(view);
				if(target != null)
					return new MoveTowardsAction(target.getPoint());
				else
					return new MoveAction(4);
			}
		}
		else if(getRecyclingLevel()>0) {
			// carrying recycling
			if(currentCell instanceof RecyclingStation) {
				// on a recycling station    
				return new DisposeAction();
			}
			else if(getRecyclingLevel() >= 200) {
				return new MoveTowardsAction(findNearestRecyclingStation(view).getPoint());
			}
			else if(currentCell instanceof RecyclingBin && ((LitterBin) currentCell).getTask() != null) {
				return new LoadAction(((LitterBin) currentCell).getTask());
			}
			else if(getRecyclingLevel() >= 140){
  				Cell recyclingBin = findNearestNonEmptyRecyclingBin(view);
	 			Cell station = findNearestRecyclingStation(view);
		 		if(recyclingBin == null && station == null)
			 		return new MoveAction(4);
				else if(recyclingBin == null)
					return new MoveTowardsAction(station.getPoint());
				else if(station == null)
					return new MoveAction(4);
				else {
					if(isBinCloser(recyclingBin, station) && isSameDirection(recyclingBin, station))
						return new MoveTowardsAction(recyclingBin.getPoint());
					else
						return new MoveTowardsAction(station.getPoint());
				}
			}
			else {// The recycling level is below 140
				Cell recyclingBin = findNearestNonEmptyRecyclingBin(view);
	 			Cell station = findNearestRecyclingStation(view);
	 			if(recyclingBin == null && station == null)
			 		return new MoveAction(4);
				else if(recyclingBin == null)
					return new MoveTowardsAction(station.getPoint());
				else if(station == null)
					return new MoveTowardsAction(recyclingBin.getPoint());
				else {
					if(isBinCloser(recyclingBin, station))
						return new MoveTowardsAction(recyclingBin.getPoint());
					else
						return new MoveTowardsAction(station.getPoint());
				}
			}
		}else { // carrying waste
			if(currentCell instanceof WasteStation) {
				// on a waste station
				return new DisposeAction();
			}
			else if(getWasteLevel() >= 200) {
				return new MoveTowardsAction(findNearestWasteStation(view).getPoint());
			}
			else if(currentCell instanceof WasteBin && ((LitterBin) currentCell).getTask() != null) {
				return new LoadAction(((LitterBin) currentCell).getTask());
			}
			else if(getWasteLevel() >= 140){
  				Cell wasteBin = findNearestNonEmptyWasteBin(view);
	 			Cell station = findNearestWasteStation(view);
		 		if(wasteBin == null && station == null)
			 		return new MoveAction(4);
				else if(wasteBin == null)
					return new MoveTowardsAction(station.getPoint());
				else if(station == null)
					return new MoveAction(4);
				else {
					if(isBinCloser(wasteBin, station) && isSameDirection(wasteBin, station))
						return new MoveTowardsAction(wasteBin.getPoint());
					else
						return new MoveTowardsAction(station.getPoint());
				}
			}
			else {// The recycling level is below 140
				Cell wasteBin = findNearestNonEmptyWasteBin(view);
	 			Cell station = findNearestWasteStation(view);
	 			if(wasteBin == null && station == null)
			 		return new MoveAction(4);
				else if(wasteBin == null)
					return new MoveTowardsAction(station.getPoint());
				else if(station == null)
					return new MoveTowardsAction(wasteBin.getPoint());
				else {
					if(isBinCloser(wasteBin, station))
						return new MoveTowardsAction(wasteBin.getPoint());
					else
						return new MoveTowardsAction(station.getPoint());
				}
			}
		}
	}
	
	/**
	 * Add new recharge point into the list
	 * @param view
	 */
	private void addNewRechargePoint(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof RechargePoint) {
					boolean inRechargeList = false;
					for (Point rechargeP : RechargePointList) {
						if(rechargeP.distanceTo(cell.getPoint()) <= 0)
							inRechargeList = true;
					}
					if(!inRechargeList)
						RechargePointList.add(cell.getPoint());
				}
			}
		}
	}
	
	/**
	 * Find the nearest point in the recharge point list
	 * @return the nearest point
	 */
	private Point findNearestRechargePointInList() {
		Point nearestRechargePoint = RECHARGE_POINT_LOCATION;
		int shortestDistance = getPosition().distanceTo(nearestRechargePoint);
		for (Point rechargeP : RechargePointList) {
			int distance = getPosition().distanceTo(rechargeP);
			if(distance < shortestDistance) {
				nearestRechargePoint = rechargeP;
				shortestDistance = distance;
			}
		}
		return nearestRechargePoint;
	}
	
	/**
	 * Check whether the rest battery power support returning to the nearest recharge point
	 * True is enough. False is not.
	 * @return The answer in boolean
	 */
	private boolean checkBatteryLevel() {
		Point nearestRechargePoint = findNearestRechargePointInList();
		int returnCost = getPosition().distanceTo(nearestRechargePoint);
		if(returnCost >= getChargeLevel()-2)
			return false;
		else
			return true;
	}
	
	
	private Cell findNearestRechargePoint(Cell[][] view) {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof RechargePoint) {
					if(DistanceTo(cell) < shortestDistance) {
						targetPoint = cell;
						shortestDistance = DistanceTo(cell);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no bin!!!)
		return targetPoint;	
	}
	
	private Cell findNearestNonEmptyLitterBin(Cell[][] view) {
		Cell targetBin = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof LitterBin) {
					if(((LitterBin) cell).getTask() != null && DistanceTo(cell) < shortestDistance) {
						targetBin = cell;
						shortestDistance = DistanceTo(cell);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no bin!!!)
		return targetBin;	
	}
	
	private Cell findNearestNonEmptyRecyclingBin(Cell[][] view){
		Cell targetBin = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof RecyclingBin) {
					if(((LitterBin) cell).getTask() != null && DistanceTo(cell) < shortestDistance) {
						targetBin = cell;
						shortestDistance = DistanceTo(cell);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no bin!!!)
		return targetBin;
	}
	
	private Cell findNearestNonEmptyWasteBin(Cell[][] view){
		Cell targetBin = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof WasteBin) {
					if(((LitterBin) cell).getTask() != null && DistanceTo(cell) < shortestDistance) {
						targetBin = cell;
						shortestDistance = DistanceTo(cell);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no bin!!!)
		return targetBin;
	}
	
	private Cell findNearestRecyclingStation(Cell[][] view){
		Cell targetStation = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof RecyclingStation) {
					if(DistanceTo(cell) < shortestDistance) {
						targetStation = cell;
						shortestDistance = DistanceTo(cell);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no station!!!)
		return targetStation;
	}
	
	private Cell findNearestWasteStation(Cell[][] view){
		Cell targetStation = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof WasteStation) {
					if(DistanceTo(cell) < shortestDistance) {
						targetStation = cell;
						shortestDistance = DistanceTo(cell);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no station!!!)
		return targetStation;
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

