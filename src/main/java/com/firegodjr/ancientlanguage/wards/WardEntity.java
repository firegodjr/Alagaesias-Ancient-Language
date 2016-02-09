package com.firegodjr.ancientlanguage.wards;

import java.util.ArrayList;
import java.util.List;

import com.firegodjr.ancientlanguage.command.CommandCast;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

public class WardEntity extends TileEntity implements IUpdatePlayerListBox{
	
	List<String> args = new ArrayList();
	private int charge;
	private List<Integer> aList;
	private String target;
	private EnumFacing direction; //TODO: Write direction to NBT or find alternative
	
	public WardEntity(List<String> argsIn, String targetIn, int chargeIn)
	{
		for(int i = 0; i < argsIn.size(); i++)
		{
			args.add(argsIn.get(i));
		}
		target = targetIn;
		charge = chargeIn;
	}
	
	@Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        compound.setString("target", target);

        //TagList of Integer Tags:
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.args.size(); i++) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("id", i);
            nbt.setString("value", this.args.get(i));
            list.appendTag(nbt);
        }
        compound.setTag("spell", list);
        compound.setInteger("charge", charge);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        target = compound.getString("Target");
        
        NBTTagList list = compound.getTagList("Spell", 8);
        this.args.clear();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            int id = nbt.getInteger("id");
            String value = nbt.getString("value");
            this.args.add(id, value);
        }
    }
	@Override
	public void update() 
	{
		worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.pos.getX() + Math.random(), this.pos.getY() + Math.random(), this.pos.getZ() + Math.random(), 0, 0, 0, 0);
		
		if(this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.getPos(), this.getPos().add(1,1,1))).size() > 0)
		{
			CommandCast cast = new CommandCast();
			String[] arguments = new String[args.size()-1];
			for(int i = 0; i<args.size()-1; i++)
			{
				arguments[i] = args.get(i);
			}
			WardSender sender = new WardSender(this.getWorld());
			try {
				cast.execute(sender, arguments);
			} catch (CommandException e) {
				System.out.println("Ward (" + this.pos.getX() + this.pos.getY() + this.pos.getZ() + ") casting failed. Stacktrace follows:");
				e.printStackTrace();
			}
		}
	}

}
