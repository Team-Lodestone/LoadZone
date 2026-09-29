package me.dexrn.loadzone.mixin;

import me.dexrn.loadzone.LoadZoneMarkerFile;
import me.dexrn.loadzone.screen.LoadVanillaLevelConfirmationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(SelectWorldScreen.class)
public abstract class WorldScreenMixin {
	@Inject(method = "Lnet/minecraft/client/gui/screen/menu/world/SelectWorldScreen;buttonClicked(Lnet/minecraft/client/gui/widget/ButtonWidget;)V", at = @At(value = "HEAD"), cancellable = true)
	public void loadZone$buttonClicked(ButtonWidget widget, CallbackInfo ci) {
		if (widget.active) {
				if (widget.id < 5) {
					File saves = new File(Minecraft.getWorkingDirectory(), "saves");
					File world = new File(saves, "World" + (widget.id + 1));
					if (world.exists() && world.isDirectory()) {
						if (!LoadZoneMarkerFile.exists(world)) {
							SelectWorldScreen self = (SelectWorldScreen) (Object) this;

							self.minecraft.openScreen(new LoadVanillaLevelConfirmationScreen(self, widget.id + 1));
							ci.cancel();
						}
					}
				}
		}
	}
}
