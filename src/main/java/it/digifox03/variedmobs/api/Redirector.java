package it.digifox03.variedmobs.api;

import it.digifox03.variedmobs.VariedMobManager;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface Redirector {
    static Redirector getInstance() {
        return VariedMobManager.INSTANCE;
    }
    Identifier redirect(Identifier id, Map<Identifier, Object> ctx);
}
