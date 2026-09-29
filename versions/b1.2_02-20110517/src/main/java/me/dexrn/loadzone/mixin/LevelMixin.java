package me.dexrn.loadzone.mixin;

import me.dexrn.loadzone.LoadZoneMarkerFile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkCache;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.storage.ZonedChunkStorage;
import net.minecraft.world.level.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Mixin(Level.class)
public class LevelMixin {
	@Inject(method = "<init>(Ljava/io/File;Ljava/lang/String;JLnet/minecraft/world/level/dimension/Dimension;)V", at = @At(value = "RETURN"))
	public void loadZone$levelCtor(File string, String l, long dimension, Dimension par4, CallbackInfo ci) throws IOException {
		Level self = (Level)(Object)this;

		ChunkSource cc = self.getChunkSource();
		if (cc instanceof ChunkCache) {
			ChunkCache chunkCache = (ChunkCache)cc;

			if (chunkCache.storage instanceof ZonedChunkStorage) {
				BufferedWriter writer = LoadZoneMarkerFile.getWriter(self.dir);
				LoadZoneMarkerFile.write(writer);

				writer.close();
			}
		}
	}
}
