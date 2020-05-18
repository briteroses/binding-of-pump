# binding-of-pump
This is a recreation of video game Binding of Isaac in Java using the Slick2D engine.
WASD to move, arrow keys to shoot.
Most core features of the original game are implemented, including procedural level generation.
About ~140 items are implemented, most directly from the original game and some with new original effects.
5 bosses are implemented, some with original designs not in the base game.
There are a few gameplay mechanics not in the original game; mainly, I've added a bullet time feature where
holding down spacebar slows down player, enemy, and bullet movement, allowing for finer dodging and control, and depletes the "LP" energy meter.
There are also blue bullets which only hurt you if you are moving; this is a feature taken from another video game Undertale.
Playing the game will likely also require you to install the Slick2D library on your own machine first;
if that's a hassle, you can check out several gameplay demonstrations here:

https://www.youtube.com/watch?v=0Fqfn4jaR_w

https://www.youtube.com/watch?v=xhDND8DG8P4

https://www.youtube.com/watch?v=Rpd-_o3Nld0

These gameplay videos demonstrate how randomization of levels and items as well as diversity in item effects can create meaningful variations between each session of the game. The game design structure facilitates infinite replayability.


Full item list:
 * 0 - Lump of Coal - spells increase in damage and size at further range
 * 1 - Proptosis - spells start with massive damage and decrease in damage and size at further range
 * 2 - Triple Shot
 * 3 - Dead Dove - spells pass through walls
 * 4 - Sagittarius - spells pass through enemies
 * 5 - Soy Milk - spells have extremely high fire rate, extremely low damage
 * 6 - Double Shot
 * 7 - Quad Shot
 * 8 - God's Flesh - randomly raises three stats, lowers three stats
 * 9 - Explosivo - small chance of shooting tear which explodes into ring of tears upon expiry
 * 10 - Vampirism - small chance to heal when enemy killed
 * 11 - Revenge Eraser - removes all projectiles in room when protagonist takes damage
 * 12 - Revenge Trap - deals small damage to all enemies in room when protagonist takes damage
 * 13 - Close Kodak - orbital with small radius that deals contact damage
 * 14 - Far Kodak - orbital with far radius that deals contact damage
 * 15 - Buddy Smokepurpp - controllable familiar that deals contact damage
 * 16 - Bloodlust 1 - increases damage with killcount
 * 17 - Epiphora - increases fire rate with killcount
 * 18 - Bloodlust 2 - increases damage multiplier with rooms cleared
 * 19 - Gray Hourglass - doubles effectiveness of slowmode (twice as slow)
 * 20 - Blue Hourglass - doubles slowmode charge
 * 21 - Heart Pendant - enemies have small chance to drop heart when killed
 * 22 - Polyphemus - extremely large tears with low fire rate that pierce with lowered damage
 * 23 - Treasure Map - reveals map
 * 24 - Spider Shot - chance to slow enemy on hit (slows walk only, not shots)
 * 25 - Medusa Shot - chance to freeze enemy on hit
 * 26 - Adrenaline - multiplies damage and speed when HP is low
 * 27 - Lean Shotgun - replaces spells with a charged shotgun attack
 * 28 - Anti-Gravity - holding fire key holds all spells in place
 * 29 - Crown of Light - if HP is maxed, doubles damage for current room until damage is taken
 * 30 - Holy Mantle - negates the first damage taken in a room
 * 31 - Mascara - doubles damage but halves fire rate and shot speed
 * 32 - Ghostly - spectral and piercing tears with low shot speed
 * 33 - Leany Veins - releases 10-shot spell ring when damage is taken
 * 34 - Dunce Cap - shoot two shots at 45-deg angles
 * 35 - Dead Eye - rewards accuracy with damage multiplier
 * 36 - The Virus - protagonist deals contact damage
 * 37 - Superhot - all enemies and bullets freeze while protagonist is not performing action
 * 38 - Occult Eye - tears are controlled
 * 39 - Paschal Candle - rewards fire rate multiplier for consecutive rooms cleared without taking damage
 * 40 - Menorah - taking damage increases shot number until 7, starting over again at 0
 * 41 - Abaddon - grants huge damage and speed boost at the cost of most max HP
 * 42 - <3 - adds max HP and fully restores health
 * 43 - Aquarius - leaves trail of tears
 * 44 - The Belt - boosts speed
 * 45 - Blood of the Martyr - boosts damage
 * 46 - Blue Cap - max HP up, fire rate up, shot speed down
 * 47 - Lard - large max HP boost at the cost of speed down
 * 48 - Cat-o-nine Tails - damage and shot speed up
 * 49 - Cricket's Body - splash damage when tears hit enemy or expire range
 * 50 - Cricket's Head - grants damage multiplier
 * 51 - Growth Hormones - damage and speed boost
 * 52 - Halo - boosts all stats
 * 53 - Holy Grail - grants flying and boosts HP
 * 54 - Infamy - small chance to negate damage taken
 * 55 - Contact Lens - spells can cancel out with bullets
 * 56 - Bowtie - HP boost
 * 57 - Magic 8 Ball - shot speed boost
 * 58 - Magic Mushroom - some stats up and damage multiplier
 * 59 - Magic Scab - HP boost
 * 60 - Big Meat - Damage boost and small HP boost
 * 61 - The Contract - Sets max HP to very small value, destroys all enemies if player takes damage below max HP
 * 62 - Heels - range boost
 * 63 - Codeine Piss - doubles fire rate at the cost of range
 * 64 - Fat Shroom - HP, damage, range small boosts
 * 65 - Skinny Shroom - large fire rate boost with small damage down
 * 66 - Bandaid - HP boost
 * 67 - Ouija - grants spectral tears
 * 68 - Pentagram - large damage boost
 * 69 - Pisces - fire rate boost
 * 70 - Steroids - speed and range boost
 * 71 - Sad Onion - fire rate boost
 * 72 - The Screw - fire rate and shot speed boost
 * 73 - The Small Rock - damage and fire rate boost at the cost of some speed
 * 74 - Speed Ball - speed and shot speed boost
 * 75 - Spirit of the Night - grants flying and piercing
 * 76 - Steven - damage boost
 * 77 - Stigmata - HP and damage boost
 * 78 - Synthoil - damage and range boost
 * 79 - Continuum - spells loop through walls
 * 80 - Godhead - spells gain a damaging halo
 * 81 - Monstrance - protagonist gains a damaging halo
 * 82 - Censer - protagonist gains a slowing halo
 * 83 - Angelic Prism - orbiting prism that splits spells into four
 * 84 - Lead Pencil - creates spurt of spells for every 20 spells shot
 * 85 - Contagion - enemies release ring of spells when killed
 * 86 - Kidney Stone - creates concentrated spurt of spells at small chance for every shot fired
 * 87 - Little Horn - small chance to shoot instakilling spell
 * 88 - Succubus - familiar flies around room, projecting shadow that damages enemies and buffs player damage
 * 89 - Athame - protagonist activated shadow when hit that damages enemies and creates hearts when it kills enemies
 * 90 - Blood Rights - dramatically increases player invincibility frames
 * 91 - Immaculate Heart - chance to generate an orbiting spell with every spell fired
 * 92 - Tiny Planet - all spells are orbiting and spectral
 * 93 - Saturn - generates 7 orbiting spectral piercing spells when entering room
 * 94 - Orb of Light - if HP is maxed, grants large fire rate bonus until damage is taken in room
 * 95 - Norepi - multiplies fire rate when HP is low
 * 96 - Bloodlust 3 - taking damage boosts damage for the remaining floor
 * 97 - Celtic Cross - chance to trigger protective shield upon getting hit
 * 98 - Polaroid - triggers protective shield upon getting hit at low HP
 * 99 - The Pact - grants huge fire rate boost at the cost of most max HP
 * 100 - Wafer - all damage to player is halved
 * 101 - Mark - pay some HP for damage boost and shield
 * 102 - Relic - drops shield every 6 rooms completed
 * 103 - Crystal Ball - reveals map, small chance to heal shield when room completed
 * 104 - Mitre - increases drop chance of shield
 * 105 - Ceremonial Robes - pay some HP for damage boost and shield
 * 106 - Tanooki - player turns invulnerable when performing no action for an extended period of time
 * 107 - Black Lotus - player gains max HP and shield
 * 108 - Gimpy - chance to heal shield when hit
 * 109 - Rosary - gain large amount of shield
 * 110 - Blanket - gain shield, full HP heal, negated first hit in boss room
 * 111 - Parasite - spells split into two new spells when hitting enemy or on expiry
 * 112 - Empty Vessel - when HP is low, gain flying and a periodic protective shield
 * 113 - Stopwatch - freezes all enemies in room when player takes damage
 * 114 - Common Cold - chance to shoot poison spells
 * 115 - Serpent's Kiss - chance to shoot serpent poison spells and deals serpent poison to enemies in contact with player. When serpent poisoned enemies die, they have a small chance to drop shield.
 * 116 - Toxic Shock - all enemies are poisoned when player first enters room
 * 117 - Mirror - spells rebound like boomerangs
 * 118 - Midas Touch - player deals heavy contact damage, freezes enemies in contact
 * 119 - Broken Watch - slows all enemies in room when player takes damage
 * 120 - Eye of Belial - grants piercing spells, spells double in damage and rebound after piercing one enemy
 * 121 - Maw of the Void - pay max HP for ability to charge shadow aura via continuous fire. Shadow deals damage to enemies and has a small chance to drop shields after killing each enemy.
 * 122 - Lil Zel - fast-orbiting buddy that blocks shots and deals contact damage
 * 123 - Lil Lil Pump - orbiting buddy that deals small contact damage and shoots alongside player
 * 124 - Lil Purpp - orbiting buddy that targets and auto-fires at nearby enemies
 * 125 - Apple - small chance to shoot high-damage spell
 * 126 - Poison Brain - player gains poison aura while shooting
 * 127 - Squeezy - boosts fire rate, grants shield
 * 128 - Dolly - boosts range and fire rate, spawns three hearts or shields
 * 129 - Laced Xan - boosts speed, shrinks player sprite
 * 130 - Binky - boosts fire rate, grants shield, decreases range, shrinks player sprite
 * 131 - Yum Heart - drops heart every 6 rooms completed
 * 132 - Scapular - grants shield if player takes damage while at low HP
 * 133 - Raw Liver - dramatically boosts max HP
 * 134 - Stem Cells - boosts max HP, small heal
 * 135 - Placenta - boosts max HP, extremely low chance to heal HP every frame
 * 136 - Brittle Heart - massive boost to max HP, fully heals. When player dips 15 HP below max HP, max HP decreases permanently.
 * 137 - The Pin - range, shot speed, and speed boost
 * 138 - Mini Mushroom - range and speed boost, shrinks player sprite
 * 139 - Blood Bag - boosts max HP and speed, massive heal
 * 140 - Cancer - grants shield; if hit, grants partial defense against damage for the rest of the room
 * 141 - Berserker - if hit, grants increased damage and fire rate for a short time
