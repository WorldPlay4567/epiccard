package com.epiccard.register.card;

import com.epiccard.register.CardRegister;

public class BloodCard extends Card {

    public final int blood;

    public BloodCard(CardRegister.Build build) {
        super(build);
        this.blood = build.blood;
    }
}
