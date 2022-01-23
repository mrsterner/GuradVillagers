package dev.mrsterner.guardvillagers;

import dev.mrsterner.guardvillagers.client.*;
import dev.mrsterner.guardvillagers.client.network.GuardSyncPacket;
import dev.mrsterner.guardvillagers.common.entity.GuardEntity;
import dev.mrsterner.guardvillagers.common.registy.GuardVillagersEntityTypes;
import dev.mrsterner.guardvillagers.common.registy.GuardVillagersScreenHandlers;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class GuardVillagers implements ModInitializer, ClientModInitializer {
	public static final String MODID = "guardvillagers";
	public static GuardVillagersConfig config;

	public static boolean hotvChecker(PlayerEntity player) {
		return player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE) && GuardVillagersConfig.get().giveGuardStuffHOTV || !GuardVillagersConfig.get().giveGuardStuffHOTV;
	}

	public static Hand getHandWith(LivingEntity livingEntity, Predicate<Item> itemPredicate) {
		return itemPredicate.test(livingEntity.getMainHandStack().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}


	public static final ScreenHandlerType<GuardVillagerScreenHandler> BAT_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(MODID, "guard_screen"), GuardVillagerScreenHandler::new);


	public static EntityModelLayer GUARD = new EntityModelLayer(new Identifier(GuardVillagers.MODID + "guard"), "guard");
	public static EntityModelLayer GUARD_STEVE = new EntityModelLayer(new Identifier(GuardVillagers.MODID + "guard_steve"), "guard_steve");
	public static EntityModelLayer GUARD_ARMOR_OUTER = new EntityModelLayer(new Identifier(GuardVillagers.MODID + "guard_armor_outer"), "guard_armor_outer");
	public static EntityModelLayer GUARD_ARMOR_INNER = new EntityModelLayer(new Identifier(GuardVillagers.MODID + "guard_armor_inner"), "guard_armor_inner");

	@Override
	public void onInitialize() {
		AutoConfig.register(GuardVillagersConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(GuardVillagersConfig.class).getConfig();
		GuardVillagersEntityTypes.init();

	}

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(BAT_SCREEN_HANDLER, GuardInventoryScreen::new);
		EntityModelLayerRegistry.registerModelLayer(GUARD, GuardVillagerModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(GUARD_STEVE, GuardSteveModel::createMesh);
		EntityModelLayerRegistry.registerModelLayer(GUARD_ARMOR_OUTER, GuardArmorModel::createOuterArmorLayer);
		EntityModelLayerRegistry.registerModelLayer(GUARD_ARMOR_INNER, GuardArmorModel::createInnerArmorLayer);
		EntityRendererRegistry.register(GuardVillagersEntityTypes.GUARD_VILLAGER, GuardRenderer::new);
		/*
		ClientPlayNetworking.registerGlobalReceiver(GuardSyncPacket.ID, (client, network, buf, sender) -> {

			int syncId = buf.readInt();
			int traderId = buf.readInt();
			client.execute(() -> {
				if (client.player != null) {
					ScreenHandler screenHandler = client.player.currentScreenHandler;
					if (syncId == screenHandler.syncId && screenHandler instanceof GuardVillagerScreenHandler) {
						//((GuardVillagerScreenHandler) screenHandler).player = (client.player);
						//((GuardVillagerScreenHandler) screenHandler).guard = ((GuardEntity) client.world.getEntityById(traderId));
					}
				}
			});
		});

		 */



	}
}
