package uk.ac.nott.cs.g53dia.agent;

import uk.ac.nott.cs.g53dia.library.*;
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
public class MyLitterAgent extends LitterAgent {

	public MyLitterAgent() {
		this(new Random());
	}

	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * 
	 * @param r
	 *            The random number generator.
	 */
	public MyLitterAgent(Random r) {
		this.r = r;
	}

	/*
	 * The following is a simple demonstration of how to write a tanker. The
	 * code below is very stupid and simply moves the tanker randomly until the
	 * charge agt is half full, at which point it returns to a charge pump.
	 */
	public Action senseAndAct(Cell[][] view, long timestep) {

		if(getChargeLevel() < 100) {
			if(view[VIEW_RANGE][VIEW_RANGE] instanceof RechargePoint) {
				// on a recharge point
				return new RechargeAction();
			}
			else {
				// find a recharge point
				Cell target = findNearestRechargePoint(view);
				if(target != null)
					return new MoveTowardsAction(target.getPoint());
				else
					return new MoveAction(0);
			}
		}
		else if (getLitterLevel() <= 0) {
			// carrying nothing
			if(view[VIEW_RANGE][VIEW_RANGE] instanceof LitterBin) {
				// on a litter bin
				if(((LitterBin) view[VIEW_RANGE][VIEW_RANGE]).getTask() != null) 
					return new LoadAction(((LitterBin) view[VIEW_RANGE][VIEW_RANGE]).getTask());
				else
					return new MoveAction(0);
			}
			else {
				// find a bin
				Cell target = findNearestNonEmptyLitterBin(view);
				if(target != null)
					return new MoveTowardsAction(target.getPoint());
				else
					return new MoveAction(0);
			}
		}
		else if(getRecyclingLevel()>0) {
			// carrying recycling
			if(view[VIEW_RANGE][VIEW_RANGE] instanceof RecyclingStation) {
				// on a recycling station
				return new DisposeAction();
			}
			else {
				Cell target = findNearestRecyclingStation(view);
				if(target != null)
					return new MoveTowardsAction(target.getPoint());
				else
					return new MoveAction(0);
			}
		}else {
			// carrying waste
			if(view[VIEW_RANGE][VIEW_RANGE] instanceof WasteStation) {
				// on a waste station
				return new DisposeAction();
			}
			else {
				Cell target = findNearestWasteStation(view);
				if(target != null)
					return new MoveTowardsAction(target.getPoint());
				else
					return new MoveAction(0);
			}
		}
	}
	
	private Cell findNearestRechargePoint(Cell[][] view) {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for (int m = 0; m < view.length; m++) {
			for (int n = 0; n < view[0].length; n++) {
				if(view[m][n] instanceof RechargePoint) {
					if(DistanceTo(view[m][n]) < shortestDistance) {
						targetPoint = view[m][n];
						shortestDistance = DistanceTo(view[m][n]);
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
		for (int m = 0; m < view.length; m++) {
			for (int n = 0; n < view[0].length; n++) {
				if(view[m][n] instanceof LitterBin) {
					if(((LitterBin) view[m][n]).getTask() != null && DistanceTo(view[m][n]) < shortestDistance) {
						targetBin = view[m][n];
						shortestDistance = DistanceTo(view[m][n]);
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
		for (int m = 0; m < view.length; m++) {
			for (int n = 0; n < view[0].length; n++) {
				if(view[m][n] instanceof RecyclingBin) {
					if(((LitterBin) view[m][n]).getTask() != null && DistanceTo(view[m][n]) < shortestDistance) {
						targetBin = view[m][n];
						shortestDistance = DistanceTo(view[m][n]);
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
		for (int m = 0; m < view.length; m++) {
			for (int n = 0; n < view[0].length; n++) {
				if(view[m][n] instanceof WasteBin) {
					if(((LitterBin) view[m][n]).getTask() != null && DistanceTo(view[m][n]) < shortestDistance) {
						targetBin = view[m][n];
						shortestDistance = DistanceTo(view[m][n]);
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
		for (int m = 0; m < view.length; m++) {
			for (int n = 0; n < view[0].length; n++) {
				if(view[m][n] instanceof RecyclingStation) {
					if(DistanceTo(view[m][n]) < shortestDistance) {
						targetStation = view[m][n];
						shortestDistance = DistanceTo(view[m][n]);
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
		for (int m = 0; m < view.length; m++) {
			for (int n = 0; n < view[0].length; n++) {
				if(view[m][n] instanceof WasteStation) {
					if(DistanceTo(view[m][n]) < shortestDistance) {
						targetStation = view[m][n];
						shortestDistance = DistanceTo(view[m][n]);
					}
				}
			}
		}
		// attention!!!!!!!!!!!!!! maybe null (no station!!!)
		return targetStation;
	}
	
	// input the target cell and return the distance from agent to it
	private int DistanceTo(Cell target) {
		return getPosition().distanceTo(target.getPoint());
	}
}

















