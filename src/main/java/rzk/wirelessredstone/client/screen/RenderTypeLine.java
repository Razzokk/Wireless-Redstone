package rzk.wirelessredstone.client.screen;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public abstract class RenderTypeLine extends RenderType
{
    public static final RenderType LINES = create("wr_line_render", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false, RenderType.CompositeState.builder()
        .setShaderState(RENDERTYPE_LINES_SHADER)
        .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setOutputState(ITEM_ENTITY_TARGET)
        .setWriteMaskState(COLOR_DEPTH_WRITE)
        .setDepthTestState(NO_DEPTH_TEST)
        .setCullState(NO_CULL)
        .createCompositeState(false));

    private RenderTypeLine(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_)
    {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
}
