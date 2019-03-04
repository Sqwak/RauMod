package xyz.perfected.config.util;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.perfected.config.util.javafx.Pair;

import java.util.List;

public class Polygon
{
    private List<Pair<Double, Double>> vertices;
    
    private Polygon(Object o) {
       // this.vertices = (List<Pair<Double, Double>>)Lists.newArrayList();
        this.vertices = Lists.newArrayList();
    }
    
    public void draw(final int color) {
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        for (final Pair<Double, Double> vertice : this.vertices) {
            worldrenderer.pos((double)vertice.getKey(), (double)vertice.getValue(), 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static class Builder
    {
        private Polygon drawer;
        
        private Builder() {
            this.drawer = new Polygon(null);
        }
        
        public static Builder create() {
            return new Builder();
        }
        
        public Builder addVertex(final double x, final double z) {
            this.drawer.vertices.add(new Pair((Object)x, (Object)z));
            return this;
        }
        
        public Polygon build() {
            return this.drawer;
        }
    }
}
