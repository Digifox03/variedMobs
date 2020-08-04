package it.digifox03.variedmobs.api;

import it.digifox03.variedmobs.core.VariedSelector;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public abstract class BaseSelector extends VariedSelector {
    public BaseSelector(@NotNull Identifier type) {
        super(type);
    }
}
