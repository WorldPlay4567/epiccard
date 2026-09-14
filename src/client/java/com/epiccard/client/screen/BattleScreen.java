package com.epiccard.client.screen;

import com.epiccard.client.ModelsRegister;
import com.epiccard.client.game.TableManager;
import com.epiccard.client.utility.RayCastMouse;
import com.epiccard.entity.entity.InteractionCardEntity;
import com.epiccard.entity.entity.TableEntity;
import com.epiccard.game.InteractionType;
import com.epiccard.payload.BattleCloseC2SPayload;
import com.epiccard.payload.InteractionC2SPayload;
import com.epiccard.payload.PlaceCardC2SPayload;
import com.epiccard.register.CardBattle;
import com.epiccard.register.CardRegister;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.dimension.DimensionTypes;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BattleScreen extends Screen {

    private final TableEntity tableEntity;
    private final List<CardUI> cardUIList = new ArrayList<>();
    private double mouseX;
    private double mouseY;

    private CardUI cardUIDraging = null;
    private CardUI cardUISelected = null;
    private double xOffset = 0;
    private double yOffset = 0;


    public BattleScreen(String title, TableEntity tableEntity) {
        super(Text.of(title));
        this.tableEntity = tableEntity;
    }

    @Override
    protected void init() {
        super.init();
    }

    public void addCard(NbtCompound nbtCompound) {
        CardBattle cardBattle = new CardBattle(CardRegister.CARD_ANIMAL);
        cardBattle.readNbt(nbtCompound);
        cardUIList.add(new CardUI((this.width /2 + 10 * 7) - (50), (int) (this.height / 1.45f), cardBattle));
        updatePosCard();
    }

    public void removeCard(int i) {
        CardUI cardUI = cardUIList.get(i);
        cardUIList.remove(cardUI);
        if(cardUIDraging == cardUI) {
            cardUIDraging = null;
        }
        if(cardUISelected == cardUI) {
            cardUISelected = null;
        }
        updatePosCard();
    }

    public void updatePosCard() {
        for(int i = 0; i < cardUIList.size(); i++) {
            CardUI cardUI = cardUIList.get(i);
            cardUI.setPos((this.width /2 + 10 * 7) - (50 * i), (int) (this.height / 1.45f));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.mouseY = mouseY;
        this.mouseX = mouseX;
        MinecraftClient.getInstance().player.sendMessage(Text.of(mouseX + " " + mouseY), true);

        MatrixStack matrixStack = context.getMatrices();



        for(int i = 0; i < cardUIList.size(); i++) {

            CardUI cardUI = cardUIList.get(i);

            matrixStack.push();

            int light = 2;

            matrixStack.translate(cardUI.getPosRender().x, cardUI.getPosRender().y, 50.0f - i);
            if(cardUI == cardUIDraging || cardUISelected == cardUI) {
                matrixStack.translate(0,0,100);
                light = 1;
            }
            matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));

            float scale = 130;

            matrixStack.scale(scale, scale, scale);
            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    Items.STONE.getDefaultStack(),
                    ModelTransformationMode.GUI,
                    false,
                    matrixStack,
                    context.getVertexConsumers(),
                    LightmapTextureManager.MAX_LIGHT_COORDINATE / light,
                    OverlayTexture.DEFAULT_UV,
                    MinecraftClient.getInstance().getBakedModelManager().getModel(ModelsRegister.CARD_MODEL)

            );


            matrixStack.pop();

            matrixStack.push();

            matrixStack.translate(cardUI.getPosRender().x, cardUI.getPosRender().y - 37, 107.0f - i);

            if(cardUI == cardUIDraging || cardUISelected == cardUI) {
                matrixStack.translate(0, 0, 100);
            }
            float textScale = 1.2f;
            matrixStack.scale(textScale, textScale, textScale);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, cardUI.cardBattle.name, 0, 0, -1);

            matrixStack.pop();
        }

        if(cardUIDraging != null) {
            matrixStack.push();

            matrixStack.translate(0,0,600);
            context.drawBorder(cardUIDraging.x, cardUIDraging.y,  cardUIDraging.width, cardUIDraging.height, 0xFFFFFFFF);

            matrixStack.pop();
        }

        if(cardUISelected != null) {
            matrixStack.push();

            matrixStack.translate(0,0,600);
            context.drawBorder(cardUISelected.x, cardUISelected.y,  cardUISelected.width, cardUISelected.height, 0xFFFF0000);

            matrixStack.pop();
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            EntityHitResult hitResult = RayCastMouse.getMobUnderMouse(mouseX, mouseY);
            if (hitResult != null) {
                Entity entity = hitResult.getEntity();
                entity.getWorld().addParticle(ParticleTypes.CRIT, entity.getX(), entity.getY() + .2, entity.getZ(), 0, 0, 0);
                if(entity instanceof InteractionCardEntity interactionCardEntity) {
                    if(interactionCardEntity.interactionType != null) {
                        if(interactionCardEntity.interactionType == InteractionType.PLACE_CARD && cardUISelected != null) {
                            PlaceCardC2SPayload placeCardC2SPayload = new PlaceCardC2SPayload(interactionCardEntity.id, cardUISelected.cardBattle.id);
                            ClientPlayNetworking.send(placeCardC2SPayload);
                        } else {
                            InteractionC2SPayload interactionC2SPayload = new InteractionC2SPayload(interactionCardEntity.interactionType.ordinal());
                            ClientPlayNetworking.send(interactionC2SPayload);
                        }
                    }
                }
            }

            for (CardUI cardUI : cardUIList) {
                if (cardUI.isMouseOver(mouseX, mouseY)) {
                    if (cardUISelected == null) {
                        cardUISelected = cardUI;
                        xOffset = cardUI.x - mouseX;
                        yOffset = cardUI.y - mouseY;
                        break;
                    } else {
                        cardUISelected = null;
                    }
                }
            }

        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {



        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        for (CardUI cardUI : cardUIList) {
            if (cardUI.isMouseOver(mouseX, mouseY)) {

                if (true) {
                    cardUIDraging = cardUI;
                    xOffset = cardUI.x - mouseX;
                    yOffset = cardUI.y - mouseY;
                    break;
                }
            }
        }

//        if(cardUIDraging != null) {
//            cardUIDraging.setPos((int) (mouseX + xOffset), (int) (mouseY + yOffset));
//        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(button == 0) {
            cardUIDraging = null;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            BattleCloseC2SPayload battleCloseC2SPayload = new BattleCloseC2SPayload(tableEntity.getId());
            ClientPlayNetworking.send(battleCloseC2SPayload);
            this.close();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderDarkening(DrawContext context) {

    }

    public TableEntity getTableEntity() {
        return tableEntity;
    }

    @Override
    public void blur() {

    }

    @Override
    protected void applyBlur(float delta) {
        return;
    }
}
