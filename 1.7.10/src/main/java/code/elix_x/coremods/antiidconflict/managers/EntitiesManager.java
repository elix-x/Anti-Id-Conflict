package code.elix_x.coremods.antiidconflict.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.entity.EntityList;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import code.elix_x.coremods.antiidconflict.AntiIdConflictBase;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class EntitiesManager extends AntiIdConflictBase{

	public static int idToCheckUntil = 2048;
	
	public static String avaibleIDs = "";
	public static String occupiedIDs = "";
	public static String conflictedIDs = "";
	
	public static void preinit(FMLPreInitializationEvent event) throws Exception
	{ 
		entitiesFolder = new File(mainFolder, "\\entities");
		entitiesFolder.mkdir();
		
		setUpEntitiesFolder();
	}

	public static void init(FMLInitializationEvent event)
	{ 

	}

	public static void postinit(FMLPostInitializationEvent event) throws Exception
	{ 
		updateEntitiesFolder();
	}
	
	public static void setUpEntitiesFolder() throws Exception {
		File conf = new File(entitiesFolder, "\\main.cfg");
		conf.createNewFile();
		Configuration config = new Configuration(conf);
		config.load();
		idToCheckUntil = config.getInt("idToChekUntil", "Scanning settings", 256, 256, Integer.MAX_VALUE, "The value until which, scanner will check ids, to be free/occupied...");
		config.save();
	}

	public static void updateEntitiesFolder() throws Exception {
		{
			for(int i = 1; i < idToCheckUntil + 1; i++){
				Class clas = EntityList.getClassFromID(i);
				if(clas != null){
					occupiedIDs += i + " : " + EntityList.getStringFromID(i) + " (" + clas.getName() + ")\n";
				} else {
					avaibleIDs += i + "\n";
				}
			}
		}
		{
			File freeIds = new File(entitiesFolder, "\\avaibleIDs.txt");
			if(freeIds.exists()){
				freeIds.delete();
			}
			freeIds.createNewFile();
			PrintWriter writer = new PrintWriter(freeIds);
			writer.println("List of avaible entities ids:");
			for(String s : avaibleIDs.split("\n")){
				writer.println(s);
			}
			writer.close();
		}
		{
			File occupiedIds = new File(entitiesFolder, "\\occupiedIDs.txt");
			if(occupiedIds.exists()){
				occupiedIds.delete();
			}
			occupiedIds.createNewFile();
			PrintWriter writer = new PrintWriter(occupiedIds);
			writer.println("Table of occupied entities ids and their owners\nid:name(class)");
			for(String s : occupiedIDs.split("\n")){
				writer.println(s);
			}
			writer.close();
		}
		/*{
			File file = new File(biomesFolder, "\\conflictedIDs.txt");
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			PrintWriter writer = new PrintWriter(file);
			writer.println("IDs in conflict:\n");
			writer.println(conflictedIDs);
			writer.close();
		}*/
		{
			File all = new File(entitiesFolder, "\\AllIDs.txt");
			PrintWriter writer = new PrintWriter(all);

			for(int i = 0; i < idToCheckUntil + 1; i++){
				/*if(conflicts[i] != null){
					writer.println(conflicts[i].getCrashMessage());
				} else if(BiomeGenBase.getBiomeGenArray()[i] != null){
					writer.println(i + " is Occupied by " + BiomeGenBase.getBiomeGenArray()[i].biomeName);
				} else {
					writer.println(i + " is Avaible");
				}*/
				Class clas = EntityList.getClassFromID(i);
				if(clas != null){
					writer.println(i + " is Occupied by " + EntityList.getStringFromID(i) + "(" + clas.getName() + ")");
				} else {
					writer.println(i + " is Avaible");
				}
			}

			writer.close();
		}
	}
	
	
}