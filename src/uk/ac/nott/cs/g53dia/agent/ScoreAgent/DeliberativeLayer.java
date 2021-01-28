package uk.ac.nott.cs.g53dia.agent.ScoreAgent;

import java.util.ArrayList;

import uk.ac.nott.cs.g53dia.library.*;

public class DeliberativeLayer {
private ScoreAgent agent= null;
	
	public DeliberativeLayer(ScoreAgent agent) {
		this.agent = agent;
	}
	
	public Action Execute() {
		if(!agent.getAgentMemory().checkBatteryLevel(agent.getChargeLevel())) {
			if(agent.getAgentMemory().isRechargePointListEmpty())
				return new MoveAction(exploreMove());
			else {
				//System.out.println(1);
				return new MoveTowardsAction(agent.getAgentMemory().findNearestRechargePoint().getPoint());}
		}
		
		// Find a task bin when there are no litter carried
		if(agent.getLitterLevel() <= 0) {
			if(agent.getAgentMemory().isTaskBinListEmpty())
				return new MoveAction(exploreMove());
			else {
				//System.out.println(2);
				return new MoveTowardsAction(agent.getAgentMemory().findNearestTaskBin().getPoint());}
		}
		
		// The agent carrys recycling 
		if(agent.getRecyclingLevel() > 0) {
			if(agent.getRecyclingLevel() >= 200) {
				//System.out.println(3);
				return new MoveTowardsAction(agent.getAgentMemory().findNearestRecyclingStation().getPoint());}
			else {
				//System.out.println(4);
				return new MoveTowardsAction(findPathToRecyclingStation().getPoint());}
		}
		
		// The agent carrys waste
		else {
			if(agent.getWasteLevel() >= 200) {
			//	System.out.println(5);
				return new MoveTowardsAction(agent.getAgentMemory().findNearestWasteStation().getPoint());}
			else {
				//System.out.println(6);
				return new MoveTowardsAction(findPathToWasteStation().getPoint());}
		}
	}
	
	private int exploreMove() {
		return agent.getAgentMemory().exploreDirection();
	}
	
	public Cell findPathToRecyclingStation() {
		
		Cell station = agent.getAgentMemory().findNearestRecyclingStation();
		ArrayList<TaskBin> pathwayRecyclingBin = agent.getAgentMemory().findPathWayRecyclingBins(station);
		ArrayList<Object> pathwayRecyclingBins = new ArrayList<Object>();
		for(TaskBin bin : pathwayRecyclingBin)
			pathwayRecyclingBins.add((Object) bin);
		CombinationAndPermutation cap = new CombinationAndPermutation();
		//System.out.println(pathwayRecyclingBins.size());
		ArrayList<ArrayList<Object>> Path = cap.getCombinationAndPermutation(pathwayRecyclingBins);
		
		double averageScore = 0;
		ArrayList<Object> bestPath = new ArrayList<Object>();
		for (ArrayList<Object> path : Path) {
			double aScore = calculateScore(path)/calculateCost(path, station);
			if(aScore > averageScore) {
				averageScore = aScore;
				bestPath = path;
			}
		}
		if(bestPath.size() < 1) return station;
		else return ((TaskBin)bestPath.get(0)).getBin();
	}
	
	public Cell findPathToWasteStation() {
		
		Cell station = agent.getAgentMemory().findNearestWasteStation();
		ArrayList<TaskBin> pathwayWasteBin = agent.getAgentMemory().findPathWayWasteBins(station);
		ArrayList<Object> pathwayWasteBins = new ArrayList<Object>();
		for(TaskBin bin : pathwayWasteBin)
			pathwayWasteBins.add((Object) bin);
		CombinationAndPermutation cap = new CombinationAndPermutation();
		//System.out.println(pathwayWasteBins.size());
		ArrayList<ArrayList<Object>> Path = cap.getCombinationAndPermutation(pathwayWasteBins);
		
		double averageScore = 0;
		ArrayList<Object> bestPath = new ArrayList<Object>();
		for (ArrayList<Object> path : Path) {
			double aScore = calculateScore(path)/calculateCost(path, station);
			if(aScore > averageScore) {
				averageScore = aScore;
				bestPath = path;
			}
		}
		if(bestPath.size() < 1) return station;
		else return ((TaskBin)bestPath.get(0)).getBin();
	}
	
	public double calculateScore(ArrayList<Object> path) {
		double score = agent.getLitterLevel();
		for (Object bin : path) {
			score += ((TaskBin)bin).getLitterLevel();
		}
		if(score > LitterAgent.MAX_LITTER) return LitterAgent.MAX_LITTER;
		else return score;
	}

	public double calculateCost(ArrayList<Object> path, Cell station) {
		if(path.size()<1) {
			return agent.getPosition().distanceTo(station.getPoint());
		}
		else {
			double cost = 0;
			for (int x = 0; x < path.size()-1; x++) {
				cost += ((TaskBin)path.get(x)).getBin().getPoint().distanceTo(((TaskBin)path.get(x+1)).getBin().getPoint());
			}
			cost += agent.getPosition().distanceTo(((TaskBin)path.get(0)).getBin().getPoint());
			cost += ((TaskBin)path.get(path.size()-1)).getBin().getPoint().distanceTo(station.getPoint());
			return cost;
		}
	}
}
