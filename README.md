# Varied Mob Textures
This mod allows the resource packs creator to have multiple randomized textures for the same mob. It allows also to display certain textures based on various parameters such as the mob age, current biome, name, health and other properties.

## Information for resource pack development

### Replacing a texture

With this mod, whenever minecraft tries to load the texture for an entity
(e.g. "minecraft:texture/entity/zombie/zombie.png") it checks if a varied
texture descriptor is present in the "varied" folder with the same name
but with .json extension (e.g.
"minecraft:varied/texture/entity/zombie/zombie.json").

If the descriptor has been found it is read to determine which texture will
be used instead.

### Varied texture descriptor format.

```json
{
  "type": "varied-mobs:pick",
  "choices": [
    "minecraft:varied/texture/entity/zombie/zombie-variant1.json",
    "minecraft:varied/texture/entity/zombie/zombie-variant2.json",
    "minecraft:varied/texture/entity/zombie/zombie-variant3.json"
  ]
}
```

the root of the json document is a selector object; a selector object is a
json object which is queried when a texture has to be selected.

There are many types of selector, each does something different, and some mods
might add more selectors.

When queried each selector does some operations (such as checking some properties)
and can either return a texture identifier or nothing. If the root selector
returns nothing then the normal texture will be used instead.

### List of selectors

#### Result selector

The simplest selector, always return the id in `"result"` or nothing if the field
is missing.

As a special case, when a string has been found where a selector where
a selector should have been, it will be treated as the `"result"` field of a
`varied-mobs:result` selector.
```json
{
  "type": "varied-mobs:result",
  "result": "<TEXTURE_ID>"
}
```

#### Pick selector

Pick a selector from the `"choices"` list randomly, a `"weights"` list can be given
to make some choices more probable.

If the picked selector returns nothing then another selector will be picked;
the process will be repeated until either a selector returns an identifier,
or it runs out of choices (and will return nothing)
```json
{
  "type": "varied-mobs:pick",
  "weights": [1, 3, 2],
  "choices": [
    {"type": "..."},
    {"type": "..."},
    {"type": "..."}
  ]
}
```

#### Sequence selector

the seq selector will try each selector in order until either a selector returns an
identifier (which will return), or it runs out of choices (and will return null).

```json
{
  "type": "varied-mobs:seq",
  "choices": [
    {"type": "..."},
    {"type": "..."},
    {"type": "..."}
  ]
}
```

#### Biomes selector

checks that the current mob biome is present in the `"biomes"` list.
if it is then it returns the result of `"value"`, nothing otherwise.

```json
{
  "type": "varied-mobs:biome",
  "biomes": ["minecraft:plains", "minecraft:desert"],
  "value": {"type": "..."}
}
```

#### name selector

checks that the regex contained in the `"regex"` field matches current mob name-tag.
if it does then it returns the result of `"value"`, nothing otherwise.

```json
{
  "type": "varied-mobs:name",
  "regex": "<REGULAR EXPRESSION>",
  "value": {"type": "..."}
}
```

#### baby selector

if the mob currently is a baby then it returns the result of `"value"`, nothing otherwise.

```json
{
  "type": "varied-mobs:baby",
  "value": {"type": "..."}
}
```

#### Value selectors

for more complex things than are represented by a range of
values instead of a boolean value, the value selectors get used.

example: health selector.

```json
{
  "type": "varied-mobs:health-prop",
  "positions": [0, 0.25, 0.5, 0.75, 1],
  "choices": [
    "minecraft:varied/texture/entity/zombie/zombie-very_hurt.png",
    "minecraft:varied/texture/entity/zombie/zombie-hurt.png",
    "minecraft:varied/texture/entity/zombie/zombie-slightly_hurt.png",
    "minecraft:varied/texture/entity/zombie/zombie.png"
  ]
}
```

in this example, when the zombie health is between 0 (inclusive) and 1/4
(exclusive) the `zombie-very_hurt.png` texture will be used, when it is
between 1/4 and 1/2 `zombie-hurt.png`, `zombie-slightly_hurt.png` for between 1/2
and 3/4, and `zombie.png` for 3/4 to 1.

##### table of value selectors

| name                   | desc                            | min  | max    |
|------------------------|---------------------------------|------|--------|
| health-prop            | current health / max health     | 0    | 1      |
| y-prop                 | the current y position          | -inf | +inf   |
| x-prop                 | the current x position          | -inf | +inf   |
| z-prop                 | the current z position          | -inf | +inf   |
| age-prop               | the mob age in ticks            | 0    | +inf   |
| time-prop              | time of the day in ticks        | 0    | 24,000 |
| biome-temperature-prop | the biome temperature           | 0.0  | 2.0    |
| biome-rainfall-prop    | the biome rainfall              | 0.0  | 2.0    |
| biome-depth-prop       | the biome depth                 | 0.0  | 2.0    |
| slot-prop              | the slot of the armor item      | 0    | 5      |
| item-damage-prop       | the level of damage of the item | 0    | 1      |
| cmd-prop               | the item CustomModelData        | -inf | +inf   |

(do not forget to prefix the names with `varied-mobs:`, e.g.
`varied-mobs:health-prop`)

check out the [releases](https://github.com/Digifox03/variedMobs/releases) for the latest experimental version,
 and the example resource pack
