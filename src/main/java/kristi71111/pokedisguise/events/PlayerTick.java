package kristi71111.pokedisguise.events;

import kristi71111.pokedisguise.ConfigRegistry;
import kristi71111.pokedisguise.Helpers;
import kristi71111.pokedisguise.PokeDisguise;
import kristi71111.pokedisguise.objects.DisguisedPlayer;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;

public class PlayerTick {
    @SubscribeEvent
    public void PlayerTick(TickEvent.PlayerTickEvent event) {
        //So that we only do it once
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
        DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(playerMP);
        //Not disguised
        if (disguisedPlayer == null) {
            return;
        }
        //Check if anyone is even tracking us so that we don't need to do stuff below if it's not needed
        final EntityTracker entityTracker = playerMP.getServerWorld().getEntityTracker();
        final Set<EntityPlayerMP> playerSet = (Set<EntityPlayerMP>) entityTracker.getTrackingPlayers(playerMP);
        if (playerSet.isEmpty() && !ConfigRegistry.shouldPlayerSeeOwnDisguise) {
            return;
        }
        Packet movementAndLook = Helpers.getEntityLookMovePacket(disguisedPlayer, playerMP);
        //Sending to self if defined
        if (ConfigRegistry.shouldPlayerSeeOwnDisguise) {
            playerMP.connection.sendPacket(movementAndLook);
        }
        for (EntityPlayer player : playerSet) {
            EntityPlayerMP playerMP1 = (EntityPlayerMP) player;
            playerMP1.connection.sendPacket(movementAndLook);
        }
    }
}
