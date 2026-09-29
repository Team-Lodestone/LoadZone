package me.dexrn.loadzone.mixin;

import net.minecraft.world.level.chunk.storage.ZoneFile;
import net.minecraft.world.level.chunk.storage.ZonedChunkStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Mixin(ZonedChunkStorage.class)
public abstract class ZonedChunkStorageMixin {
	@Unique
	private static final String ZONE_FILENAME = "zone";
	@Unique
	private static final String ENTITY_FILENAME = "entities";

	@Shadow
	private File dir;

	@Shadow
	private long tickCount;

	@Shadow
	protected abstract int getSlot(int cx, int cz);

	@Shadow
	private Map<Long, ZoneFile> zoneFiles;

	/**
	 * @author Dexrn ZacAttack
	 * @reason replace with my own method from when I decompiled, cleaned up, and wrote docs for the zone classes.
	 */
	@Overwrite
	private @Nullable ZoneFile getZoneFile(int cx, int cz, boolean create) throws IOException {
		int slot = getSlot(cx, cz);

		int zoneX = cx >> ZonedChunkStorage.CHUNKS_PER_ZONE_BITS;
		int zoneZ = cz >> ZonedChunkStorage.CHUNKS_PER_ZONE_BITS;

		long position = zoneX + ((long) zoneZ << 20);
		if (!this.zoneFiles.containsKey(position)) {
			File zone = new File(this.dir, ZONE_FILENAME + "_" + Integer.toString(zoneX, 36) + "_" + Integer.toString(zoneZ, 36) + ".dat");
			if (!zone.exists()) {
				if (!create) {
					return null;
				}

				zone.createNewFile();
			}

			File entityFile = new File(this.dir, ENTITY_FILENAME + "_" + Integer.toString(zoneX, 36) + "_" + Integer.toString(zoneZ, 36) + ".dat");
			this.zoneFiles.put(position, new ZoneFile(position, zone, entityFile));
		}

		ZoneFile file = this.zoneFiles.get(position);
		file.lastUse = this.tickCount;
		return !file.containsSlot(slot) && !create ? null : file;
	}
}
