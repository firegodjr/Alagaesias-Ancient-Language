package com.firegodjr.ancientlanguage.items;

import com.firegodjr.ancientlanguage.command.CommandCast;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class Spellbook extends ItemEditableBook
{
	public Spellbook(String unlocalname)
	{
		this.setUnlocalizedName(unlocalname);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
        if (!world.isRemote)
        {
            this.resolveContents(stack, player);
        }
        
        if(player.isSneaking())
        {
	        player.displayGUIBook(stack);
	        player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        else
        {
        	try{
	        	if(stack.getTagCompound() != null)
	        	{
		        	NBTTagList nbttaglist = stack.getTagCompound().getTagList("pages", 8);
		        	String s = nbttaglist.getStringTagAt(1);
		        	CommandCast cast = new CommandCast();
		        	try {
						cast.execute(player, s.split(" "));
					} catch (CommandException e) {
						player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Spell casting failed!"));
					}
	        	}
        	} catch(Exception ex)
        	{
        		System.out.println("SPELLBOOK CAST FAILED");
        		System.out.println(ex.getStackTrace());
        	}
        }
        return stack;
	}

	private void resolveContents(ItemStack stack, EntityPlayer player)
	{
	    if (stack != null && stack.getTagCompound() != null)
	    {
	        NBTTagCompound nbttagcompound = stack.getTagCompound();
	
	        if (!nbttagcompound.getBoolean("resolved"))
	        {
	            nbttagcompound.setBoolean("resolved", true);
	
	            if (validBookTagContents(nbttagcompound))
	            {
	                NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
	
	                for (int i = 0; i < nbttaglist.tagCount(); ++i)
	                {
	                    String s = nbttaglist.getStringTagAt(i);
	                    Object object;
	
	                    try
	                    {
	                        IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
	                        object = ChatComponentProcessor.func_179985_a(player, ichatcomponent, player);
	                    }
	                    catch (Exception exception)
	                    {
	                        object = new ChatComponentText(s);
	                    }
	
	                    nbttaglist.set(i, new NBTTagString(IChatComponent.Serializer.componentToJson((IChatComponent)object)));
	                }
	
	                nbttagcompound.setTag("pages", nbttaglist);
	
	                if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack)
	                {
	                    Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
	                    ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, slot.slotNumber, stack));
	                }
	            }
	        }
	    }
	}
}