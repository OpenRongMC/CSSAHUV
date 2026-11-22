
package net.xbtstudio.school.command;


import net.xbtstudio.school.procedures.AtProcedure;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class HowdoyousleepCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("how_do_you_sleep").requires(s -> s.permissions() instanceof net.minecraft.server.permissions.LevelBasedPermissionSet _lbs && _lbs.level().isEqualOrHigherThan(net.minecraft.server.permissions.PermissionLevel.byId(1))).executes(arguments -> {
			Level world = arguments.getSource().getLevel();
			double x = arguments.getSource().getPosition().x();
			double y = arguments.getSource().getPosition().y();
			double z = arguments.getSource().getPosition().z();
			Entity entity = arguments.getSource().getEntity();
			if (entity == null && world instanceof ServerLevel _servLevel)
				entity = FakePlayerFactory.getMinecraft(_servLevel);
			Direction direction = Direction.DOWN;
			if (entity != null)
				direction = entity.getDirection();

			AtProcedure.execute(world, x, y, z);
			return 0;
		}));
	}
}
