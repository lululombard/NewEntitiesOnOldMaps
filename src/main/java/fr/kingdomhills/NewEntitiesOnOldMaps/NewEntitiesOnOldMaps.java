package fr.kingdomhills.NewEntitiesOnOldMaps;

import java.util.Arrays;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class NewEntitiesOnOldMaps extends JavaPlugin {
	
	public void onEnable() {
		Biome[] biomelist_bear = {Biome.ICE_FLATS};
		new EntityToSpawn(EntityType.POLAR_BEAR, 50, Arrays.asList(biomelist_bear), 0.0, 0.5, 50.0, 0.01, 0.2);
		Biome[] biomelist_parrot = {Biome.JUNGLE, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS};
		new EntityToSpawn(EntityType.PARROT, 200, Arrays.asList(biomelist_parrot), 0.0, 1, 50.0, 0.01, 0.5);
	}
	
	public boolean isDebugEnabled() {
		return false;
	}
}