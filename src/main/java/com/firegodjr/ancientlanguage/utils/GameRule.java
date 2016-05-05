package com.firegodjr.ancientlanguage.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A self-contained class to allow Object-Oriented manipulation of game rules
 */
public class GameRule {

	static {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	private static GameRules rules;
	private static List<GameRule> gameRulesList = new ArrayList<GameRule>();

	private final String name;
	private String value;
	private ValueType type;

	public GameRule(String name) {
		this(name, "");
	}

	public GameRule(String name, String defValue) {
		this(name, defValue, ValueType.ANY_VALUE);
	}

	/**
	 * Defines a game rule (if it doesn't already exist)
	 * 
	 * @param name
	 *            The name of the game rule
	 * @param defValue
	 *            The default value of the game rule (if value does not exist)
	 * @param defType
	 *            The default type of the game rule (if value's type does not
	 *            exist)
	 */
	public GameRule(String name, String defValue, ValueType defType) {
		this.name = name;
		this.value = defValue;
		this.type = defType;
		this.processValue();
		this.processType();
		gameRulesList.add(this);
	}

	/**
	 * Processes the value, correcting the value if incorrect, adding it if it
	 * doesn't exist, other corrections according to {@link #rules}
	 */
	private void processValue() {
		if (this.value == null)
			this.value = "";
		if (this.type == null)
			this.type = ValueType.ANY_VALUE;
		if (rules != null && !this.doesRuleExist())
			rules.addGameRule(this.name, this.value, this.type);
		this.value = rules == null ? "" : rules
				.getGameRuleStringValue(this.name);
	}

	/**
	 * Processes the type, performing corrections according to {@link #rules}
	 */
	private void processType() {
		if (rules == null)
			this.type = ValueType.ANY_VALUE;
		else
			for (ValueType t : ValueType.values()) {
				if (rules.areSameType(this.name, t))
					this.type = t;
			}
	}

	/**
	 * Retrieves the name of the game rule
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves the value of the game rule
	 * 
	 * @see #getValueBoolean()
	 * @see #getValueInteger()
	 */
	public String getValue() {
		processValue();
		return value;
	}

	/**
	 * Retrieves the boolean value of the game rule
	 * 
	 * @see #getValue()
	 * @see #getValueInteger()
	 */
	public boolean getValueBoolean() {
		Boolean b = Boolean.valueOf(this.getValue());
		if (this.value == "1")
			b = true;
		return b == null ? false : b.booleanValue();
	}

	/**
	 * Retrieves the integer value of the game rule
	 * 
	 * @see #getValue()
	 * @see #getValueBoolean()
	 */
	public int getValueInteger() {
		Integer i;
		try {
			i = Integer.valueOf(this.getValue());
		} catch (NumberFormatException e) {
			return 0;
		}
		if (i == null)
			return 0;
		return i.intValue();
	}

	/**
	 * Returns the {@link ValueType} of the game rule
	 */
	public ValueType getType() {
		return this.type;
	}

	/**
	 * Determines whether a rule exists
	 * 
	 * @return true if rule exists, false if it doesn't, or false if
	 *         {@link #rules} is null
	 */
	public boolean doesRuleExist() {
		if (rules == null)
			return false;
		return rules.hasRule(this.name);
	}

	/**
	 * Sets the value for the game rule
	 */
	public void setValue(String value) {
		if (rules == null)
			return;
		this.value = value;
		rules.addGameRule(this.name, this.value, this.type);
	}

	/**
	 * Sets the type for the game rule
	 */
	public void setType(ValueType type) {
		if (rules == null)
			return;
		this.type = type;
		rules.addGameRule(this.name, this.value, this.type);
	}

	private static void setGameRules(GameRules r) {
		if (rules != null)
			return;
		rules = r;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + ", Value: " + this.value + ", Type: "
				+ this.type;
	}

	public static final class EventHandler {
		@SubscribeEvent
		public void onWorldLoad(WorldEvent.Load event) {
			if (rules != null)
				return;
			setGameRules(MinecraftServer.getServer().worldServerForDimension(0)
					.getGameRules());
			for (GameRule r : gameRulesList) {
				r.processValue();
			}
		}
	}
}
