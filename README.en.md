# Chinese-style School as well as Home (CSSAHUV)

[简体中文](./README.md) · English

Ports the Forge 1.20.1 mod’s Chinese school and household content to Fabric 26.3, including desks, classroom equipment, home decor, entities and interactive menus. Assets and world blocks use the school namespace.

## Download and installation

- Version · [v0.90.9-rongmc.2+26.3](https://github.com/OpenRongMC/CSSAHUV/releases/tag/v0.90.9-rongmc.2%2B26.3) (Release)
- Release asset · `rongmc-school-0.90.9-rongmc.2.26.3.jar`
- Runtime · Minecraft Java 26.3, Fabric, Java 25
- Fabric Loader · `>=0.20.1`
- Fabric mod ID · `rongmc_school`
- Dependencies declared by the JAR · [Fabric API](https://modrinth.com/mod/fabric-api)

Place the release JAR in the instance’s `mods/` directory. Install the same release on the client and server. Try older Forge worlds on a copy and check interactive blocks, entities and structure templates separately.

## How to try it

On a dedicated server, place a desk, Seewo display and projector, then open the laboratory cabinet and verify that the connection stays up. The school:ner template was tested. Other legacy mechanics still need individual checks.

## Working from source

```sh
./gradlew tasks
```

Inspect the tasks available in this checkout with Java 25 before selecting a build task.

## Documentation and support

- [Previous README (historical guide, contributors and credits)](https://github.com/OpenRongMC/CSSAHUV/blob/50d415766fa9be05fe43f2e33bafa6da8b5cd7d0/README.md)
- [Upstream project and documentation](https://github.com/bii20081Aztec/CSSAHUV)
- [Report a RongMC port issue](https://github.com/OpenRongMC/CSSAHUV/issues) · Include the game version, client and server JAR names, reproduction steps and relevant logs.

## License and credits

See this repository’s [LICENSE](./LICENSE) and the original project’s license, credits and third-party asset notices.
