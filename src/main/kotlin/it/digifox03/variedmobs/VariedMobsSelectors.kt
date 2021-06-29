package it.digifox03.variedmobs

import it.digifox03.variedmobs.props.bool.*
import it.digifox03.variedmobs.props.entity.*
import it.digifox03.variedmobs.props.item.ContextItemProp
import it.digifox03.variedmobs.props.item.ItemProp
import it.digifox03.variedmobs.props.value.*
import it.digifox03.variedmobs.selectors.*
import it.digifox03.variedmobs.props.string.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal object VariedMobsSelectors: VariedMobsSelectorRegister {
    override fun module() = SerializersModule {
        polymorphic(Selector::class) {
            subclass(ConstantSelector::class)
            subclass(RangeSelector::class)
            subclass(StringSelector::class)
            subclass(IfSelector::class)
        }
        polymorphic(BoolProp::class) {
            subclass(BeeHasFlowerBoolProp::class)
            subclass(BeeHasHiveBoolProp::class)
            subclass(BeeHasStungBoolProp::class)
            subclass(FoxIsSittingBoolProp::class)
            subclass(FoxIsSleepingBoolProp::class)
        }
        polymorphic(EntityProp::class) {
            subclass(ContextEntityProp::class)
        }
        polymorphic(ItemProp::class) {
            subclass(ContextItemProp::class)
        }
        polymorphic(StringProp::class) {
            subclass(BiomeStringProp::class)
            subclass(ItemNameStringProp::class)
            subclass(ItemTypeStringProp::class)
            subclass(NameStringProp::class)
        }
        polymorphic(ValueProp::class) {
            subclass(BeeAngerValueProp::class)
            subclass(FireValueProp::class)
            subclass(FreezeValueProp::class)
            subclass(HealthValueProp::class)
            subclass(PitchValueProp::class)
            subclass(XValueProp::class)
            subclass(YawValueProp::class)
            subclass(YValueProp::class)
            subclass(ZValueProp::class)
        }
    }
}
