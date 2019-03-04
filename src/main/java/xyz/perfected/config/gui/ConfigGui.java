package xyz.perfected.config.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import xyz.perfected.config.Config;
import xyz.perfected.config.ConfigHolder;
import xyz.perfected.config.gui.element.GuiElement;
import xyz.perfected.config.util.ScaledResolution;
import xyz.perfected.config.value.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConfigGui extends GuiScreen
{
    private FontRenderer fontRendererObj;
    private ValueScrollingList scrollingList;
    private int selectedIndex;
    private Optional<GuiElement> selectedElement;
    private int buttonWidth;
    private int buttonHeight;
    private String updateInfo;
    private long lastUpdate;
    private int updateCounter;
    private String name;
    private ConfigHolder configHolder;
    private Config config;
    private List<GuiElement> entries;

    public ConfigGui(final Config config, final ConfigHolder configHolder) {
        this.selectedIndex = -1;
        this.selectedElement = Optional.empty();
        this.buttonWidth = 50;
        this.buttonHeight = 20;
        this.entries = Lists.newArrayList();
        this.fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        this.name = EnumChatFormatting.BOLD + this.name;
        this.configHolder = configHolder;
        this.config = config;
        this.name = config.getName();
        this.setupEntries();
    }

    private void setupEntries() {
        this.configHolder.getConfigEntries().stream().filter(Value::showInHud).map(entry -> entry.getMeta().createGui(entry, this)).sorted((e1, e2) -> e1.getValue().getCategory().compareToIgnoreCase(e2.getValue().getCategory())).forEach(this.entries::add);
        String lastCategory = null;
        for (int i = 0; i < this.entries.size(); ++i) {
            final String category = this.entries.get(i).getValue().getCategory();
            if (lastCategory == null || !category.equalsIgnoreCase(lastCategory)) {
                this.entries.add(i, new DummyElement(category, this));
                if (i != 0) {
                    this.entries.add(i, new DummyElement("", this));
                }
                ++i;
            }
            lastCategory = category;
        }
    }

    public void initGui() {
        this.updateCounter = 0;
        this.lastUpdate = 0L;
        this.scrollingList = new ValueScrollingList(this, this.entries);
        this.addButtons();
    }

    public int getVerticalPadding() {
        return 20;
    }

    public int getHorizontalPadding() {
        return 10;
    }

    public int getWidthFromMiddle() {
        return new ScaledResolution().getScaledWidth() / 2 - this.getHorizontalPadding();
    }

    private void addButtons() {
        final int xOffset = this.scrollingList.getRight();
        final int yOffset = this.height - this.getVerticalPadding() - this.buttonHeight;
        final int buttonCount = 3;
        final int padding = (this.getWidthFromMiddle() - this.buttonWidth * buttonCount) / buttonCount + 1;
        int counter = 1;
        this.buttonList.add(new Buttons.Save(xOffset + counter * padding + (counter - 1) * this.buttonWidth, yOffset, this.buttonWidth, this.buttonHeight) {
            public void mouseReleased(final int mouseX, final int mouseY) {
                if (this.isMouseOver()) {
                    ConfigGui.this.prepareSlotChange();
                    ConfigGui.this.mc.displayGuiScreen((GuiScreen)null);
                    if (ConfigGui.this.mc.currentScreen == null) {
                        ConfigGui.this.mc.setIngameFocus();
                    }
                    ConfigGui.this.config.saveAllFields();
                    ConfigGui.this.config.loadAllFields();
                }
            }
        });
        ++counter;
        this.buttonList.add(new Buttons.Reset(xOffset + counter * padding + (counter - 1) * this.buttonWidth, yOffset, this.buttonWidth, this.buttonHeight) {
            public void mouseReleased(final int mouseX, final int mouseY) {
                if (this.isMouseOver() && ConfigGui.this.selectedElement.isPresent()) {
                    ConfigGui.this.selectedElement.get().getValue().setValue(ConfigGui.this.selectedElement.get().getValue().getDefaultValue());
                    ConfigGui.this.selectedElement.get().onReset();
                    ConfigGui.this.updateInfo("Reset");
                }
            }
        });
        ++counter;
        this.buttonList.add(new Buttons.Cancel(xOffset + counter * padding + (counter - 1) * this.buttonWidth, yOffset, this.buttonWidth, this.buttonHeight) {
            public void mouseReleased(final int mouseX, final int mouseY) {
                if (this.isMouseOver()) {
                    ConfigGui.this.mc.displayGuiScreen((GuiScreen)null);
                    if (ConfigGui.this.mc.currentScreen == null) {
                        ConfigGui.this.mc.setIngameFocus();
                    }
                }
            }
        });
        ++counter;
    }

    protected void keyTyped(final char c, final int i) throws IOException {
        super.keyTyped(c, i);
        if (this.selectedElement.isPresent()) {
            this.selectedElement.get().keyTyped(c, i);
        }
    }

    public void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        if (this.selectedElement.isPresent()) {
            this.selectedElement.get().mouseClicked(i, j, k);
        }
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    public void selectModIndex(final int index) {
        if (index == this.selectedIndex) {
            return;
        }
        if (index < 0 || index >= this.entries.size()) {
            this.prepareSlotChange();
            this.selectedElement = Optional.empty();
            this.selectedIndex = -1;
            return;
        }
        this.prepareSlotChange();
        this.selectedIndex = index;
        this.selectedElement = Optional.ofNullable(this.entries.get(this.selectedIndex));
        this.entries.get(this.selectedIndex).onSelect();
    }

    private void prepareSlotChange() {
        if (this.selectedElement.isPresent()) {
            final Boolean ret = this.selectedElement.get().updateValue();
            if (ret == null) {
                this.updateInfo("Error");
                if (this.mc.thePlayer != null) {
                    this.mc.thePlayer.addChatComponentMessage((IChatComponent)new ChatComponentText(EnumChatFormatting.RED + "Couldn't save! Did you enter anything invalid? Value name: " + this.selectedElement.get().getValue().getDisplayName()));
                }
            }
            else if (ret) {
                this.updateInfo("Saved");
            }
        }
    }

    public boolean fieldIndexSelected(final int index) {
        return index == this.selectedIndex;
    }

    public void updateInfo(final String text) {
        this.lastUpdate = System.currentTimeMillis();
        this.updateInfo = text;
        this.updateCounter = 0;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.scrollingList.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution res = new ScaledResolution();
        final int middle = res.getScaledWidth() / 2;
        this.drawCenteredString(this.fontRendererObj, this.name, middle, 12, 16777215);
        if (this.selectedElement.isPresent()) {
            final GuiElement element = this.selectedElement.get();
            final int maxlinewidth = this.getWidthFromMiddle();
            int height = this.scrollingList.getTop();
            final List<String> header = this.lineBreaksAfterWidth(EnumChatFormatting.UNDERLINE + element.getDisplayName(), maxlinewidth);
            for (final String head : header) {
                final int xOffset = (middle * 2 + middle) / 2 - this.fontRendererObj.getStringWidth(head) / 2;
                this.drawString(this.fontRendererObj, head, xOffset, height, 16777215);
                height += (int)(this.fontRendererObj.FONT_HEIGHT * 1.5);
            }
            final List<String> info = this.lineBreaksAfterWidth(element.getDescription(), maxlinewidth);
            for (final String str : info) {
                this.drawString(this.fontRendererObj, str, middle + this.getHorizontalPadding(), height, 16777215);
                height += this.fontRendererObj.FONT_HEIGHT;
            }
            final float fontOffset = (info.size() + 1.5f) * this.fontRendererObj.FONT_HEIGHT;
            final int elementHeight = (int)(this.scrollingList.getBottom() - this.scrollingList.getTop() - this.buttonHeight - fontOffset);
            element.draw(this.scrollingList.getRight() + this.getHorizontalPadding(), (int)(this.scrollingList.getTop() + fontOffset), this.getWidthFromMiddle(), elementHeight, mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (System.currentTimeMillis() - this.lastUpdate <= 1500L) {
            this.fontRendererObj.drawString(this.updateInfo, res.getScaledWidth() - this.fontRendererObj.getStringWidth(this.updateInfo) - 1, res.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 1, 0xFFFFFF | 255 - this.updateCounter << 24);
            this.updateCounter += 2;
            this.updateCounter = Math.max(this.updateCounter, 150);
        }
    }

    private List<String> lineBreaksAfterWidth(String text, final int maxlinewidth) {
        final StringBuilder output = new StringBuilder();
        while (this.fontRendererObj.getStringWidth(text) > maxlinewidth) {
            int index;
            for (index = 1; this.fontRendererObj.getStringWidth(text.substring(0, index)) < maxlinewidth; ++index) {}
            --index;
            String subText = text.substring(0, index);
            subText = subText.trim();
            if (subText.contains(" ")) {
                if (subText.endsWith(" ")) {
                    subText = subText.substring(0, subText.length() - 1);
                }
                final int spaceIndex = subText.lastIndexOf(" ");
                subText = subText.substring(0, spaceIndex);
                text = text.substring(spaceIndex);
                output.append(subText).append("\n");
            }
            else {
                text = text.substring(index);
                output.append(subText).append("\n");
            }
        }
        output.append(text).append("\n");
        String[] a = output.toString().split("\n");
        return Lists.newArrayList(Arrays.asList(output.toString().replace("\n ","\n").split("\n")));
        //return (List<String>)Lists.newArrayList((Object[])output.toString().replace("\n ", "\n").split("\n"));
    }
}
