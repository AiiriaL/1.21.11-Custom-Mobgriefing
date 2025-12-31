package net.aiirial.custommobgriefing.events;

import net.aiirial.custommobgriefing.config.CustomMobGriefingConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

public class CustomMobGriefingEvents {

    /* Explosionen */
    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (!(event.getLevel() instanceof ServerLevel)) return;

        Entity e = event.getExplosion().getDirectSourceEntity();
        if (e == null) return;

        if (e instanceof Creeper && !CustomMobGriefingConfig.isGriefingAllowed("creeper")
                || e instanceof WitherBoss && !CustomMobGriefingConfig.isGriefingAllowed("wither")
                || e instanceof EnderDragon && !CustomMobGriefingConfig.isGriefingAllowed("ender_dragon")) {
            event.getAffectedBlocks().clear();
        }
    }

    /* Projektile */
    @SubscribeEvent
    public static void onProjectile(ProjectileImpactEvent event) {
        if (!(event.getProjectile().level() instanceof ServerLevel)) return;

        if (event.getProjectile() instanceof Fireball
                && !CustomMobGriefingConfig.isGriefingAllowed("ghast")
                && event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
            event.setCanceled(true);
        }

        if (event.getProjectile() instanceof WitherSkull
                && !CustomMobGriefingConfig.isGriefingAllowed("wither")
                && event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
            event.setCanceled(true);
        }
    }

    /* Blockzerstörung */
    @SubscribeEvent
    public static void onBlockBreak(LivingDestroyBlockEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel)) return;

        Entity e = event.getEntity();
        BlockState state = event.getState();

        if (e instanceof Zombie
                && !CustomMobGriefingConfig.isGriefingAllowed("zombie")
                && (state.is(Blocks.TURTLE_EGG) || state.is(BlockTags.WOODEN_DOORS))) {
            event.setCanceled(true);
        }

        if (e instanceof Ravager
                && !CustomMobGriefingConfig.isGriefingAllowed("ravager")
                && state.is(BlockTags.LEAVES)) {
            event.setCanceled(true);
        }

        if (e instanceof Silverfish
                && !CustomMobGriefingConfig.isGriefingAllowed("silverfish")) {
            event.setCanceled(true);
        }
    }

    /* Globales MobGriefing */
    @SubscribeEvent
    public static void onMobGriefing(EntityMobGriefingEvent event) {
        Entity e = event.getEntity();

        boolean allow =
                e instanceof Creeper ? CustomMobGriefingConfig.isGriefingAllowed("creeper") :
                        e instanceof WitherBoss ? CustomMobGriefingConfig.isGriefingAllowed("wither") :
                                e instanceof EnderDragon ? CustomMobGriefingConfig.isGriefingAllowed("ender_dragon") :
                                        e instanceof Ghast ? CustomMobGriefingConfig.isGriefingAllowed("ghast") :
                                                e instanceof EnderMan ? CustomMobGriefingConfig.isGriefingAllowed("enderman") :
                                                        e instanceof Ravager ? CustomMobGriefingConfig.isGriefingAllowed("ravager") :
                                                                e instanceof Silverfish ? CustomMobGriefingConfig.isGriefingAllowed("silverfish") :
                                                                        e instanceof Zombie ? CustomMobGriefingConfig.isGriefingAllowed("zombie") :
                                                                                true;

        event.setCanGrief(allow);
    }
}
