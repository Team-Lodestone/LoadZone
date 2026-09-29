package me.dexrn.loadzone;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/** Marker file to prevent loading a vanilla world */
public class LoadZoneMarkerFile {
	/** The default marker filename */
	private static final String FILENAME = ".loadzone";

	/** Writes an informational string to the marker file stream */
	public static void write(BufferedWriter writer) throws IOException {
		// Source - https://stackoverflow.com/a/22091291
		// Posted by Dipali Vasani
		// Retrieved 2026-09-28, License - CC BY-SA 3.0
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		//END CC BY-SA 3.0 LICENSED CODE

		writer.write("LoadZone v" + LoadZone.VERSION + " / https://github.com/Team-Lodestone/LoadZone");
		writer.newLine();
		writer.write("Last loaded on " + sdf.format(currentTime));
		writer.newLine();
		writer.newLine();
		writer.write("Loading this world without LoadZone installed may cause the game to fail to load any zones and start creating new ones.");
		writer.newLine();
		writer.write("This will effectively look like a new world was generated... However, because player data is left intact, you may spawn in suffocating within in the newly generated zones :P");
		writer.newLine();
		writer.write("DO NOTE HOWEVER: Depending on where you go in the world, you may unintentionally overwrite some zones created while LoadZone was installed.");
		writer.newLine();
		writer.newLine();
		writer.write("If you choose to load this world in a client without LoadZone, you bare the risk of zone corruption. At-least back up your world first!!!");
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
	}

	/** Gets the marker file inside the parent directory */
	public static File getFile(File parent) {
		return new File(parent, FILENAME);
	}

	/** Gets a writer for the marker file in the parent directory */
	public static BufferedWriter getWriter(File parent) throws FileNotFoundException {
		return getFileWriter(getFile(parent));
	}

	/** Gets a writer for the marker file itself */
	public static BufferedWriter getFileWriter(File markerFile) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(markerFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
		return new BufferedWriter(osw);
	}

	/** Returns true if the marker file exists and is a file in the parent directory */
	public static boolean exists(File parent) {
		File f = getFile(parent);

		return f.exists() && f.isFile();
	}
}
