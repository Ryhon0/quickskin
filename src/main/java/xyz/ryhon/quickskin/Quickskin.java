package xyz.ryhon.quickskin;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Arm;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.entity.player.PlayerModelPart;

public class Quickskin implements ModInitializer {
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
					SimpleOption<Arm> arm = gameOptions.getMainArm();
					Boolean isright = arm.getValue() == Arm.RIGHT;
					gameOptions.getMainArm().setValue(isright ? Arm.LEFT : Arm.RIGHT);
					gameOptions.write();
				}
			});
		}

		// toggle body parts
		{
			for(PlayerModelPart p :PlayerModelPart.values() ) {

				KeyBinding keyBinding = new KeyBinding(
						"key.quickskin.togglebody." + p,
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_UNKNOWN,
						"category.quickskin"
					);
				KeyBindingHelper.registerKeyBinding(keyBinding);

				ClientTickEvents.END_CLIENT_TICK.register(client -> {		 
					while (keyBinding.wasPressed())
					{
						GameOptions gameOptions = client.options;

						gameOptions.setPlayerModelPart(p, !gameOptions.isPlayerModelPartEnabled(p));
						gameOptions.write();
					}
				});
			}
		}

		// toggle all body parts at once
		{
			KeyBinding keyBinding = new KeyBinding(
				"key.quickskin.toggleall",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				"category.quickskin"
			);
			KeyBindingHelper.registerKeyBinding(keyBinding);

			ClientTickEvents.END_CLIENT_TICK.register(client -> {		 
				while (keyBinding.wasPressed())
				{
					GameOptions gameOptions = client.options;

					Boolean anyEnabled = false;
					for(PlayerModelPart p : PlayerModelPart.values())
					{
						if(gameOptions.isPlayerModelPartEnabled(p))
						{
							anyEnabled = true;
							break;
						}
					}

					for(PlayerModelPart p : PlayerModelPart.values())
					{
						gameOptions.setPlayerModelPart(p, !anyEnabled);
					}
					gameOptions.write();
				}
			});
		}
	}
}
