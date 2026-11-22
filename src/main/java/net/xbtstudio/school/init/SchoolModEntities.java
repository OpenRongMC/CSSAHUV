
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.xbtstudio.school.init;

import net.xbtstudio.school.entity.SighingCatEntity;
import net.xbtstudio.school.entity.ChalkSMGProjectileEntity;
import net.xbtstudio.school.entity.ChalkRFProjectileEntity;
import net.xbtstudio.school.entity.ChalkHairSprayerProjectileEntity;
import net.xbtstudio.school.entity.ChalkCannonProjectileEntity;
import net.xbtstudio.school.SchoolMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SchoolModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SchoolMod.MODID);
	public static final RegistryObject<EntityType<ChalkHairSprayerProjectileEntity>> CHALK_HAIR_SPRAYER_PROJECTILE = register("chalk_hair_sprayer_projectile",
			EntityType.Builder.<ChalkHairSprayerProjectileEntity>of(ChalkHairSprayerProjectileEntity::new, MobCategory.MISC).clientTrackingRange(64)
					.updateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<ChalkSMGProjectileEntity>> CHALK_SMG_PROJECTILE = register("chalk_smg_projectile", EntityType.Builder.<ChalkSMGProjectileEntity>of(ChalkSMGProjectileEntity::new, MobCategory.MISC)
			.clientTrackingRange(64).updateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<ChalkRFProjectileEntity>> CHALK_RF_PROJECTILE = register("chalk_rf_projectile", EntityType.Builder.<ChalkRFProjectileEntity>of(ChalkRFProjectileEntity::new, MobCategory.MISC)
			.clientTrackingRange(64).updateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<ChalkCannonProjectileEntity>> CHALK_CANNON_PROJECTILE = register("chalk_cannon_projectile", EntityType.Builder.<ChalkCannonProjectileEntity>of(ChalkCannonProjectileEntity::new, MobCategory.MISC)
			.clientTrackingRange(64).updateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<SighingCatEntity>> SIGHING_CAT = register("sighing_cat",
			EntityType.Builder.<SighingCatEntity>of(SighingCatEntity::new, MobCategory.MONSTER).clientTrackingRange(5).updateInterval(3)

					.sized(0.6f, 0.7f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(net.minecraftforge.registries.DeferredRegister.currentEntityKey()));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SighingCatEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SIGHING_CAT.get(), SighingCatEntity.createAttributes().build());
	}
}
