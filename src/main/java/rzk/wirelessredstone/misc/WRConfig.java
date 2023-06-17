package rzk.wirelessredstone.misc;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import rzk.wirelessredstone.WirelessRedstone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WRConfig
{
	private static final Gson GSON = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setPrettyPrinting()
			.create();

	private static final String FILE_NAME = WirelessRedstone.MODID + ".json";

	// General
	public static int redstoneReceiverSignalStrength = 15;
	public static boolean redstoneReceiverStrongPower = true;

	// Client
	public static int frequencyDisplayColor = 0;
	public static int highlightColor = 0xFF3F3F;
	public static int highlightTimeSeconds = 10;

	public static void load()
	{
		File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), FILE_NAME);

		if (!file.exists()) save();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
		{
			JsonObject config = JsonParser.parseReader(bufferedReader).getAsJsonObject();

			// General
			redstoneReceiverSignalStrength = config.getAsJsonPrimitive("signal_strength").getAsInt();
			redstoneReceiverStrongPower = config.getAsJsonPrimitive("provide_strong_power").getAsBoolean();

			// Client
			frequencyDisplayColor = config.getAsJsonPrimitive("display_color").getAsInt();
			highlightColor = config.getAsJsonPrimitive("highlight_color").getAsInt();
			highlightTimeSeconds = config.getAsJsonPrimitive("highlight_time").getAsInt();
		}
		catch (IOException e)
		{
			WirelessRedstone.LOGGER.error("Couldn't load Wireless Redstone configs from file");
			e.printStackTrace();
		}
	}

	public static void save()
	{
		File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), FILE_NAME);
		JsonObject config = new JsonObject();

		// General
		config.addProperty("signal_strength", redstoneReceiverSignalStrength);
		config.addProperty("provide_strong_power", redstoneReceiverStrongPower);

		// Client
		config.addProperty("display_color", frequencyDisplayColor);
		config.addProperty("highlight_color", highlightColor);
		config.addProperty("highlight_time", highlightTimeSeconds);

		try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file)))
		{
			fileWriter.write(GSON.toJson(config));
		}
		catch (IOException e)
		{
			WirelessRedstone.LOGGER.error("Couldn't save Wireless Redstone configs to file");
			e.printStackTrace();
		}
	}
}
