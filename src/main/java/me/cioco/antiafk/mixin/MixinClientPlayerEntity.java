package me.cioco.antiafk.mixin;

import me.cioco.antiafk.Main;
import me.cioco.antiafk.commands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntity.class)
public class MixinClientPlayerEntity {

    @Unique
    private static final Random random = new Random();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        boolean utiltick = player.age % IntervalCommand.interval == 0;

        int moveX = random.nextInt(11) - 5;

        if (Main.toggled) {
            if (JumpCommand.autoJumpEnabled && utiltick && player.isOnGround()) {
                player.jump();
            }

            if (SpinCommand.autoSpinEnabled) {
                player.setYaw(player.getYaw() + SpinCommand.spinSpeed);
            }

            if (MouseMovementCommand.mousemovement && utiltick) {
                player.setYaw(player.getYaw() + moveX);
            }

            if (SneakCommand.sneak && utiltick) {
                mc.options.sneakKey.setPressed(true);
            } else if (SneakCommand.sneak) {
                mc.options.sneakKey.setPressed(false);
            }

            if (SwingCommand.shouldSwing && mc.world != null && mc.player != null && utiltick) {
                mc.player.swingHand(mc.player.getActiveHand());
            }
        }
    }
}
