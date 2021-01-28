package uk.ac.nott.cs.g53dia.agent.MemoryAgent;

import java.util.ArrayList;

import uk.ac.nott.cs.g53dia.library.*;

public class Database {
	
	private Cell currentCell = null;
	
	private ArrayList<Cell> rechargePointList = new ArrayList<Cell>();
	
	private ArrayList<Cell> recyclingStationList = new ArrayList<Cell>();
	
	private ArrayList<Cell> wasteStationList = new ArrayList<Cell>();
	
	private ArrayList<Cell> taskBinList = new ArrayList<Cell>();
	
	public Database() {
		
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
	
	/**
	 * Add new-found recharge points into recharge point list
	 * @param view
	 */
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
	
	/**
	 * Add new-found recycling stations into recycling station list
	 * @param view
	 */
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
	
	/**
	 * Add new-found waste stations into waste station list
	 * @param view
	 */
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
	
	/**
	 * Add new-found bin with task into task bin list
	 * @param view
	 */
	public void updateTaskBinList(Cell[][] view) {
		for (Cell[] row : view) {
			for (Cell cell : row) {
				if(cell instanceof LitterBin && ((LitterBin) cell).getTask() != null) {
					boolean inTaskBinList = false;
					for (Cell taskBin : taskBinList) {
						if(taskBin.getPoint().distanceTo(cell.getPoint()) <= 0)
							inTaskBinList = true;
					}
					if(!inTaskBinList)
						taskBinList.add(cell);
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
		for (Cell bin : taskBinList) {
			if(bin instanceof RecyclingBin)
				return true;
		}
		return false;
	}
	
	public boolean isTaskBinListContainWaste() {
		for (Cell bin : taskBinList) {
			if(bin instanceof WasteBin)
				return true;
		}
		return false;
	}
	
	/**
	 * find nearest recharge point
	 * @return
	 */
	public Cell findNearestRechargePoint() {
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
	
	/**
	 * find nearest recycling station 
	 * @return
	 */
	public Cell findNearestRecyclingStation() {
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
	
	/**
	 * find nearest waste station
	 * @return
	 */
	public Cell findNearestWasteStation() {
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
	
	/**
	 * find nearest bin with task
	 * @return
	 */
	public Cell findNearestTaskBin() {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(Cell taskB : taskBinList) {
			int distance = currentCell.getPoint().distanceTo(taskB.getPoint());
			if( distance < shortestDistance) {
				targetPoint = taskB;
				shortestDistance = distance;
			}		
		}	
		return targetPoint;	
	}
	
	/**
	 * find nearest recycling bin with task
	 * @return
	 */
	public Cell findNearestRecyclingTaskBin() {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(Cell recyclingB : taskBinList) {
			if(recyclingB instanceof RecyclingBin) {
				int distance = currentCell.getPoint().distanceTo(recyclingB.getPoint());
				if( distance < shortestDistance) {
					targetPoint = recyclingB;
					shortestDistance = distance;
				}
			}
		}	
		return targetPoint;	
	}
	
	/**
	 * find nearest waste bin with task
	 * @return
	 */
	public Cell findNearestWasteTaskBin() {
		Cell targetPoint = null;
		int shortestDistance = Integer.MAX_VALUE;
		for(Cell wasteB : taskBinList) {
			if(wasteB instanceof WasteBin) {
				int distance = currentCell.getPoint().distanceTo(wasteB.getPoint());
				if( distance < shortestDistance) {
					targetPoint = wasteB;
					shortestDistance = distance;
				}
			}
		}	
		return targetPoint;	
	}
	
	/**
	 * Remove the bin from task bin list if 
	 * @param currentBin
	 */
	public void taskFinished(Cell currentBin) {
		for (Cell bin : taskBinList) {
			if (bin.getPoint().equals(currentBin.getPoint())) {
				taskBinList.remove(bin);
				return;
			}
		}
	}
		
}

















