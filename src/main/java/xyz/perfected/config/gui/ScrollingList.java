package xyz.perfected.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.perfected.config.util.ScaledResolution;

import java.util.List;

public abstract class ScrollingList
{
    private final Minecraft client;
    private final int listHeight;
    private final int screenWidth;
    private final int screenHeight;
    private final int top;
    private final int bottom;
    private final int right;
    private final int left;
    private final int slotHeight;
    private int scrollUpActionId;
    private int scrollDownActionId;
    private int mouseX;
    private int mouseY;
    private float initialMouseClickY;
    private float scrollFactor;
    private float scrollDistance;
    private int selectedIndex;
    private long lastClickTime;
    private boolean highlightSelected;
    private boolean hasHeader;
    private int headerHeight;

    public ScrollingList(final int height, final int top, final int bottom, final int left, final int entryHeight, final int screenWidth, final int screenHeight) {
        this.client = Minecraft.getMinecraft();
        this.initialMouseClickY = -2.0f;
        this.selectedIndex = -1;
        this.lastClickTime = 0L;
        this.highlightSelected = true;
        this.listHeight = height;
        this.top = top;
        this.bottom = bottom;
        this.slotHeight = entryHeight;
        this.left = left;
        this.right = getWidth() + left;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public int getWidth() {
        return new xyz.perfected.config.util.ScaledResolution().getScaledWidth() / 2 - 20;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public void setHeaderInfo(final boolean hasHeader, final int headerHeight) {
        this.hasHeader = hasHeader;
        this.headerHeight = headerHeight;
        if (!hasHeader) {
            this.headerHeight = 0;
        }
    }

    protected abstract int getSize();

    protected abstract void elementClicked(final int p0, final boolean p1);

    protected abstract boolean isSelected(final int p0);

    protected int getContentHeight() {
        return getSize() * slotHeight + headerHeight;
    }

    protected abstract void drawBackground();

    protected abstract void drawSlot(final int p0, final int p1, final int p2, final int p3, final Tessellator p4);

    @Deprecated
    protected void func_27260_a(final int entryRight, final int relativeY, final Tessellator tess) {
    }

    protected void drawHeader(final int entryRight, final int relativeY, final Tessellator tess) {
        func_27260_a(entryRight, relativeY, tess);
    }

    @Deprecated
    protected void func_27255_a(final int x, final int y) {
    }

    protected void clickHeader(final int x, final int y) {
        func_27255_a(x, y);
    }

    @Deprecated
    protected void func_27257_b(final int mouseX, final int mouseY) {
    }

    protected void drawScreen(final int mouseX, final int mouseY) {
        func_27257_b(mouseX, mouseY);
    }

    public int func_27256_c(final int x, final int y) {
        final int left = this.left + 1;
        final int right = left + getWidth() - 7;
        final int relativeY = y - top - headerHeight + (int)scrollDistance - 4;
        final int entryIndex = relativeY / slotHeight;
        return (x >= left && x <= right && entryIndex >= 0 && relativeY >= 0 && entryIndex < getSize()) ? entryIndex : -1;
    }

    public void registerScrollButtons(final List buttons, final int upActionID, final int downActionID) {
        scrollUpActionId = upActionID;
        scrollDownActionId = downActionID;
    }

    private void applyScrollLimits() {
        int listHeight = getContentHeight() - (bottom - top - 4);
        if (listHeight < 0) {
            listHeight /= 2;
        }
        if (scrollDistance < 0.0f) {
            scrollDistance = 0.0f;
        }
        if (scrollDistance > listHeight) {
            scrollDistance = listHeight;
        }
    }

    public void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == scrollUpActionId) {
                scrollDistance -= slotHeight * 2 / 3;
                initialMouseClickY = -2.0f;
                applyScrollLimits();
            }
            else if (button.id == scrollDownActionId) {
                scrollDistance += slotHeight * 2 / 3;
                initialMouseClickY = -2.0f;
                applyScrollLimits();
            }
        }
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        drawBackground();
        final boolean isHovering = mouseX >= left && mouseX <= left + getWidth() && mouseY >= top && mouseY <= bottom;
        final int listLength = getSize();
        final int scrollBarWidth = 6;
        final int scrollBarRight = left + getWidth();
        final int scrollBarLeft = scrollBarRight - scrollBarWidth;
        final int entryLeft = left;
        final int entryRight = scrollBarLeft - 1;
        final int viewHeight = bottom - top;
        final int border = 4;
        if (Mouse.isButtonDown(0)) {
            if (initialMouseClickY == -1.0f) {
                if (isHovering) {
                    final int mouseListY = mouseY - top - headerHeight + (int)scrollDistance - border;
                    final int slotIndex = mouseListY / slotHeight;
                    if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength) {
                        elementClicked(slotIndex, slotIndex == selectedIndex && System.currentTimeMillis() - lastClickTime < 250L);
                        selectedIndex = slotIndex;
                        lastClickTime = System.currentTimeMillis();
                    }
                    else if (mouseX >= entryLeft && mouseX <= entryRight && mouseListY < 0) {
                        clickHeader(mouseX - entryLeft, mouseY - top + (int)scrollDistance - border);
                    }
                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {
                        scrollFactor = -1.0f;
                        int scrollHeight = getContentHeight() - viewHeight - border;
                        if (scrollHeight < 1) {
                            scrollHeight = 1;
                        }
                        int var13 = viewHeight * viewHeight / getContentHeight();
                        if (var13 < 32) {
                            var13 = 32;
                        }
                        if (var13 > viewHeight - border * 2) {
                            var13 = viewHeight - border * 2;
                        }
                        scrollFactor /= (viewHeight - var13) / scrollHeight;
                    }
                    else {
                        scrollFactor = 1.0f;
                    }
                    initialMouseClickY = mouseY;
                }
                else {
                    initialMouseClickY = -2.0f;
                }
            }
            else if (initialMouseClickY >= 0.0f) {
                scrollDistance -= (mouseY - initialMouseClickY) * scrollFactor;
                initialMouseClickY = mouseY;
            }
        }
        else {
            while (isHovering && Mouse.next()) {
                int scroll = Mouse.getEventDWheel();
                if (scroll != 0) {
                    if (scroll > 0) {
                        scroll = -1;
                    }
                    else if (scroll < 0) {
                        scroll = 1;
                    }
                    scrollDistance += scroll * slotHeight * 2;
                }
            }
            initialMouseClickY = -1.0f;
        }
        applyScrollLimits();
        final Tessellator tess = Tessellator.getInstance();
        final WorldRenderer worldr = tess.getWorldRenderer();
        final xyz.perfected.config.util.ScaledResolution res = new ScaledResolution();
        final double scaleW = client.displayWidth / res.getScaledWidth_double();
        final double scaleH = client.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(3089);
        GL11.glScissor((int)(left * scaleW), (int)(client.displayHeight - bottom * scaleH), (int)(getWidth() * scaleW), (int)(viewHeight * scaleH));
        if (client.theWorld != null) {
            drawGradientRect(left, top, right, bottom, -1072689136, -804253680);
        }
        else {
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            client.renderEngine.bindTexture(Gui.optionsBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final float scale = 32.0f;
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double)left, (double)bottom, 0.0).tex((double)(left / scale), (double)((bottom + (int)scrollDistance) / scale)).color(32, 32, 32, 255).endVertex();
            worldr.pos((double)right, (double)bottom, 0.0).tex((double)(right / scale), (double)((bottom + (int)scrollDistance) / scale)).color(32, 32, 32, 255).endVertex();
            worldr.pos((double)right, (double)top, 0.0).tex((double)(right / scale), (double)((top + (int)scrollDistance) / scale)).color(32, 32, 32, 255).endVertex();
            worldr.pos((double)left, (double)top, 0.0).tex((double)(left / scale), (double)((top + (int)scrollDistance) / scale)).color(32, 32, 32, 255).endVertex();
            tess.draw();
        }
        final int baseY = top + border - (int)scrollDistance;
        if (hasHeader) {
            drawHeader(entryRight, baseY, tess);
        }
        for (int slotIdx = 0; slotIdx < listLength; ++slotIdx) {
            final int slotTop = baseY + slotIdx * slotHeight + headerHeight;
            final int slotBuffer = slotHeight - border;
            if (slotTop <= bottom && slotTop + slotBuffer >= top) {
                if (highlightSelected && isSelected(slotIdx)) {
                    final int min = left;
                    final int max = entryRight;
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.disableTexture2D();
                    worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos((double)min, (double)(slotTop + slotBuffer + 2), 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                    worldr.pos((double)max, (double)(slotTop + slotBuffer + 2), 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                    worldr.pos((double)max, (double)(slotTop - 2), 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                    worldr.pos((double)min, (double)(slotTop - 2), 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                    worldr.pos((double)(min + 1), (double)(slotTop + slotBuffer + 1), 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                    worldr.pos((double)(max - 1), (double)(slotTop + slotBuffer + 1), 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                    worldr.pos((double)(max - 1), (double)(slotTop - 1), 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                    worldr.pos((double)(min + 1), (double)(slotTop - 1), 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                    tess.draw();
                    GlStateManager.enableTexture2D();
                }
                drawSlot(slotIdx, entryRight, slotTop, slotBuffer, tess);
            }
        }
        GlStateManager.disableDepth();
        final int extraHeight = getContentHeight() - viewHeight - border;
        if (extraHeight > 0) {
            int height = viewHeight * viewHeight / getContentHeight();
            if (height < 32) {
                height = 32;
            }
            if (height > viewHeight - border * 2) {
                height = viewHeight - border * 2;
            }
            int barTop = (int)scrollDistance * (viewHeight - height) / extraHeight + top;
            if (barTop < top) {
                barTop = top;
            }
            GlStateManager.disableTexture2D();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double)scrollBarLeft, (double)bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldr.pos((double)scrollBarRight, (double)bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldr.pos((double)scrollBarRight, (double)top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            worldr.pos((double)scrollBarLeft, (double)top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double)scrollBarLeft, (double)(barTop + height), 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            worldr.pos((double)scrollBarRight, (double)(barTop + height), 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            worldr.pos((double)scrollBarRight, (double)barTop, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            worldr.pos((double)scrollBarLeft, (double)barTop, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double)scrollBarLeft, (double)(barTop + height - 1), 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
            worldr.pos((double)(scrollBarRight - 1), (double)(barTop + height - 1), 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
            worldr.pos((double)(scrollBarRight - 1), (double)barTop, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
            worldr.pos((double)scrollBarLeft, (double)barTop, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
            tess.draw();
        }
        drawScreen(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(3089);
    }

    protected void drawGradientRect(final int left, final int top, final int right, final int bottom, final int color1, final int color2) {
        final float a1 = (color1 >> 24 & 0xFF) / 255.0f;
        final float r1 = (color1 >> 16 & 0xFF) / 255.0f;
        final float g1 = (color1 >> 8 & 0xFF) / 255.0f;
        final float b1 = (color1 & 0xFF) / 255.0f;
        final float a2 = (color2 >> 24 & 0xFF) / 255.0f;
        final float r2 = (color2 >> 16 & 0xFF) / 255.0f;
        final float g2 = (color2 >> 8 & 0xFF) / 255.0f;
        final float b2 = (color2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)right, (double)top, 0.0).color(r1, g1, b1, a1).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).color(r1, g1, b1, a1).endVertex();
        worldrenderer.pos((double)left, (double)bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
