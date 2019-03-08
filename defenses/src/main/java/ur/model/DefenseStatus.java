package ur.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ur.controller.WorkQueueController;

/**
 * This class contains the valid building status, the costs and the transition
 * between them. Similar to a state machine
 */
public enum DefenseStatus {
	/**
	 * valid targets are Building deck, armoredDeck cannon and armoredCannon
	 *
	 */
	Empty {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == BuildingDeck) {
				return BuildingCostsPropertyReader.get_resorce_costs("Deck");
			} else if (target == BuildingArmoredDeck) {
				return BuildingCostsPropertyReader.get_resorce_costs("ArmoredDeck");
			}
			return null;

		}
	},
	/**
	 * No cost transition to Deck
	 *
	 */
	BuildingDeck {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == Deck) {
				return Collections.emptyList();
			}
			return null;
		}

		@Override
		public String buildingQueue() {
			return WorkQueueController.DECK_QUEUE;
		}
	},
	/**
	 * valid targets are BuildingArmoredDeck and BuildingCannon (Cannot build
	 * armored cannon over this deck directly)
	 *
	 */
	Deck {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == BuildingArmoredDeck) {
				return BuildingCostsPropertyReader.get_resorce_costs("ArmoredDeck");
			} else if (target == BuildingCannon) {
				return BuildingCostsPropertyReader.get_resorce_costs("Cannon");
			}
			return null;
		}
	},
	/**
	 * No cost transition to ArmoredDeck
	 *
	 */
	BuildingArmoredDeck {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == ArmoredDeck) {
				return Collections.emptyList();
			}
			return null;
		}

		@Override
		public String buildingQueue() {
			return WorkQueueController.DECK_QUEUE;
		}
	},
	/**
	 * Valid targets are cannon and amored cannon
	 *
	 */
	ArmoredDeck {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == BuildingCannon) {
				return BuildingCostsPropertyReader.get_resorce_costs("Cannon");
			} else if (target == BuildingArmoredCannon) {
				return BuildingCostsPropertyReader.get_resorce_costs("ArmoredCannon");
			}
			return null;
		}
	},
	/**
	 * No cost transition to Cannon
	 *
	 */
	BuildingCannon {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == Cannon) {
				return Collections.emptyList();
			}
			return null;
		}

		@Override
		public String buildingQueue() {
			return WorkQueueController.CANNON_QUEUE;
		}
	},
	/**
	 * No target, Final state
	 *
	 */
	Cannon,
	/**
	 * No cost transition to ArmoredCannon
	 *
	 */
	BuildingArmoredCannon {
		@Override
		public List<MaterialCost> getTargetCost(DefenseStatus target) {
			if (target == ArmoredCannon) {
				return Collections.emptyList();
			}
			return null;
		}

		@Override
		public String buildingQueue() {
			return WorkQueueController.CANNON_QUEUE;
		}
	},
	/**
	 * No target, final state
	 *
	 */
	ArmoredCannon;

	// ------------- methods ----------------------------------------

	/**
	 * Method to obtain the cost for the next possible state. This method will
	 * return <code>null</code> if the target state is not a valid one return Empty
	 * list if the target state dont need resources
	 * 
	 * @param target
	 * @return null or a List of Costs
	 */
	public List<MaterialCost> getTargetCost(DefenseStatus target) {
		return null;
	}

	/**
	 * Get the time unit cost to reach the target status. The default cost is
	 * defined in building_costs.properties Some states could override this method
	 * to custom cost transtions
	 * 
	 * @return
	 */
	public int get_time_cost(DefenseStatus target) {
		return BuildingCostsPropertyReader.get_time_cost(target.name());
	}

	/**
	 * @return The set of valid targets (states) from this one
	 */
	public Set<DefenseStatus> getValidTargetStatus() {
		HashSet<DefenseStatus> valid = new HashSet<DefenseStatus>();
		for (DefenseStatus target : DefenseStatus.values()) {
			// if the status method returns null it means this transition is not possible
			// otherwise the target status is a valid one
			if (this.getTargetCost(target) != null) {
				valid.add(target);
			}
		}
		return valid;
	}

	/**
	 * @return The name of the queue where this defense Status should be built from
	 *         null if this state dont require a job queue. Only "building" status
	 *         will return a Queue
	 */
	public String buildingQueue() {
		return null;
	}
}
