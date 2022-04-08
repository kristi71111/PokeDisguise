#PokeDisguise
A forge mod that allows you to disguise yourself as a Pokemon. Using only packets and not spawning any entities in the world on the server.
This mod however is **server side only!**
* ##Commands
  * There is currently only one command which is **/pokediguise** and has the aliases **/disguise**, **/pixeldisguise**, **/bacodisguise**, **/pd**
  * The command usage is fairly simple for example **/pokedisguise Abra** would disguise you as a non shiny Abra. Using **/pokedisguise Abra shiny** would disguise you as a shiny Abra.
- ##Permissions
    * Base command permission: **pokedisguise.command.pokedisguise**
  * The permissions that are enabled per config argument are as followed (also mentioned in the config).
  * lockLegendaryBehindPerm permission node: **pokedisguise.legendarypermitted**
  * lockShinyArgumentBehindPerm permission node: **pokedisguise.shinypermitted**
  * lockAllPokemonBehindPerm permission node (Make sure name starts with upper case): **pokedisguise.disguise.Pokemonname** example **pokedisguise.disguise.Abra**
#Mohist users
  - Note that this mod uses [sponge's for forge mod permission implementation.](https://docs.spongepowered.org/stable/en-GB/plugin/permissions.html#for-forge-mods) which causes configurable permission nodes on **Mohist** to be required twice. Once in the original format and once in the format stated below.
    * lockLegendaryBehindPerm permission node: **minecraft.command.pokedisguise.legendarypermitted**
    * lockShinyArgumentBehindPerm permission node: **minecraft.command.pokedisguise.shinypermitted**
    * lockAllPokemonBehindPerm permission node (Make sure name starts with upper case): **minecraft.command.pokedisguise.disguise.Pokemonname**

#Known issues   
- Teleporting while disguised will make your disguise no longer render for yourself if shouldPlayerSeeOwnDisguise is set to true. Reenabling disguise fixes this.
- Vanishing while disguised will completely break stuff (Sponge not sure how Mohist handles this).
#Support
  - [**Click here to join my support discord.**](https://discord.gg/x53dk93Xsk)