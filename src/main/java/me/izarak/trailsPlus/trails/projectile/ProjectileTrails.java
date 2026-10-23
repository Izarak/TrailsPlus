package me.izarak.trailsPlus.trails.projectile;

import me.izarak.trailsPlus.trails.IProjectileTrailAction;

public enum ProjectileTrails {
    ARROW_AQUA(new ArrowAquaTrail()),
    ARROW_BLACK(new ArrowBlackTrail()),
    ARROW_BLUE(new ArrowBlueTrail()),
    ARROW_FUCHSIA(new ArrowFuchsiaTrail()),
    ARROW_GRAY(new ArrowGrayTrail()),
    ARROW_GREEN(new ArrowGreenTrail()),
    ARROW_LIME(new ArrowLimeTrail()),
    ARROW_MAROON(new ArrowMaroonTrail()),
    ARROW_NAVY(new ArrowNavyTrail()),
    ARROW_OLIVE(new ArrowOliveTrail()),
    ARROW_ORANGE(new ArrowOrangeTrail()),
    ARROW_PURPLE(new ArrowPurpleTrail()),
    ARROW_RED(new ArrowRedTrail()),
    ARROW_SILVER(new ArrowSilverTrail()),
    ARROW_TEAL(new ArrowTealTrail()),
    ARROW_YELLOW(new ArrowYellowTrail()),
    BLOCK_PARTICLE_SPRAY(new BlockParticleSprayTrail()),
    BLOOD_HELIX(new BloodHelix());

    final IProjectileTrailAction action;

    ProjectileTrails(IProjectileTrailAction action) {
        this.action = action;
    }

    public IProjectileTrailAction getTrailAction() {
        return action;
    }
}
