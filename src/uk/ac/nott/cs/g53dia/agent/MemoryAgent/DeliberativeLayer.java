package uk.ac.nott.cs.g53dia.agent.MemoryAgent;

import uk.ac.nott.cs.g53dia.library.*;

public class DeliberativeLayer {
	Agent agent = null;
	Database database = null;
	
	public DeliberativeLayer(Agent agent) {
		this.agent = agent;
		database = agent.getDatabase();
	}
	
	public Action execute() {
		// Check battery level and recharge if the remaining battery is not enough to return recharge point
		if(!database.checkBatteryLevel(agent.getChargeLevel())) {
			if(database.isRechargePointListEmpty())
				return new MoveAction(agent.exploreDirection());
			else
				return new MoveTowardsAction(database.findNearestRechargePoint().getPoint());
		}
				
		// Find a task bin when there are no litter carried
		if(agent.getLitterLevel() <= 0) {
			if(database.isTaskBinListEmpty())
				return new MoveAction(agent.exploreDirection());
			else
				return new MoveTowardsAction(database.findNearestTaskBin().getPoint());
		}
				
		// The agent carrys recycling 
		if(agent.getRecyclingLevel() > 0) {
			if(agent.getRecyclingLevel() >= 200) {
				if(database.isRecyclingStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else
					return new MoveTowardsAction(database.findNearestRecyclingStation().getPoint());
			}
			else if(agent.getRecyclingLevel() < 100){
				if(!database.isTaskBinListContainRecycling() && database.isRecyclingStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else if(!database.isTaskBinListContainRecycling())
					return new MoveTowardsAction(database.findNearestRecyclingStation().getPoint());
				else if(database.isRecyclingStationListEmpty())
					return new MoveTowardsAction(database.findNearestRecyclingTaskBin().getPoint());
				else {
					return new MoveTowardsAction(database.findNearestRecyclingTaskBin().getPoint());			
				}
			}
			else if(agent.getRecyclingLevel() >= 160){
				if(!database.isTaskBinListContainRecycling() && database.isRecyclingStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else if(!database.isTaskBinListContainRecycling())
					return new MoveTowardsAction(database.findNearestRecyclingStation().getPoint());
				else if(database.isRecyclingStationListEmpty())
					return new MoveTowardsAction(database.findNearestRecyclingTaskBin().getPoint());
				else {
					if(isBinCloser(database.findNearestRecyclingTaskBin(), database.findNearestRecyclingStation()) && 
						isSameDirection(database.findNearestRecyclingTaskBin(), database.findNearestRecyclingStation()))
						return new MoveTowardsAction(database.findNearestRecyclingTaskBin().getPoint());
					else
						return new MoveTowardsAction(database.findNearestRecyclingStation().getPoint());
						}
					}
			else {
				if(!database.isTaskBinListContainRecycling() && database.isRecyclingStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else if(!database.isTaskBinListContainRecycling())
					return new MoveTowardsAction(database.findNearestRecyclingStation().getPoint());
				else if(database.isRecyclingStationListEmpty())
					return new MoveTowardsAction(database.findNearestRecyclingTaskBin().getPoint());
				else {
					if(isBinCloser(database.findNearestRecyclingTaskBin(), database.findNearestRecyclingStation()))
						return new MoveTowardsAction(database.findNearestRecyclingTaskBin().getPoint());
					else
						return new MoveTowardsAction(database.findNearestRecyclingStation().getPoint());
				}
			}
		}
				
		// The agent carrys waste
		else { 
			if(agent.getWasteLevel() >= 200) {
				if(database.isWasteStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else
					return new MoveTowardsAction(database.findNearestWasteStation().getPoint());
			}
			else if(agent.getWasteLevel() < 100){
				if(!database.isTaskBinListContainWaste() && database.isWasteStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else if(!database.isTaskBinListContainWaste())
					return new MoveTowardsAction(database.findNearestWasteStation().getPoint());
				else if(database.isWasteStationListEmpty())
					return new MoveTowardsAction(database.findNearestWasteTaskBin().getPoint());
				else {
					return new MoveTowardsAction(database.findNearestWasteTaskBin().getPoint());	
				}
			}
			else if(agent.getWasteLevel() >= 160){
				if(!database.isTaskBinListContainWaste() && database.isWasteStationListEmpty())
					return new MoveAction(agent.exploreDirection());
				else if(!database.isTaskBinListContainWaste())
					return new MoveTowardsAction(database.findNearestWasteStation().getPoint());
				else if(database.isWasteStationListEmpty())
					return new MoveTowardsAction(database.findNearestWasteTaskBin().getPoint());
				else {
					if(isBinCloser(database.findNearestWasteTaskBin(), database.findNearestWasteStation()) && 
						isSameDirection(database.findNearestWasteTaskBin(), database.findNearestWasteStation()))
						return new MoveTowardsAction(database.findNearestWasteTaskBin().getPoint());
					else
						return new MoveTowardsAction(database.findNearestWasteStation().getPoint());
				}
			}
			else {// The waste level is below 160
				if(!database.isTaskBinListContainWaste() && database.isWasteStationListEmpty())
			 		return new MoveAction(agent.exploreDirection());
				else if(!database.isTaskBinListContainWaste())
					return new MoveTowardsAction(database.findNearestWasteStation().getPoint());
				else if(database.isWasteStationListEmpty())
					return new MoveTowardsAction(database.findNearestWasteTaskBin().getPoint());
				else {
					if(isBinCloser(database.findNearestWasteTaskBin(), database.findNearestWasteStation()))
						return new MoveTowardsAction(database.findNearestWasteTaskBin().getPoint());
					else
						return new MoveTowardsAction(database.findNearestWasteStation().getPoint());
				}
			}
		}
	}
	
	/**
	 * Check if the distance from agent to bin is closer than the distance from agent to station 
	 * @param bin
	 * @param station
	 * @return
	 */
	private boolean isBinCloser(Cell bin, Cell station) {
		return agent.getPosition().distanceTo(bin.getPoint()) < agent.getPosition().distanceTo(station.getPoint());
	}
	
	/**
	 * Check if the bin and station are in the same quadrant with respect to agent position
	 * @param bin
	 * @param station
	 * @return
	 */
	private boolean isSameDirection(Cell bin, Cell station) {
		// positive means same direction
		boolean xDirection = !(bin.getPoint().getX() > agent.getPosition().getX() ^ station.getPoint().getX() > agent.getPosition().getX());
		boolean yDirection = !(bin.getPoint().getY() > agent.getPosition().getY() ^ station.getPoint().getY() > agent.getPosition().getY());
		return xDirection && yDirection;
	}

							
}
