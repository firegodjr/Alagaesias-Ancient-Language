package com.firegodjr.ancientlanguage;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.firegodjr.ancientlanguage.utils.VersionUtils;

public class ParticleHandler {
	private static int iAmbient = 0;

	public static void burst(Vec3 pos, World world, int quantity,
			double velocity, VersionUtils.EnumParticleTypes particleType) {
		burst(pos, world, quantity, velocity, particleType.toParticleType());
	}

	public static void burst(Vec3 pos, World world, int quantity,
			double velocity, EnumParticleTypes particleType) {
		for (int i = 0; i < quantity; i++) {
			world.spawnParticle(particleType, pos.xCoord, pos.yCoord,
					pos.zCoord, (Math.random() - 0.5) * velocity,
					(Math.random() - 0.5) * velocity, (Math.random() - 0.5)
							* velocity, 0);
		}
	}

	public static void ambient(Vec3 v, int delay, World world,
			VersionUtils.EnumParticleTypes particle) {
		ambient(v, delay, world, particle.toParticleType());
	}

	public static void ambient(Vec3 v, int delay, World world,
			EnumParticleTypes particle) {
		double x = v.xCoord + (Math.random() - 0.5);
		double y = v.yCoord + Math.random() * 2;
		double z = v.zCoord + (Math.random() - 0.5);
		iAmbient++;
		if (iAmbient >= delay) {
			world.spawnParticle(particle, x, y, z, 0, 0, 0, 0);
			iAmbient = 0;
		}
	}

	public static void ambient(Entity entity, int delay,
			VersionUtils.EnumParticleTypes particle) {
		ambient(entity, delay, particle.toParticleType());
	}

	public static void ambient(Entity entity, int delay,
			EnumParticleTypes particle) {
		ambient(VersionUtils.getEntityPosition(entity), delay, entity.worldObj,
				particle);
	}

	public static void particlePlatform(Entity entity, int quantity,
			VersionUtils.EnumParticleTypes particleType) {
		particlePlatform(entity, quantity, particleType.toParticleType());
	}

	public static void particlePlatform(Entity entity, int quantity,
			EnumParticleTypes particleType) {
		for (int i = 0; i < quantity; i++) {
			System.out
					.println("Attempting to spawn particles: particlePlatform");
			entity.worldObj.spawnParticle(particleType, entity.posX,
					entity.posY, entity.posZ, (Math.random() - 0.5) / 25, 0,
					(Math.random() - 0.5) / 25, 0);
		}
	}

	/**
	 * Spawns GhostLight particles at the position in the world, with specified
	 * intensity
	 * 
	 * @param v
	 *            The position of the ghostlight
	 * @param intensity
	 *            The strength (density) of the particles generated
	 * @param world
	 *            The world to spawn in
	 */
	public static void ghostLight(Vec3 v, int intensity, World world) {
		int delay = 1; // setting this to 0 and doing delay++ once is the same
						// as this
		Random sparkDelay = new Random();

		if (delay == sparkDelay.nextInt(5)) {
			VersionUtils.spawnParticle(world, v.addVector(0.5, 0.5, 0.5),
					generateRandomVec(), VersionUtils.EnumParticleTypes.FLAME);
			VersionUtils.spawnParticle(world, v.addVector(0.5, 0.5, 0.5),
					generateRandomVec(),
					VersionUtils.EnumParticleTypes.FIREWORKS_SPARK);
		}
	}

	private static Vec3 generateRandomVec() {
		return VersionUtils.createVec3(Math.random() / 50 - 0.01,
				Math.random() / 50 - 0.01, Math.random() / 50 - 0.01);
	}
}
