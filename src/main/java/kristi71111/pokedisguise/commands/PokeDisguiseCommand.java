package kristi71111.pokedisguise.commands;

import com.pixelmongenerations.api.command.PixelmonCommand;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import kristi71111.pokedisguise.ConfigRegistry;
import kristi71111.pokedisguise.Helpers;
import kristi71111.pokedisguise.objects.DisguisedPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.*;

import static kristi71111.pokedisguise.PokeDisguise.disguisedPlayers;

public class PokeDisguiseCommand extends CommandBase {
    private static final String commandUsage =
            "--------------------------------\n " +
                    "§bAvailable commands:\n " +
                    "- §6/pokedisguise <§bpokemon§6> [§bshiny§6]\n " +
                    "- §6/pokedisguise §boff\n" +
                    "§bExamples:\n " +
                    "- §6/pokedisguise §bAbra shiny\n " +
                    "- §6/pokedisguise §bPikachu\n " +
                    "- §6/pokedisguise §boff\n " +
                    "\n§bMade by §6kristi71111\n " +
                    "-------------------------------";
    private static final List<String> shiny = Collections.singletonList("shiny");
    private static final List<String> commandAliases = new ArrayList<>(Arrays.asList("disguise", "pixeldisguise", "bacodisguise", "pd"));

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getName() {
        return "pokedisguise";
    }

    @Override
    public List<String> getAliases() {
        return commandAliases;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return commandUsage;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString("§4Only players can run this command!"));
            return;
        }
        int length = args.length;
        final EntityPlayerMP playerMP = (EntityPlayerMP) sender;
        if (length >= 1) {
            final WorldServer worldServer = playerMP.getServerWorld();
            final PokemonSpec pokemonSpec = PokemonSpec.from(args);
            //Since people can write the names in wrong and handling the off switch
            if (pokemonSpec.name == null) {
                if (args[0].equals("off")) {
                    Helpers.removeDisguise(playerMP, false);
                    return;
                }
                playerMP.sendMessage(new TextComponentString(getUsage(sender)));
                return;
            }
            //Creating the Pokemon
            final EntityPixelmon pokemon = pokemonSpec.create(worldServer);
            //Handling per perm if it's enabled
            if (ConfigRegistry.lockAllPokemonBehindPerm) {
                if (!Helpers.hasPerm(playerMP, "pokedisguise.disguise." + pokemonSpec.name)) {
                    playerMP.sendMessage(new TextComponentString("§4You don't have permission to disguise as §b" + pokemonSpec.name + "§4!"));
                    return;
                }
            }
            //Legendary handling
            if (pokemon.isLegendary()) {
                if (!ConfigRegistry.allowLegendaryDisguise) {
                    sender.sendMessage(new TextComponentString("§4You can't disguise as a §blegendary pokemon §4on this server!"));
                    return;
                } else {
                    if (ConfigRegistry.lockLegendaryBehindPerm && !Helpers.hasPerm(playerMP, "pokedisguise.legendarypermitted")) {
                        playerMP.sendMessage(new TextComponentString("§4You don't have permission §bto disguise §4as a §blegendary pokemon§4!"));
                        return;
                    }
                }
            }
            //Shiny argument handling
            if (length == 2) {
                if (!(args[1].equals("shiny"))) {
                    playerMP.sendMessage(new TextComponentString(getUsage(sender)));
                    return;
                }
                if (!ConfigRegistry.allowShinyArgument) {
                    playerMP.sendMessage(new TextComponentString("§4The §bshiny argument §4is disabled on this server!"));
                    return;
                } else {
                    if (ConfigRegistry.lockShinyArgBehindPerm) {
                        if (!Helpers.hasPerm(playerMP, "pokedisguise.shinypermitted")) {
                            playerMP.sendMessage(new TextComponentString("§4You don't have permission to use the §bshiny argument§4!"));
                            return;
                        }
                    }
                    pokemon.setShiny(true);
                }
            }
            //Setting the stuff on the pixelmon entity
            pokemon.setPositionAndRotation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, playerMP.rotationPitch);

            //First we check if he's already disguised so that we can handle switching disguises
            DisguisedPlayer disguisedPlayer = disguisedPlayers.get(playerMP);
            if (disguisedPlayer == null) {
                //Ok so he's fresh
                final EntityTracker entityTracker = worldServer.getEntityTracker();
                Set<EntityPlayerMP> playerSet = (Set<EntityPlayerMP>) entityTracker.getTrackingPlayers(playerMP);
                //create the object
                disguisedPlayer = new DisguisedPlayer(playerMP, pokemon);
                //Set player as invisible
                playerMP.setInvisible(true);
                //Send packet fake entity spawn packet to everyone
                for (EntityPlayerMP trackedPlayer : playerSet) {
                    Helpers.SendFakePokemon(trackedPlayer, pokemon);
                }
                //Self disguise check
                if (ConfigRegistry.shouldPlayerSeeOwnDisguise) {
                    Helpers.SendFakePokemon(playerMP, pokemon);
                    //Hacky way to get rid of collisions by making the player think they are on a team
                    if(ConfigRegistry.shouldPlayerHaveDisabledCollisions){
                        playerMP.connection.sendPacket(new SPacketTeams(disguisedPlayer.getHackedCollision(), 0));
                    }
                }
                disguisedPlayers.put(playerMP, disguisedPlayer);
            } else {
                //Oh no he's switching
                //We get the old entity id
                int entityIDOLD = disguisedPlayer.getDisguisedEntity().getEntityId();
                //We set the new entity
                disguisedPlayer.setDisguisedEntity(pokemon);
                //We get everyone who's tracked
                final EntityTracker entityTracker = worldServer.getEntityTracker();
                Set<EntityPlayerMP> playerSet = (Set<EntityPlayerMP>) entityTracker.getTrackingPlayers(playerMP);
                for (EntityPlayerMP trackedPlayer : playerSet) {
                    //We destroy the old entity and spawn the new one in
                    trackedPlayer.connection.sendPacket(new SPacketDestroyEntities(entityIDOLD));
                    Helpers.SendFakePokemon(trackedPlayer, pokemon);
                }
                //Set player as invisible again just in case
                playerMP.setInvisible(true);
                //Self disguise check
                if (ConfigRegistry.shouldPlayerSeeOwnDisguise) {
                    //Removing the old entity
                    playerMP.connection.sendPacket(new SPacketDestroyEntities(entityIDOLD));
                    //Spawning the new entity
                    Helpers.SendFakePokemon(playerMP, pokemon);
                }
            }
            //Messages
            if (pokemon.isShiny()) {
                playerMP.sendMessage(new TextComponentString("§6You are now disguised as a §4S§ch§6i§en§2y§b " + pokemonSpec.name + "§6!" + "\n§6Make sure your §bmain §6and §boffhand slots §6are empty."));
            } else {
                playerMP.sendMessage(new TextComponentString("§6You are now disguised as §b" + pokemonSpec.name + "§6!" + "\n§6Make sure your §bmain §6and §boffhand slots §6are empty."));
            }
        } else {
            playerMP.sendMessage(new TextComponentString(getUsage(sender)));
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, EnumSpecies.getNameList());
        } else if (args.length == 2 && ConfigRegistry.allowShinyArgument) {
            if (sender instanceof EntityPlayerMP) {
                if (ConfigRegistry.lockShinyArgBehindPerm) {
                    EntityPlayerMP completer = (EntityPlayerMP) sender;
                    if (!Helpers.hasPerm(completer, "pokedisguise.shinypermitted")) {
                        return new ArrayList<>();
                    }
                }
            }
            return shiny;
        }
        return new ArrayList<>();
    }
}
