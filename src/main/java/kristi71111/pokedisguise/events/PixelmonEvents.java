package kristi71111.pokedisguise.events;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import kristi71111.pokedisguise.PokeDisguise;
import kristi71111.pokedisguise.objects.DisguisedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PixelmonEvents {
    @SubscribeEvent
    public void onPixelmonSendOutEvent(PixelmonSendOutEvent event) {
        EntityPlayerMP playerMP = event.player;
        DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(playerMP);
        if (disguisedPlayer != null) {
            playerMP.sendMessage(new TextComponentString("§4You can't send out pokemon while disguised!"));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBattleStartedEvent(BattleStartedEvent event) {
        EntityLivingBase battleParticipant1 = event.participant1[0].getEntity();
        EntityLivingBase battleParticipant2 = event.participant2[0].getEntity();
        if (battleParticipant1 instanceof EntityPlayerMP) {
            DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(battleParticipant1);
            if (disguisedPlayer != null) {
                event.setCanceled(true);
            }
        }
        if (battleParticipant2 instanceof EntityPlayerMP) {
            DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(battleParticipant2);
            if (disguisedPlayer != null) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPokeBallImpact(PokeballImpactEvent event){
        if(!PixelmonConfig.pokeBallPlayerEngage){
            return;
        }
        RayTraceResult rayTraceResult = event.ballPosition;
        if(rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK){
            if(rayTraceResult.entityHit instanceof EntityPlayerMP){
                DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(rayTraceResult.entityHit);
                if (disguisedPlayer != null) {
                    event.setCanceled(true);
                }
            }
        }
    }
}

