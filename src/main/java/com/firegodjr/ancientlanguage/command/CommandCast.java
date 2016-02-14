package com.firegodjr.ancientlanguage.command;

import java.util.ArrayList;
import java.util.List;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

@SuppressWarnings({"unused", "rawtypes"})
public class CommandCast implements ICommand {

	private final int INVALID = 0;
	private final int ACTION = 1;
	private final int TARGET = 2;
	private final int TILE = 3;
	private final int SEPARATOR = -1;

	private final List<String> aliases;

	public CommandCast() {
		aliases = new ArrayList<String>();
		aliases.add("cast");
		aliases.add("c");
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "cast";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/cast <action> <target>";
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0)
			sender.addChatMessage(new ChatComponentText(
					EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + "You said nothing."));
		else {

			Main.getLogger().info("Starting new ScriptHandler object");
			ScriptHandler scriptHandler = new ScriptHandler();
			Main.getLogger().info("Starting new ScriptInstance object");
			ScriptInstance instance = scriptHandler.createScriptInstance(sender, args);
			if (instance == null) {
				Main.getLogger().info("Script instance was null");
				return;
			}
			Main.getLogger().info("Entering script execution");
			instance.onExecute(sender.getEntityWorld(), sender.getPositionVector());

			/*
			 * EntityPlayer player = null; if (sender.getCommandSenderEntity()
			 * instanceof EntityPlayer) player = (EntityPlayer)
			 * sender.getCommandSenderEntity();
			 * 
			 * List<EntityLivingBase> targets = new ArrayList();
			 * 
			 * script = scriptHandler.getScriptFromSpell(args);
			 * Main.getLogger().info("Spell script built!");
			 * 
			 * script = scriptHandler.cleanScript(script);
			 * Main.getLogger().info("Cleaned up script!");
			 * 
			 * scriptHandler.executeScript(script, player);
			 */

			if (sender instanceof EntityPlayer) {
				Main.getLogger().info("Telling Player Chant");
				EntityPlayer player = (EntityPlayer) sender;
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + ""
						+ EnumChatFormatting.ITALIC + "You chant: \"" + EnumChatFormatting.RESET
						+ EnumChatFormatting.AQUA
						+ /* scriptHandler.getChantFromScript(script) */ scriptHandler.getScriptInstanceChant(instance)
						+ EnumChatFormatting.GRAY + EnumChatFormatting.ITALIC + "\""));
			}
			Main.getLogger().info("Cast Command Executed!");

		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}