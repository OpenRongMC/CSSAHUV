# 中式学校及家庭（CSSAHUV）

简体中文 · [English](./README.en.md)

把原 Forge 1.20.1 模组的中式校园与家庭内容移植到 Fabric 26.3，包含课桌椅、教室设备、家庭装饰、实体及交互菜单。资源和世界方块使用 school 命名空间。

## 下载与安装

- 版本 · [v0.90.9-rongmc.2+26.3](https://github.com/OpenRongMC/CSSAHUV/releases/tag/v0.90.9-rongmc.2%2B26.3) (已发布)
- 下载文件 · `rongmc-school-0.90.9-rongmc.2.26.3.jar`
- 运行环境 · Minecraft Java 26.3, Fabric, Java 25
- Fabric Loader · `>=0.20.1`
- Fabric 模组 ID · `rongmc_school`
- JAR 声明的依赖 · [Fabric API](https://modrinth.com/mod/fabric-api)

把发行 JAR 放入对应实例的 `mods/` 目录。联机两端安装同一版本。旧 Forge 存档先复制再测试，尤其检查交互方块、实体与结构模板。

## 如何验证

专用服可试放课桌、希沃一体机和投影机，打开实验室柜子并确认连接保持。移植版验证过 school:ner 模板加载。其他旧玩法仍需逐项核对。

## 从源码开始

```sh
./gradlew tasks
```

先用 Java 25 检查当前源码树提供的任务，再选择对应的构建任务。

## 文档与反馈

- [原 README（旧版指南、贡献者与署名）](https://github.com/OpenRongMC/CSSAHUV/blob/50d415766fa9be05fe43f2e33bafa6da8b5cd7d0/README.md)
- [上游项目与文档](https://github.com/bii20081Aztec/CSSAHUV)
- [报告 RongMC 移植问题](https://github.com/OpenRongMC/CSSAHUV/issues) · 请附游戏版本、两端 JAR 文件名、复现步骤和相关日志。

## 许可与署名

请参阅本仓库的 [LICENSE](./LICENSE) 及原项目的许可、署名和第三方资源说明。
