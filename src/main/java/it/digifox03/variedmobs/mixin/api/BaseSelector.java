package it.digifox03.variedmobs.mixin.api;

import it.digifox03.variedmobs.VariedSelector;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public abstract class BaseSelector extends VariedSelector {
    public BaseSelector(@NotNull Identifier type) {
        super(type);
    }
}
