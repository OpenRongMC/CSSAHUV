# CSSAHUV（Chinese-style school as well as home）

上游为 Forge 1.20.1 模组 **Chinese-style school as well as home**（作者：江竹雨offical / Noel-Cornehl），提供中式校园与家居向建筑内容（大量方块、若干实体/音效等）。

本仓库是 RongMC 将其迁到 **Fabric · Minecraft 26.3** 的移植树。

---

## RongMC · Minecraft 26.3 Fabric 移植说明

本仓库是 RongMC 维护的 **Minecraft 26.3 / Fabric / Java 25** 移植构建，不是上游官方发行渠道。

### 与原版的主要差异
- 加载器从 Forge 改为 Fabric；游戏版本到 26.3。
- Fabric id：`rongmc_school`（provides `school`）；资源命名空间仍为 `school`。
- 注册体量大（数百方块/物品），启动与进世界成本高于一般装饰模组。

### 已知问题 / 限制
- 从 Forge 迁移的结构/能力未必与原版 Forge 行为 100% 一致（容器、实体 AI、结构模板等需按 26.3 实测）。
- 旧 Forge 存档直接迁过来**不保证**方块 ID 与数据兼容。
- 上游若无公开完整 README，功能细节以游戏内创造页签与资源名为准。

### 构建与安装
- Fabric mod id：`rongmc_school`（`provides`：`school`，旧依赖名一般仍可解析）
- Release 产物见本仓库 Releases（jar 名带 `rongmc-` 前缀）
- 许可证：本仓以 **GPL-3.0** 发布（见 `LICENSE`）；请同时尊重上游原作者条款与署名

