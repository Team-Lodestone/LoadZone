package me.dexrn.loadzone.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.world.DeleteWorldScreen;
import net.minecraft.client.gui.screen.menu.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public final class LoadNoneLoadZoneWorldScreen extends Screen {
	private int worldId;
	private SelectWorldScreen parent;

	public LoadNoneLoadZoneWorldScreen(SelectWorldScreen parent, int worldId) {
		this.worldId = worldId;
		this.parent = parent;
	}

	@Override
	public void init() {
		this.buttons.clear();

		this.buttons.add(new ButtonWidget(2, this.width / 2 - 100, this.height / 6 + 120 + 12, "Continue anyway"));
		this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		if (!button.active) {
			return;
		}

		switch (button.id) {
			case 1:
				this.minecraft.openScreen(parent);
				break;
			case 2:
				File saves = new File(Minecraft.getWorkingDirectory(), "saves");
				File world = new File(saves, "World" + worldId);
				if (world.exists() && world.isDirectory()) {
					File wldCopy = new File(saves, "World" + worldId + "_LoadZone_" + System.currentTimeMillis());
					try {
						FileUtils.copyDirectory(world, wldCopy);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

					parent.selectWorld(worldId);
				}
				break;
		}
	}

	public final void render(int mouseX, int mouseY, float tickDelta) {
		this.drawBackgroundTexture();
		drawCenteredString(this.textRenderer, "This world was NOT created with LoadZone.", this.width / 2, 20, 16777215);

		drawCenteredString(this.textRenderer, "Trying to load a world that was created without the", this.width / 2, 50, 16777215);
		drawCenteredString(this.textRenderer, "LoadZone mod installed will likely corrupt your world!", this.width / 2, 60, 16777215);

		drawCenteredString(this.textRenderer, "However, if you understand this warning,", this.width / 2, 90, 16777215);
		drawCenteredString(this.textRenderer, "and wish to continue anyway, you may do so AT YOUR OWN RISK.", this.width / 2, 100, 16777215);

		drawCenteredString(this.textRenderer, "Note that your world will be backed up if you choose to continue.", this.width / 2, 120, 0x555555);

		super.render(mouseX, mouseY, tickDelta);
	}
}
