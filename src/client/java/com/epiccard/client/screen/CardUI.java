package com.epiccard.client.screen;

import com.epiccard.register.CardBattle;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class CardUI {
    public int x, y;
    public final int width = 105;
    public final int height = 164;

    private int x_offset = 57, y_offset = 65;

    public final CardBattle cardBattle;

    public CardUI(int x, int y, CardBattle cardBattle) {
        this.x = x;
        this.y = y;
        this.cardBattle = cardBattle;
    }

    public Vector2i getPos() {
        return new Vector2i(x,y);
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPos(int[] pos) {
        setPos(pos[0], pos[1]);
    }

    public Vector2i getPosRender() {
        return new Vector2i(x + x_offset, y + y_offset);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
