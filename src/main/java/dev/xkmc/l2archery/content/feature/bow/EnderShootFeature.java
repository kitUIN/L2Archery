package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public record EnderShootFeature(int range) implements OnShootFeature, OnPullFeature, IGlowFeature {

	@Override
	public boolean onShoot(@Nullable Player player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		if (player == null)
			return false;
		Entity target = RayTraceUtil.serverGetTarget(player);
		if (target == null)
			return false;

		consumer.accept(entity -> {
			float w = target.getBbWidth(), h = target.getBbHeight();
			Vec3 dst = target.position().add(0, h / 2, 0);
			double r = Math.sqrt(w * w * 2 + h * h) / 2;
			Vec3 src = dst.add(entity.getDeltaMovement().normalize().scale(-r - 1));
			entity.setPos(src);
		});
		return true;
	}

	@Override
	public void tickAim(Player player, GenericItemStack<GenericBowItem> bow) {
		RayTraceUtil.clientUpdateTarget(player, range);
	}

	@Override
	public void stopAim(Player player, GenericItemStack<GenericBowItem> bow) {
		RayTraceUtil.TARGET.updateTarget(null);
	}


	@Override
	public void addTooltip(List<MutableComponent> list) {
		list.add(LangData.FEATURE_ENDER_SHOOT.get());
	}
}
