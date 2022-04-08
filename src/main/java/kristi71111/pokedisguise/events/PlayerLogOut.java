package kristi71111.pokedisguise.events;

import kristi71111.pokedisguise.Helpers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLogOut {
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        //Handling on disconnect since disguises vanish the moment the player dcs
        EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
        Helpers.removeDisguise(playerMP, true);
    }
}
