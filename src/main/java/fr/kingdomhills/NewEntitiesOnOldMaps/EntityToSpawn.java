package fr.kingdomhills.NewEntitiesOnOldMaps;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class EntityToSpawn implements Listener {
	
	EntityType et;
	int limit;
	List<Biome> biomes;
	double mintemp;
	double maxtemp;
	double radius;
	double luckspawn;
	double luckadult;
	NewEntitiesOnOldMaps plugin;
	
	EntityToSpawn(EntityType et, int limit, List<Biome> biomes, double mintemp, double maxtemp, double radius, double luckspawn, double luckadult) {
		this.et = et;
		this.limit = limit;
		this.biomes = biomes;
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
		this.radius = radius;
		this.luckspawn = luckspawn;
		this.luckadult = luckadult;
		plugin = (NewEntitiesOnOldMaps) Bukkit.getServer().getPluginManager().getPlugin("NewEntitiesOnOldMaps");
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Custom rules for " + et.name() + " hooked into ChunkLoadEvent !");
	}
	
	public void unregister() {
		ChunkLoadEvent.getHandlerList().unregister(this);
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		if (e.getWorld().getAllowAnimals()) {
			int count = 0;
			for (Entity ent : e.getWorld().getEntities()) {
				if (ent.getType().equals(et)) count++;
				if (count > limit) return;
			}
			if (Math.random() < luckspawn) {
				int x = (int) (Math.random()*15);
				int z = (int) (Math.random()*15);
				Location location = e.getChunk().getBlock(x, 0, z).getLocation();
				int y = 255;
				for (int i = y; i > 0; i--) {
					Location temploc = location;
					temploc.setY(i);
					Material m = temploc.getBlock().getType();
					if (!m.isOccluding() && m.isSolid()) break;
					else y = i;
				}
				if (y < 130) {
					location.setY(y + 1);
					if (biomes.contains(location.getBlock().getBiome()) && location.getBlock().getTemperature() >= mintemp && location.getBlock().getTemperature() <= maxtemp) {
						Entity ent = e.getWorld().spawnEntity(location, et);
						boolean delete = false;
						for (Entity nearent : ent.getNearbyEntities(radius, radius, radius)) {
							if (nearent instanceof Player) {
								delete = false;
								break;
							}
							if (nearent.getType().equals(et)) delete = true;
						}
						if (delete) {
							plugin.getLogger().info(et.name() + " will not spawn (too much same entities) at " + location);
							ent.remove();
						}
						else if (ent instanceof Ageable) {
							boolean adult = Math.random() < luckadult;
							if (adult) ((Ageable) ent).setAdult();
							else ((Ageable) ent).setBaby();
							if (plugin.isDebugEnabled()) plugin.getLogger().info("Spawned " + et.name() + " (adult = " + adult + ")" + " at " + ent.getLocation());
						}
						else if (plugin.isDebugEnabled()) plugin.getLogger().info("Spawned " + et.name() + " at " + ent.getLocation());
					}
				}
			}
		}
	}
}
