package com.firegodjr.ancientlanguage.api.script.events;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;

/**
 * ScriptEvent is fired when an event involving any Script occurs.<br>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child event of this class.<br>
 * <br>
 * {@link #instance} contains the ScriptInstance that caused this event to
 * occur.<br>
 * {@link #args} contains the arguments being run under this event<br>
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 **/
public class ScriptEvent extends Event {

	private ScriptInstance instance;
	private List<String> args;

	public ScriptEvent(ScriptInstance instance, List<String> args) {
		this.instance = instance;
		this.args = args;
	}

	public ScriptInstance getScriptInstance() {
		return this.instance;
	}

	public List<String> getArguments() {
		return this.args;
	}

	/**
	 * PostScriptEvent is fired when an event involving a script finishes.<br>
	 * If a method utilizes this {@link Event} as its parameter, the method will
	 * receive every child event of this class.<br>
	 * <br>
	 * {@link #instance} contains the ScriptInstance that caused this event to
	 * occur.<br>
	 * {@link #args} contains the arguments being run under this event<br>
	 * {@link #scriptObjects} contains the objects resulting from a parse<br>
	 * <br>
	 * All children of this event are fired on the
	 * {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
	public static class PostScriptEvent extends ScriptEvent {

		private List<IScriptObject> scriptObjects;

		public PostScriptEvent(ScriptInstance instance, List<String> args,
				List<IScriptObject> objs) {
			super(instance, args);
			this.scriptObjects = objs;
		}

		public List<IScriptObject> getScriptObjects() {
			return this.scriptObjects;
		}
	}

	/**
	 * PreParse is fired when a Script is about to be parsed. <br>
	 * This event is fired at the start of {@link ScriptInstance#parseScript(String)}<br>
	 * <br>
	 * {@link #instance} contains the ScriptInstance that caused this event to
	 * occur.<br>
	 * {@link #args} contains the arguments being run under this event<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If this event is canceled, parsing and all following events do not take
	 * place.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class PreParse extends ScriptEvent {
		public PreParse(ScriptInstance instance, List<String> args) {
			super(instance, args);
		}
	}

	/**
	 * PostParse is fired when a Script has been parsed but not activated. <br>
	 * This event is fired at the end of {@link ScriptInstance#parseScript(String)}<br>
	 * <br>
	 * {@link #instance} contains the ScriptInstance that caused this event to
	 * occur.<br>
	 * {@link #args} contains the arguments being run under this event<br>
	 * {@link #scriptObjects} contains the objects resulting from a parse<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If this event is canceled, script activation and all following events do
	 * not take place.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class PostParse extends PostScriptEvent {
		public PostParse(ScriptInstance instance, List<String> args,
				List<IScriptObject> objs) {
			super(instance, args, objs);
		}
	}

	/**
	 * End is fired after a Script has been parsed and activated. <br>
	 * This event is fired at the end of {@link ScriptInstance#onExecute(World, Vec3)}<br>
	 * <br>
	 * {@link #instance} contains the ScriptInstance that caused this event to
	 * occur.<br>
	 * {@link #args} contains the arguments being run under this event<br>
	 * {@link #scriptObjects} contains the objects resulting from a parse<br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	public static class End extends PostScriptEvent {
		public End(ScriptInstance instance, List<String> args,
				List<IScriptObject> objs) {
			super(instance, args, objs);
		}
	}

	/**
	 * TypeActive is fired when a Script reaches a specific object in its
	 * activation. <br>
	 * This event is fired in {@link ScriptInstance#onExecute(World, Vec3)} before type activation<br>
	 * <br>
	 * <b>Note:</b> Fires before activeWord insertion for IWords, not before
	 * activation<br>
	 * <br>
	 * {@link #instance} contains the ScriptInstance that caused this event to
	 * occur.<br>
	 * {@link #args} contains the arguments being run under this event<br>
	 * {@link #scriptObjects} contains the objects resulting from a parse<br>
	 * {@link #wordSet} is the set representing the word string and the object
	 * parsed from the string<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If this event is canceled, word activation does not take place.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class TypeActive<T extends IScriptObject> extends
			PostScriptEvent {

		private SimpleEntry<String, T> wordSet;

		public TypeActive(ScriptInstance instance, List<String> args,
				List<IScriptObject> objs, SimpleEntry<String, T> wordSet) {
			super(instance, args, objs);
			this.wordSet = wordSet;
		}

		public String getString() {
			return this.wordSet.getKey();
		}

		public T getWordObject() {
			return this.wordSet.getValue();
		}
	}
}
