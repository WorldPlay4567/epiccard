package com.epiccard.register.card;

import com.epiccard.register.CardRegister;

public class BoneCard extends Card{

    public final int bone;

    public BoneCard(CardRegister.Build build) {
        super(build);
        this.bone = build.bone;
    }

}
