package kristi71111.pokedisguise.objects;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import kristi71111.pokedisguise.ConfigRegistry;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class DisguisedPlayer {
    private EntityPixelmon disguisedEntity;
    private long lastEncodedPosX;
    private long lastEncodedPosY;
    private long lastEncodedPosZ;
    private ScorePlayerTeam hackedCollision;

    public DisguisedPlayer(EntityPlayerMP trackedPlayer, EntityPixelmon disguisedEntity) {
        this.disguisedEntity = disguisedEntity;
        this.lastEncodedPosX = EntityTracker.getPositionLong(trackedPlayer.posX);
        this.lastEncodedPosY = EntityTracker.getPositionLong(trackedPlayer.posY);
        this.lastEncodedPosZ = EntityTracker.getPositionLong(trackedPlayer.posZ);
        if (ConfigRegistry.shouldPlayerSeeOwnDisguise && ConfigRegistry.shouldPlayerHaveDisabledCollisions) {
            hackedCollision = new ScorePlayerTeam(trackedPlayer.getWorldScoreboard(), "PokeDisguiseHack");
            hackedCollision.getMembershipCollection().add(trackedPlayer.getName());
            hackedCollision.setCollisionRule(Team.CollisionRule.NEVER);
            hackedCollision.setSeeFriendlyInvisiblesEnabled(false);
        }
    }

    public ScorePlayerTeam getHackedCollision() {
        return hackedCollision;
    }

    public long getLastEncodedPosX() {
        return lastEncodedPosX;
    }

    public void setLastEncodedPosX(long encodedPosX) {
        this.lastEncodedPosX = encodedPosX;
    }

    public long getLastEncodedPosY() {
        return lastEncodedPosY;
    }

    public void setLastEncodedPosY(long lastEncodedPosY) {
        this.lastEncodedPosY = lastEncodedPosY;
    }

    public long getLastEncodedPosZ() {
        return lastEncodedPosZ;
    }

    public void setLastEncodedPosZ(long lastEncodedPosZ) {
        this.lastEncodedPosZ = lastEncodedPosZ;
    }

    public EntityPixelmon getDisguisedEntity() {
        return disguisedEntity;
    }

    public void setDisguisedEntity(EntityPixelmon disguisedEntity) {
        this.disguisedEntity = disguisedEntity;
    }
}
