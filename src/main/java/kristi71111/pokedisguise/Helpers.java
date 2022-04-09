package kristi71111.pokedisguise;

import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import kristi71111.pokedisguise.objects.DisguisedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;

import java.util.Collection;
import java.util.Set;

public class Helpers {
    public static void SendFakePokemon(EntityPlayerMP playerMP, EntityPixelmon pixelmon) {
        //Spawn the fake entity in
        Packet<?> pkt = net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.getEntitySpawningPacket(pixelmon);
        if (pkt != null) {
            playerMP.connection.sendPacket(pkt);
        } else {
            playerMP.connection.sendPacket(new SPacketSpawnMob(pixelmon));
        }
        //Additional data
        if (!pixelmon.getDataManager().isEmpty()) {
            playerMP.connection.sendPacket(new SPacketEntityMetadata(pixelmon.getEntityId(), pixelmon.getDataManager(), true));
        }
        Collection<IAttributeInstance> collection = pixelmon.getAttributeMap().getAllAttributes();
        if (!collection.isEmpty()) {
            playerMP.connection.sendPacket(new SPacketEntityProperties(pixelmon.getEntityId(), collection));
        }
    }

    public static boolean hasPerm(EntityPlayerMP playerMP, String node) {
        //https://docs.spongepowered.org/stable/en-GB/plugin/permissions.html#for-forge-mods
        return playerMP.canUseCommand(3, node);
    }

    public static SPacketEntity.S17PacketEntityLookMove getEntityLookMovePacket(DisguisedPlayer disguisedPlayer, EntityPlayerMP playerMP) {
        //Movement
        //Head rotation and pitch
        int yaw = MathHelper.floor(playerMP.getRotationYawHead() * 256.0F / 360.0F);
        int pitch = MathHelper.floor(playerMP.rotationPitch * 256.0F / 360.0F);
        //old ones
        long lastEncodedPosX = disguisedPlayer.getLastEncodedPosX();
        long lastEncodedPosY = disguisedPlayer.getLastEncodedPosY();
        long lastEncodedPosZ = disguisedPlayer.getLastEncodedPosZ();
        //new ones
        long newEncodedPosX = EntityTracker.getPositionLong(playerMP.posX);
        long newEncodedPosY = EntityTracker.getPositionLong(playerMP.posY);
        long newEncodedPosZ = EntityTracker.getPositionLong(playerMP.posZ);
        //Calculate from old pos default behavior
        long x = newEncodedPosX - lastEncodedPosX;
        long y = newEncodedPosY - lastEncodedPosY;
        long z = newEncodedPosZ - lastEncodedPosZ;
        //Set new
        disguisedPlayer.setLastEncodedPosX(newEncodedPosX);
        disguisedPlayer.setLastEncodedPosY(newEncodedPosY);
        disguisedPlayer.setLastEncodedPosZ(newEncodedPosZ);
        //Packet sending
        EntityPixelmon pixelmon = disguisedPlayer.getDisguisedEntity();
        return new SPacketEntity.S17PacketEntityLookMove(pixelmon.getEntityId(), x, y, z, (byte) yaw, (byte) pitch, !playerMP.capabilities.isFlying);
    }

    public static void removeDisguise(EntityPlayerMP playerMP, boolean isDcing) {
        DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.remove(playerMP);
        if (disguisedPlayer != null) {
            EntityLivingBase disguise = disguisedPlayer.getDisguisedEntity();
            final EntityTracker entityTracker = playerMP.getServerWorld().getEntityTracker();
            Set<EntityPlayerMP> playerSet = (Set<EntityPlayerMP>) entityTracker.getTrackingPlayers(playerMP);
            for (EntityPlayerMP trackedPlayer : playerSet) {
                //We destroy the entity
                trackedPlayer.connection.sendPacket(new SPacketDestroyEntities(disguise.getEntityId()));
            }
            //Handling if the person wearing the disguise can also see it.
            if (ConfigRegistry.shouldPlayerSeeOwnDisguise && !isDcing) {
                //This should remove the team from the client
                if(ConfigRegistry.shouldPlayerHaveDisabledCollisions){
                    playerMP.connection.sendPacket(new SPacketTeams(disguisedPlayer.getHackedCollision(), 1));
                }
                //And destroy the entity on the disguisee as well
                playerMP.connection.sendPacket(new SPacketDestroyEntities(disguise.getEntityId()));
            }
            //Disable invisibility
            playerMP.setInvisible(false);
            //Send message
            if (!isDcing) {
                playerMP.sendMessage(new TextComponentString("§bPokeDisguise §6turned off!"));
            }
        } else if (!isDcing) {
            playerMP.sendMessage(new TextComponentString("§4You are not disguised so there's nothing to turn off!"));
        }
    }

}

