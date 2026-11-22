package net.minecraftforge.common.extensions;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@FunctionalInterface
public interface IForgeMenuType<T extends AbstractContainerMenu> {
	T create(int windowId, Inventory inv, FriendlyByteBuf buf);

	static <T extends AbstractContainerMenu> MenuType<T> create(IForgeMenuType<T> factory) {
		StreamCodec<RegistryFriendlyByteBuf, FriendlyByteBuf> codec = StreamCodec.of(
				(out, data) -> {
					byte[] bytes = new byte[data.readableBytes()];
					data.getBytes(data.readerIndex(), bytes);
					out.writeByteArray(bytes);
				},
				in -> new FriendlyByteBuf(Unpooled.wrappedBuffer(in.readByteArray())));
		return new ExtendedMenuType<>((id, inv, data) -> factory.create(id, inv, data), codec);
	}
}
