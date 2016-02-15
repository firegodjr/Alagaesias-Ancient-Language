package com.firegodjr.ancientlanguage;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleHandler {
	private static int iAmbient = 0;

	public static void burst(BlockPos pos, World world, int quantity, double velocity, EnumParticleTypes particleType) {
		for (int i = 0; i < quantity; i++) {
			world.spawnParticle(particleType, pos.getX(), pos.getY(), pos.getZ(), (Math.random() - 0.5) * velocity,
					(Math.random() - 0.5) * velocity, (Math.random() - 0.5) * velocity, 0);
		}
	}

	public static void ambient(double posX, double posY, double posZ, int delay, World world,
			EnumParticleTypes particle) {
		double x = posX + (Math.random() - 0.5);
		double y = posY + Math.random() * 2;
		double z = posZ + (Math.random() - 0.5);
		iAmbient++;
		if (iAmbient >= delay) {
			world.spawnParticle(particle, x, y, z, 0, 0, 0, 0);
			iAmbient = 0;
		}
	}

	public static void ambient(Entity entity, int delay, EnumParticleTypes particle) {
		ambient(entity.posX, entity.posY, entity.posZ, delay, entity.worldObj, particle);
	}

	public static void particlePlatform(Entity entity, int quantity, EnumParticleTypes particleType) {
		for (int i = 0; i < quantity; i++) {
			System.out.println("Attempting to spawn particles: particlePlatform");
			entity.worldObj.spawnParticle(particleType, entity.posX, entity.posY, entity.posZ,
					(Math.random() - 0.5) / 25, 0, (Math.random() - 0.5) / 25, 0);
		}
	}

	/**
	 * Spawns GhostLight particles at the position in the world, with specified
	 * intensity
	 * 
	 * @param posX
	 *            The X position for the ghostlight
	 * @param posY
	 *            The Y position for the ghostlight
	 * @param posZ
	 *            The Z position for the ghostlight
	 * @param intensity
	 *            The strength (density) of the particles generated
	 * @param world
	 *            The world to spawn in
	 */
	public static void ghostLight(double posX, double posY, double posZ, int intensity, World world) {
		int delay = 1; // setting this to 0 and doing delay++ once is the same
						// as this
		Random sparkDelay = new Random();

		if (delay == sparkDelay.nextInt(5)) {
			world.spawnParticle(EnumParticleTypes.FLAME, posX + 0.5, posY + 0.5, posZ + 0.5,
					Math.random() / 100 - 0.005, Math.random() / 100 - 0.005, Math.random() / 100 - 0.005, 0);
			world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, posX + 0.5, posY + 0.5, posZ + 0.5,
					Math.random() / 50 - 0.01, Math.random() / 50 - 0.01, Math.random() / 50 - 0.01, 0);
		}
	}
}
