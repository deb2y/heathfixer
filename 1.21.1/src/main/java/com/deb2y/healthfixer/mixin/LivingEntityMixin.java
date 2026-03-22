package com.deb2y.healthfixer.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "getHealth", at = @At("HEAD"), cancellable = true)
    private void healthfixer$getHealth(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity && entity.getEntityWorld() != null) {
            if (entity == MinecraftClient.getInstance().player) {
                return;
            }

            Scoreboard scoreboard = entity.getEntityWorld().getScoreboard();
            if (scoreboard != null) {
                ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
                if (objective != null) {
                    ReadableScoreboardScore score = scoreboard.getScore(entity, objective);
                    if (score != null && score.getScore() > 0) {
                        cir.setReturnValue((float) score.getScore());
                    }
                }
            }
        }
    }
}