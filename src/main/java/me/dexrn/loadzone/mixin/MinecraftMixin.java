package me.dexrn.loadzone.mixin;

import me.dexrn.loadzone.LoadZone;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Mixin(Minecraft.class)
public class MinecraftMixin {//how scary.....
	@Inject(method = "Lnet/minecraft/client/Minecraft;startGame(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V", shift = At.Shift.AFTER))
	public void loadZone$startGame(String save, CallbackInfo ci) throws IOException {
		File levelsDir = new File(Minecraft.getWorkingDirectory(), "saves");
		File levelDir = new File(levelsDir, save);

		levelDir.mkdirs();

		File marker = new File(levelDir, ".loadzone");
		FileOutputStream fos = new FileOutputStream(marker);
		OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
		BufferedWriter bw = new BufferedWriter(osw);

		// Source - https://stackoverflow.com/a/22091291
		// Posted by Dipali Vasani
		// Retrieved 2026-09-28, License - CC BY-SA 3.0
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		//END CC BY-SA 3.0 LICENSED CODE

		bw.write("LoadZone v" + LoadZone.VERSION + " / https://github.com/Team-Lodestone/LoadZone");
		bw.newLine();
		bw.write("Last loaded on " + sdf.format(currentTime));
		bw.newLine();
		bw.newLine();
		bw.write("Loading this world without LoadZone installed may cause the game to fail to load any zones and start creating new ones.");
		bw.newLine();
		bw.write("This will effectively look like a new world was generated... However, because player data is left intact, you may spawn in suffocating within in the newly generated zones :P");
		bw.newLine();
		bw.write("DO NOTE HOWEVER: Depending on where you go in the world, you may unintentionally overwrite some zones created while LoadZone was installed.");
		bw.newLine();
		bw.newLine();
		bw.write("If you choose to load this world in a client without LoadZone, you bare the risk of zone corruption. At-least back up your world first!!!");
		//oh how badly I wanted to put that xda copypasta
		/*
		 * Your warranty is now void.
		 *
		 * I am not responsible for bricked devices, dead SD cards,
		 * thermonuclear war, or you getting fired because the alarm app failed. Please
		 * do some research if you have any concerns about doing this to your device
		 * YOU are choosing to make these modifications, and if
		 * you point the finger at me for messing up your device, I will laugh at you.
		 */

		bw.close();
	}
}
