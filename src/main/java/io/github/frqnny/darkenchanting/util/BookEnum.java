package io.github.frqnny.darkenchanting.util;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

//TODO make it render based on the ID on here
public enum BookEnum implements StringIdentifiable {
    DEFAULT(DarkEnchanting.id("entity/book1"), "default"),
    HELL(DarkEnchanting.id("entity/book2"), "hell");

    final Identifier id;
    final String string;

    BookEnum(Identifier id, String string) {
        this.id = id;
        this.string = string;
    }

    @Override
    public String asString() {
        return string;
    }
}
