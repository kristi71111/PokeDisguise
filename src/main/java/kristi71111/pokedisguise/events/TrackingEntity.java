package kristi71111.pokedisguise.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import kristi71111.pokedisguise.Helpers;
import kristi71111.pokedisguise.PokeDisguise;
import kristi71111.pokedisguise.objects.DisguisedPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TrackingEntity {
    @SubscribeEvent
    public void startTrackingEvent(PlayerEvent.StartTracking event) {
        //This part is basically used for handling players that may be tracker after the person was disguised for example if someone gets into range.
        EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntityPlayer();
        Entity target = event.getTarget();
        if (target instanceof EntityPlayerMP) {
            DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(playerMP);
            if (disguisedPlayer != null) {
                EntityPlayerMP targetPlayer = (EntityPlayerMP) target;
                //Disguise
                EntityPixelmon disguise = disguisedPlayer.getDisguisedEntity();
                disguise.setPositionAndRotation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, playerMP.rotationPitch);
                //Packet stuff
                Helpers.SendFakePokemon(targetPlayer, disguise);
            }
        }
    }

    @SubscribeEvent
    public void stopTrackingEvent(PlayerEvent.StopTracking event) {
        //Now this is where we handle if the player goes out of range of the disguised player then we need to make sure the fake thing we spawned in is gone as well!
        EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntityPlayer();
        Entity target = event.getTarget();
        if (target instanceof EntityPlayerMP) {
            DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(playerMP);
            if (disguisedPlayer != null) {
                EntityPlayerMP targetPlayer = (EntityPlayerMP) target;
                targetPlayer.connection.sendPacket(new SPacketDestroyEntities(disguisedPlayer.getDisguisedEntity().getEntityId()));
            }
        }
    }
}
