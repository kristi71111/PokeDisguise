package kristi71111.pokedisguise.objects;

import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
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
    private double lastPositionX;
    private double LastPositionY;
    private double LastPositionZ;
    private ScorePlayerTeam hackedCollision;

    public DisguisedPlayer(EntityPlayerMP trackedPlayer, EntityPixelmon disguisedEntity) {
        this.disguisedEntity = disguisedEntity;
        this.lastEncodedPosX = EntityTracker.getPositionLong(trackedPlayer.posX);
        this.lastEncodedPosY = EntityTracker.getPositionLong(trackedPlayer.posY);
        this.lastEncodedPosZ = EntityTracker.getPositionLong(trackedPlayer.posZ);
        if (ConfigRegistry.shouldPlayerSeeOwnDisguise) {
            if (ConfigRegistry.shouldPlayerHaveDisabledCollisions) {
                hackedCollision = new ScorePlayerTeam(trackedPlayer.getWorldScoreboard(), "PokeDisguiseHack");
                hackedCollision.getMembershipCollection().add(trackedPlayer.getName());
                hackedCollision.setCollisionRule(Team.CollisionRule.NEVER);
                hackedCollision.setSeeFriendlyInvisiblesEnabled(false);
            }
            lastPositionX = trackedPlayer.posX;
            LastPositionY = trackedPlayer.posY;
            LastPositionZ = trackedPlayer.posZ;
        }
    }

    public double getLastPositionY() {
        return LastPositionY;
    }

    public void setLastPositionY(double lastPositionY) {
        LastPositionY = lastPositionY;
    }

    public double getLastPositionX() {
        return lastPositionX;
    }

    public void setLastPositionX(double lastPositionX) {
        this.lastPositionX = lastPositionX;
    }

    public double getLastPositionZ() {
        return LastPositionZ;
    }

    public void setLastPositionZ(double lastPositionZ) {
        LastPositionZ = lastPositionZ;
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