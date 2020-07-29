package net.fabricmc.quickskin;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.options.Option;
import net.minecraft.client.render.entity.PlayerModelPart;

public class quickskinMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// flip hands
		{
			KeyBinding keyBinding = new KeyBinding(
				"key.quickskin.fliphand",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				"category.quickskin"
			);
			KeyBindingHelper.registerKeyBinding(keyBinding);
			ClientTickEvents.END_CLIENT_TICK.register(client -> {		 
				while (keyBinding.wasPressed())
				{
					GameOptions gameOptions = client.options;

					Option.MAIN_HAND.cycle(gameOptions, 1);
					gameOptions.write();
				}
			});
		}

		// toggle body parts
		{
			PlayerModelPart[] var2 = PlayerModelPart.values();
			int var3 = var2.length;
			for(int var4 = 0; var4 < var3; ++var4) {
				PlayerModelPart playerModelPart = var2[var4];

				KeyBinding keyBinding = new KeyBinding(
						"key.quickskin.togglebody." + playerModelPart,
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_UNKNOWN,
						"category.quickskin"
					);
				KeyBindingHelper.registerKeyBinding(keyBinding);

				ClientTickEvents.END_CLIENT_TICK.register(client -> {		 
					while (keyBinding.wasPressed())
					{
						GameOptions gameOptions = client.options;
	
						gameOptions.togglePlayerModelPart(playerModelPart);
						gameOptions.write();
					}
				});
			}
		}
	}
}
