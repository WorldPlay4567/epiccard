package com.epiccard.register.card;

import com.epiccard.register.CardBattle;
import com.epiccard.register.CardRegister;

public class Card {

    public final int hp;
    public final int attack;
    public final String name;

    public Card(CardRegister.Build build) {
        this.hp = build.hp;
        this.attack = build.attack;
        this.name = build.name;
    }

    public CardBattle getBattleCard() {
        return new CardBattle(this);
    }

    public void damage() {

    }

    public void attack() {

    }


}
