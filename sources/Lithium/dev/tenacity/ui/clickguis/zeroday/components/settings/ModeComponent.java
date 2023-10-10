package dev.tenacity.ui.clickguis.zeroday.components.settings;

import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.ui.clickguis.zeroday.components.SettingComponent;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.utils.tuples.mutable.MutablePair;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.Render;

import java.awt.*;

public class ModeComponent extends SettingComponent<ModeSetting> {

    private final Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);
    private final Animation openAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);
    private final Animation selectionBox = new DecelerateAnimation(250, 1, Direction.BACKWARDS);

    private boolean opened;

    public float realHeight;
    public float normalCount;

    private final ContinualAnimation selectionBoxY = new ContinualAnimation();
    private String hoveringMode = "";

    public ModeComponent(ModeSetting modeSetting) {
        super(modeSetting);
        this.height -= 5;
        normalCount = 2;
    }


    @Override
    public void initGui() {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        ModeSetting modeSetting = getSetting();

        float boxHeight = 24;
        float boxY = y + realHeight / 2f - (boxHeight / 2f) + 2;
        float boxX = x + 5;
        float boxWidth = width - 10;

        boolean hoveringBox = HoveringUtil.isHovering(boxX, boxY, boxWidth, boxHeight, mouseX, mouseY);

        hoverAnimation.setDirection(hoveringBox ? Direction.FORWARDS : Direction.BACKWARDS);
        openAnimation.setDirection(opened ? Direction.FORWARDS : Direction.BACKWARDS);

        Color rectColor = ColorUtil.interpolateColorC(settingRectColor, settingRectColor,
                (.5f * hoverAnimation.getOutput().floatValue()) + (openAnimation.getOutput().floatValue()));

        Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, rectColor.getRGB());

        lithiumFont16.drawString(
                modeSetting.name,
                boxX + lithiumFont16.getMiddleOfBox(boxHeight),
                boxY + lithiumFont16.getMiddleOfBox(boxHeight),
                textColor
        );

        lithiumFont16.drawString(
                modeSetting.getMode(),
                boxX + boxWidth - lithiumFont16.getStringWidth(modeSetting.getMode()) - 15,
                boxY + lithiumFont16.getMiddleOfBox(boxHeight),
                textColor
        );

        float arrowX = boxX + boxWidth - 12;
        float arrowY = boxY + iconFont20.getMiddleOfBox(boxHeight) + 1;
        float openAnim = openAnimation.getOutput().floatValue();

        RenderUtil.rotateStart(arrowX, arrowY, iconFont20.getStringWidth(FontUtil.DROPDOWN_ARROW), iconFont20.getHeight(), 180 * openAnim);
        iconFont20.drawString(FontUtil.DROPDOWN_ARROW, boxX + boxWidth - 11, boxY + iconFont20.getMiddleOfBox(boxHeight) + 1, textColor);
        RenderUtil.rotateEnd();

        if (opened || !openAnimation.isDone()) {
            float rectHeight = 20;
            float rectCount = 0;

            float modeHeight = modeSetting.modes.size() * rectHeight;
            float modeY = boxY + boxHeight + 4;
            float modeX = boxX - .25f;
            RoundedUtil.drawRound(modeX, modeY, boxWidth, Math.max(4, modeHeight * openAnim), 0, settingRectColor);

            boolean mouseOutsideRect = (mouseY < modeY || mouseY > modeY + modeHeight) || (mouseX < modeX || mouseX > modeX + boxWidth);

            selectionBox.setDirection(mouseOutsideRect ? Direction.BACKWARDS : Direction.FORWARDS);

            for (String mode : modeSetting.modes) {
                boolean hoveringMode = HoveringUtil.isHovering(modeX, modeY + rectCount * rectHeight, boxWidth, rectHeight, mouseX, mouseY);

                if (hoveringMode) {
                    this.hoveringMode = mode;
                }

                if (mode.equals(this.hoveringMode)) {
                    selectionBoxY.animate(rectCount * rectHeight, 17);
                }

                RenderUtil.drawCircleNotSmooth(
                        (int) modeX + 3,
                        (int) modeY + rectHeight * openAnim * rectCount + 5,
                        10,
                        ColorUtil.rainbow(10, (int) (x) / 10, 0.35F, 0.8F, 1.0F).getRGB()
                );

                RenderUtil.drawCircleNotSmooth(
                        (int) modeX + 4,
                        (int) modeY + rectHeight * openAnim * rectCount + 6,
                        8,
                        settingRectColor.getRGB()
                );

                RenderUtil.drawCircleNotSmooth(
                        (int) modeX + 5,
                        (int) modeY + rectHeight * openAnim * rectCount + 7,
                        6,
                        mode.equals(modeSetting.getMode())
                                ? ColorUtil.rainbow(10, (int) (x) / 10, 0.35F, 0.8F, 1.0F).getRGB()
                                : settingRectColor.getRGB()
                );

                RenderUtil.resetColor();
                lithiumFont16.drawString(mode, modeX + 15,
                        modeY + ((lithiumFont16.getMiddleOfBox(rectHeight) + (rectHeight * rectCount)) * openAnimation.getOutput().floatValue()),
                        ColorUtil.applyOpacity(textColor, openAnim));

                rectCount++;
            }

            countSize = 2 + ((.25f + (rectCount * (rectHeight / (realHeight / normalCount)))) * openAnimation.getOutput().floatValue());
        } else {
            countSize = 2;
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        ModeSetting modeSetting = getSetting();
        float boxHeight = 20;
        float boxY = y + realHeight / 2f - (boxHeight / 2f) + 4;
        float boxX = x + 5;
        float boxWidth = width - 10;

        if (isClickable(boxY + boxHeight) && HoveringUtil.isHovering(boxX, boxY, boxWidth, boxHeight, mouseX, mouseY) && button == 1) {
            opened = !opened;
        }

        if (opened) {
            float rectHeight = 20;
            float rectCount = 0;
            float modeY = boxY + boxHeight + 4;
            float modeX = boxX - 1;

            for (String mode : modeSetting.modes) {
                boolean hoveringMode = HoveringUtil.isHovering(modeX, modeY + rectCount * rectHeight, boxWidth, rectHeight, mouseX, mouseY);
                if(isClickable((modeY + rectCount * rectHeight) + rectHeight) && hoveringMode && button == 0) {
                    modeSetting.setCurrentMode(mode);
                    return;
                }

                rectCount++;
            }
        }


    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
