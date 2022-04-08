package kristi71111.pokedisguise;

import net.minecraftforge.common.config.Configuration;

public class ConfigRegistry {
    private static Configuration config;
    public static boolean allowShinyArgument;
    public static boolean lockShinyArgBehindPerm;
    public static boolean allowLegendaryDisguise;
    public static boolean lockLegendaryBehindPerm;
    public static boolean lockAllPokemonBehindPerm;
    public static boolean shouldPlayerSeeOwnDisguise;
    public static boolean shouldPlayerHaveDisabledCollisions;
    public static boolean shouldCancelEvents;

    public static void init(Configuration c) {
        config = c;
        c.load();
        createOrSyncConfig();
    }

    private static void createOrSyncConfig() {
        allowShinyArgument = config.getBoolean("allowShinyArgument", "default", true, "Should the shiny argument be enabled? Example /pokedisguise Abra shiny");
        lockShinyArgBehindPerm = config.getBoolean("lockShinyArgumentBehindPerm", "default", false, "If the shiny argument is enabled. Should it be locked behind it's own permission node? (pokedisguise.shinypermitted)");
        allowLegendaryDisguise = config.getBoolean("allowLegendaryDisguise", "default", true, "Can players disguise as legendary pokemon?");
        lockLegendaryBehindPerm = config.getBoolean("lockLegendaryBehindPerm", "default", false, "If players can disguise as legendary pokemon. Should it be locked behind it's own permission node? (pokedisguise.legendarypermitted)");
        lockAllPokemonBehindPerm = config.getBoolean("lockAllPokemonBehindPerm", "default", false, "Should we lock all pokemon behind their own permission nodes? (pokedisguise.disguise.Pokemonname) (Make sure name starts with upper case)");
        shouldPlayerSeeOwnDisguise = config.getBoolean("shouldPlayerSeeOwnDisguise", "default", false, "Should the player who's disguised see their own disguise? This uses a hacky team packet to disable collisions. So keep this at false if you encounter issues.");
        shouldPlayerHaveDisabledCollisions = config.getBoolean("shouldPlayerHaveDisabledCollisions", "default", false, "If shouldPlayerSeeOwnDisguise is enabled should the player not be able to collide with the disguise (So that bounding boxes don't move you around)? This uses a hacky team packet to disable collisions. So disable this if you encounter issues. May not even work on proxy setups.");
        shouldCancelEvents = config.getBoolean("shouldCancelBattleEvents", "default", true, "If a player is disguised should we cancel any attempt at battling them be it wild encounter or player and the player sending out pokemon. Even if the player initializes it?");
        if (config.hasChanged()) {
            config.save();
        }
    }
}
