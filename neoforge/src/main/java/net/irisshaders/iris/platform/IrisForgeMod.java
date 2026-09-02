package net.irisshaders.iris.platform;

import net.irisshaders.iris.Iris;
import net.irisshaders.iris.gui.screen.ShaderPackScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod(value = "iris", dist = Dist.CLIENT)
public class IrisForgeMod {
	public static List<KeyMapping> KEYLIST = new ArrayList<>();
	public static boolean inWorld = false;
	public static boolean lastInWorld = false;

	public IrisForgeMod(IEventBus bus, ModContainer modContainer) {
		NeoForge.EVENT_BUS.register(this);
		bus.addListener(this::registerKeys);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, (game, screen) -> new ShaderPackScreen(screen));
	}

	public void registerKeys(RegisterKeyMappingsEvent event) {
		KEYLIST.forEach(event::register);
		KEYLIST.clear();
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent.Post event) {
		inWorld = Minecraft.getInstance().level != null;
		if (inWorld != lastInWorld) {
			lastInWorld = inWorld;
			try {
				Iris.reload();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
