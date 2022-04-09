package kristi71111.pokedisguise.events;

import com.pixelmongenerations.api.events.PixelmonSendOutEvent;
import com.pixelmongenerations.api.events.PokeballImpactEvent;
import com.pixelmongenerations.api.events.battles.BattleRequestEvent;
import com.pixelmongenerations.core.config.PixelmonConfig;
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
        EntityPlayerMP playerMP = event.getPlayer();
        DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(playerMP);
        if (disguisedPlayer != null) {
            playerMP.sendMessage(new TextComponentString("§4You can't send out pokemon while disguised!"));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBattleStartedEvent(BattleRequestEvent event) {
        EntityLivingBase battleParticipant1 = event.getTarget();
        EntityLivingBase battleParticipant2 = event.getAttacker();
        DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(battleParticipant1);
        if (disguisedPlayer != null) {
            event.setCanceled(true);
        }
        DisguisedPlayer disguisedPlayer2 = PokeDisguise.disguisedPlayers.get(battleParticipant2);
        if (disguisedPlayer2 != null) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onPokeBallImpact(PokeballImpactEvent event) {
        if (!PixelmonConfig.pokeBallPlayerEngage) {
            return;
        }
        RayTraceResult rayTraceResult = event.getBallPosition();
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
            if (rayTraceResult.entityHit instanceof EntityPlayerMP) {
                DisguisedPlayer disguisedPlayer = PokeDisguise.disguisedPlayers.get(rayTraceResult.entityHit);
                if (disguisedPlayer != null) {
                    event.setCanceled(true);
                }
            }
        }
    }
}

