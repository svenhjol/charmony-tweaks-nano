package svenhjol.charmony.tweaks.client.crafting_table_nearby;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import svenhjol.charmony.core.base.Setup;

import java.util.Optional;

public class Handlers extends Setup<CraftingTableNearby> {
    private BlockPos craftingTablePos;

    public Handlers(CraftingTableNearby feature) {
        super(feature);
        craftingTablePos = null;
    }

    public void clientTick(Minecraft minecraft) {
        var level = minecraft.level;
        var player = minecraft.player;
        var gameMode = minecraft.gameMode;
        if (level == null) return;

        while (feature().registers.openCraftingKey.consumeClick()) {
            if (craftingTablePos != null) {
                if (player == null || gameMode == null) return;
                var hitResult = new BlockHitResult(Vec3.atCenterOf(craftingTablePos), Direction.NORTH, new BlockPos(craftingTablePos), false);
                gameMode.useItemOn(player, InteractionHand.MAIN_HAND, hitResult);
            }
        }

        if (level.getGameTime() % 5 == 0) {
            findCraftingTablePos();
        }

        feature().registers.hudRenderer.tick(player);
    }

    public void findCraftingTablePos() {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        var gameMode = minecraft.gameMode;
        if (player == null || gameMode == null) {
            craftingTablePos = null;
            return;
        }

        var pos = player.blockPosition();
        var level = player.level();
        var range = 4;
        craftingTablePos = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
            .filter(p -> level.getBlockState(p) == Blocks.CRAFTING_TABLE.defaultBlockState())
            .findFirst()
            .orElse(null);
    }

    public Optional<BlockPos> getCraftingTablePos() {
        return Optional.ofNullable(craftingTablePos);
    }

    public void hudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        feature().registers.hudRenderer.render(guiGraphics, deltaTracker);
    }
}