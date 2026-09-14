package com.epiccard.register;

import com.epiccard.register.card.BloodCard;
import com.epiccard.register.card.BoneCard;
import com.epiccard.register.card.Card;
import net.minecraft.nbt.NbtCompound;

public class CardBattle {

    public final Card card;
    public String name;

    public int id;
    public int hp;
    public int blood;
    public int bone;
    public int attack;

    public CardBattle(Card card) {
        this.card = card;
        this.hp = card.hp;
        this.attack = card.attack;
        this.name = card.name;

        if(card instanceof BloodCard bloodCard) {
            this.blood = bloodCard.blood;
        }
        if(card instanceof BoneCard boneCard) {
            this.bone = boneCard.bone;
        }
    }

    public void cardAttack() {
        card.attack();
    }

    public NbtCompound toNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("name", name);
        nbtCompound.putInt("id", id);
        nbtCompound.putInt("hp", hp);
        nbtCompound.putInt("blood", blood);
        nbtCompound.putInt("bone", bone);
        nbtCompound.putInt("attack", attack);
        return nbtCompound;
    }

    public void readNbt(NbtCompound nbtCompound) {
        this.name = nbtCompound.getString("name");
        this.id = nbtCompound.getInt("id");
        this.hp = nbtCompound.getInt("hp");
        this.blood = nbtCompound.getInt("blood");
        this.bone = nbtCompound.getInt("bone");
        this.attack = nbtCompound.getInt("attack");
    }
}
