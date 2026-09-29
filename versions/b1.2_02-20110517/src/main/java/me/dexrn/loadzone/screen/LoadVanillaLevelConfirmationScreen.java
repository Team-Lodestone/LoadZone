package me.dexrn.loadzone.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Button;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.SelectWorldScreen;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/** Confirmation screen for loading a vanilla world, since this is a likely destructive operation */
public final class LoadVanillaLevelConfirmationScreen extends Screen {
	/** The world ID */
	private final int worldId;
	/** The parent SelectWorldScreen, we need it primarily due to its selectWorld method */
	private final SelectWorldScreen parent;

	public LoadVanillaLevelConfirmationScreen(SelectWorldScreen parent, int worldId) {
		this.worldId = worldId;
		this.parent = parent;
	}

	@Override
	public void init() {
		this.buttons.clear();

		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 6 + 120 + 12, "Continue anyway"));
		this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	@Override
	protected void buttonClicked(Button button) {
		if (!button.active) {
			return;
		}

		switch (button.id) {
			case 1:
				File saves = new File(Minecraft.getWorkingDirectory(), "saves");
				File world = new File(saves, "World" + worldId);
				if (world.exists() && world.isDirectory()) {
					File wldCopy = new File(saves, "World" + worldId + "_LoadZone_" + System.currentTimeMillis());
					try {
						FileUtils.copyDirectory(world, wldCopy);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

					parent.worldSelected(worldId);
				}
				break;
			case 2:
				this.minecraft.setScreen(parent);
				break;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		this.renderBackground();
		drawCenteredString(this.font, "This world was NOT created with LoadZone.", this.width / 2, 20, 16777215);

		drawCenteredString(this.font, "Trying to load a world that was created without the", this.width / 2, 50, 16777215);
		drawCenteredString(this.font, "LoadZone mod installed will likely corrupt your world!", this.width / 2, 60, 16777215);

		drawCenteredString(this.font, "However, if you understand this warning,", this.width / 2, 90, 16777215);
		drawCenteredString(this.font, "and wish to continue anyway, you may do so AT YOUR OWN RISK.", this.width / 2, 100, 16777215);

		drawCenteredString(this.font, "Note that your world will be backed up if you choose to continue.", this.width / 2, 120, 0x555555);

		super.render(mouseX, mouseY, tickDelta);
	}
}
