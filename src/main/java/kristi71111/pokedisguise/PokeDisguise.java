package kristi71111.pokedisguise;

import com.pixelmonmod.pixelmon.Pixelmon;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import kristi71111.pokedisguise.commands.PokeDisguiseCommand;
import kristi71111.pokedisguise.events.PixelmonEvents;
import kristi71111.pokedisguise.events.PlayerLogOut;
import kristi71111.pokedisguise.events.PlayerTick;
import kristi71111.pokedisguise.events.TrackingEntity;
import kristi71111.pokedisguise.objects.DisguisedPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = PokeDisguise.MOD_ID,
        name = PokeDisguise.MOD_NAME,
        version = PokeDisguise.VERSION,
        acceptableRemoteVersions = "*",
        dependencies = "after:pixelmon"
)
public class PokeDisguise {
    public static final String MOD_ID = "pokedisguise";
    public static final String MOD_NAME = "PokeDisguise";
    public static final String VERSION = "1.0.2";
    public static Logger logger;
    public static Reference2ObjectMap<EntityPlayerMP, DisguisedPlayer> disguisedPlayers = new Reference2ObjectOpenHashMap<>();

    @Mod.Instance(MOD_ID)
    public static PokeDisguise INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("PokeDisguise was originally made for BacoNetworks.");
        //Config stuff
        File configDir = new File(new File(event.getModConfigurationDirectory(), MOD_ID), MOD_ID + ".cfg");
        ConfigRegistry.init(new Configuration(configDir));
        //Events
        MinecraftForge.EVENT_BUS.register(new TrackingEntity());
        MinecraftForge.EVENT_BUS.register(new PlayerTick());
        MinecraftForge.EVENT_BUS.register(new PlayerLogOut());
    }

    @Mod.EventHandler
    public void postinit(FMLServerStartingEvent event) {
        event.registerServerCommand(new PokeDisguiseCommand());
        //Pixelmon Events
        if(ConfigRegistry.shouldCancelEvents){
            Pixelmon.EVENT_BUS.register(new PixelmonEvents());
        }
    }
}
