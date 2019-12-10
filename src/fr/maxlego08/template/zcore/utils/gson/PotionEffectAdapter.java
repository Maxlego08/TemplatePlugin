package fr.maxlego08.template.zcore.utils.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import fr.maxlego08.template.zcore.ZPlugin;

public class PotionEffectAdapter extends TypeAdapter<PotionEffect> {

	private static Type seriType = new TypeToken<Map<String, Object>>() {
	}.getType();

	private static String TYPE = "effect";
	private static String DURATION = "duration";
	private static String AMPLIFIER = "amplifier";
	private static String AMBIENT = "ambient";

	@Override
	public void write(JsonWriter jsonWriter, PotionEffect potionEffect) throws IOException {
		if (potionEffect == null) {
			jsonWriter.nullValue();
			return;
		}
		jsonWriter.value(getRaw(potionEffect));
	}

	@Override
	public PotionEffect read(JsonReader jsonReader) throws IOException {
		if (jsonReader.peek() == JsonToken.NULL) {
			jsonReader.nextNull();
			return null;
		}
		return fromRaw(jsonReader.nextString());
	}

	private String getRaw(PotionEffect potion) {
		Map<String, Object> serial = potion.serialize();
		return ZPlugin.z().getGson().toJson(serial);
	}

	@SuppressWarnings("deprecation")
	private PotionEffect fromRaw(String raw) {
		Map<String, Object> keys = ZPlugin.z().getGson().fromJson(raw, seriType);
		return new PotionEffect(PotionEffectType.getById(((Double) keys.get(TYPE)).intValue()),
				((Double) keys.get(DURATION)).intValue(), ((Double) keys.get(AMPLIFIER)).intValue(),
				(Boolean) keys.get(AMBIENT));
	}

}
