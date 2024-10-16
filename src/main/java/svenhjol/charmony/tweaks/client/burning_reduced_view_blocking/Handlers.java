package svenhjol.charmony.tweaks.client.burning_reduced_view_blocking;

import com.mojang.blaze3d.vertex.PoseStack;
import svenhjol.charmony.core.base.Setup;

public class Handlers extends Setup<BurningReducedViewBlocking> {
    public Handlers(BurningReducedViewBlocking feature) {
        super(feature);
    }

    public void changeFireSize(PoseStack poseStack) {
        var pos = feature().verticalPosition();
        var scale = feature().scaledSize();
        poseStack.translate(0d, pos, 0d);
        poseStack.scale(scale, scale, scale);
    }
}
