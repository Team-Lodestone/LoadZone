package me.dexrn.loadzone.mixin;

import me.dexrn.loadzone.LoadZoneMarkerFile;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedWriter;
import java.io.IOException;

@Mixin(World.class)
public class LevelMixin {
	@Inject(method = "<init>(Ljava/io/File;Ljava/lang/String;J)V", at = @At(value = "RETURN"))
	public void loadZone$levelCtor(CallbackInfo ci) throws IOException {
		World self = (World)(Object)this;

		BufferedWriter writer = LoadZoneMarkerFile.getWriter(self.saveDir);
		LoadZoneMarkerFile.write(writer);

		writer.close();
	}
}
