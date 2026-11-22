package net.minecraftforge.network;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public final class NetworkHooks {
	private NetworkHooks() {
	}

	public static void openScreen(ServerPlayer player, MenuProvider containerSupplier) {
		player.openMenu(containerSupplier);
	}

	public static void openScreen(ServerPlayer player, MenuProvider containerSupplier, BlockPos pos) {
		openScreen(player, containerSupplier, buf -> buf.writeBlockPos(pos));
	}

	public static void openScreen(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter) {
		player.openMenu(new ExtraDataMenuProvider(containerSupplier, extraDataWriter));
	}

	public static Packet<ClientGamePacketListener> getEntitySpawningPacket(Entity entity) {
		return new ClientboundAddEntityPacket(entity, 0, entity.blockPosition());
	}

	private record ExtraDataMenuProvider(MenuProvider delegate, Consumer<FriendlyByteBuf> extraDataWriter)
			implements ExtendedMenuProvider<FriendlyByteBuf> {
		@Override
		public FriendlyByteBuf getScreenOpeningData(ServerPlayer player) {
			FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
			this.extraDataWriter.accept(buf);
			return buf;
		}

		@Override
		public net.minecraft.network.chat.Component getDisplayName() {
			return this.delegate.getDisplayName();
		}

		@Override
		public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
			return this.delegate.createMenu(windowId, inventory, player);
		}
	}
}
