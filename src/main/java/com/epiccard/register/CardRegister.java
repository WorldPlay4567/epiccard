package com.epiccard.register;



import com.epiccard.register.card.Card;

import java.util.ArrayList;
import java.util.List;

public class CardRegister {

    public static List<Card> registerCard = new ArrayList<>();

    public static Card CARD_ANIMAL = new Card(new Build().setAttack(0).setHp(1).setName("sheep"));



    public static void init() {

    }

    public Card register(Card card) {
        registerCard.add(card);
        return card;
    }


    public static class Build {

        public int blood = 1;
        public int bone = 1;
        public int attack = 1;
        public int hp = 1;

        public String name;

        public Build setAttack(int attack) {
            this.attack = attack;
            return this;
        }

        public Build setBlood(int blood) {
            this.blood = blood;
            return this;
        }

        public Build setbone(int bone) {
            this.bone = bone;
            return this;
        }

        public Build setHp(int hp) {
            this.hp = hp;
            return this;
        }

        public Build setName(String name) {
            this.name = name;
            return this;
        }

        public Build build() {
            return this;
        }
    }
}
