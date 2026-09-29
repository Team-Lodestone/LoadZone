package me.dexrn.loadzone.mixin;

import net.minecraft.world.chunk.storage.ZoneChunkStorage;
import net.minecraft.world.chunk.storage.zone.Zone;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Mixin(ZoneChunkStorage.class)
public class ZonedChunkStorageMixin {
	@Unique
	private static final int CHUNKS_PER_ZONE_BITS = 5;
	@Unique
	private static final String ZONE_FILENAME = "zone";
	@Unique
	private static final String ENTITY_FILENAME = "entities";

	@Shadow
	private Map<Long, Zone> zones;

	@Shadow
	private File dir;

	@Shadow
	private long ticks;

	@Shadow
	private static int getChunkSlot(int chunkX, int chunkZ) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * @author Dexrn ZacAttack
	 * @reason replace with my own method from when I decompiled, cleaned up, and wrote docs for the zone classes.
	 */
	@Overwrite
	private @Nullable Zone getZone(int cx, int cz, boolean create) throws IOException {
		int slot = getChunkSlot(cx, cz);

		int zoneX = cx >> CHUNKS_PER_ZONE_BITS;
		int zoneZ = cz >> CHUNKS_PER_ZONE_BITS;

		long position = zoneX + ((long) zoneZ << 20);
		if (!this.zones.containsKey(position)) {
			File zone = new File(this.dir, ZONE_FILENAME + "_" + Integer.toString(zoneX, 36) + "_" + Integer.toString(zoneZ, 36) + ".dat");
			if (!zone.exists()) {
				if (!create) {
					return null;
				}

				zone.createNewFile();
			}

			File entityFile = new File(this.dir, ENTITY_FILENAME + "_" + Integer.toString(zoneX, 36) + "_" + Integer.toString(zoneZ, 36) + ".dat");
			this.zones.put(position, new Zone(position, zone, entityFile));
		}

		Zone file = this.zones.get(position);
		file.lastUpdateTime = this.ticks;
		return !file.hasChunk(slot) && !create ? null : file;
	}
}
