package xyz.perfected.config.gui.selector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

import java.util.function.Predicate;

public class TextSelector extends Gui
{
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private int maxStringLength;
    private boolean enableBackgroundDrawing;
    private boolean canLoseFocus;
    private boolean isFocused;
    private boolean isEnabled;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean visible;
    private FontRenderer renderer;
    private final Predicate<Character> charPredicate;
    
    public TextSelector(final String text, final Predicate<Character> charPredicate) {
        this.text = "";
        this.maxStringLength = 100;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.renderer = Minecraft.getMinecraft().fontRendererObj;
        this.charPredicate = charPredicate;
        this.text = text;
    }
    
    public TextSelector(final String text) {
        this(text, character -> true);
    }
    
    public void setText(final String p_146180_1_) {
        if (p_146180_1_.length() > maxStringLength) {
            text = p_146180_1_.substring(0, maxStringLength);
        }
        else {
            text = p_146180_1_;
        }
        setCursorPositionEnd();
    }
    
    public String getText() {
        return text;
    }
    
    public String getSelectedText() {
        final int i = (cursorPosition < selectionEnd) ? cursorPosition : selectionEnd;
        final int j = (cursorPosition < selectionEnd) ? selectionEnd : cursorPosition;
        return text.substring(i, j);
    }
    
    public void writeText(final String p_146191_1_) {
        String s = "";
        final String s2 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        final int i = (cursorPosition < selectionEnd) ? cursorPosition : selectionEnd;
        final int j = (cursorPosition < selectionEnd) ? selectionEnd : cursorPosition;
        final int k = maxStringLength - text.length() - (i - j);
        int l = 0;
        if (text.length() > 0) {
            s += text.substring(0, i);
        }
        if (k < s2.length()) {
            s += s2.substring(0, k);
            l = k;
        }
        else {
            s += s2;
            l = s2.length();
        }
        if (text.length() > 0 && j < text.length()) {
            s += text.substring(j);
        }
        text = s;
        moveCursorBy(i - selectionEnd + l);
    }
    
    public void deleteWords(final int p_146177_1_) {
        if (text.length() != 0) {
            if (selectionEnd != cursorPosition) {
                writeText("");
            }
            else {
                deleteFromCursor(getNthWordFromCursor(p_146177_1_) - cursorPosition);
            }
        }
    }
    
    public void deleteFromCursor(final int p_146175_1_) {
        if (text.length() != 0) {
            if (selectionEnd != cursorPosition) {
                writeText("");
            }
            else {
                final boolean flag = p_146175_1_ < 0;
                final int i = flag ? (cursorPosition + p_146175_1_) : cursorPosition;
                final int j = flag ? cursorPosition : (cursorPosition + p_146175_1_);
                String s = "";
                if (i >= 0) {
                    s = text.substring(0, i);
                }
                if (j < text.length()) {
                    s += text.substring(j);
                }
                text = s;
                if (flag) {
                    moveCursorBy(p_146175_1_);
                }
            }
        }
    }
    
    public int getNthWordFromCursor(final int p_146187_1_) {
        return getNthWordFromPos(p_146187_1_, getCursorPosition());
    }
    
    public int getNthWordFromPos(final int p_146183_1_, final int p_146183_2_) {
        return func_146197_a(p_146183_1_, p_146183_2_, true);
    }
    
    public int func_146197_a(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
        int i = p_146197_2_;
        final boolean flag = p_146197_1_ < 0;
        for (int j = Math.abs(p_146197_1_), k = 0; k < j; ++k) {
            if (!flag) {
                final int l = text.length();
                i = text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                }
                else {
                    while (p_146197_3_ && i < l && text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            }
            else {
                while (p_146197_3_ && i > 0 && text.charAt(i - 1) == ' ') {
                    --i;
                }
                while (i > 0 && text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }
        return i;
    }
    
    public void moveCursorBy(final int p_146182_1_) {
        setCursorPosition(selectionEnd + p_146182_1_);
    }
    
    public void setCursorPosition(final int p_146190_1_) {
        cursorPosition = p_146190_1_;
        final int i = text.length();
        setSelectionPos(cursorPosition = MathHelper.clamp_int(cursorPosition, 0, i));
    }
    
    public void setCursorPositionZero() {
        setCursorPosition(0);
    }
    
    public void setCursorPositionEnd() {
        setCursorPosition(text.length());
    }
    
    public boolean textboxKeyTyped(final char p_146201_1_, final int p_146201_2_) {
        if (!isFocused) {
            return false;
        }
        if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
            setCursorPositionEnd();
            setSelectionPos(0);
            return true;
        }
        if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
            GuiScreen.setClipboardString(getSelectedText());
            return true;
        }
        if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
            if (isEnabled) {
                writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
            GuiScreen.setClipboardString(getSelectedText());
            if (isEnabled) {
                writeText("");
            }
            return true;
        }
        switch (p_146201_2_) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (isEnabled) {
                        deleteWords(-1);
                    }
                }
                else if (isEnabled) {
                    deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    setSelectionPos(0);
                }
                else {
                    setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
                    }
                    else {
                        setSelectionPos(getSelectionEnd() - 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown()) {
                    setCursorPosition(getNthWordFromCursor(-1));
                }
                else {
                    moveCursorBy(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
                    }
                    else {
                        setSelectionPos(getSelectionEnd() + 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown()) {
                    setCursorPosition(getNthWordFromCursor(1));
                }
                else {
                    moveCursorBy(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    setSelectionPos(text.length());
                }
                else {
                    setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (isEnabled) {
                        deleteWords(1);
                    }
                }
                else if (isEnabled) {
                    deleteFromCursor(1);
                }
                return true;
            }
            default: {
                if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_) && charPredicate.test(p_146201_1_)) {
                    if (isEnabled) {
                        writeText(Character.toString(p_146201_1_));
                    }
                    return true;
                }
                return false;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final boolean flag = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        if (canLoseFocus) {
            setFocused(flag);
        }
        if (isFocused && flag && mouseButton == 0) {
            int i = mouseX - x;
            if (enableBackgroundDrawing) {
                i -= 4;
            }
            final String s = renderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            setCursorPosition(renderer.trimStringToWidth(s, i).length() + lineScrollOffset);
        }
    }
    
    public int getElementX(final int x, final int availWidth) {
        return (int)(x + availWidth * 0.5) - width / 2;
    }
    
    public int getElementY(final int y, final int availHeight) {
        return (int)(y + availHeight * 0.5) - height / 2;
    }
    
    public void draw(final int xElement, final int yElement, final int availWidth, final int availHeight, final int xMouse, final int yMouse) {
        width = availWidth - 20;
        height = 20;
        x = getElementX(xElement, availWidth);
        y = getElementY(yElement, availHeight);
        if (getVisible()) {
            if (getEnableBackgroundDrawing()) {
                drawRect(x - 1, y - 1, x + width + 1, y + height + 1, -6250336);
                drawRect(x, y, x + width, y + height, -16777216);
            }
            final int i = isEnabled ? enabledColor : disabledColor;
            final int j = cursorPosition - lineScrollOffset;
            int k = selectionEnd - lineScrollOffset;
            final String s = renderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            final boolean flag = j >= 0 && j <= s.length();
            final boolean flag2 = isFocused && System.currentTimeMillis() / 500L % 2L == 0L && flag;
            final int l = enableBackgroundDrawing ? (x + 4) : x;
            final int i2 = enableBackgroundDrawing ? (y + (height - 8) / 2) : y;
            int j2 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (s.length() > 0) {
                final String s2 = flag ? s.substring(0, j) : s;
                j2 = renderer.drawStringWithShadow(s2, (float)l, (float)i2, i);
            }
            final boolean flag3 = cursorPosition < text.length() || text.length() >= getMaxStringLength();
            int k2 = j2;
            if (!flag) {
                k2 = ((j > 0) ? (l + width) : l);
            }
            else if (flag3) {
                k2 = j2 - 1;
                --j2;
            }
            if (s.length() > 0 && flag && j < s.length()) {
                j2 = renderer.drawStringWithShadow(s.substring(j), (float)j2, (float)i2, i);
            }
            if (flag2) {
                if (flag3) {
                    Gui.drawRect(k2, i2 - 1, k2 + 1, i2 + 1 + renderer.FONT_HEIGHT, -3092272);
                }
                else {
                    renderer.drawStringWithShadow("_", (float)k2, (float)i2, i);
                }
            }
            if (k != j) {
                final int l2 = l + renderer.getStringWidth(s.substring(0, k));
                drawCursorVertical(k2, i2 - 1, l2 - 1, i2 + 1 + renderer.FONT_HEIGHT);
            }
        }
    }
    
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            final int i = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i;
        }
        if (p_146188_2_ < p_146188_4_) {
            final int j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }
        if (p_146188_3_ > x + width) {
            p_146188_3_ = x + width;
        }
        if (p_146188_1_ > x + width) {
            p_146188_1_ = x + width;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)p_146188_1_, (double)p_146188_4_, 0.0).endVertex();
        worldrenderer.pos((double)p_146188_3_, (double)p_146188_4_, 0.0).endVertex();
        worldrenderer.pos((double)p_146188_3_, (double)p_146188_2_, 0.0).endVertex();
        worldrenderer.pos((double)p_146188_1_, (double)p_146188_2_, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }
    
    public void setMaxStringLength(final int p_146203_1_) {
        maxStringLength = p_146203_1_;
        if (text.length() > p_146203_1_) {
            text = text.substring(0, p_146203_1_);
        }
    }
    
    public int getMaxStringLength() {
        return maxStringLength;
    }
    
    public int getCursorPosition() {
        return cursorPosition;
    }
    
    public boolean getEnableBackgroundDrawing() {
        return enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(final boolean p_146185_1_) {
        enableBackgroundDrawing = p_146185_1_;
    }
    
    public void setTextColor(final int p_146193_1_) {
        enabledColor = p_146193_1_;
    }
    
    public void setDisabledTextColour(final int p_146204_1_) {
        disabledColor = p_146204_1_;
    }
    
    public void setFocused(final boolean p_146195_1_) {
        if (!p_146195_1_ || !isFocused) {}
        isFocused = p_146195_1_;
    }
    
    public boolean isFocused() {
        return isFocused;
    }
    
    public void setEnabled(final boolean p_146184_1_) {
        isEnabled = p_146184_1_;
    }
    
    public int getSelectionEnd() {
        return selectionEnd;
    }
    
    public int getWidth() {
        return getEnableBackgroundDrawing() ? (width - 8) : width;
    }
    
    public void setSelectionPos(int p_146199_1_) {
        final int i = text.length();
        if (p_146199_1_ > i) {
            p_146199_1_ = i;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        selectionEnd = p_146199_1_;
        if (renderer != null) {
            if (lineScrollOffset > i) {
                lineScrollOffset = i;
            }
            final int j = getWidth();
            final String s = renderer.trimStringToWidth(text.substring(lineScrollOffset), j);
            final int k = s.length() + lineScrollOffset;
            if (p_146199_1_ == lineScrollOffset) {
                lineScrollOffset -= renderer.trimStringToWidth(text, j, true).length();
            }
            if (p_146199_1_ > k) {
                lineScrollOffset += p_146199_1_ - k;
            }
            else if (p_146199_1_ <= lineScrollOffset) {
                lineScrollOffset -= lineScrollOffset - p_146199_1_;
            }
            lineScrollOffset = MathHelper.clamp_int(lineScrollOffset, 0, i);
        }
    }
    
    public void setCanLoseFocus(final boolean p_146205_1_) {
        canLoseFocus = p_146205_1_;
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public void setVisible(final boolean p_146189_1_) {
        visible = p_146189_1_;
    }
}
