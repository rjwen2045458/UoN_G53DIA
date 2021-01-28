package uk.ac.nott.cs.g53dia.agent.ScoreAgent;

import java.util.ArrayList;

import javax.print.attribute.Size2DSyntax;

import uk.ac.nott.cs.g53dia.library.*;

public class ScoreMemory {
	
	private Cell currentCell = null;
	
	private ArrayList<Cell> rechargePointList = new ArrayList<Cell>();
	
	private ArrayList<Cell> recyclingStationList = new ArrayList<Cell>();
	
	private ArrayList<Cell> wasteStationList = new ArrayList<Cell>();
	
	private ArrayList<TaskBin> taskBinList = new ArrayList<TaskBin>();
	
	private ArrayList<Cell> BinList = new ArrayList<Cell>();
	
	public ScoreMemory() {
//		this.currentCell = currentCell;
	}
	
	public void updateCurrentCell(Cell currentCell) {
		this.currentCell = currentCell;
	}
	
	/**
	 * Check whether the rest battery power support returning to the nearest recharge point
	 * True is enough. False is not.
	 * @return The answer in boolean
	 */
	public boolean checkBatteryLevel(int chargeLevel) {
		Cell nearestRechargePoint = findNearestRechargePoint();
		int returnCost = currentCell.getPoint().distanceTo(nearestRechargePoint.getPoint());
		if(returnCost >= chargeLevel-2)
			return false;
		else
			return true;
	}
	
	public void updateRechargePointList(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof RechargePoint) {
					boolean inRechargeList = false;
					for (Cell rechargeP : rechargePointList) {
						if(rechargeP.getPoint().distanceTo(cell.getPoint()) <= 0)
							inRechargeList = true;
					}
					if(!inRechargeList)
						rechargePointList.add(cell);
				}
			}
		}
	}
	
	public void updateRecyclingStationList(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof RecyclingStation) {
					boolean inRecyclingStationList = false;
					for (Cell recyclingC : recyclingStationList) {
						if(recyclingC.getPoint().distanceTo(cell.getPoint()) <= 0)
							inRecyclingStationList = true;
					}
					if(!inRecyclingStationList)
						recyclingStationList.add(cell);
				}
			}
		}
	}
	
	public void updateWasteStationList(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof WasteStation) {
					boolean inWasteStationList = false;
					for (Cell WasteS : wasteStationList) {
						if(WasteS.getPoint().distanceTo(cell.getPoint()) <= 0)
							inWasteStationList = true;
					}
					if(!inWasteStationList)
						wasteStationList.add(cell);
				}
			}
		}
	}
	
	public void updateTaskBinList(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof LitterBin && ((LitterBin) cell).getTask() != null) {
					boolean inTaskBinList = false;
					for (TaskBin taskBin : taskBinList) {
						if(taskBin.getBin().getPoint().equals(cell.getPoint()))
							inTaskBinList = true;
					}
					if(!inTaskBinList)
						taskBinList.add(new TaskBin(cell));
				}
			}
		}
	}
	
	public void updateBinList(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof LitterBin) {
					boolean inBinList = false;
					for (Cell Bin : BinList) {
						if(Bin.getPoint().equals(cell.getPoint()))
							inBinList = true;
					}
					if(!inBinList)
						BinList.add(cell);
				}
			}
		}
	}
	
	public boolean isRechargePointListEmpty() {
		return rechargePointList.isEmpty();
	}
	
	public boolean isRecyclingStationListEmpty() {
		return recyclingStationList.isEmpty();
	}
	
	public boolean isWasteStationListEmpty() {
		return wasteStationList.isEmpty();
	}
	
	public boolean isTaskBinListEmpty() {
		return taskBinList.isEmpty();
	}
	
	public boolean isTaskBinListContainRecycling() {
		for (TaskBin bin : taskBinList) {
			if(bin.getBin() instanceof RecyclingBin)
				return true;
		}
		return false;
	}
	
	public boolean isTaskBinListContainWaste() {
		for (TaskBin bin : taskBinList) {
			if(bin.getBin() instanceof WasteBin)
				return true;
		}
		return false;
	}
	
	public int exploreDirection() {
		int Northeast = 0;
		int Northwest = 0;
		int SouthWest = 0;
		int SouthEast = 0;
		
		for (Cell bin : BinList) {
			boolean xDirection = bin.getPoint().getX() > currentCell.getPoint().getX();
			boolean yDirection = bin.getPoint().getY() > currentCell.getPoint().getY();
			if(xDirection && yDirection) Northeast++;
			if(!xDirection && yDirection) Northwest++;
			if(xDirection && !yDirection) SouthWest++;
			if(!xDirection && !yDirection) SouthEast++;
		}
		
		int direction = Math.max(Math.max(Northeast, Northwest), Math.max(SouthWest, SouthEast));
		if(direction == Northeast) return MoveAction.NORTHEAST;
		else if(direction == Northwest) return MoveAction.NORTHWEST;
		else if(direction == SouthWest) return MoveAction.SOUTHWEST;
		else /*(direction == SouthEast)*/ return MoveAction.SOUTHEAST;
	}
	
	public Cell findNearestRechargePoint() {
//		if(rechargePointList.size() == 0)
//			return null;
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(Cell rechargeP : rechargePointList) {
			int distance = currentCell.getPoint().distanceTo(rechargeP.getPoint());
			if( distance < shortestDistance) {
				targetPoint = rechargeP;
				shortestDistance = distance;
			}		
		}	
		return targetPoint;	
	}
	
	public Cell findNearestRecyclingStation() {
//		if(recyclingStationList.size() == 0)
//			return null;
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(Cell recyclingC : recyclingStationList) {
			int distance = currentCell.getPoint().distanceTo(recyclingC.getPoint());
			if( distance < shortestDistance) {
				targetPoint = recyclingC;
				shortestDistance = distance;
			}		
		}	
		return targetPoint;	
	}
	
	public Cell findNearestWasteStation() {
//		if(wasteStationList.size() == 0) 
//			return null;
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(Cell wasteC : wasteStationList) {
			int distance = currentCell.getPoint().distanceTo(wasteC.getPoint());
			if( distance < shortestDistance) {
				targetPoint = wasteC;
				shortestDistance = distance;
			}		
		}	
		return targetPoint;	
	}
	
	public Cell findNearestTaskBin() {
//		if(taskBinList.size() == 0)
//			return null;
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(TaskBin taskB : taskBinList) {
			int distance = currentCell.getPoint().distanceTo(taskB.getBin().getPoint());
			if( distance < shortestDistance) {
				targetPoint = taskB.getBin();
				shortestDistance = distance;
			}		
		}	
		return targetPoint;	
	}
	
	public Cell findNearestRecyclingTaskBin() {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(TaskBin recyclingB : taskBinList) {
			if(recyclingB.getBin() instanceof RecyclingBin) {
				int distance = currentCell.getPoint().distanceTo(recyclingB.getBin().getPoint());
				if( distance < shortestDistance) {
					targetPoint = recyclingB.getBin();
					shortestDistance = distance;
				}
			}
		}	
		return targetPoint;	
	}
	
	public Cell findNearestWasteTaskBin() {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(TaskBin wasteB : taskBinList) {
			if(wasteB.getBin() instanceof WasteBin) {
				int distance = currentCell.getPoint().distanceTo(wasteB.getBin().getPoint());
				if( distance < shortestDistance) {
					targetPoint = wasteB.getBin();
					shortestDistance = distance;
				}
			}
		}	
		return targetPoint;	
	}
	
	public Cell findWorthiestTaskBin() {
		Cell targetPoint = null;
		double highestScore = 0;
		for(TaskBin taskB : taskBinList) {
			double distance = currentCell.getPoint().distanceTo(taskB.getBin().getPoint());
			double score = taskB.getLitterLevel()/ distance;
			if( highestScore < score) {
				targetPoint = taskB.getBin();
				highestScore = score;
			}		
		}	
		return targetPoint;	
	}
	
	public Cell findWorthiestRecyclingTaskBin() {
		Cell targetPoint = null;
		double highestScore = 0;
		for(TaskBin recyclingB : taskBinList) {
			if(recyclingB.getBin() instanceof RecyclingBin) {
				int distance = currentCell.getPoint().distanceTo(recyclingB.getBin().getPoint());
				double score = recyclingB.getLitterLevel()/ distance;
				if( highestScore < score) {
					targetPoint = recyclingB.getBin();
					highestScore = score;
				}
			}
		}	
		return targetPoint;	
	}
	
	public Cell findWorthiestWasteTaskBin() {
		Cell targetPoint = null;
		double highestScore = 0;
		for(TaskBin wasteB : taskBinList) {
			if(wasteB.getBin() instanceof WasteBin) {
				int distance = currentCell.getPoint().distanceTo(wasteB.getBin().getPoint());
				double score = wasteB.getLitterLevel()/ distance;
				if( highestScore < score) {
					targetPoint = wasteB.getBin();
					highestScore = score;
				}
			}
		}	
		return targetPoint;	
	}
	
	public ArrayList<TaskBin> findPathWayRecyclingBins(Cell station){
		ArrayList<TaskBin> pathwayRecyclingBins = new ArrayList<TaskBin>();
		for (TaskBin recyclingB : taskBinList) {
			if(recyclingB.getBin() instanceof RecyclingBin) {
				if(currentCell.getPoint().distanceTo(recyclingB.getBin().getPoint()) 
					< currentCell.getPoint().distanceTo(station.getPoint())) {
					pathwayRecyclingBins.add(recyclingB);
				}
			}
		}
		while (pathwayRecyclingBins.size()>8) {
			pathwayRecyclingBins.remove(pathwayRecyclingBins.size()-1);
		}
		return pathwayRecyclingBins;
	}
	
	public ArrayList<TaskBin> findPathWayWasteBins(Cell station){
		ArrayList<TaskBin> pathwayWasteBins = new ArrayList<TaskBin>();
		for (TaskBin wasteB : taskBinList) {
			if(wasteB.getBin() instanceof WasteBin) {
				if(currentCell.getPoint().distanceTo(wasteB.getBin().getPoint()) 
					< currentCell.getPoint().distanceTo(station.getPoint())) {
					pathwayWasteBins.add(wasteB);
				}
			}
		}
		while (pathwayWasteBins.size()>8) {
			pathwayWasteBins.remove(pathwayWasteBins.size()-1);
		}
		return pathwayWasteBins;
	}
	
	public void LoadLitter(Cell currentBin, ScoreAgent agent) {
		for (TaskBin bin : taskBinList) {
			if (bin.getBin().getPoint().equals(currentBin.getPoint())) {
				if((LitterAgent.MAX_LITTER - agent.getLitterLevel()) >= bin.getLitterLevel()) {
					taskBinList.remove(bin);
					return;
				}
				else {
					bin.setLitterLevel(bin.getLitterLevel() - (LitterAgent.MAX_LITTER - agent.getLitterLevel()));
					return;
				}
			}
		}
	}
	
	
}


















